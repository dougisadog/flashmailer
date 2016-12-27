package com.doug.component.bean;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Invest {

    private String id;
    private String name;
    private String rate;            //预计年化收益
    private String price;            //投资金额
    private String lastReturn;            //回款总额
    private String principalAndInterest; //回款本息
    private String repayTime;            //下个还款日
    private String statusText;        //状态
    private String createDate;
    private String interestBeginDate;
    private String endDate;

    private boolean hasCoupon;        //是否使用优惠券
    private String couponName;
    private String couponPrice;
    private String couponLastReturn;
    
    private String activityRate;


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isHasCoupon() {
        return hasCoupon;
    }

    public void setHasCoupon(boolean hasCoupon) {
        this.hasCoupon = hasCoupon;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getCouponLastReturn() {
        return couponLastReturn;
    }

    public void setCouponLastReturn(String couponLastReturn) {
        this.couponLastReturn = couponLastReturn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterestBeginDate() {
        return interestBeginDate;
    }

    public void setInterestBeginDate(String interestBeginDate) {
        this.interestBeginDate = interestBeginDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLastReturn() {
        return lastReturn;
    }

    public void setLastReturn(String lastReturn) {
        this.lastReturn = lastReturn;
    }

    public String getPrincipalAndInterest() {
        return principalAndInterest;
    }

    public void setPrincipalAndInterest(String principalAndInterest) {
        this.principalAndInterest = principalAndInterest;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(long repayTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(repayTime * 1000));
        this.repayTime = sd;
    }

    @Override
    public String toString() {
        return "Invest [name=" + name + ", rate=" + rate + ", price=" + price
                + ", lastReturn=" + lastReturn + ", repayTime=" + repayTime
                + ", statusText=" + statusText + "]";
    }

	public String getActivityRate() {
		return activityRate;
	}

	public void setActivityRate(String activityRate) {
		this.activityRate = activityRate;
	}

}
