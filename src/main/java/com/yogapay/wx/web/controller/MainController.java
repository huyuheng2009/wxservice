package com.yogapay.wx.web.controller;

import com.yogapay.core.ErrorCodeException;
import com.yogapay.core.Result;
import com.yogapay.wx.entity.WXWebConfig;
import com.yogapay.wx.service.ConfigService;
import com.yogapay.wx.service.WXCommonService;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	@Autowired
	private WXCommonService wxCommonService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ObjectMapper om;

	@RequestMapping("wx-service-config.js")
	public void js(
			@RequestParam String uid,
			HttpServletRequest req, HttpServletResponse res) throws IOException {
		Result ret = new Result();
		a:
		try {
			String referer = req.getHeader("Referer");
			if (referer == null) {
				ret.setErrorCode(1);
				ret.setMessage("获取不到 Referer");
				break a;
			}
			WXWebConfig c = configService.queryWebConfig(uid);
			if (c == null) {
				ret.setErrorCode(2);
				ret.setMessage("uid is not exists");
				break a;
			}
			String json = wxCommonService.getJSConfig(c, referer);
			ret.setValue(json);
		} catch (Exception ex) {
			ret.setErrorCode(-100);
			ret.setMessage("加载出错！");
			LoggerFactory.getLogger(MainController.class).error(null, ex);
		}
		res.setCharacterEncoding("UTF-8");
		res.setBufferSize(1024 * 8);
		res.setContentType("text/javascript;charset=UTF-8");
		PrintWriter w = res.getWriter();
		try {
			if (ret.getErrorCode() == 0) {
				w.print(String.format("var wxService={config:%s};", ret.getValue()));
			} else {
				w.print(String.format("alert('wx-service-config.js\\r\\n%s');", ret.getMessage()));
			}
			w.flush();
		} finally {
			w.close();
		}
	}

	@RequestMapping("edit_address")
	public String edit_address(
			@RequestParam String uid,
			@RequestParam String openid,
			@RequestParam String wx_edit_address,
			@RequestParam(required = false) String title,
			ModelMap mmap, HttpServletRequest req) throws IOException {
		mmap.put("page.title", title);
		Result ret = new Result();
		a:
		try {
			String referer = req.getRequestURL().toString();
			if (referer == null) {
				ret.setErrorCode(1);
				ret.setMessage("获取不到 Referer");
				break a;
			}
			if (req.getQueryString() != null) {
				referer += "?" + req.getQueryString();
			}
			WXWebConfig c = configService.queryWebConfig(uid);
			if (c == null) {
				ret.setErrorCode(2);
				ret.setMessage("uid is not exists");
				break a;
			}
			Map<String, Object> config = wxCommonService.getJsapiAddressConfig(c, openid, referer);
			ret.setValue(config);
		} catch (Exception ex) {
			ret.setErrorCode(-100);
			ret.setMessage("加载出错！");
			LoggerFactory.getLogger(MainController.class).error(null, ex);
		}
		mmap.put("ret", om.writeValueAsString(ret));
		mmap.put("jsonpUrl", wx_edit_address);
		return "edit_address";
	}

	public Result error(Exception ex) {
		if (ex instanceof ErrorCodeException) {
			ErrorCodeException _e = (ErrorCodeException) ex;
			return new Result(_e.getErrorCode(), _e.getMessage(), null);
		}
		LoggerFactory.getLogger(MainController.class).error(null, ex);
		return new Result(-2, ex.getMessage(), null);
	}

	@RequestMapping("error")
	public Object error(HttpServletRequest req) {
		Exception e = (Exception) req.getAttribute("exception");
		if (e == null) {
			return new ResponseEntity<Object>("未知错误", HttpStatus.BAD_REQUEST);
		}
		Result ret = error(e);
		req.setAttribute("ret", ret);
		return "error";
	}

	@RequestMapping(value = "error", produces = "application/json")
	@ResponseBody
	public Object error_json(HttpServletRequest req) {
		Exception e = (Exception) req.getAttribute("exception");
		if (e == null) {
			return new ResponseEntity<Object>("未知错误", HttpStatus.BAD_REQUEST);
		}
		return error(req);
	}

}
