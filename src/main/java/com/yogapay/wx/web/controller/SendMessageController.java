package com.yogapay.wx.web.controller;


import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yogapay.util.StringUtils;
import com.yogapay.wx.service.SendMessageService;
import com.yogapay.wx.web.AccessToken;

@Controller
@RequestMapping(value = "/sendMessage")
public class SendMessageController extends BaseController {

	Logger log = LoggerFactory.getLogger(SendMessageController.class);
	@Resource
	private SendMessageService sendMessageService;
	@Autowired
	private ObjectMapper objectMapper ;

	/**
	 * 下单成功通知
	 *
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"/orderaccept"})
	public void orderaccept(@RequestParam Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) {
		String r = "{\"errcode\":\"9000\",\"errmsg\":\"异常\"}" ;
		String dskey = StringUtils.nullString(params.get("dskey")) ;
		try {
			AccessToken token = sendMessageService.getAccessToken(dskey) ;
			if (token==null) {
				outJson("{\"errcode\":\"9001\",\"errmsg\":\"参数dskey无效\"}", response);
				return ;
			}	
			//templateService.initTemplate(params.get("dskey"),false);
			r = sendMessageService.orderaccept(params,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		outJson(r, response);
	}
	
	
	/**
	 * 收单成功通知
	 *
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping(value = {"/ordertake"})
	public void ordertake(@RequestParam Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) {
		String r = "{\"errcode\":\"9000\",\"errmsg\":\"异常\"}" ;
		String dskey = StringUtils.nullString(params.get("dskey")) ;
		try {
			AccessToken token = sendMessageService.getAccessToken(dskey) ;
			if (token==null) {
				outJson("{\"errcode\":\"9001\",\"errmsg\":\"参数dskey无效\"}", response);
				return ;
			}
			r = sendMessageService.orderTake(params,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		outJson(r, response);
	}



	/**
	 * 派件通知
	 *
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = {"/pickup"})
	public void pickup(@RequestParam Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) {
		String r = "{\"errcode\":\"9000\",\"errmsg\":\"异常\"}" ;
		String dskey = StringUtils.nullString(params.get("dskey")) ;
		try {
			AccessToken token = sendMessageService.getAccessToken(dskey) ;
			if (token==null) {
				outJson("{\"errcode\":\"9001\",\"errmsg\":\"参数dskey无效\"}", response);
				return ;
			}
			r = sendMessageService.pickup(params,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		outJson(r, response);
	}

	/**
	 * 签收通知
	 *
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"/signin"})
	public void signin(@RequestParam Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) {
		String r = "{\"errcode\":\"9000\",\"errmsg\":\"异常\"}" ;
		String dskey = StringUtils.nullString(params.get("dskey")) ;
		try {
			AccessToken token = sendMessageService.getAccessToken(dskey) ;
			if (token==null) {
				outJson("{\"errcode\":\"9001\",\"errmsg\":\"参数dskey无效\"}", response);
				return ;
			}
			r = sendMessageService.signin(params,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		outJson(r, response);
	}

	/**
	 * 快件异常通知
	 *
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"/pro"})
	public void pro(@RequestParam Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) {
		String r = "{\"errcode\":\"9000\",\"errmsg\":\"异常\"}" ;
		String dskey = StringUtils.nullString(params.get("dskey")) ;
		try {
			AccessToken token = sendMessageService.getAccessToken(dskey) ;
			if (token==null) {
				outJson("{\"errcode\":\"9001\",\"errmsg\":\"参数dskey无效\"}", response);
				return ;
			}
			r = sendMessageService.pro(params,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		outJson(r, response);
	}
	
	
	/**
	 * 
	 *
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"/cod"})
	public void cod(@RequestParam Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) {
		String r = "{\"errcode\":\"9000\",\"errmsg\":\"异常\"}" ;
		String dskey = StringUtils.nullString(params.get("dskey")) ;
		try {
			AccessToken token = sendMessageService.getAccessToken(dskey) ;
			if (token==null) {
				outJson("{\"errcode\":\"9001\",\"errmsg\":\"参数dskey无效\"}", response);
				return ;
			}
			int t = Integer.valueOf(StringUtils.nullString(params.get("t"),"0")) ;
			switch (t) {
			case 1:
				r = sendMessageService.orderaccept(params,token);
				break;
			case 2:
				r = sendMessageService.orderTake(params,token) ;
				break;
			case 3:
				r = sendMessageService.pickup(params,token) ;
				break;
			case 4:
				r = sendMessageService.signin(params,token) ;
				break;
			case 5:
				r = sendMessageService.pro(params,token);
				break;
			default:
				break;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		outJson(r, response);
	}
	
	
	/**
	 * 通知通用接口
	 *
	 * @param params，包含dskey，模板id，模板数据data的json格式
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"/commom"})
	public void commom(@RequestParam Map<String, String> params,
			HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println(params);
		System.out.println(params.get("data"));
		
		String r = "{\"errcode\":\"9000\",\"errmsg\",\"异常\"}" ;
		if (StringUtils.isEmptyWithTrim(StringUtils.nullString(params.get("dskey")))||
				StringUtils.isEmptyWithTrim(StringUtils.nullString(params.get("tmplateId")))||
				StringUtils.isEmptyWithTrim(StringUtils.nullString(params.get("data")))||
				StringUtils.isEmptyWithTrim(StringUtils.nullString(params.get("touser")))) {
			outJson("{\"errcode\":\"9001\",\"errmsg\":\"参数异常\"}", response);
			return ;
		}
		
		String dskey = StringUtils.nullString(params.get("dskey")) ;
		String tmplateId = StringUtils.nullString(params.get("tmplateId")) ;
		String data = StringUtils.nullString(params.get("data")) ;
		String touser = StringUtils.nullString(params.get("touser")) ;
		
		 Object pdata = null ;
		 boolean val = true ;
		try {
			  pdata = objectMapper.readValue(data, Map.class) ;
		} catch (Exception e) {
			e.printStackTrace();
            val = false ;
		}
		if (!val) {
			outJson("{\"errcode\":\"9001\",\"errmsg\":\"参数data异常\"}", response);
			return ;
		}
		
		try {
			
			AccessToken token = sendMessageService.getAccessToken(dskey) ;
			if (token==null) {
				outJson("{\"errcode\":\"9001\",\"errmsg\":\"参数dskey无效\"}", response);
				return ;
			}	
			r = sendMessageService.commom(dskey,StringUtils.nullString(params.get("tmplateId")),pdata,touser,params,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		outJson(r, response);
	}
	
	
	
}
