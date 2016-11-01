package com.yogapay.wx.service;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yogapay.sql.mybatis.BaseDAO;
import com.yogapay.util.HttpUtils;
import com.yogapay.util.StringUtils;
import com.yogapay.wx.web.Template;

@Service
public class TemplateService extends BaseDAO{

	public static final String ADD_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=";
	
	@Autowired
	private ObjectMapper objectMapper ;
	 
	public void addTemplate(String dsKey,String template_short_id,String token) throws Exception {

		
			Map<String, String> map = new HashMap<String, String>();
			map.put("template_id_short",template_short_id);
			
			HttpUtils httpUtils = new HttpUtils() ;
			String result = httpUtils.post(ADD_TEMPLATE + token, map, 60) ;
			
			Map<String, Object> rmap = objectMapper.readValue(result, Map.class) ;
			
			String errcode = rmap.get("errcode") == null ? "" : rmap.get("errcode") + "";
			String templateId = (String) rmap.get("template_id");
			
			if (StringUtils.isEmpty(errcode)||"0".equals(errcode)) {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("ds_key", dsKey);
				param.put("template_short_id", template_short_id);
				param.put("template_id", templateId);
				insert(STATEMENT_ID + "insertTemplate", param);
				System.out.println(errcode);
			}else {
				System.out.println(result);
			}
			
		
	}
	




	
	/**
	 * 通过short_id 获取tmplate
	 * @param key
	 * @param shortId
	 * @return
	 * @throws SQLException
	 */
	public Template getTmByKeyShort(String key,String shortId) throws SQLException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ds_key", key);
		param.put("template_short_id", shortId);
		return (Template) selectOne(STATEMENT_ID + "getTmByKeyShort", param);
}	
	
	
	
}
