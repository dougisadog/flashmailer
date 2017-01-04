package com.doug.component.utils;

import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

public class MOP_APIUtils {
	
	private static KJHttp kjh = new KJHttp();
	
	private final static String MOP_API_KEY = "1a7b2d89ae578";
	
	private final static String MOP_API_ID_URL = "http://apicloud.mob.com/idcard/query";
	
	public static void idcardValid(String idcard, HttpCallBack callback) {
		HttpParams params = new HttpParams();
		params.put("key", MOP_API_KEY);
		params.put("cardno", idcard.trim());
		kjh.get(MOP_API_ID_URL, params, callback);
	} 

}
