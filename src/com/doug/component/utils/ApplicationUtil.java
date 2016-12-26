/*
 * @(#)ApplicationUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-9-13
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.doug.component.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.util.Log;

import com.doug.AppConstants;
import com.doug.AppVariables;
import com.doug.component.bean.database.UserConfig;
import com.doug.component.cache.CacheBean;
import com.doug.component.support.ApkInfo;
import com.louding.frame.KJDB;

import java.util.Date;
import java.util.List;

/**
 * 应用工具类。
 *
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ApplicationUtil {

	/**
	 * 通过包名获取应用程序的名称。
	 *
	 * @param packageName
	 *            包名。
	 * @return 返回包名所对应的应用程序的名称。
	 */
	public static String getProgramNameByPackageName(Context ctx,
			String packageName) {
		PackageManager pm = ctx.getPackageManager();
		String name = null;
		try {
			name = pm.getApplicationLabel(
					pm.getApplicationInfo(packageName,
							PackageManager.GET_META_DATA)).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取有关本程序的信息。
	 *
	 * @return 有关本程序的信息。
	 */
	public static ApkInfo getApkInfo(Context ctx) {
		ApkInfo apkInfo = null;
		if (null == CacheBean.getInstance().getApkInfo()) {
			apkInfo = new ApkInfo();
			ApplicationInfo applicationInfo = ctx.getApplicationInfo();
			apkInfo.packageName = applicationInfo.packageName;
			apkInfo.iconId = applicationInfo.icon;
			apkInfo.iconDrawable = ctx.getResources().getDrawable(apkInfo.iconId);
			apkInfo.programName = ctx.getResources()
					.getText(applicationInfo.labelRes).toString();
			PackageInfo packageInfo = null;
			try {
				packageInfo = ctx.getPackageManager().getPackageInfo(
						apkInfo.packageName, 0);
				apkInfo.versionCode = packageInfo.versionCode;
				apkInfo.versionName = packageInfo.versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
			DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
			apkInfo.width = displayMetrics.widthPixels;
			apkInfo.height = displayMetrics.heightPixels;
			apkInfo.dpi = displayMetrics.densityDpi;
			float density = displayMetrics.density;//屏幕密度（0.75 / 1.0 / 1.5）  
	         //屏幕宽度算法:屏幕宽度（像素）/屏幕密度  
			 apkInfo.screenWidth = (int) (apkInfo.width/density);//屏幕宽度(dp)  
			 apkInfo.screenHeight = (int)(apkInfo.height/density);//屏幕高度(dp)  
			
			Log.i("MainActivity", "height:" + displayMetrics.heightPixels); 
			Log.i("MainActivity", "width:" + displayMetrics.widthPixels); 
			
			CacheBean.getInstance().setApkInfo(apkInfo);
		}
		else {
			apkInfo = CacheBean.getInstance().getApkInfo();
		}
		
		return apkInfo;
	}

	
	/**
	 * 检测 时间是否超时
	 * @param lastTime 上次检测时间
	 * @return
	 */
	public static boolean checkTime(long lastTime) {
		return new Date().getTime() - lastTime > AppConstants.TIME;
	}
	
	/**
	 * 手势密码弹出 判定
	 * @param context
	 * @return
	 */
	public static boolean isNeedGesture(Context context) {
		KJDB kjdb = KJDB.create(context);
		if (null == kjdb)
			return false;
		List<UserConfig> userConfigs = kjdb.findAllByWhere(UserConfig.class, "uid=" + AppVariables.uid);
		UserConfig userConfig = null;
		if (userConfigs.size() > 0) {
			userConfig = userConfigs.get(0);
		}
		if (null != userConfig && userConfig.isNeedGesture()) {
			if (AppVariables.needGesture || checkTime(userConfig.getLastGestureCheckTime())) {
				userConfig.setLastGestureCheckTime(new Date().getTime());
				kjdb.update(userConfig);
				AppVariables.needGesture = false;
				return true;
			}
		}
		return false;
	}

	/**
	 * 重启
	 * @param context
	 */
	public static void restartApplication(Context context) {
		final Intent intent = context.getPackageManager()
				.getLaunchIntentForPackage(context.getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		context.startActivity(intent);
	}
	

}
