package com.doug.component.bean.pay;

import java.util.Date;
import java.util.List;


public class OrderData {
	
	private Integer orderId;
	private String orderNo;
	private int state;
	private Date editTime;
	private String tag;
	private int payType; //
	
	public static int PAYTYPE_CREDITS = 1;
	public static int PAYTYPE_CASH = 0;
	public static int PAYTYPE_BOTH = -1;

	private List<String> addresses;

	private double cost;

	private String expressNo;

	private String expressor;

	private Integer goodsId;

	private int amount;

	private String goodsName;

	private String goodsPic;
	
	private Boolean virtualGoods;
	private Integer func;
	
	private String giverName;
	private String giverHeadUrl;
	private Long giverId;
	
	private Integer isCode; //0无激活码商品类型  1则为有   
	private String exchangeCode; //激活码
	private Date overTime;
	
	private boolean supportedDeposit;


	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getExpressor() {
		return expressor;
	}

	public void setExpressor(String expressor) {
		this.expressor = expressor;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getEditTime() {
		return editTime;
	}

	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

//	public AddressData getAddressData() {
//		return addressData;
//	}
//
//	public void setAddressData(AddressData addressData) {
//		this.addressData = addressData;
//	}


	public List<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<String> addresses) {
		this.addresses = addresses;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}

	public Boolean getVirtualGoods() {
		return virtualGoods;
	}

	public void setVirtualGoods(Boolean virtualGoods) {
		this.virtualGoods = virtualGoods;
	}

	public Integer getFunc() {
		return func;
	}

	public void setFunc(Integer func) {
		this.func = func;
	}

	public String getGiverName() {
		return giverName;
	}

	public void setGiverName(String giverName) {
		this.giverName = giverName;
	}

	public String getGiverHeadUrl() {
		return giverHeadUrl;
	}

	public void setGiverHeadUrl(String giverHeadUrl) {
		this.giverHeadUrl = giverHeadUrl;
	}

	public Long getGiverId() {
		return giverId;
	}

	public void setGiverId(Long giverId) {
		this.giverId = giverId;
	}

	public Integer getIsCode() {
		return isCode;
	}

	public void setIsCode(Integer isCode) {
		this.isCode = isCode;
	}

	public String getExchangeCode() {
		return exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	public boolean isSupportedDeposit() {
		return supportedDeposit;
	}

	public void setSupportedDeposit(boolean supportedDeposit) {
		this.supportedDeposit = supportedDeposit;
	}


}
