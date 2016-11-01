package com.yogapay.wx.web;

import java.io.IOException;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;

public class WXOrderQuery extends WXResponse {

	/**
	 微信分配的公众账号ID（企业号corpid即为此appId）
	 */
	@RequiredField
	public String appid;
	/**
	 微信支付分配的商户号
	 */
	@RequiredField
	public String mch_id;
	/**
	 商户系统内部的订单号，当没提供transaction_id时需要传这个。 
	 */
	@RequiredField
	public String out_trade_no;
	/**
	 随机字符串，不长于32位。
	 */
	@RequiredField
	public String nonce_str = RandomStringUtils.randomAlphanumeric(32);
	//
	public String trade_state;
	public int total_fee;
	public String attach;

	public Map<String, Object> getAttach() throws IOException {
		return new ObjectMapper().readValue(attach, Map.class);
	}
}
