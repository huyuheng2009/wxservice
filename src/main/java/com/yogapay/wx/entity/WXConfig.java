package com.yogapay.wx.entity;

public class WXConfig {

	public String uid;
	public String app_id;
	public String app_secret;
	public String mch_id;
	public String mch_secret;
	public String sign_salt;
	public String channel;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getApp_secret() {
		return app_secret;
	}

	public void setApp_secret(String app_secret) {
		this.app_secret = app_secret;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getMch_secret() {
		return mch_secret;
	}

	public void setMch_secret(String mch_secret) {
		this.mch_secret = mch_secret;
	}

	public String getSign_salt() {
		return sign_salt;
	}

	public void setSign_salt(String sign_salt) {
		this.sign_salt = sign_salt;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

}
