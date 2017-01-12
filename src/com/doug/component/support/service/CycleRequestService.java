package com.doug.component.support.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Message;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.doug.AppConstants;
import com.doug.component.bean.ShopADData;
import com.doug.component.bean.ShopADData.ShopADType;
import com.doug.component.cache.CacheBean;
import com.doug.component.error.DebugPrinter;
import com.louding.frame.KJHttp;
import com.louding.frame.KJActivity.ActivityState;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

public class CycleRequestService extends Service {
	
	private KJHttp kjh = new KJHttp();

	public CycleRequestService() {
		super();
	}

	@Override
	public void onCreate() {
		
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (null != intent) {
		boolean state = intent.getBooleanExtra("state", true);
		String id = intent.getStringExtra("typeId");
		}
		startTimeTask();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void sendRequest() {
		// 一步任务获取图片
		HttpParams params = new HttpParams();
		kjh.post(AppConstants.GET_SLIDE_IMAGE, params, new HttpCallBack(this, false) {

			@Override
			public void success(JSONObject ret) {
				DebugPrinter.d("cycle request success");
				super.success(ret);
			}
			
		});
	}
	
	private Timer mTimer;
    private TimerTask mTimerTask;
	
	/**
	 * 开启条跳转倒计时
	 */
    private void startTimeTask() {
    	
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                	sendRequest();
                }
            };
            //开始一个定时任务
            mTimer.schedule(mTimerTask, 1000, 1000 * 10);
    }

	@Override
	public void onDestroy() {
		System.out.println("当前线程 执行了=====TestService=结束====="
				+ Thread.currentThread().getId());
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
