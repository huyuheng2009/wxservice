package com.yogapay.wx.web.controller;

import com.yogapay.core.WebUtils;
import com.yogapay.web.spring.ResponseEntityFactory;
import com.yogapay.wx.entity.Oauth2;
import com.yogapay.wx.entity.WXWebConfig;
import com.yogapay.wx.service.ConfigService;
import com.yogapay.wx.service.OauthService;
import com.yogapay.wx.web.Oauth2AccessToken;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("oauth")
public class OauthController {

	@Autowired
	private ConfigService configService;
	@Autowired
	private OauthService oauthService;

	@RequestMapping("register")
	public void register(
			@RequestParam String uid,
			@RequestParam(defaultValue = "") String random,
			@RequestParam(required = false) String token,
			@RequestParam(defaultValue = "snsapi_base") String scope,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		WXWebConfig c = configService.queryWebConfig(uid);
		if (c == null) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST, "uid is not exists");
			return;
		}
		Oauth2 oauth2 = new Oauth2();
		oauth2.setUid(uid);
		oauth2.setRandom(random);
		oauth2.setToken(token);
		String regToken = oauthService.create(oauth2);
		StringBuilder buff = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize?");
		buff.append("appid=").append(c.app_id);
		buff.append("&redirect_uri=").append(URLEncoder.encode(WebUtils.getUrl(req, "callback"), "UTF-8"));
		buff.append("&response_type=code");
		buff.append("&scope=").append(scope);
		buff.append("&state=").append(regToken);
		buff.append("#wechat_redirect");
		res.sendRedirect(buff.toString());
	}

	@RequestMapping("callback")
	public Object callback(
			@RequestParam String code,
			@RequestParam(value = "state") String regToken,
			ModelMap mmap) throws Exception {
		//不能使用remove，客户端网络问题，可能会请求两次。第二次就无效了
		Oauth2 o = oauthService.get(regToken);
		if (o == null) {
			return new ResponseEntity<Object>("[wxservice] token is not exists.", HttpStatus.BAD_REQUEST);
		}
		WXWebConfig c = configService.queryWebConfig(o.getUid());
		Oauth2AccessToken token = oauthService.access_token(c.app_id, c.app_secret, code);
		if (token.getErrcode() != 0) {
			return ResponseEntityFactory.textPlain(token.getResponse());
		}
		mmap.put("openid", token.getOpenid());
		mmap.put("access_token", token.getAccess_token());
		mmap.put("random", o.getRandom());
		if (o.getToken() != null) {
			mmap.put("token", o.getToken());
		}
		if (c.isOauth_pay()) {
			mmap.put("oauth_pay", 1);
		}
		if (c.sign_salt != null) {
			String sign = WebUtils.sign(mmap, c.sign_salt);
			mmap.put("sign", sign);
		}
		return "redirect:" + c.oauthURL;
	}

}
