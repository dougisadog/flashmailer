package com.doug.component.bean.pay;

/**
 * 订单支付返回信息/订单详情
 * @author doug
 *
 */
public class WXOrderResult {
	
	public static final int SUCCESS = 200;
	public static final int UNVALID = 401;
	private String prepay_id;  //支付ID
	private String sign; //签名信息 
//	private GoodsData goodsData;
//	private OrderData orderData;
	private String nonce_str;
	private String timeStamp;
	private int return_code = 200;
	private String wxAppId;
	private String wxPartnerId;
	

	public String getWxAppId() {
		return wxAppId;
	}

	public void setWxAppId(String wxAppId) {
		this.wxAppId = wxAppId;
	}

	public String getWxPartnerId() {
		return wxPartnerId;
	}

	public void setWxPartnerId(String wxPartnerId) {
		this.wxPartnerId = wxPartnerId;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

//	public OrderData getOrderData() {
//		return orderData;
//	}
//
//	public void setOrderData(OrderData orderData) {
//		this.orderData = orderData;
//	}

	public int getReturn_code() {
		return return_code;
	}

	public void setReturn_code(int return_code) {
		this.return_code = return_code;
	}

//	public GoodsData getGoodsData() {
//		return goodsData;
//	}
//
//	public void setGoodsData(GoodsData goodsData) {
//		this.goodsData = goodsData;
//	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
}
