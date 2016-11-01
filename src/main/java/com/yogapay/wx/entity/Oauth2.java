package com.yogapay.wx.entity;

import java.io.Serializable;

public class Oauth2 implements Serializable {

	private static final long serialVersionUID = 20151022L;
	private int id;
	private String token;
	private String uid;
	private String random;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

}
