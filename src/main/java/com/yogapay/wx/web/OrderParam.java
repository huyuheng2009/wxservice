package com.yogapay.wx.web;

import java.io.Serializable;

public class OrderParam implements Serializable {

	private static final long serialVersionUID = 20151010L;
	public String uid;
	public String body;
	public String trade_no;
	public int total_fee;
	public String notify_url;

	public OrderParam(String uid, String body, String trade_no, int total_fee, String notify_url) {
		this.uid = uid;
		this.body = body;
		this.trade_no = trade_no;
		this.total_fee = total_fee;
		this.notify_url = notify_url;
	}

}
