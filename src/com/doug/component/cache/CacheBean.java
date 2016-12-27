package com.doug.component.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.kymjs.kjframe.http.Cache;

import com.baidu.location.BDLocation;
import com.doug.AppVariables;
import com.doug.component.bean.CItem;
import com.doug.component.bean.MainAD;
import com.doug.component.bean.ShopADData;
import com.doug.component.bean.database.UPushMessage;
import com.doug.component.bean.database.UserSearch;
import com.doug.component.bean.jsonbean.Account;
import com.doug.component.bean.jsonbean.User;
import com.doug.component.support.ApkInfo;
import com.doug.component.utils.ApplicationUtil;
import com.louding.frame.KJDB;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class CacheBean {
	
	private static CacheBean instance = null;
	
	public static CacheBean getInstance() {
		if (null == instance) {
			instance = new CacheBean();
		}
		return instance;
	}
	
	public void init(Context context) {
		KJDB kjdb = KJDB.create(context);
		List<UserSearch> searches = kjdb.findAll(UserSearch.class);
		this.searches = searches;
	}
	
	public void clear() {
		user = null;
		account = null;
	}
	
	//清空webcookie数据
	public static void syncCookie(Context context) {
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//移除  
	    CookieSyncManager.getInstance().sync();  
	}
	
	/**
	 * 个人信息是否需要更新
	 * @return
	 */
	public static boolean checkNeedUpdate() {
		User user = getInstance().getUser();
		if (null == user || AppVariables.uid != Integer.parseInt(user.getUid())) return true;
		return System.currentTimeMillis()
				- user.getLastModTime() > AppVariables.cacheLiveTime;
	}
	
	private User user;
	
	private Account account;
	
	private List<UserSearch> searches = new ArrayList<UserSearch>();
	
	private List<CItem> items = new ArrayList<CItem>(); //全部表情页面数据
	
	private List<CItem> currentItems = new ArrayList<CItem>(); //当前表情页面数据
	
	private BDLocation myBDLocation;
	
	public List<UserSearch> getSearches() {
		return searches;
	}

	public void setSearches(List<UserSearch> searches) {
		this.searches = searches;
	}

	private ApkInfo apkInfo;
	
	private List<MainAD> adDatas;
	
	private List<ShopADData> shopADDatas;
	
	
	private HashMap<String, String> redConditions = new HashMap<String, String>();
	
	private MainAD ad; //主页广告绝对地址（图片src直接调用）
	
	//现金券背景bitmap缓存
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		user.setLastModTime(new Date().getTime());
		this.user = user;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ApkInfo getApkInfo() {
		return apkInfo;
	}

	public void setApkInfo(ApkInfo apkInfo) {
		this.apkInfo = apkInfo;
	}



	public HashMap<String, String> getRedConditions() {
		return redConditions;
	}

	public void setRedConditions(HashMap<String, String> redConditions) {
		this.redConditions = redConditions;
	}


	public List<MainAD> getAdDatas() {
		return adDatas;
	}

	public void setAdDatas(List<MainAD> adDatas) {
		this.adDatas = adDatas;
	}

	public MainAD getAd() {
		return ad;
	}

	public void setAd(MainAD ad) {
		this.ad = ad;
	}

	public List<ShopADData> getShopADDatas() {
		return shopADDatas;
	}

	public void setShopADDatas(List<ShopADData> shopADDatas) {
		this.shopADDatas = shopADDatas;
	}

	public List<CItem> getItems() {
		return items;
	}

	public void setItems(List<CItem> items) {
		this.items = items;
	}

	public List<CItem> getCurrentItems() {
		return currentItems;
	}

	public void setCurrentItems(List<CItem> currentItems) {
		this.currentItems = currentItems;
	}

	public BDLocation getMyBDLocation() {
		return myBDLocation;
	}

	public void setMyBDLocation(BDLocation myBDLocation) {
		this.myBDLocation = myBDLocation;
	}


}
