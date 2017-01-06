package com.doug.component.adapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.doug.component.bean.bdmap.MyPoiInfo;
import com.doug.component.cache.CacheBean;
import com.doug.flashmailer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 核心单词 适配器
 * @author tovey
 *
 */
public class AdapterLocationSearch extends ArrayAdapter {

	private static final String TAG = "wordItem";
	private LayoutInflater mInflater;

	 private List<MyPoiInfo> datas = new ArrayList<MyPoiInfo>();
	 private Context context;

	public AdapterLocationSearch(Context context) {
		super(context, 0);
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
	}
	
	public AdapterLocationSearch(Context context, int resource) {
		super(context, resource);
//		datas = getDataFromMapArrayList();
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	/*
	 * public ReadWordData getItem(int position){ return datas.get(position); }
	 */
	public MyPoiInfo getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LocationViewHolder holder = null;
//		List<ReadWordData> dataFromMapArrayList = getDataFromMapArrayList();
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_search_drop, null, true);
			holder = new LocationViewHolder();
			
			holder.tName = (TextView) convertView.findViewById(R.id.name);
			holder.tLocation = (TextView) convertView.findViewById(R.id.location);
			holder.tDistance = (TextView) convertView.findViewById(R.id.distance);
			
			convertView.setTag(holder);
		} else {
			holder = (LocationViewHolder) convertView.getTag();
		}
		// ReadWordData readWordData = datas.get(position);

		//ArrayList<String> data = dataFromMapArrayList.get(position);

		MyPoiInfo item = datas.get(position);
		holder.tName.setText(item.name);
		holder.tLocation.setText(item.address);
		
		BDLocation myBDLocation = CacheBean.getInstance().getMyBDLocation();
		if (null == myBDLocation) {
			holder.tDistance.setText("暂无数据");
		}
		else {
			 
			LatLng my = new LatLng(myBDLocation.getLatitude(), myBDLocation.getLongitude());
			double d = DistanceUtil.getDistance(my, item.location)/1000;
			BigDecimal b = new BigDecimal(d);
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 
			holder.tDistance.setText(f1 + "km");
		}
		return convertView;
	}

	public final class LocationViewHolder {
		TextView tName, tLocation, tDistance;
	}

	
	public List<MyPoiInfo> getDatas() {
		return datas;
	}
	
	public void setDatas(List<MyPoiInfo> infos) {
		this.datas = infos;
		notifyDataSetChanged();
	}
	
	
}
