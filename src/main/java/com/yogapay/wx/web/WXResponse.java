package com.yogapay.wx.web;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class WXResponse extends URLResponse {

	private String request;
	public String return_code;
	public String return_msg;
	public String result_code;
	public String err_code;
	public String err_code_des;
	protected Map<String, String> xmlAttributes;

	public Map<String, Object> buildParams() {
		Map<String, Object> param = new LinkedHashMap<String, Object>();
		try {
			ObjectMapper om = null;
			for (Field t : getClass().getFields()) {
				RequiredField a = t.getAnnotation(RequiredField.class);
				if (a == null) {
					continue;
				}
				Object value = t.get(this);
				if (value == null) {
					if (!a.nullable()) {
						throw new RuntimeException(String.format("字段 %s 不能为 null", t.getName()));
					}
				} else {
					if (value instanceof Map) {
						if (om == null) {
							om = new ObjectMapper();
						}
						value = om.writeValueAsString(value);
					}
					param.put(t.getName(), value);
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
		return param;
	}

	public void parseResponse() {
		if (response == null) {
			return;
		}
		try {
			xmlAttributes = new HashMap<String, String>();
			Document doc = DocumentHelper.parseText(response);
			for (Iterator it = doc.getRootElement().elementIterator(); it.hasNext();) {
				Object next = it.next();
				if (next instanceof Element) {
					Element e = (Element) next;
					String name = e.getName();
					String value = e.getText();
					xmlAttributes.put(name, value);
				}
			}
			ObjectMapper om = null;
			for (Field f : getClass().getFields()) {
				String v = xmlAttributes.get(f.getName());
				if (v != null) {
					Class<?> type = f.getType();
					if (type == int.class || type == Integer.class) {
						f.set(this, Integer.parseInt(v));
					} else if (Map.class.isAssignableFrom(type)) {
						if (om == null) {
							om = new ObjectMapper();
						}
						f.set(this, om.readValue(v, type));
					} else {
						f.set(this, v);
					}
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException(response, ex);
		} catch (DocumentException ex) {
			throw new RuntimeException(response, ex);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(response, ex);
		} catch (IllegalAccessException ex) {
			throw new RuntimeException(response, ex);
		}
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public Map<String, String> getXmlAttributes() {
		return xmlAttributes;
	}

}
