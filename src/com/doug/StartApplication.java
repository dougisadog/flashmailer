package com.doug;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.doug.AutoUpdateManager.UpdateCallback;
import com.doug.component.bean.MainAD;
import com.doug.component.bean.ShopADData;
import com.doug.component.cache.CacheBean;
import com.doug.component.error.DebugPrinter;
import com.doug.component.ui.AtyMainAD;
import com.doug.component.ui.MainActivity;
import com.doug.component.utils.ImageUtils;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.StringUtils;
import com.doug.emojihelper.R;

public class StartApplication extends KJActivity {
	
	private KJHttp kjh;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_start);
	}
	
	/**
	 * 数据预加载
	 */
	private void loadData() {
		loadImageUrls();
	}
	
	/**
	 * 图片地址下载
	 */
	private void loadImageUrls() {
		HttpParams params = new HttpParams();
		kjh.post(AppConstants.GET_SLIDE_IMAGE, params, new HttpCallBack(this, false) {

			@Override
			public void success(JSONObject ret) {
				try {
					List<MainAD> ads = new ArrayList<MainAD>();
					JSONArray ja = ret.getJSONArray("data");
					for (int i = 0; i < ja.length(); i++) {
						MainAD ad = new MainAD();
						ad.setImg(ja.getJSONObject(i).getJSONObject("extra")
								.getString("img"));
						ad.setLink(ja.getJSONObject(i).getJSONObject("extra")
								.getString("url"));
						ads.add(ad);
					}
					CacheBean.getInstance().setAdDatas(ads);
					downLoadADImage();
				} catch (JSONException e) {
					updateDone();
					e.printStackTrace();
				}
				super.success(ret);
			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				updateDone();
				super.onFailure(t, errorNo, strMsg);
			}
			
		});
	}
	
	/**
	 * 图片文件下载
	 */
	private void downLoadADImage() {
		final List<MainAD> ads = CacheBean.getInstance().getAdDatas();
		if (null== ads || ads.size() == 0) {
			updateDone();
		}
		else {
			//随机获取图片
			final int index = (int) (ads.size() * Math.random());
			MainAD mainAD = ads.get(index);
			String filePath = ImageUtils.getImagePath(mainAD.getImg());
			File path = new File(filePath);
			kjh.download(mainAD.getImg(), path, new HttpCallBack(StartApplication.this,false) {
				@Override
				public void onSuccess(File f) {
					CacheBean.getInstance().setAd(ads.get(index));
					updateDone();
					
				}
				@Override
				public void onFailure(Throwable t, int errorNo, String strMsg) {
					ads.remove(index);
					CacheBean.getInstance().setAdDatas(ads);
					if (ads.size() > 0)
						downLoadADImage();
					else {
						updateDone();
					}
				}
			});
		}
	}

	@Override
	public void initData() {
		kjh = new KJHttp();
		super.initData();
		CacheBean.getInstance().init(this);
		
		AutoUpdateManager.getInstance().setUpdateCallback(new UpdateCallback() {

			@Override
			public void onUpdated() {
				loadData();
			}

			@Override
			public void onNoUpdate() {
			}

			@Override
			public void onBeforeUpdate() {
			}
		});
//		AutoUpdateManager.getInstance().setShowMsg(false);
//		parseChannel(this);
		updateDone();
	}

	/**
	 * 渠道解析 读取manifest的 channel来执行对应自动更新
	 */
	public static void parseChannel(Context context) {
		try {
			ApplicationInfo appInfo = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			String channel = appInfo.metaData.getString("UMENG_CHANNEL", "")
					.toUpperCase();
			if ("BAIDU".equals(channel)) {
//				AutoUpdateManager.getInstance().initBaidu(context);
			}
			else if ("XIAOMI".equals(channel)) {
				AutoUpdateManager.getInstance().initXiaomi(context, false);
			}
			else if ("YYB".equals(channel)) {
				AutoUpdateManager.getInstance().initYYB(context);
			}
			else if ("LOCAL".equals(channel)) {
				AutoUpdateManager.getInstance().initLocalUpdate(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 无更新或者 完成更新的正常进入流程
	 */
	private void updateDone() {
		// 动态添加数据库字段 暂时只支持一个数据库AppConstants.USER_DB_NAME
		// KJDB kjdb = KJDB.create(this, AppConstants.USER_DB_NAME, true, 4, new
		// DbUpdateListener() {
		//
		// @Override
		// public void onUpgrade(SQLiteDatabase db, int oldVersion, int
		// newVersion) {
		// List<String> columns = new ArrayList<String>();
		// //此处增加UserConfig(要增加字段的表的bean) 中增加的字段
		// columns.add("otest");
		// columns.add("ptest");
		// KJDB.upgradeTables(db, UserConfig.class, columns);
		//
		// }
		// });
		AppVariables.needGesture = true;
		Intent mainIntent = null;
		// mainIntent = new Intent(StartApplication.this,
		// GuideActivity.class);// 引导页
		MainAD ad = CacheBean.getInstance().getAd();
		if (null == ad || StringUtils.isEmpty(ad.getImg())) {
			mainIntent = new Intent(StartApplication.this, MainActivity.class);
		}
		else {
			mainIntent = new Intent(StartApplication.this, AtyMainAD.class);
		}
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		StartApplication.this.startActivity(mainIntent);
		StartApplication.this.finish();
	}
	
	public static boolean parse = false;

	@Override
	protected void onResume() {
		if (parse) {
			updateDone();
		}
		super.onResume();
	}
	
	


}
