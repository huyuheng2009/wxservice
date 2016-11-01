package com.yogapay.wx.web;

import java.io.Serializable;
import java.util.Date;

public class AccessToken implements Serializable {

	private static final long serialVersionUID = 201605111L;

	private String access_token;
	private int expires_in;
	private String account;
	private Date create_time;
	private String appid;
	private String secret;
	private String ds_key;
	private String industry;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public int getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getDs_key() {
		return ds_key;
	}
	public void setDs_key(String ds_key) {
		this.ds_key = ds_key;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}

	
	
}
