package com.yogapay.wx.service;

import com.yogapay.core.LangUitls;
import com.yogapay.wx.ErrorCodeException;
import com.yogapay.wx.entity.WXAccessToken;
import com.yogapay.wx.entity.WXConfig;
import com.yogapay.wx.entity.WXJSTicket;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WXCommonService {

	private final ConcurrentMap<String, WXAccessToken> accessTokens = new ConcurrentHashMap<String, WXAccessToken>();
	private final ConcurrentMap<String, WXJSTicket> jsTickets = new ConcurrentHashMap<String, WXJSTicket>();
	@Autowired
	private ObjectMapper om;
	@Autowired
	private OauthService oauthService;

	public String getAccessToken(WXConfig c) throws IOException {
		WXAccessToken t = accessTokens.get(c.app_id);
		if (t == null) {
			t = LangUitls.putIfAbsent(accessTokens, c.app_id, new WXAccessToken());
		}
		return t.getAccessToken(c);
	}

	public synchronized String getJSTicket(WXConfig c) throws IOException {
		WXJSTicket t = jsTickets.get(c.app_id);
		if (t == null) {
			t = LangUitls.putIfAbsent(jsTickets, c.app_id, new WXJSTicket());
		}
		String accessToken = getAccessToken(c);
		return t.getTicket(accessToken);
	}

	public static String getRequestURL(HttpServletRequest req) {
		String query = req.getQueryString();
		if (query != null) {
			return req.getRequestURL() + "?" + query;
		} else {
			return req.getRequestURL().toString();
		}
	}

	public String getJSConfig(WXConfig c, HttpServletRequest req) throws IOException {
		return getJSConfig(c, getRequestURL(req));
	}

	public String getJSConfig(WXConfig c, String url) throws IOException {
		String noncestr = RandomStringUtils.randomAlphanumeric(32);
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String signature;
		{
			Map<String, Object> param = new TreeMap<String, Object>();
			param.put("noncestr", noncestr);
			param.put("jsapi_ticket", getJSTicket(c));
			param.put("timestamp", timestamp);
			param.put("url", url);
			StringBuilder buff = new StringBuilder();
			for (Map.Entry<String, Object> e : param.entrySet()) {
				if (buff.length() > 0) {
					buff.append('&');
				}
				buff.append(e.getKey()).append('=').append(e.getValue().toString());
			}
			signature = DigestUtils.shaHex(buff.toString());
		}
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("appId", c.app_id);
		config.put("timestamp", timestamp);
		config.put("nonceStr", noncestr);
		config.put("signature", signature);
		return om.writeValueAsString(config);
	}

	public Map<String, Object> getJsapiAddressConfig(WXConfig c, String openid, String url) throws ErrorCodeException, IOException {
		String accesstoken = oauthService.get_access_token(c.app_id, openid);
		//
		String noncestr = RandomStringUtils.randomAlphanumeric(32);
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String signature;
		{
			Map<String, Object> param = new TreeMap<String, Object>();
			param.put("appid", c.getApp_id());
			param.put("url", url);
			param.put("timestamp", timestamp);
			param.put("accesstoken", accesstoken);
			param.put("noncestr", noncestr);
			StringBuilder buff = new StringBuilder();
			for (Map.Entry<String, Object> e : param.entrySet()) {
				if (buff.length() > 0) {
					buff.append('&');
				}
				buff.append(e.getKey()).append('=').append(e.getValue().toString());
			}
			signature = DigestUtils.shaHex(buff.toString()).toLowerCase();
		}
		Map<String, Object> config = new HashMap<String, Object>();
		config.put("appId", c.app_id);
		config.put("scope", "jsapi_address");
		config.put("signType", "sha1");
		config.put("addrSign", signature);
		config.put("timeStamp", timestamp);
		config.put("nonceStr", noncestr);
		return config;
	}
}
