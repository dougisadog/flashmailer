package com.doug.component.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

public class ServiceUtils {

	public static interface ServiceIntentCallBack {
		public void setServiceIntent(Intent intent);
	}
	
	public static <T> boolean checkService(Context context, final Class<T> clazz,
			ServiceIntentCallBack si) {
		return checkService(context, clazz, si, true);
	}

	/**
	 * 查看该service是否存在 不存在则开启
	 * 
	 * @param context
	 * @param clazz
	 *            service的class
	 */
	public static <T> boolean checkService(Context context, final Class<T> clazz,
			ServiceIntentCallBack si, boolean autoStart) {
		boolean isServiceRunning = false;

		// 检查Service状态

		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (clazz.getName().equals(service.service.getClassName())) {
				isServiceRunning = true;
			}
		}
		if (!isServiceRunning && autoStart) {
			Intent i = new Intent(context, clazz);
			if (null != si) {
				si.setServiceIntent(i);
			}
			context.startService(i);
		}
		return isServiceRunning;
	}
	
	public static <T> boolean stopService(Context context, final Class<T> clazz) {
		// 检查Service状态

		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (clazz.getName().equals(service.service.getClassName())) {
				Intent i = new Intent(context, clazz);
				context.stopService(i);
				return true;
			}
		}
		return false;
	}
}
