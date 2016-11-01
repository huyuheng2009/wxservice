package com.yogapay.wx.service;

import com.yogapay.sql.mybatis.BaseDAO;
import com.yogapay.wx.entity.WXConfig;
import com.yogapay.wx.entity.WXWebConfig;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ConfigService extends BaseDAO {

	public WXWebConfig queryWebConfig(String key) {
		return (WXWebConfig) selectOne(STATEMENT_ID + "queryWebConfig", key);
	}

	public WXConfig queryOne(String app_id, String mch_id) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("app_id", app_id);
		param.put("mch_id", mch_id);
		return (WXConfig) selectOne(STATEMENT_ID + "queryOne", param);
	}
}
