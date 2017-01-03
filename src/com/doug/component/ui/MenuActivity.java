package com.doug.component.ui;

import java.util.List;

import com.doug.AppVariables;
import com.doug.FlashApplication;
import com.doug.component.support.ScreenObserver.ScreenStateListener;
import com.doug.flashmailer.R;
import com.umeng.message.PushAgent;
import com.yanshang.yilicai.lib.SlidingMenu;
import com.yanshang.yilicai.lib.app.SlidingFragmentActivity;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;



public class MenuActivity extends SlidingFragmentActivity implements ScreenStateListener{

	public final static int CODE_QUIT = 1;
	public static boolean isActive = true;
	private boolean isRunning = false;
	
	private AudioManager mAudioManager;
    @Override
    protected void onResume() {
        super.onResume();
        FlashApplication.getInstance().setCurrentRunningActivity(this);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //从后台唤醒，进入前台
        if (!isActive) {
        	isActive = true;
//        	System.out.println("从后台唤醒，进入前台");
//        	PushManager.getInstance().start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //友盟统计
    }
    
	@Override
	protected void onStop() {
		super.onStop();
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        forceShowActionBarOverflowMenu();
//        initNFC();
        PushAgent.getInstance(this).onAppStart();
        FlashApplication.getInstance().setActivity(this);
        FlashApplication.getInstance().addStackActivity(this);
        isRunning = true;
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_SAME, 0);
        //Menu
        setBehindContentView(R.layout.menu_frame);
        findViewById(R.id.accountDetail).setOnClickListener(onMenuClickListener);
        findViewById(R.id.news).setOnClickListener(onMenuClickListener);
        findViewById(R.id.orders).setOnClickListener(onMenuClickListener);
        
        findViewById(R.id.rlOrder1).setOnClickListener(onMenuClickListener);
        findViewById(R.id.rlOrder2).setOnClickListener(onMenuClickListener);
        findViewById(R.id.rlOrder3).setOnClickListener(onMenuClickListener);
        
        findViewById(R.id.transDetail).setOnClickListener(onMenuClickListener);
        findViewById(R.id.remainAccount).setOnClickListener(onMenuClickListener);
        findViewById(R.id.charge).setOnClickListener(onMenuClickListener);
        
        findViewById(R.id.rlTTS).setOnClickListener(onMenuClickListener);
        
        
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindWidthRes(R.dimen.slidingmenu_width);
//		sm.setFadeDegree(0.5f);
		sm.setFadeEnabled(false);
//		sm.setBgFadeEnabled(true);
//		sm.setBgFadeDegree(0.8f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		initData();
		initRequestData();
    }
    
    public void setTouchEnabled(boolean enabled) {
		getSlidingMenu().setSlidingEnabled(enabled);
    }
    
    private OnClickListener onMenuClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MenuActivity.this, OrderListActivity.class);
			switch (v.getId()) {
				case R.id.accountDetail: //个人资料
					if (!AppVariables.isSignin) {
						startActivity(new Intent(MenuActivity.this, SigninActivity.class));
					}
					break;
				case R.id.news: //消息
					break;
				case R.id.orders: //订单信息
					
					i.putExtra("type", 0);
					startActivity(i);
					break;
				case R.id.rlOrder1: //待抢单
					i.putExtra("type", 1);
					startActivity(i);
					break;
				case R.id.rlOrder2: //闪送中
					i.putExtra("type", 2);
					startActivity(i);
					break;
				case R.id.rlOrder3: //待评价
					i.putExtra("type", 3);
					startActivity(i);
					break;
				case R.id.transDetail: //订单信息
					break;
				case R.id.remainAccount: //余额
					break;
				case R.id.charge: //充值
					break;
				case R.id.rlTTS: //充值
					startActivity(new Intent(MenuActivity.this, AtyTTS.class));
					break;
					
			}
		}
		
	};

    protected void dealWithException(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void finish() {
    	View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    	super.finish();
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (onBack()) {
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,
                    AudioManager.FX_FOCUS_NAVIGATION_UP);
		}
		return super.onKeyDown(keyCode, event);
	}
    
    public boolean onBack() {
		return false;
    }
    
    /*************************************************************************************************************************************************/
    
    /**
     * 程序是否在前台运行
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
                return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
            }
        }
        return false;
    }
    
    /**
     * 初始化个人数据
     */
    private void initCurrentDetail() {
    	
    }

	
	/***********************************************************************************************************************************************/
	/**
	/** 数据
	/**
	/***********************************************************************************************************************************************/
	
	protected void initData() {
		initCurrentDetail();
	}
	
	protected void initRequestData() {
		
	}

	@Override
	public void onScreenOn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScreenOff() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserPresent() {
		// TODO Auto-generated method stub
		
	}
}
