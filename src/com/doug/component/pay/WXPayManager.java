package com.doug.component.pay;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.doug.AppConstants;
import com.doug.component.bean.pay.WXOrderResult;
import com.doug.component.utils.pay.MD5;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Context;
import android.util.Log;

public class WXPayManager {
	
	private static WXPayManager manager;
	
	private IWXAPI msgApi;
	
	private Context context;
	
	private WXPayManager() {
		
	}
	
	public void init(Context context) {
		this.context = context;
		 msgApi = WXAPIFactory.createWXAPI(context, null);
	}
	
	public static WXPayManager getInstance() {
		if (null == manager) 
			manager = new WXPayManager();
		return manager;
	}
	
	/**
	 * 发请求  -> 微信服务器
	 */
	private void sendReqToWXServer(WXOrderResult payDataWx) {
		StringBuffer sbuffer = new StringBuffer();
		PayReq req = new PayReq();
		
//		payDataWx = GlobalRes.getInstance().getBeans().getOrderResult();
		req.appId = payDataWx.getWxAppId();
		req.partnerId = payDataWx.getWxPartnerId();
		req.prepayId = payDataWx.getPrepay_id();
		req.packageValue = "Sign=WXPay";
		req.nonceStr = payDataWx.getNonce_str();
		req.timeStamp = payDataWx.getTimeStamp();
		
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sbuffer.append("sign\n" + req.sign + "\n\n");
		
		if (null != req){
			msgApi.registerApp(AppConstants.WX_APP_ID);
			msgApi.sendReq(req);
		}

		// show.setText(sb.toString());
		//Log.e("orion", signParams.toString());
	}
	
	/**
	 * 生成微信支付数据的签名方法 ,是  genPayReq() 方法里面的子方法
	 * @param params
	 * @return
	 */
	private String genAppSign(List<NameValuePair> params) {
		StringBuffer sbuffer = new StringBuffer();
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(AppConstants.WX_PAY_KEY);

		sbuffer.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sbuffer.toString().getBytes()).toUpperCase();
		Log.e("orion", appSign);
		return appSign;
		
	}
	
}
