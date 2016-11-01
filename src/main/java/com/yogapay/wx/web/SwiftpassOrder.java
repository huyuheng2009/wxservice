package com.yogapay.wx.web;

import org.apache.commons.lang.RandomStringUtils;

public class SwiftpassOrder extends SwiftpassResponse {

	private int id;
	@RequiredField
	public final String service = "pay.weixin.jspay";
	@RequiredField
	public String mch_id;
	/**
	 是否原生态 is_raw 否 String(1) 1：是；0：否；默认：0
	 */
	@RequiredField(nullable = true)
	public String is_raw;
	@RequiredField
	public String out_trade_no;
	@RequiredField
	public String body;
	@RequiredField
	public String sub_openid;
	@RequiredField
	public int total_fee;
	@RequiredField
	public String mch_create_ip;
	@RequiredField
	public String notify_url;
	@RequiredField
	public String nonce_str = RandomStringUtils.randomAlphanumeric(32);
	//
	public String token_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getIs_raw() {
		return is_raw;
	}

	public void setIs_raw(String is_raw) {
		this.is_raw = is_raw;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSub_openid() {
		return sub_openid;
	}

	public void setSub_openid(String sub_openid) {
		this.sub_openid = sub_openid;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getMch_create_ip() {
		return mch_create_ip;
	}

	public void setMch_create_ip(String mch_create_ip) {
		this.mch_create_ip = mch_create_ip;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getToken_id() {
		return token_id;
	}

	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}

}
