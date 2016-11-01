package com.yogapay.wx.web;

import com.yogapay.wx.entity.WXWebConfig;

public class PayParam {

	public final WXWebConfig c;
	public final OrderParam op;
	public final String openid;

	public PayParam(WXWebConfig c, OrderParam op, String openid) {
		this.c = c;
		this.op = op;
		this.openid = openid;
	}

}
