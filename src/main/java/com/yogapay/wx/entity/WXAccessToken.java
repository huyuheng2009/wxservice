package com.yogapay.wx.entity;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;

public class WXAccessToken {

	private String access_token;
	private int expires_in = -1;
	//
	private long refresh_time;
	private long expires_time;

	public synchronized String getAccessToken(WXConfig c) throws IOException {
		if (expires_in == -1 || System.currentTimeMillis() >= expires_time) {
			String query = String.format("grant_type=client_credential&appid=%s&secret=%s", c.app_id, c.app_secret);
			String json = IOUtils.toString(new URL("https://api.weixin.qq.com/cgi-bin/token?" + query), "UTF-8");
			LoggerFactory.getLogger(WXAccessToken.class).info("[{}][access_token]返回：{}", new Object[]{c.app_id, json});
			Map map = new ObjectMapper().readValue(json, Map.class);
			Number errcode = (Number) map.get("errcode");
			if (errcode != null && (errcode).intValue() != 0) {
				throw new IOException();
			}
			access_token = (String) map.get("access_token");
			expires_in = (Integer) map.get("expires_in");
			//
			refresh_time = System.currentTimeMillis();
			expires_time = refresh_time + 1000L * (expires_in - 300);
		}
		return access_token;
	}
}
