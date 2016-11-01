package com.yogapay.wx.entity;

public class WXWebConfig extends WXConfig {

	public String oauthURL;
	public boolean oauth_pay;
	public Integer total_fee;
	public String spbill_create_ip;
	public String tradeSuccess;
	public String tradeFailed;

	public String buildOutTradeNo(String trade_no) {
		return uid + "_" + trade_no;
	}

	public String getOauthURL() {
		return oauthURL;
	}

	public void setOauthURL(String oauthURL) {
		this.oauthURL = oauthURL;
	}

	public boolean isOauth_pay() {
		return oauth_pay;
	}

	public void setOauth_pay(boolean oauth_pay) {
		this.oauth_pay = oauth_pay;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTradeSuccess() {
		return tradeSuccess;
	}

	public void setTradeSuccess(String tradeSuccess) {
		this.tradeSuccess = tradeSuccess;
	}

	public String getTradeFailed() {
		return tradeFailed;
	}

	public void setTradeFailed(String tradeFailed) {
		this.tradeFailed = tradeFailed;
	}

}
