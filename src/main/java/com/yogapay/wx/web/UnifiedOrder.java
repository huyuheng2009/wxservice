package com.yogapay.wx.web;

import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;

public class UnifiedOrder extends WXResponse {

	private int id;
	private String key;
	private boolean tracde_success;
	/**
	 * 公众账号ID
	 */
	@RequiredField
	public String appid;
	/**
	 * 商户号
	 */
	@RequiredField
	public String mch_id;
	/**
	 * 设备号 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	 */
	@RequiredField(nullable = true)
	public String device_info;
	/**
	 * 随机字符串
	 */
	@RequiredField
	public String nonce_str = RandomStringUtils.randomAlphanumeric(32);
	/**
	 * 商品描述
	 */
	@RequiredField
	public String body;
	/**
	 * 商品详情
	 */
	@RequiredField(nullable = true)
	public String detail;
	/**
	 * 附加数据
	 */
	@RequiredField(nullable = true)
	public Map<String, Object> attach;
	/**
	 * 商户订单号
	 */
	@RequiredField
	public String out_trade_no;
	/**
	 * 货币类型
	 */
	@RequiredField(nullable = true)
	public String fee_type;
	/**
	 * 总金额
	 */
	@RequiredField
	public int total_fee;
	/**
	 * 终端IP
	 */
	@RequiredField
	public String spbill_create_ip;
	/**
	 * 交易起始时间
	 */
	@RequiredField(nullable = true)
	public String time_start;
	/**
	 * 交易结束时间
	 */
	@RequiredField(nullable = true)
	public String time_expire;
	/**
	 * 商品标记
	 */
	@RequiredField(nullable = true)
	public String goods_tag;
	/**
	 * 通知地址
	 */
	@RequiredField
	public String notify_url;
	/**
	 * 交易类型
	 */
	@RequiredField
	public String trade_type;
	/**
	 * 商品ID
	 */
	@RequiredField(nullable = true)
	public String product_id;
	/**
	 * 用户标识
	 */
	@RequiredField(nullable = true)
	public String openid;
	/**
	 微信生成的预支付回话标识，用于后续接口调用中使用，该值有效期为2小时
	 */
	public String prepay_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isTracde_success() {
		return tracde_success;
	}

	public void setTracde_success(boolean tracde_success) {
		this.tracde_success = tracde_success;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Map<String, Object> getAttach() {
		return attach;
	}

	public void setAttach(Map<String, Object> attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}

	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}

	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

}
