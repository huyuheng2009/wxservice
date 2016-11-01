package com.yogapay.wx.entity;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.LoggerFactory;

public class WXJSTicket {

	private String ticket;
	private int expires_in = -1;
	//
	private long refresh_time;
	private long expires_time;

	public synchronized String getTicket(String accessToken) throws IOException {
		if (expires_in == -1 || System.currentTimeMillis() >= expires_time) {
			String query = String.format("access_token=%s&type=jsapi", accessToken);
			String json = IOUtils.toString(new URL("https://api.weixin.qq.com/cgi-bin/ticket/getticket?" + query), "UTF-8");
			LoggerFactory.getLogger(WXJSTicket.class).info("[{}][jsapi_ticket]返回：{}", new Object[]{accessToken, json});
			Map map = new ObjectMapper().readValue(json, Map.class);
			if (((Number) map.get("errcode")).intValue() != 0) {
				throw new IOException();
			}
			ticket = (String) map.get("ticket");
			expires_in = (Integer) map.get("expires_in");
			//
			refresh_time = System.currentTimeMillis();
			expires_time = refresh_time + 1000L * (expires_in - 300);
		}
		return ticket;
	}
}
