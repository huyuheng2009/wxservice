package com.yogapay.wx.web;

import java.io.Serializable;
import java.util.Date;

public class Template implements Serializable {

	private static final long serialVersionUID = 201605112L;

	private String ds_key;
	private String template_short_id;
	private String template_id;
	private String descb;
	private String source;
	public String getDs_key() {
		return ds_key;
	}
	public void setDs_key(String ds_key) {
		this.ds_key = ds_key;
	}
	public String getTemplate_short_id() {
		return template_short_id;
	}
	public void setTemplate_short_id(String template_short_id) {
		this.template_short_id = template_short_id;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getDescb() {
		return descb;
	}
	public void setDescb(String descb) {
		this.descb = descb;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

	
	
}
