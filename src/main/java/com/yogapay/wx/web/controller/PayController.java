package com.yogapay.wx.web.controller;

import com.yogapay.core.ErrorCodeException;
import com.yogapay.core.LangUitls;
import com.yogapay.core.Result;
import com.yogapay.core.WebUtils;
import com.yogapay.core.service.CommonCacheService;
import com.yogapay.wx.entity.WXWebConfig;
import com.yogapay.wx.service.ConfigService;
import com.yogapay.wx.service.WXCommonService;
import com.yogapay.wx.service.WXService;
import com.yogapay.wx.web.OrderParam;
import com.yogapay.wx.web.UnifiedOrder;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("pay")
public class PayController {

	public static final String KEY_PAY_PARAM = "payParam";
	private final Log log = LogFactory.getLog(PayController.class);
	@Autowired
	private WXService wxService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private WXCommonService commonService;
	@Autowired
	private ObjectMapper om;
	@Value("#{config['oauth_pay.uid']}")
	private String oauth_pay_uid;
	@Value("#{config['oauth_pay.openid']}")
	private String oauth_pay_openid;
	//
	@Resource
	private CommonCacheService<OrderParam> orderCacheService;

	private Object pay_0(WXWebConfig c, OrderParam op, String openid, ModelMap mmap, HttpServletRequest request) throws Exception {
		if (c.getChannel() != null) {
			mmap.addAttribute("c", c);
			mmap.addAttribute("op", op);
			mmap.addAttribute("openid", openid);
			return "forward:" + c.getChannel() + "/";
		}
		UnifiedOrder o = new UnifiedOrder();
		o.appid = c.app_id;
		o.mch_id = c.mch_id;
		o.body = op.body;
		o.out_trade_no = c.buildOutTradeNo(op.trade_no);
		o.total_fee = LangUitls.getNotNull(c.total_fee, op.total_fee);
		o.spbill_create_ip = (String) LangUitls.getNotNull(c.spbill_create_ip, WebUtils.getIpAddr(request));
		if (op.notify_url != null) {
			o.notify_url = op.notify_url;
		} else {
			o.notify_url = WebUtils.getUrl(request, "notify");
		}
		o.trade_type = "JSAPI";
		o.openid = openid;
		o.setAttach(new HashMap<String, Object>());
		o.getAttach().put("uid", c.uid);
		String prepay_id = wxService.unifiedOrder(o, c.mch_secret);
		//
		Map<String, Object> params = wxService.getBrandWCPayRequest(c.app_id, c.mch_secret, prepay_id);
		mmap.put("paramsJson", new ObjectMapper().writeValueAsString(params));
		mmap.put("uid", c.uid);
		mmap.put("out_trade_no", o.getOut_trade_no());
		mmap.put("trade_no", op.trade_no);
		return "pay";
	}

	@RequestMapping(value = "/")
	public Object pay(
			@RequestParam String uid,
			@RequestParam String body,
			@RequestParam String trade_no,
			@RequestParam int total_fee,
			@RequestParam(required = false) String notify_url,
			@RequestParam(required = false) String openid,
			HttpServletRequest request, ModelMap mmap) throws IOException, Exception {
		WXWebConfig c = configService.queryWebConfig(uid);
		if (c == null) {
			return new ResponseEntity<Object>("uid is not exists", HttpStatus.BAD_REQUEST);
		}
		if (!WebUtils.checkSign(request, c.sign_salt)) {
			return new ResponseEntity<Object>("[wxservice] sign is invalid.", HttpStatus.BAD_REQUEST);
		}
		OrderParam op = new OrderParam(uid, body, trade_no, total_fee, notify_url);
		if (StringUtils.isBlank(openid)) {
			if (c.oauth_pay) {
				String token;
				for (int i = 0;; i++) {
					if (i >= 100) {
						throw new RuntimeException();
					}
					token = RandomStringUtils.randomAlphanumeric(32);
					if (orderCacheService.putIfAbsent(token, op) == null) {
						break;
					}
				}
				mmap.put("token", token);
				mmap.put("uid", op.uid);
				return "redirect:../oauth/register";
			} else {
				return new ResponseEntity<Object>("openid is blank", HttpStatus.BAD_REQUEST);
			}
		}
		return pay_0(c, op, openid, mmap, request);
	}

	@RequestMapping(value = "/", params = "oauth_pay=1")
	public Object oauth_pay(
			@RequestParam String openid,
			@RequestParam String token,
			@RequestParam String sign,
			ModelMap mmap, HttpServletRequest request) throws Exception {
		OrderParam op = orderCacheService.get(token);
		if (op == null) {
			return new ResponseEntity<Object>(OrderParam.class.getSimpleName() + " is not exists", HttpStatus.BAD_REQUEST);
		}
		WXWebConfig c = configService.queryWebConfig(op.uid);
		if (c == null) {
			return new ResponseEntity<Object>("uid is not exists", HttpStatus.BAD_REQUEST);
		}
		if (!WebUtils.checkSign(request, c.sign_salt)) {
			return new ResponseEntity<Object>("[wxservice] sign is invalid", HttpStatus.BAD_REQUEST);
		}
		if (!oauth_pay_uid.isEmpty()) {
			op.uid = oauth_pay_uid;
			c = configService.queryWebConfig(op.uid);
		}
		if (!oauth_pay_openid.isEmpty()) {
			openid = oauth_pay_openid;
		}
		return pay_0(c, op, openid, mmap, request);
	}

	@RequestMapping("query")
	@ResponseBody
	public Object query(
			@RequestParam String uid,
			@RequestParam String trade_no,
			ModelMap mmap, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ErrorCodeException {
		WXWebConfig c = configService.queryWebConfig(uid);
		if (c == null) {
			return new Result(-90, "uid is not exists", null);
		}
		String out_trade_no = c.buildOutTradeNo(trade_no);
		if (c.getChannel() != null) {
			request.setAttribute("c", c);
			request.setAttribute("out_trade_no", out_trade_no);
			request.getRequestDispatcher(c.getChannel() + "/query").forward(request, response);
			return null;
		}
		try {
			wxService.orderquery(c, out_trade_no, null);
			return new Result();
		} catch (ErrorCodeException ex) {
			return new Result(ex.getErrorCode(), ex.getMessage(), null);
		}
	}

	@RequestMapping("finish")
	public Object finish(
			@RequestParam String uid,
			@RequestParam String out_trade_no,
			@RequestParam String trade_no,
			@RequestParam String wxResponse,
			ModelMap mmap, HttpServletRequest req) throws IOException, com.yogapay.core.ErrorCodeException {
		WXWebConfig c = configService.queryWebConfig(uid);
		if (c == null) {
			return new ResponseEntity<Object>("key is not exists", HttpStatus.BAD_REQUEST);
		}
		try {
			wxService.orderquery(c, out_trade_no, wxResponse);
			mmap.put("success", true);
			mmap.put("doneConfig", c.tradeSuccess);
		} catch (ErrorCodeException ex) {
			mmap.put("success", false);
			mmap.put("doneConfig", c.tradeFailed);
			mmap.put("errorCode", ex.getErrorCode());
		}
		Map<String, Object> doneConfigParams = new HashMap<String, Object>();
		doneConfigParams.put("trade_no", trade_no);
		mmap.put("doneConfigParams", om.writeValueAsString(doneConfigParams));
		mmap.put("js_config", commonService.getJSConfig(c, req));
		return "pay_finish";
	}

	@RequestMapping("notify")
	public void payNotify(HttpServletRequest req, HttpServletResponse res) throws IOException {
		StringWriter buff = new StringWriter();
		PrintWriter w = new PrintWriter(buff);
		w.println("notify:");
		try {
			w.println("Header:");
			for (Object k : Collections.list(req.getHeaderNames())) {
				w.format("%20s: %s\n", k, req.getHeader((String) k));
			}
			String xml;
			ServletInputStream in = req.getInputStream();
			try {
				xml = IOUtils.toString(in, "UTF-8");
			} finally {
				in.close();
			}
			w.println("inpustream:");
			w.println(xml);
			w.flush();
			wxService.notice(xml);
			log.info(buff.toString());
		} catch (Exception ex) {
			log.error(buff.toString(), ex);
		}
		res.setBufferSize(8 * 1024);
		res.setContentType("'text/xml;charset=UTF-8");
		PrintWriter outw = res.getWriter();
		try {
			outw.append("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
		} finally {
			outw.close();
		}
	}
}
