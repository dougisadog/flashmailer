/*
 * @(#)ApkInfo.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-4-1
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package com.doug.component.support;

import android.graphics.drawable.Drawable;

/**
 * apk信息实体类。
 *
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ApkInfo {
	public String packageName;
	public int iconId;
	public Drawable iconDrawable;
	public String programName;
	public int versionCode;
	public String versionName;
	public int width;
	public int height;
	public int dpi;
	public int screenWidth;
	public int screenHeight;
	

	public static String ANDROID_VERSION = null;
	public static String PHONE_BRAND = null;
	public static String PHONE_MODEL = null;
	public static String PHONE_MANUFACTURER = null;

	public ApkInfo() {
		ApkInfo.ANDROID_VERSION = android.os.Build.VERSION.RELEASE;
		ApkInfo.PHONE_BRAND = android.os.Build.BRAND;
		ApkInfo.PHONE_MODEL = android.os.Build.MODEL;
		ApkInfo.PHONE_MANUFACTURER = android.os.Build.MANUFACTURER;
	}

	@Override
	public String toString() {
		return "ApkInfo [android version= " + ANDROID_VERSION + "phone manufacturer=" + PHONE_MANUFACTURER
				+ "phone model=" + PHONE_MODEL + "phone brand=" + PHONE_BRAND + "packageName=" + packageName
				+ ", iconId=" + iconId + ", iconDrawable=" + iconDrawable + ", programName=" + programName
				+ ", versionCode=" + versionCode + ", versionName=" + versionName + "]";
	}
}
