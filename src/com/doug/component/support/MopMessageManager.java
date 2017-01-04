package com.doug.component.support;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import cn.smssdk.SMSSDK;

public class MopMessageManager {
	
	private static final String DEFAULT_COUNTRY_ID = "42";
	private static final String APP_KEY = "1a80080bb01d6";
	private static final String APP_SECRET = "6c50287addae59222e2ba213bfd60f48";
	
	private static MopMessageManager manager;
	private Context context;
	
	private MopMessageManager() {
		
	}
	
	public void init(Context context) {
		this.context = context;
		SMSSDK.initSDK(context,APP_KEY,APP_SECRET);
	}
	
	public static MopMessageManager getInstance() {
		if (null == manager) 
			manager = new MopMessageManager();
		return manager;
	}
	
	
	public String[] getCurrentCountry() {
		String mcc = getMCC();
		String[] country = null;
		if (!TextUtils.isEmpty(mcc)) {
			country = SMSSDK.getCountryByMCC(mcc);
		}

		if (country == null) {
			Log.w("SMSSDK", "no country found by MCC: " + mcc);
			country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
		}
		return country;
	}

	private String getMCC() {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		// 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
		String networkOperator = tm.getNetworkOperator();
		if (!TextUtils.isEmpty(networkOperator)) {
			return networkOperator;
		}

		// 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
		return tm.getSimOperator();
	}
}
