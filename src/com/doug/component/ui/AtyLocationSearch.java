package com.doug.component.ui;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.doug.FlashApplication;
import com.doug.component.adapter.AdapterLocationSearch;
import com.doug.component.cache.CacheBean;
import com.doug.component.service.LocationService;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.utils.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author doug
 *
 */
public class AtyLocationSearch extends KJActivity implements OnClickListener {

	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private PoiSearch mPoiSearch;

	private EditText condition1;
	private EditText result;

	private TextView submit;

	private ListView searchResult;

	private LocationService locationService;
	private Marker myMarker;

	private Marker cMaker; // 当前标识

	private AdapterLocationSearch adapter;

	private OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
		public void onGetPoiResult(PoiResult result) {
			List<PoiInfo> infos = result.getAllPoi();
			if (null == infos)
				return;
			for (PoiInfo poiInfo : infos) {
				System.out.println(
						poiInfo.address + poiInfo.location + poiInfo.name);
			}
			adapter.setDatas(infos);
			// 获取POI检索结果
		}
		public void onGetPoiDetailResult(PoiDetailResult result) {
			// 获取Place详情页检索结果
		}
		@Override
		public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_search);

		condition1 = (EditText) findViewById(R.id.condition1);
		condition1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				searchLocation(s.toString());

			}
		});
		result = (EditText) findViewById(R.id.result);

		submit = (TextView) findViewById(R.id.submit);
		submit.setOnClickListener(this);

		searchResult = (ListView) findViewById(R.id.searchResult);
		searchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PoiInfo info = adapter.getDatas().get(position);
				setMapLating(info.location, info.address);
				showMap(info.location.latitude, info.location.longitude, info.address);
				result.setText(info.address);

			}
		});
		adapter = new AdapterLocationSearch(this);
		searchResult.setAdapter(adapter);
		// 检索
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(poiListener);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
		setLocaton();
		initMapView();
		initLocation();
	}

	private void initLocation() {
		locationService = FlashApplication.getInstance().locationService;
		// 获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationService.registerListener(mListener);
		// 注册监听
		int type = getIntent().getIntExtra("from", 0);
		if (type == 0) {
			locationService.setLocationOption(
					locationService.getDefaultLocationClientOption());
		}
		else if (type == 1) {
			locationService.setLocationOption(locationService.getOption());
		}
		locationService.start();// 定位SDK
	}

	private void setLocaton() {
		
		BDLocation bdLocation = CacheBean.getInstance().getMyBDLocation();
		if (null == bdLocation) return;
		boolean firstLocation = false;
		if (null != myMarker) {
			myMarker.remove();
		}
		else {
			firstLocation = true;
		}
		LatLng latLng = new LatLng(bdLocation.getLatitude(),
				bdLocation.getLongitude());

		
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		OverlayOptions options = new MarkerOptions().position(latLng) // 设置marker的位置
				.icon(bitmap) // 设置marker图标
				.zIndex(9) // 设置marker所在层级
				.draggable(false); // 设置手势拖拽
		// 将marker添加到地图上
		myMarker = (Marker) (mBaiduMap.addOverlay(options));
		setMapLating(latLng, bdLocation.getAddrStr(), false);
		if (firstLocation)
			showMap(latLng.latitude, latLng.longitude, bdLocation.getAddrStr());
	}

	private void setMapLating(LatLng latLng, String address) {
		setMapLating(latLng, address, true);
	}

	private void setMapLating(LatLng latLng, String address, boolean reset) {
		if (null != cMaker && reset) {
			cMaker.remove();
		}
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		OverlayOptions options = new MarkerOptions().position(latLng) // 设置marker的位置
				.icon(bitmap) // 设置marker图标
				.zIndex(9) // 设置marker所在层级
				.draggable(false); // 设置手势拖拽
		// 将marker添加到地图上
		cMaker = (Marker) (mBaiduMap.addOverlay(options));
	}
	private void initMapView() {
		mMapView.setLongClickable(true);
	}

	private void showMap(double latitude, double longtitude, String address) {
		LatLng llA = new LatLng(latitude, longtitude);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(llA, 17.0f);
		mBaiduMap.animateMapStatus(u);
	}

	private void searchLocation(String keyword) {
		String city = getIntent().getStringExtra("city");
		if (StringUtils.isEmpty(city)) {
			city = "大连";
		}
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city(city)
				.keyword(keyword).pageNum(10));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.submit :
				String finalResult = result.getText().toString();
				Intent i = getIntent();
				i.putExtra("finalResult", finalResult);
				setResult(10001, i);
				finish();
				break;
			default :
				break;
		}

	}

	@Override
	protected void onDestroy() {
		locationService.unregisterListener(mListener);
		super.onDestroy();
	}

	@Override
	public void setRootView() {
		// TODO Auto-generated method stub

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
				setLocaton();
				adapter.notifyDataSetChanged();
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
