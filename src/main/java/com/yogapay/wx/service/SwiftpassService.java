package com.yogapay.wx.service;

import com.yogapay.core.ErrorCodeException;
import com.yogapay.core.Result;
import com.yogapay.core.ResultResourceBundle;
import com.yogapay.sql.mybatis.BaseDAO;
import com.yogapay.wx.entity.WXWebConfig;
import com.yogapay.wx.web.SwiftpassOrder;
import com.yogapay.wx.web.SwiftpassQuery;
import com.yogapay.wx.web.SwiftpassResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SwiftpassService extends BaseDAO implements ParameterWriter {

	private final Log log = LogFactory.getLog(SwiftpassService.class);
	@Autowired
	private ResultResourceBundle bundle;

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

	private void success(SwiftpassResponse res) throws ErrorCodeException {
		res.parseResponse();
		if (!"0".equals(res.status)) {
			throw bundle.errorCodeException(200, res.status, res.message);
		}
		if (!"0".equals(res.result_code)) {
			throw bundle.errorCodeException(201, res.status, res.result_code, res.err_code, res.err_msg);
		}
	}

	private void success(SwiftpassResponse res, String mch_secret) throws ErrorCodeException, IOException {
		Map<String, Object> param = res.buildParams();
		WXService.sign(param, mch_secret);
		String request;
		{
			StringWriter w = new StringWriter();
			write(w, param);
			request = w.toString();
		}
		if (log.isDebugEnabled()) {
			log.info("请求参数：\r\n" + request);
		}
		Utils.post("https://pay.swiftpass.cn/pay/gateway", param, res, this);
		if (log.isDebugEnabled()) {
			log.info("响应结果：\r\n" + res.getResponse());
		}
		success(res);
	}

	private Map<String, Object> queryOrder(String out_trade_no) {
		return (Map<String, Object>) selectOne(STATEMENT_ID + "queryOrder", out_trade_no);
	}

	@Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public String jspay(WXWebConfig c, SwiftpassOrder order) throws ErrorCodeException, IOException {
		Map<String, Object> _order = queryOrder(order.getOut_trade_no());
		if (_order != null) {
			String token_id = (String) _order.get("token_id");
			if (token_id == null) {
				throw bundle.errorCodeException(1);
			}
			return token_id;
		}
		//
		//
		success(order, c.mch_secret);
		insert(STATEMENT_ID + "insertOrder", order);
		return order.token_id;
	}

	public Result orderquery(WXWebConfig c, String out_trade_no) throws ErrorCodeException, IOException {
		Map<String, Object> o = queryOrder(out_trade_no);
		if (o == null) {
			return bundle.result(2);
		}
		String trade_state = (String) o.get("trade_state");
		if (trade_state == null || "NOTPAY".equals(trade_state)) {
			SwiftpassQuery q = new SwiftpassQuery();
			q.mch_id = c.mch_id;
			q.out_trade_no = out_trade_no;
			//
			success(q, c.mch_secret);
			trade_state = q.trade_state;
			update(STATEMENT_ID + "updateOrder", q);
		}
		if ("SUCCESS".equals(trade_state)) {
			return new Result();
		} else if ("NOTPAY".equals(trade_state)) {
			return bundle.result(210);
		} else {
			return bundle.result(211);
		}
	}
}
