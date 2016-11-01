package com.yogapay.wx.service;

import com.yogapay.core.service.CommonCacheService;
import com.yogapay.sql.mybatis.BaseDAO;
import com.yogapay.wx.ErrorCodeException;
import com.yogapay.wx.entity.Oauth2;
import com.yogapay.wx.web.Oauth2AccessToken;
import com.yogapay.wx.web.URLResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OauthService extends BaseDAO {

	@Autowired
	private ObjectMapper om;
	@Resource
	private CommonCacheService<Oauth2> oauthCacheService;

	public Oauth2 get(String token) throws SQLException {
		return oauthCacheService.get(token);
	}

	public String create(Oauth2 oauth2) throws SQLException {
		String token;
		for (int i = 0;; i++) {
			if (i >= 100) {
				throw new RuntimeException();
			}
			token = RandomStringUtils.randomAlphanumeric(32);
			if (oauthCacheService.putIfAbsent(token, oauth2) == null) {
				break;
			}
		}
		return token;
	}

	private Oauth2AccessToken parse_access_token(String res) throws IOException {
		Oauth2AccessToken token = om.readValue(res, Oauth2AccessToken.class);
		token.setExpiresTime(new Date(System.currentTimeMillis() + (token.getExpires_in() - 10 * 60) * 1000));
		return token;
	}

	public Oauth2AccessToken access_token(String appid, String secret, String code) throws IOException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("appid", appid);
		param.put("secret", secret);
		param.put("code", code);
		param.put("grant_type", "authorization_code");
		URLResponse res = new URLResponse();
		Utils.post("https://api.weixin.qq.com/sns/oauth2/access_token", param, res);
		if (res.getResponse_code() != 200) {
			throw new IOException();
		}
		param.clear();
		Oauth2AccessToken token = parse_access_token(res.getResponse());
		token.setResponse(res.getResponse());
		if (token.getErrcode() == 0) {
			param.put("appid", appid);
			param.put("openid", token.getOpenid());
			param.put("access_token", token.getAccess_token());
			param.put("refresh_token", token.getRefresh_token());
			param.put("expiresTime", token.getExpiresTime());
			param.put("res", res.getResponse());
			update(STATEMENT_ID + "insertAccessToken", param);
		}
		return token;
	}

	public String get_access_token(String appid, String openid) throws ErrorCodeException, IOException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("appid", appid);
		param.put("openid", openid);
		Oauth2AccessToken token = (Oauth2AccessToken) selectOne(STATEMENT_ID + "queryAccessToken", param);
		if (token == null) {
			throw new ErrorCodeException(1, "token is not exists.");
		}
		param.clear();
		//
		if (token.getExpiresTime().getTime() <= System.currentTimeMillis()) {
			URLResponse res = new URLResponse();
			param.put("appid", appid);
			param.put("grant_type", "refresh_token");
			param.put("refresh_token", token.getRefresh_token());
			Utils.post("https://api.weixin.qq.com/sns/oauth2/refresh_token", param, res);
			if (res.getResponse_code() != 200) {
				throw new IOException();
			}
			param.clear();
			token = parse_access_token(res.getResponse());
			if (token.getErrcode() != 0) {
				throw new ErrorCodeException(2, "token can't refresh.");
			}
			//
			param.put("appid", appid);
			param.put("openid", openid);
			param.put("access_token", token.getAccess_token());
			param.put("refresh_token", token.getRefresh_token());
			param.put("expiresTime", token.getExpiresTime());
			param.put("refresh_res", res.getResponse());
			update(STATEMENT_ID + "updateAccessToken", param);
			param.clear();
		}
		return token.getAccess_token();
	}
}
