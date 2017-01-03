package com.doug;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map.Entry;

import com.doug.component.bean.database.UPushMessage;
import com.doug.component.cache.CacheBean;
import com.louding.frame.KJDB;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

public class UmengManager {

	private static UmengManager instance = null;

	public static UmengManager getInstance() {
		if (null == instance) {
			instance = new UmengManager();
		}
		return instance;
	}

	public void initPushInfo(Context context) {
		PushAgent mPushAgent = PushAgent.getInstance(context);
		// 注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {

			@Override
			public void onSuccess(String deviceToken) {
				// 注册成功会返回device token
				System.out.println("设备token:" + deviceToken);
			}

			@Override
			public void onFailure(String s, String s1) {
				System.out.println("推送消息注册失败" + s + "####" + s1);

			}
		});
		// 完全自定义处理推送
//		 mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
		mPushAgent.setPushIntentServiceClass(null);

		// umeng 后台配置自定义信息时的处理
//		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//			@Override
//			public void dealWithCustomAction(Context context, UMessage msg) {
//				UPushMessage m = new UPushMessage();
//				for (Entry<String, String> entry : msg.extra.entrySet()) {
//				    String key = entry.getKey();
//				    String value = entry.getValue();
//				    if (key.equals("type")) {
//				    	m.setType(Integer.parseInt(value));
//				    }
//				    if (key.equals("url")) {
//				    	m.setUrl(value);
//				    }
//				    if (key.equals("productId")) {
//				    	m.setProductId(Integer.parseInt(value));
//				    }
//				    m.setContent(msg.custom);
//				    m.setShowed(0);
//				    m.setReceiveTime(System.currentTimeMillis());
//				    CacheBean.getInstance().setMsg(m);
//				    
//				    
//				  //删除所有存贮umeng推送信息只保存最新获取的
//					KJDB kjdb = KJDB.create(context);
//					kjdb.deleteByWhere(UPushMessage.class, null);
//					kjdb.save(m);
//				}
//				// 自动以notification消息处理
//				Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//			}
//		};
//		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}

	public static int AnalyticsOn = 0;
	public static int AnalyticsOff = -1;
	public static int analyticsStatus = -1;

	@SuppressLint("DefaultLocale")
	/**
	 * 开启Umeng的数据分析
	 * 
	 * @param context
	 * @param debug
	 *            true 开启debug集成测试
	 */
	public void initAnalytics(Context context, boolean debug) {
		if (debug) {
			MobclickAgent.setDebugMode(true);
		}
		MobclickAgent.openActivityDurationTrack(false);
		ApplicationInfo appInfo;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			String appKey = appInfo.metaData.getString("UMENG_APPKEY", "");
			String channel = appInfo.metaData.getString("UMENG_CHANNEL", "")
					.toUpperCase();
			MobclickAgent.startWithConfigure(
					new UMAnalyticsConfig(context, appKey, channel));
			analyticsStatus = AnalyticsOn;
			// 关闭Umeng异常捕获
			MobclickAgent.setCatchUncaughtExceptions(false);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean checkPermission(Context context, String permission) {
		boolean result = false;
		if (Build.VERSION.SDK_INT >= 23) {
			try {
				Class<?> clazz = Class.forName("android.content.Context");
				Method method = clazz.getMethod("checkSelfPermission",
						String.class);
				int rest = (Integer) method.invoke(context, permission);
				if (rest == PackageManager.PERMISSION_GRANTED) {
					result = true;
				}
				else {
					result = false;
				}
			} catch (Exception e) {
				result = false;
			}
		}
		else {
			PackageManager pm = context.getPackageManager();
			if (pm.checkPermission(permission, context
					.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
				result = true;
			}
		}
		return result;
	}
	public static String getDeviceInfo(Context context) {
		try {
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String device_id = null;
			if (checkPermission(context, permission.READ_PHONE_STATE)) {
				device_id = tm.getDeviceId();
			}
			String mac = null;
			FileReader fstream = null;
			try {
				fstream = new FileReader("/sys/class/net/wlan0/address");
			} catch (FileNotFoundException e) {
				fstream = new FileReader("/sys/class/net/eth0/address");
			}
			BufferedReader in = null;
			if (fstream != null) {
				try {
					in = new BufferedReader(fstream, 1024);
					mac = in.readLine();
				} catch (IOException e) {
				} finally {
					if (fstream != null) {
						try {
							fstream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			json.put("mac", mac);
			if (TextUtils.isEmpty(device_id)) {
				device_id = mac;
			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}
			json.put("device_id", device_id);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
