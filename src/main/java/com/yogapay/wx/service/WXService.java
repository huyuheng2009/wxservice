package com.yogapay.wx.service;

import com.yogapay.core.ErrorCodeException;
import com.yogapay.core.ResultResourceBundle;
import com.yogapay.sql.mybatis.BaseDAO;
import com.yogapay.wx.entity.WXConfig;
import com.yogapay.wx.web.OrderNotice;
import com.yogapay.wx.web.UnifiedOrder;
import com.yogapay.wx.web.WXOrderQuery;
import com.yogapay.wx.web.WXResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WXService extends BaseDAO implements ParameterWriter {

	private final Log log = LogFactory.getLog(WXService.class);
	@Autowired
	private ObjectMapper om;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ResultResourceBundle bundle;

	public static void sign(Map<String, Object> source, String secret) {
		Map<String, Object> map = (source instanceof NavigableMap) ? source : new TreeMap<String, Object>(source);
		map.remove("sign");
		StringBuilder buff = new StringBuilder();
		for (Map.Entry<String, Object> t : map.entrySet()) {
			if (buff.length() > 0) {
				buff.append('&');
			}
			buff.append(t.getKey());
			buff.append('=');
			Object v = t.getValue();
			if (v.getClass().isArray()) {
				for (int i = 0, n = Array.getLength(v); i < n; i++) {
					buff.append(Array.get(v, i).toString());
				}
			} else {
				buff.append(t.getValue().toString());
			}
		}
		buff.append("&key=").append(secret);
		String sign = DigestUtils.md5Hex(buff.toString()).toUpperCase();
		source.put("sign", sign);
	}

	@Override
	public void write(Writer w, Map<String, Object> params) throws IOException {
		try {
			XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(w);
			try {
				out.writeStartElement("xml");
				for (Map.Entry<String, Object> t : params.entrySet()) {
					out.writeStartElement(t.getKey());
					out.writeCharacters(t.getValue().toString());
					out.writeEndElement();
				}
				out.writeEndElement();
				out.flush();
			} finally {
				out.close();
			}
		} catch (FactoryConfigurationError ex) {
			throw new IOException(ex);
		} catch (XMLStreamException ex) {
			throw new IOException(ex);
		}
	}

	private void success(WXResponse res) throws ErrorCodeException {
		res.parseResponse();
		if (!"SUCCESS".equals(res.return_code)) {
			throw bundle.errorCodeException(100, res.return_code, res.return_msg);
		}
		if (!"SUCCESS".equals(res.result_code)) {
			throw bundle.errorCodeException(101, res.return_code, res.err_code, res.err_code_des);
		}
	}

	private void success(String url, WXResponse res, String mch_secret) throws ErrorCodeException, IOException {
		Map<String, Object> param = res.buildParams();
		for (Map.Entry<String, Object> t : param.entrySet()) {
			Object v = t.getValue();
			if (v == null) {
				continue;
			}
			if (v instanceof Map) {
				t.setValue(om.writeValueAsString(v));
			}
		}
		sign(param, mch_secret);
		String request;
		{
			StringWriter sw = new StringWriter();
			PrintWriter w = new PrintWriter(sw);
			write(w, param);
			request = sw.toString();
		}
		if (log.isDebugEnabled()) {
			log.info("请求URL：" + url);
			log.info("请求参数：\r\n" + request);
		}
		res.setRequest(request);
		Utils.post(url, param, res, this);
		if (log.isDebugEnabled()) {
			log.info("响应结果：\r\n" + res.getResponse());
		}
		res.parseResponse();
		success(res);
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public String unifiedOrder(UnifiedOrder order, String mch_secret) throws ErrorCodeException, Exception {
		UnifiedOrder _order = (UnifiedOrder) selectOne(STATEMENT_ID + "queryUnifiedOrder", order);
		if (_order != null) {
			if (_order.prepay_id == null) {
				throw bundle.errorCodeException(1);
			}
			return _order.prepay_id;
		}
		insert(STATEMENT_ID + "insertUnifiedOrder", order);
		//
		success(order);
		update(STATEMENT_ID + "updateUnifiedOrderPrepayId", order);
		//
		insertResponse(order.getId(), UnifiedOrder.class.getSimpleName(), order.getRequest(), order);
		return order.prepay_id;
	}

	public void orderquery(WXConfig c, String out_trade_no, String wxResponse) throws ErrorCodeException {
		UnifiedOrder order = (UnifiedOrder) selectOne(STATEMENT_ID + "queryUnifiedOrder", out_trade_no);
		if (order == null) {
			throw new ErrorCodeException(-100, "订单不存在");
		}
		if (order.isTracde_success()) {
			return;
		}
		WXOrderQuery req = new WXOrderQuery();
		req.appid = c.app_id;
		req.mch_id = c.mch_id;
		req.out_trade_no = out_trade_no;
		success(req);
		//
		boolean success = false;
		String trade_state = req.trade_state;
		if ("SUCCESS".equals(trade_state)) {
			success = true;
		}
		//
		Map<String, Object> param = new HashMap<String, Object>();
		if (success || wxResponse != null) {
			param.put("tracde_success", success);
			param.put("out_trade_no", out_trade_no);
			param.put("wxResponse", wxResponse);
			update(STATEMENT_ID + "updateUnifiedOrderStatus", param);
		}
		if (!success) {
			throw new ErrorCodeException(-10, (String) null);
		}
	}

	public Map<String, Object> getBrandWCPayRequest(String app_id, String app_secret, String prepay_id) {
		Map<String, Object> param = new TreeMap<String, Object>();
		param.put("appId", app_id);
		param.put("timeStamp", String.valueOf(System.currentTimeMillis()));
		param.put("nonceStr", RandomStringUtils.randomAlphanumeric(32));
		param.put("package", "prepay_id=" + prepay_id);
		param.put("signType", "MD5");
		sign(param, app_secret);
		String paySign = (String) param.remove("sign");
		param.put("paySign", paySign);
		return param;
	}

	public void insertResponse(Integer requestId, String className, String request, WXResponse res) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("requestId", requestId);
		param.put("className", className);
		param.put("request", request);
		param.put("res", res);
		insert(STATEMENT_ID + "insertResponse", param);
	}

	public void notice(String xml) throws ErrorCodeException {
		OrderNotice t = new OrderNotice();
		t.setResponse(xml);
		success(t);
		WXConfig c = configService.queryOne(t.appid, t.mch_id);
		if (c == null) {
			return;
		}
		Map<String, Object> source = new HashMap(t.getXmlAttributes());
		sign(source, c.mch_secret);
		String sign = (String) source.get("sign");
		if (!sign.equalsIgnoreCase(t.getXmlAttributes().get("sign"))) {
			return;
		}
		orderquery(c, t.out_trade_no, "{\"err_msg\":\"_notify:ok\"}");
	}
}
