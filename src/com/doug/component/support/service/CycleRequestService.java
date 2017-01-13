package com.doug.component.support.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.doug.AppConstants;
import com.doug.FlashApplication;
import com.doug.component.cache.CacheBean;
import com.doug.component.error.DebugPrinter;
import com.doug.component.service.LocationService;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

public class CycleRequestService extends Service {
	
	private KJHttp kjh = new KJHttp();
	
	private LocationService locationService;
	
	public final static String ACTION = Intent.ACTION_DEFAULT; 

	public CycleRequestService() {
		super();
	}

	@Override
	public void onCreate() {
		
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initLocation();
		if (null != intent) {
		boolean state = intent.getBooleanExtra("state", true);
		String id = intent.getStringExtra("typeId");
		}
		startTimeTask();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void sendRequest() {
		BDLocation location = CacheBean.getInstance().getMyBDLocation();
		if (null == location) return;
		DebugPrinter.d(location.getAddrStr());
		
		 Intent intent=new Intent();
         intent.putExtra("text", System.currentTimeMillis() + "");
         intent.setAction(ACTION);//action与接收器相同
         sendBroadcast(intent);
		// 一步任务获取图片
//		HttpParams params = new HttpParams();
//		kjh.post(AppConstants.GET_SLIDE_IMAGE, params, new HttpCallBack(this, false) {
//
//			@Override
//			public void success(JSONObject ret) {
//				DebugPrinter.d("cycle request success");
//				super.success(ret);
//			}
//			
//		});
	}
	
	/**
	 * 初始化当前位置监听
	 */
	private void initLocation() {
		locationService = FlashApplication.getInstance().locationService;
		// 获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationService.registerListener(mListener);
		// 注册监听
			locationService.setLocationOption(
					locationService.getDefaultLocationClientOption());
		locationService.start();// 定位SDK
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
		if (null != mTimer)
			mTimer.cancel();
		mTimer = null;
		System.out.println("当前线程 执行了=====TestService=结束====="
				+ Thread.currentThread().getId());
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*****
	 * @see copy funtion to you project
	 *      定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
	 *
	 */
	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (null != location
					&& location.getLocType() != BDLocation.TypeServerError) {
				CacheBean.getInstance().setMyBDLocation(location);
				StringBuffer sb = new StringBuffer(256);
				sb.append("time : ");
				/**
				 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
				 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
				 */
				// sb.append(location.getTime());
				// sb.append("\nlocType : ");// 定位类型
				// sb.append(location.getLocType());
				// sb.append("\nlocType description : ");// *****对应的定位类型说明*****
				// sb.append(location.getLocTypeDescription());
				// sb.append("\nlatitude : ");// 纬度
				// sb.append(location.getLatitude());
				// sb.append("\nlontitude : ");// 经度
				// sb.append(location.getLongitude());
				// sb.append("\nradius : ");// 半径
				// sb.append(location.getRadius());
				// sb.append("\nCountryCode : ");// 国家码
				// sb.append(location.getCountryCode());
				// sb.append("\nCountry : ");// 国家名称
				// sb.append(location.getCountry());
				// sb.append("\ncitycode : ");// 城市编码
				// sb.append(location.getCityCode());
				// sb.append("\ncity : ");// 城市
				// sb.append(location.getCity());
				// sb.append("\nDistrict : ");// 区
				// sb.append(location.getDistrict());
				// sb.append("\nStreet : ");// 街道
				// sb.append(location.getStreet());
				// sb.append("\naddr : ");// 地址信息
				// sb.append(location.getAddrStr());
				// sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
				// sb.append(location.getUserIndoorState());
				// sb.append("\nDirection(not all devices have value): ");
				// sb.append(location.getDirection());// 方向
				// sb.append("\nlocationdescribe: ");
				// sb.append(location.getLocationDescribe());// 位置语义化信息
				// sb.append("\nPoi: ");// POI信息
				// if (location.getPoiList() != null &&
				// !location.getPoiList().isEmpty()) {
				// for (int i = 0; i < location.getPoiList().size(); i++) {
				// Poi poi = (Poi) location.getPoiList().get(i);
				// sb.append(poi.getName() + ";");
				// }
				// }
				if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
					// sb.append("\nspeed : ");
					// sb.append(location.getSpeed());// 速度 单位：km/h
					// sb.append("\nsatellite : ");
					// sb.append(location.getSatelliteNumber());// 卫星数目
					// sb.append("\nheight : ");
					// sb.append(location.getAltitude());// 海拔高度 单位：米
					// sb.append("\ngps status : ");
					// sb.append(location.getGpsAccuracyStatus());//
					// *****gps质量判断*****
					// sb.append("\ndescribe : ");
					// sb.append("gps定位成功");

				}
				else if (location
						.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
					// 运营商信息
					if (location.hasAltitude()) {// *****如果有海拔高度*****
						sb.append("\nheight : ");
						sb.append(location.getAltitude());// 单位：米
					}
					sb.append("\noperationers : ");// 运营商信息
					sb.append(location.getOperators());
					sb.append("\ndescribe : ");
					sb.append("网络定位成功");
				}
				else if (location
						.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
					sb.append("\ndescribe : ");
					sb.append("离线定位成功，离线定位结果也是有效的");
				}
				else if (location.getLocType() == BDLocation.TypeServerError) {
					sb.append("\ndescribe : ");
					sb.append(
							"服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				}
				else if (location
						.getLocType() == BDLocation.TypeNetWorkException) {
					sb.append("\ndescribe : ");
					sb.append("网络不同导致定位失败，请检查网络是否通畅");
				}
				else if (location
						.getLocType() == BDLocation.TypeCriteriaException) {
					sb.append("\ndescribe : ");
					sb.append(
							"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				}
			}
		}

	};

}
