package com.doug.component.dialog;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.doug.flashmailer.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import wheel.OnWheelChangedListener;
import wheel.OnWheelClickedListener;
import wheel.WheelView;
import wheel.adapters.AbstractWheelTextAdapter;

public class DialogOrderTimeFragment extends DialogFragment1 implements OnClickListener, OnWheelChangedListener {
	
	public static interface CallBackDialogCitiesWheel {
		
		public void cancel();
		public void submit(String data);
		
	}
	
	private WheelView mDate;
	private WheelView mHour;
	private WheelView mMinute;
	private TextView txtDialogTitle, txtSubmit, txtCancel;
	private CallBackDialogCitiesWheel callback;
	
	private int level = 1;
	private String title;
	private String currentData;
	private boolean isFinished = false;
	
	private boolean notSecrecy = true;
	private String[] mDateDatas;
	private String[] mHourDatasMap;
	private String [] mMinuteDatasMap;
	private String mCurrentDate;
	private String mCurrentHour;
	private String mCurrentMinute;
	
	private String defaultStr = "默认";
	
	public DialogOrderTimeFragment(CallBackDialogCitiesWheel callback, String title, String currentData) {
		super();
		this.callback = callback;
		this.title = title;
		this.currentData = currentData;
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE, theme = 0; 
    	setStyle(style, theme);
	}
	
	public DialogOrderTimeFragment(CallBackDialogCitiesWheel callback, String title, String currentData, boolean notSecrecy, int level) {
		super();
		this.callback = callback;
		this.title = title;
		this.currentData = currentData;
		this.notSecrecy = notSecrecy;
		this.level = level;
		setCancelable(true);
		int style = DialogFragment1.STYLE_NO_TITLE, theme = 0; 
		setStyle(style, theme);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_wheel_cities, null);
		setUpViews(v);
		setUpListener();
		setUpData();
		return v;
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	private void setUpViews(View v) {
		mDate = (WheelView) v.findViewById(R.id.id_date);
		mDate.setWheelBackground(R.drawable.wheel_bg_holo);
		mDate.setWheelForeground(R.drawable.wheel_val_holo);
		mDate.setShadowColor(0xefffffff, 0xcfffffff, 0x3fffffff);
		mHour = (WheelView) v.findViewById(R.id.id_hour);
		mHour.setWheelBackground(R.drawable.wheel_bg_holo);
		mHour.setWheelForeground(R.drawable.wheel_val_holo);
		mHour.setShadowColor(0xefffffff, 0xcfffffff, 0x3fffffff);
		mHour.setVisibility(level > 0 ? View.VISIBLE : View.GONE);
		mMinute = (WheelView) v.findViewById(R.id.id_minute);
		mMinute.setWheelBackground(R.drawable.wheel_bg_holo);
		mMinute.setWheelForeground(R.drawable.wheel_val_holo);
		mMinute.setShadowColor(0xefffffff, 0xcfffffff, 0x3fffffff);
		mMinute.setVisibility(level > 1 ? View.VISIBLE : View.GONE);
		
		txtDialogTitle = (TextView) v.findViewById(R.id.txtDialogTitle);
		txtDialogTitle.setText(title);
		txtSubmit = (TextView) v.findViewById(R.id.txtSubmit);
		txtSubmit.setOnClickListener(this);
		txtCancel = (TextView) v.findViewById(R.id.txtCancel);
		txtCancel.setOnClickListener(this);
	}
	
	private void setUpListener() {
    	mDate.addChangingListener(this);
    	mHour.addChangingListener(this);
    	mMinute.addChangingListener(this);
    	mDate.addClickingListener(oOnWheelClickedListener);
    	mHour.addClickingListener(oOnWheelClickedListener);
    	mMinute.addClickingListener(oOnWheelClickedListener);
    }
	
	private OnWheelClickedListener oOnWheelClickedListener = new OnWheelClickedListener() {
		
		@Override
		public void onItemClicked(WheelView wheel, int itemIndex) {
//			wv.setCurrentItem(itemIndex);
			if (wheel.getCurrentItem() != itemIndex) {
				wheel.scroll(itemIndex - wheel.getCurrentItem(), 1000);
				return;
			}
			isFinished = true;
			callback.submit(getVal());
		}
	};
	
	private void setUpData() {
		//TODO 未确定
		mDateDatas = getResources().getStringArray(R.array.appointDate);
		mDate.setViewAdapter(new WeelAdapter(getActivity(), mDateDatas));
		// 设置可见条目数量
//		mViewProvince.setVisibleItems(7);
//		mViewCity.setVisibleItems(7);
//		mViewDistrict.setVisibleItems(7);
		
		String[] strs = currentData.split(" ");
		String str = "";
		if (strs.length > 0)
			str = strs[0];
		int i = mDateDatas.length - 1;
		for (; i > 0; --i) {
			if (mDateDatas[i].equals(str))
				break;
		}
		mDate.setCurrentItem(i);
		mCurrentDate = mDateDatas[i];
		
		if (level > 1) {
			String[] cities = null;
			if (i == 0) {//当天
				int currentHour = new Date().getHours();
				cities[0] = "立即取件";
				for (int j = currentHour + 1; j < 23; j++) {
					cities[j - currentHour] = j + "";
				}
			}
			else {
				cities = getResources().getStringArray(R.array.appointHour);
			}
			if (cities == null) {
				cities = new String[] { "" };
			}
			if (strs.length > 1)
				str = strs[1];
			for (i = cities.length - 1; i > 0; --i) {
				if (cities[i].equals(str))
					break;
			}
			mHour.setViewAdapter(new WeelAdapter(getActivity(), cities));
			mHour.setCurrentItem(i);
			mCurrentHour = cities[i];
		}
		
		if (level > 2 && i > 0) {
			if (null == mCurrentHour) return;
			String[] areas = getResources().getStringArray(R.array.appointMinute);
			if (areas == null)
				areas = new String[] { "" };
			if (strs.length > 2)
				str = strs[2];
			for (i = areas.length - 1; i > 0; --i) {
				if (areas[i].equals(str))
					break;
			}
			mMinute.setViewAdapter(new WeelAdapter(getActivity(), areas));
			mMinute.setCurrentItem(i);
			mCurrentMinute = areas[i];
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (isFinished) {
			return;
		}
		if (wheel == mDate) {
			updateHours();
		} else if (wheel == mHour) {
			updateMinutes();
		} else if (wheel == mMinute) {
			
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateMinutes() {
		String[] minutes = null;
		if (mDate.getCurrentItem() == 0 && mHour.getCurrentItem() == 1) {
			int currentMinute = (new Date().getMinutes()/5 + 1) * 5;
			for (int j = currentMinute; j < 56; j = j + 5) {
				minutes[(j - currentMinute)/5] = j + "";
//				System.out.println((j - currentMinute)/5 + ":" + j + "\n");
			}
		}
		else {
			minutes = getResources().getStringArray(R.array.appointMinute);
		} 

		if (minutes == null) {
			minutes = new String[] { "" };
		}
		mCurrentMinute = minutes[0];
		mMinute.setViewAdapter(new WeelAdapter(getActivity(), minutes));
		mMinute.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateHours() {
		int pCurrent = mDate.getCurrentItem();
		mCurrentDate = mDateDatas[pCurrent];
		String[] cities = null;
		if (pCurrent == 0) {//当天
			int currentHour = new Date().getHours();
			cities[0] = "立即取件";
			for (int j = currentHour + 1; j < 23; j++) {
				cities[j - currentHour] = j + "";
			}
		}
		else {
			cities = getResources().getStringArray(R.array.appointHour);
		}
		if (cities == null) {
			cities = new String[] { "" };
		}
		mCurrentHour = cities[0];
		mHour.setViewAdapter(new WeelAdapter(getActivity(), cities));
		mHour.setCurrentItem(0);
		updateMinutes();
	}

	private class WeelAdapter extends AbstractWheelTextAdapter {

		private String[] datas = null;
		
		/**
		 * Constructor
		 */
		protected WeelAdapter(Context context, String[] datas) {
			super(context, R.layout.item_wheel, NO_RESOURCE);
			this.datas = datas;
			setItemTextResource(R.id.txtWheelTitle);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return datas.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return datas[index];
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtSubmit:
			isFinished = true;
			callback.submit(getVal());
			break;
		case R.id.txtCancel:
			callback.cancel();
			break;
		}
	}
	
	private String getVal() {
		String str = "";
		if (0 == mDate.getCurrentItem()) {
			if (0 == mHour.getCurrentItem()) {
				return "立即取件";
			}
			else {
				str = str + "(预约)";
			}
		}
		SimpleDateFormat s = new SimpleDateFormat("YYYY-mm");
		Date date = new Date();
		int myDate = date.getDate();
		if (0 == mDate.getCurrentItem()) myDate++;
		str = str + s.format(new Date()) + "-" + myDate + " ";
		if (level > 0 && !TextUtils.isEmpty(mCurrentHour) && !mCurrentHour.equals(defaultStr) && !mCurrentHour.equals(mCurrentDate)) {
			if (!TextUtils.isEmpty(str)) {
				str += " ";
			}
			str += mCurrentHour;
		}
		if (level > 1 && !TextUtils.isEmpty(mCurrentMinute) && !mCurrentMinute.equals(defaultStr) && !mCurrentMinute.equals(mCurrentHour)) {
			if (!TextUtils.isEmpty(str)) {
				str += " ";
			}
			str += ":" + mCurrentMinute;
		}
		return str;
	}
	
}
