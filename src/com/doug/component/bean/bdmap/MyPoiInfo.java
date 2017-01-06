package com.doug.component.bean.bdmap;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.utils.DistanceUtil;
import com.doug.component.cache.CacheBean;

public class MyPoiInfo extends PoiInfo implements Comparable<MyPoiInfo>{
	
	public MyPoiInfo (CloudPoiInfo poiInfo) {
		this.address = poiInfo.address;
		this.city = poiInfo.city;
		this.location = new LatLng(poiInfo.latitude, poiInfo.longitude);
		this.name = poiInfo.title;
		
	}
	
	public MyPoiInfo (PoiInfo poiInfo) {
		this.address = poiInfo.address;
		this.city = poiInfo.city;
		this.location = poiInfo.location;
		this.name = poiInfo.name;
		
	}
	
	public MyPoiInfo (com.baidu.mapapi.cloud.CloudRgcResult.PoiInfo poiInfo) {
		this.address = poiInfo.address;
		this.location = poiInfo.location;
		this.name = poiInfo.name;
		
	}

	@Override
	public int compareTo(MyPoiInfo another) {
		BDLocation myBDLocation = CacheBean.getInstance().getMyBDLocation();
		LatLng my = new LatLng(myBDLocation.getLatitude(), myBDLocation.getLongitude());
		Double dis1 = DistanceUtil.getDistance(my, this.location);
		Double dis2 = DistanceUtil.getDistance(my, another.location);
		return dis1.compareTo(dis2);
	}
	
	

}
