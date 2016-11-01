package com.yogapay.wx.service;

import com.yogapay.sql.mybatis.BaseDAO;
import com.yogapay.util.HttpUtils;
import com.yogapay.wx.entity.WXConfig;
import com.yogapay.wx.web.AccessToken;
import com.yogapay.wx.web.Template;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendMessageService extends BaseDAO {

	Logger log = LoggerFactory.getLogger(SendMessageService.class);
	public static final String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
	
	@Autowired
	private WXCommonService wxCommonService ;
	@Autowired
	private TemplateService templateService ;
	@Autowired
	private ObjectMapper objectMapper ;
	

	// 订单受理通知
	public String orderaccept(Map<String, String> params,AccessToken token) throws Exception {
		// 1.获取access_token
		String dskey = params.get("dskey");
		String tm = "OPENTM205471398";
		Template template = templateService.getTmByKeyShort(dskey, tm) ;
		 String tk = getRefeshToken(token) ;
		 
		if (template==null) {
			templateService.addTemplate(dskey, tm, tk);
			template = templateService.getTmByKeyShort(dskey, tm) ;
		}
		if (template==null) {
			template = new Template() ;
			template.setTemplate_id("");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("url", params.get("url"));
		map.put("touser", params.get("touser"));
		map.put("template_id", template.getTemplate_id());
		
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>() ;
		 Map<String, String> first = new HashMap<String, String>() ;
		 first.put("value", params.get("first"));
		
		 Map<String, String> keyword1 = new HashMap<String, String>() ;
		 keyword1.put("value", params.get("sendName"));
		 
		 Map<String, String> keyword2 = new HashMap<String, String>() ;
		 keyword2.put("value", params.get("sendPhone"));
		 
		 Map<String, String> keyword3 = new HashMap<String, String>() ;
		 keyword3.put("value", params.get("lgcName"));
		
		 
		 Map<String, String> keyword4 = new HashMap<String, String>() ;
		 keyword4.put("value", params.get("sendAddr"));
		 
		 Map<String, String> remark = new HashMap<String, String>() ;
		 remark.put("value", params.get("remark"));
		 
		 data.put("first", first) ;
		 data.put("keyword1", keyword1) ;
		 data.put("keyword2", keyword2) ;
		 data.put("keyword3", keyword3) ;
		 data.put("keyword4", keyword4) ;
		 data.put("remark", remark) ;
		 map.put("data", data) ;
		 
		 String post = objectMapper.writeValueAsString(map) ;
		 
		HttpUtils httpUtils = new HttpUtils() ;
		String result = httpUtils.postJson(SEND_MESSAGE_URL + tk, post, 60) ;
		 
		log.info("result=" + result);

		return result;
	}
	
	
	
	public String orderTake(Map<String, String> params,AccessToken token)throws Exception {
		// 1.获取access_token
		String dskey = params.get("dskey");
		String tm = "TM00071";
		Template template = templateService.getTmByKeyShort(dskey, tm) ;
		 String tk = getRefeshToken(token) ;
		 
		if (template==null) {
			templateService.addTemplate(dskey, tm, tk);
			template = templateService.getTmByKeyShort(dskey, tm) ;
		}
		if (template==null) {
			template = new Template() ;
			template.setTemplate_id("");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("url", params.get("url"));
		map.put("touser", params.get("touser"));
		map.put("template_id",template.getTemplate_id());
		
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>() ;
		 Map<String, String> lgcOrderNo = new HashMap<String, String>() ;
		 lgcOrderNo.put("value", params.get("lgcOrderNo"));
		 
		 Map<String, String> company = new HashMap<String, String>() ;
		 company.put("value", "我们");
		 
		 Map<String, String> remark = new HashMap<String, String>() ;
		 remark.put("value", params.get("remark"));
		 
		 data.put("orderNumber", lgcOrderNo) ;
		 data.put("company", company) ;
		 data.put("remark", remark) ;
		 map.put("data", data) ;
		 
		 String post = objectMapper.writeValueAsString(map) ;
		 
		HttpUtils httpUtils = new HttpUtils() ;
		
		log.info("token=" + tk );
		log.info("post=" + post);
		String result = httpUtils.postJson(SEND_MESSAGE_URL+tk, post, 60) ;
		 
		log.info("result=" + result);

		return result;
	}

	// 派件
	public String pickup(Map<String, String> params,AccessToken token)throws Exception {
		// 1.获取access_token
		String dskey = params.get("dskey");
		String tm = "TM00453";
		Template template = templateService.getTmByKeyShort(dskey, tm) ;
		 String tk = getRefeshToken(token) ;
		 
		if (template==null) {
			templateService.addTemplate(dskey, tm, tk);
			template = templateService.getTmByKeyShort(dskey, tm) ;
		}
		if (template==null) {
			template = new Template() ;
			template.setTemplate_id("");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("url", params.get("url"));
		map.put("touser", params.get("touser"));
		map.put("template_id", template.getTemplate_id());
		
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>() ;
		
		 Map<String, String> first = new HashMap<String, String>() ;
		 first.put("value", params.get("first"));
		 
		 
		 Map<String, String> lgcOrderNo = new HashMap<String, String>() ;
		 lgcOrderNo.put("value", params.get("lgcOrderNo"));
		 
		 Map<String, String> contact = new HashMap<String, String>() ;
		 contact.put("value", params.get("contact"));
		 
		 Map<String, String> remark = new HashMap<String, String>() ;
		 remark.put("value", params.get("remark"));
		 
		 data.put("first", first) ;
		 data.put("waybillNo", lgcOrderNo) ;
		 data.put("contact", contact) ;
		 data.put("remark", remark) ;
		 map.put("data", data) ;
		 
		 String post = objectMapper.writeValueAsString(map) ;
		 
		 log.info("token=" + tk );
			log.info("post=" + post);
		HttpUtils httpUtils = new HttpUtils() ;
		String result = httpUtils.postJson(SEND_MESSAGE_URL+tk, post, 60) ;
		 
		log.info("result=" + result);

		return result;
	}

	// 签收
	public String signin(Map<String, String> params,AccessToken token)throws Exception {
		// 1.获取access_token
		String dskey = params.get("dskey");
		String tm = "TM00452";
		Template template = templateService.getTmByKeyShort(dskey, tm) ;
		 String tk = getRefeshToken(token) ;
		 
		if (template==null) {
			templateService.addTemplate(dskey, tm, tk);
			template = templateService.getTmByKeyShort(dskey, tm) ;
		}
		if (template==null) {
			template = new Template() ;
			template.setTemplate_id("");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("url", params.get("url"));
		map.put("touser", params.get("touser"));
		map.put("template_id", template.getTemplate_id());
		
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>() ;
		
		 Map<String, String> first = new HashMap<String, String>() ;
		 first.put("value", params.get("first"));
		 
		 
		 Map<String, String> lgcOrderNo = new HashMap<String, String>() ;
		 lgcOrderNo.put("value", params.get("lgcOrderNo"));
		 
		 Map<String, String> time = new HashMap<String, String>() ;
		 time.put("value", params.get("stime"));
		 
		 Map<String, String> remark = new HashMap<String, String>() ;
		 remark.put("value", params.get("remark"));
		 
		 data.put("first", first) ;
		 data.put("waybillNo", lgcOrderNo) ;
		 data.put("time", time) ;
		 data.put("remark", remark) ;
		 map.put("data", data) ;
		 
		 String post = objectMapper.writeValueAsString(map) ;
		 
		HttpUtils httpUtils = new HttpUtils() ;
		String result = httpUtils.postJson(SEND_MESSAGE_URL+tk, post, 60) ;
		 
		log.info("result=" + result);

		return result;
	}
	
	
	// 问题件
	public String pro(Map<String, String> params,AccessToken token)throws Exception {
		// 1.获取access_token
		String dskey = params.get("dskey");
		String tm = "OPENTM205321418";
		Template template = templateService.getTmByKeyShort(dskey, tm) ;
		 String tk = getRefeshToken(token) ;
		 
		if (template==null) {
			templateService.addTemplate(dskey, tm, tk);
			template = templateService.getTmByKeyShort(dskey, tm) ;
		}
		if (template==null) {
			template = new Template() ;
			template.setTemplate_id("");
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("url", params.get("url"));
		map.put("touser", params.get("touser"));
		map.put("template_id",template.getTemplate_id());
		
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>() ;
		
		 Map<String, String> first = new HashMap<String, String>() ;
		 first.put("value", params.get("first"));
		 
		 
		 Map<String, String> lgcOrderNo = new HashMap<String, String>() ;
		 lgcOrderNo.put("value", params.get("lgcOrderNo"));
		 
		 Map<String, String> keyword2 = new HashMap<String, String>() ;
		 keyword2.put("value", params.get("stime"));
		 
		 Map<String, String> remark = new HashMap<String, String>() ;
		 remark.put("value", params.get("remark"));
		 
		 data.put("first", first) ;
		 data.put("keyword1", lgcOrderNo) ;
		 data.put("keyword2", keyword2) ;
		 data.put("remark", remark) ;
		 map.put("data", data) ;
		 
		 String post = objectMapper.writeValueAsString(map) ;
		 
		HttpUtils httpUtils = new HttpUtils() ;
		String result = httpUtils.postJson(SEND_MESSAGE_URL+tk, post, 60) ;
		 
		log.info("result=" + result);

		return result;
	}
	
	
	

	public String commom(String dskey, String tmplateId, Object data,String touser,Map<String, String> params,AccessToken token) throws Exception {
		// 1.获取access_token
		
		Template template = templateService.getTmByKeyShort(dskey,tmplateId) ;
		 String tk = getRefeshToken(token) ;
		 
		if (template==null) {
			templateService.addTemplate(dskey, tmplateId, tk);
			template = templateService.getTmByKeyShort(dskey, tmplateId) ;
		}
		if (template==null) {
			template = new Template() ;
			template.setTemplate_id("");
		}
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("url", params.get("url"));
		map.put("touser", touser);
		map.put("template_id", template.getTemplate_id());
		
		 //Object pdata = JSON.parse(data) ;
		 map.put("data", data) ;
		 
		 String post = objectMapper.writeValueAsString(map) ;
		 
		HttpUtils httpUtils = new HttpUtils() ;
		String result = httpUtils.postJson(SEND_MESSAGE_URL+tk, post, 60) ;
		 
		log.info("result=" + result);
		return result;
	}
	
	
	
	/**
	 * 
	 * @param key
	 * @return
	 * @throws SQLException
	 * @throws IOException 
	 */
	public String  getRefeshToken(AccessToken token) throws Exception {
      WXConfig config = new WXConfig() ;
      config.setApp_id(token.getAppid());
      config.setApp_secret(token.getSecret());
       return wxCommonService.getAccessToken(config) ;
}
	
	public AccessToken getAccessToken(String dskey) throws IOException {
		return (AccessToken) selectOne("AccessToken.getByKey", dskey);

	}
	
}
