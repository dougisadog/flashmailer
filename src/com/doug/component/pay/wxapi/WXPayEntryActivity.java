package com.doug.component.pay.wxapi;

import org.json.JSONException;
import org.json.JSONObject;

import com.doug.AppConstants;
import com.doug.component.bean.pay.OrderData;
import com.doug.component.dialog.DialogAlertFragment;
import com.doug.component.dialog.DialogAlertFragment.CallBackDialogConfirm;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WXPayEntryActivity extends KJActivity implements IWXAPIEventHandler{
	
	public static final Integer SUCCESS_STATUS = 0;
	public static final Integer UNCHECKED_STATUS = 1;
	public static final Integer FAIL_STATUS = 2;
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	private static final int RESULTCODE = 1<<2;
	
    private IWXAPI api;
    private DialogAlertFragment successDialog;
    private Integer code;
    
    private OrderData orderData = new OrderData();
    private static String orderNo;
    private static int amount = 0;
    
    public static void setWXPayData(String orderNo, int amount) {
    	WXPayEntryActivity.orderNo = orderNo;
    	WXPayEntryActivity.amount = amount;
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result); 
    	api = WXAPIFactory.createWXAPI(this, AppConstants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		//TODO 支付状态
//		AtyShopOrderPay.paying = false;  //给微信支付的结果码加上的一个标识,防止 微信支付和支付宝支付 重复点击 和 支付失败设置点击事件恢复正常 
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			code = resp.errCode;
			//非成功不处理
//			Toast.makeText(WXPayEntryActivity.this, "微信支付返回" +code, Toast.LENGTH_SHORT).show();
			if (code != 0) {
				finish();
				return;
			}
			requestPayData();
		}
	}
	
	private void requestPayData() {
		
//		List<String> orderNos = new ArrayList<String>();
//		orderNos.add(orderNo);
//		List<Integer> errCodes = new ArrayList<Integer>();
//		errCodes.add(code);
		
		// 一步任务获取图片
		KJHttp kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("orderNo", orderNo);
		params.put("code", code);
		kjh.post("", params, new HttpCallBack(this, false) {

			@Override
			public void success(JSONObject ret) {
				try {
					if (ret.get("status") == null) {
						successDialog = new DialogAlertFragment(callback2, "服务器验证失败，点击确定重新验证", "", 0);
						successDialog.showDialog(getSupportFragmentManager());
					}
					else {
						//TODO success
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.success(ret);
			}
			
		});
	}
	
//	private void researchAty() {
//		List<Activity> atyList = GlobalApp.getInstance().getStackActivities();
//		for (int i = 0; i < atyList.size(); i++) {
//			if (atyList.get(i).getLocalClassName().toString().equals("com.shuangge.english.view.shop.AtyShopOrderPay")) {
//				atyList.get(i).finish();
//			}
//		}
//	}
	
	private CallBackDialogConfirm callback2 = new CallBackDialogConfirm() {
		
		@Override
		public void onSubmit(int position) {
//			showLoading();
			requestPayData();
			successDialog.dismiss();
			successDialog = null;
		}
		
		@Override
		public void onKeyBack() {
			successDialog.dismiss();
			successDialog = null;
		}
		
	};

	@Override
	public void setRootView() {
		// TODO Auto-generated method stub
		
	}
}