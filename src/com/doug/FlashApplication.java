package com.doug;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.doug.component.error.ErrLogManager;
import com.doug.component.service.LocationService;
import com.doug.component.support.AppActivityLifecycleCallbacks;
import com.doug.component.support.CrashHandler;
import com.doug.component.support.ScreenObserver;
import com.louding.frame.ui.AnnotateUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FlashApplication extends Application {

	public LocationService locationService;
    public Vibrator mVibrator;

	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext()); 

		instance = this;
		ErrLogManager.registerHandler();
		if (Build.VERSION.SDK_INT >= 14) {
			registerActivityLifecycleCallbacks(new AppActivityLifecycleCallbacks());
		}
		//开启webview chrome调试
		if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {  
			   WebView.setWebContentsDebuggingEnabled(true);  
		} 

		// 推送消息SDK 初始化
//		UmengManager.getInstance().initPushInfo(this);
		//false 正式 true 集成测试
//		UmengManager.getInstance().initAnalytics(this, true);
//		String info = UmengManager.getDeviceInfo(this);
//		AnnotateUtil.writeErr(info);

		// 监听屏幕
		observer = new ScreenObserver();

	}


	/**
	 * Application
	 * 用于 数据传递，数据共享 等,数据缓存等操作。
	 * 
	 * @author Doug
	 *
	 */
		private static final int SHOW_CUSTOM_ERR_MSG = 0;
		private static final int SHOW_CONNECT_SERVER_ERR_MSG = 10;
		private static final int SHOW_SERVER_ERR_MSG = 11;
		private static final int PUSH_MSG = 20;
		private static final int RESET_TO_LOGIN = 100;

	    private static FlashApplication instance = null;
	    
	    /**
	     * app性格信息
	     */
	    private Activity activity = null;
	    private Activity currentRunningActivity = null;
	    private List<String> stackActivities = new ArrayList<String>();
	    private ScreenObserver observer = null;

	    @SuppressLint("HandlerLeak")
		private Handler uiHandler = new Handler() {
	    	
	    	public void handleMessage(Message msg) {
	    		switch (msg.what) {
				case SHOW_CONNECT_SERVER_ERR_MSG:
					Toast.makeText(FlashApplication.getInstance().getActivity(), "connect fail", Toast.LENGTH_SHORT).show();
					break;
				case SHOW_SERVER_ERR_MSG:
					String serverMsg = (String) msg.obj;
					Toast.makeText(FlashApplication.getInstance().getActivity(), serverMsg, Toast.LENGTH_SHORT).show();
					break;
				case SHOW_CUSTOM_ERR_MSG:
					String errMsg = (String) msg.obj;
					Toast.makeText(FlashApplication.getInstance().getActivity(), errMsg, Toast.LENGTH_SHORT).show();
					break;
				case RESET_TO_LOGIN:
//					M.startAty(YilicaiApplication.getInstance().getActivity());
//					clearStackActivities();
					break;
				}
	    	};
	    	
	    };
	    
	    /**********************************************************************************************************************************************************/
	    
	    public void showCustomErrMsg(String errMsg) {
	    	Message msg = new Message();
	    	msg.what = SHOW_CUSTOM_ERR_MSG;
	    	msg.obj = errMsg;
	    	uiHandler.sendMessage(msg);
	    }
	    
	    public void showConnectServerErrMsg() {
	    	Message msg = new Message();
	        msg.what = SHOW_CONNECT_SERVER_ERR_MSG;
	        uiHandler.sendMessage(msg);
		}
	    
	    
	    public void showErrMsg(String errorMsg) {
	    	Message msg = new Message();
	    	msg.what = SHOW_SERVER_ERR_MSG;
	    	msg.obj = errorMsg;
	    	uiHandler.sendMessage(msg);
	    }
	    
	    public void pushMsg() {
	    	Message msg = new Message();
	    	msg.what = PUSH_MSG;
	    	uiHandler.sendMessage(msg);
	    }
	    
	    public void resetToLogin() {
	    	Message msg = new Message();
	    	msg.what = RESET_TO_LOGIN;
	    	uiHandler.sendMessage(msg);
	    }
	    
	    
	    /**********************************************************************************************************************************************************/
	    
	    public static FlashApplication getInstance() {
	        return instance;
	    }
	    
		public Activity getActivity() {
	        return activity;
	    }

	    public void setActivity(Activity activity) {
	        this.activity = activity;
	    }

	    public Activity getCurrentRunningActivity() {
	        return currentRunningActivity;
	    }

	    public void setCurrentRunningActivity(Activity currentRunningActivity) {
	        this.currentRunningActivity = currentRunningActivity;
	    }
		
		public Handler getUiHandler() {
			return uiHandler;
		}

		public List<String> getStackActivities() {
			return stackActivities;
		}
		
		public void addStackActivity(Activity activity) {
			stackActivities.add(activity.getClass().getSimpleName());
		}
		
		public void removeStackActivity(Activity activity) {
			String name = activity.getClass().getSimpleName();
			if (stackActivities.contains(name))
				stackActivities.remove(name);
		}

		public ScreenObserver getObserver() {
			return observer;
		}
		
	}

