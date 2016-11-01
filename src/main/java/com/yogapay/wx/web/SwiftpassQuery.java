package com.yogapay.wx.web;

import org.apache.commons.lang.RandomStringUtils;

public class SwiftpassQuery extends SwiftpassResponse {

	@RequiredField
	public String service = "unified.trade.query";
	@RequiredField
	public String mch_id;
	@RequiredField
	public String out_trade_no;
	@RequiredField
	public String nonce_str = RandomStringUtils.randomAlphanumeric(32);
	//
	public String trade_state;
	public String transaction_id;

}
