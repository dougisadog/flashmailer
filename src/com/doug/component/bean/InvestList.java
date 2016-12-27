package com.doug.component.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.doug.component.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class InvestList {

	private List<Invest> invests;

	public InvestList(JSONArray array) throws JSONException {
		super();
		invests = new ArrayList<Invest>();
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Invest a = new Invest();
			JSONObject o = (JSONObject) array.get(i);
			JSONObject order = o.getJSONObject("order");
			if (order.has("lastReturn"))
				a.setLastReturn(order.getString("lastReturn"));
			if (order.has("principalAndInterest"))
				a.setPrincipalAndInterest(order
						.getString("principalAndInterest"));
			a.setName(order.getString("name"));
			a.setPrice(order.getString("price"));
			if (order.has("rate")) {
				String rate = order.getString("rate");
				a.setRate(FormatUtils.getSimpleNum(rate));
			}
			if (order.has("repayTime"))
				a.setRepayTime(order.getLong("repayTime"));
			a.setCreateDate(order.getString("createDate"));
			a.setInterestBeginDate(order.getString("interestBeginDate"));
			a.setStatusText(order.getString("statusText"));
			a.setId(order.getString("id"));
			if (order.has("endDate"))
				a.setEndDate(order.getString("endDate"));
			if (o.has("coupon")) {
				JSONObject coupon = o.getJSONObject("coupon");
				a.setHasCoupon(true);
				a.setCouponLastReturn(coupon.getString("return"));
				a.setCouponName(coupon.getString("name"));
				a.setCouponPrice(coupon.getString("price"));
			} else {
				a.setHasCoupon(false);
			}
			if (order.has("activityRate") && Double.parseDouble(order.getString("activityRate")) != 0) {
				//.00结尾屏蔽
				String activityRate = order.getString("activityRate");
				a.setActivityRate(FormatUtils.getSimpleNum(activityRate));
			}
			invests.add(a);
		}
	}

	public InvestList(List<Invest> li, JSONArray array) throws JSONException {
		super();
		invests = li;
		int len = array.length();
		for (int i = 0; i < len; i++) {
			Invest a = new Invest();
			JSONObject o = (JSONObject) array.get(i);
			JSONObject order = o.getJSONObject("order");
			if (order.has("lastReturn"))
				a.setLastReturn(order.getString("lastReturn"));
			if (order.has("principalAndInterest"))
				a.setPrincipalAndInterest(order
						.getString("principalAndInterest"));
			a.setName(order.getString("name"));
			a.setPrice(order.getString("price"));
			if (order.has("rate")) {
				String rate = order.getString("rate");
				a.setRate(FormatUtils.getSimpleNum(rate));
			}
			if (order.has("repayTime"))
				a.setRepayTime(order.getLong("repayTime"));
			a.setCreateDate(order.getString("createDate"));
			a.setInterestBeginDate(order.getString("interestBeginDate"));
			a.setStatusText(order.getString("statusText"));
			a.setId(order.getString("id"));
			if (order.has("endDate"))
				a.setEndDate(order.getString("endDate"));
			if (o.has("coupon")) {
				JSONObject coupon = o.getJSONObject("coupon");
				a.setHasCoupon(true);
				a.setCouponLastReturn(coupon.getString("return"));
				a.setCouponName(coupon.getString("name"));
				a.setCouponPrice(coupon.getString("price"));
			} else {
				a.setHasCoupon(false);
			}
			if (order.has("activityRate") && Double.parseDouble(order.getString("activityRate")) != 0) {
				//.00结尾屏蔽
				String activityRate = order.getString("activityRate");
				a.setActivityRate(FormatUtils.getSimpleNum(activityRate));
			}
			invests.add(a);
		}
	}

	public List<Invest> getInvests() {
		return invests;
	}

	public void setInvests(List<Invest> invests) {
		this.invests = invests;
	}

	@Override
	public String toString() {
		int len = invests.size();
		String st = "";
		for (int i = 0; i < len; i++) {
			Invest a = invests.get(i);
			st += " /**" + i + a.toString();
		}
		return st;
	}

}
