package com.yogapay.wx.web.controller;

import com.yogapay.core.ErrorCodeException;
import com.yogapay.core.LangUitls;
import com.yogapay.core.Result;
import com.yogapay.core.WebUtils;
import com.yogapay.web.spring.WebAttribute;
import com.yogapay.wx.entity.WXWebConfig;
import com.yogapay.wx.service.SwiftpassService;
import com.yogapay.wx.web.OrderParam;
import com.yogapay.wx.web.SwiftpassOrder;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("pay/swiftpass")
public class PaySwiftpassController {

	@Autowired
	private SwiftpassService swiftpassService;

	@RequestMapping("/")
	public Object pay(
			@WebAttribute WXWebConfig c,
			@WebAttribute OrderParam op,
			@WebAttribute String openid,
			ModelMap mmap, HttpServletRequest request) throws Exception {
		SwiftpassOrder o = new SwiftpassOrder();
		o.mch_id = c.mch_id;
		o.out_trade_no = c.buildOutTradeNo(op.trade_no);
		o.body = op.body;
		o.sub_openid = openid;
		o.total_fee = op.total_fee;
		o.mch_create_ip = (String) LangUitls.getNotNull(c.spbill_create_ip, WebUtils.getIpAddr(request));
		o.notify_url = WebUtils.getUrl(request, "notify");
		String token_id = swiftpassService.jspay(c, o);
		mmap.clear();
		mmap.put("token_id", token_id);
		mmap.put("showwxtitle", 1);
		return "redirect:https://pay.swiftpass.cn/pay/jspay";
	}

	@RequestMapping("query")
	@ResponseBody
	public Object query(
			@WebAttribute WXWebConfig c,
			@WebAttribute String out_trade_no,
			ModelMap mmap) throws IOException, ErrorCodeException {
		return swiftpassService.orderquery(c, out_trade_no);
	}
}
