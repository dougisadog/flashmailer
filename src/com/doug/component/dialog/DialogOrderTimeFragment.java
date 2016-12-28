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

public class DialogOrderTimeFragment extends DialogFragment1
		implements
			OnClickListener,
			OnWheelChangedListener {

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

	private String[] mDateDatas;
	private String[] mHourDatasMap;
	private String[] mMinuteDatasMap;
	public DialogOrderTimeFragment(CallBackDialogCitiesWheel callback,
			String title, String currentData) {
		super();
		this.callback = callback;
		this.title = title;
		this.currentData = currentData;
		setCancelable(true);
		int style = DialogFragment1.STYLE_NO_TITLE, theme = 0;
		setStyle(style, theme);
	}

	public DialogOrderTimeFragment(CallBackDialogCitiesWheel callback,
			String title, String currentData, boolean notSecrecy, int level) {
		super();
		this.callback = callback;
		this.title = title;
		this.currentData = currentData;
		this.level = level;
		setCancelable(true);
		int style = DialogFragment1.STYLE_NO_TITLE, theme = 0;
		setStyle(style, theme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
			// wv.setCurrentItem(itemIndex);
			if (wheel.getCurrentItem() != itemIndex) {
				wheel.scroll(itemIndex - wheel.getCurrentItem(), 1000);
				return;
			}
		}
	};

	private void setUpData() {
		// TODO 未确定
		mDateDatas = getResources().getStringArray(R.array.appointDate);
		mDate.setViewAdapter(new WeelAdapter(getActivity(), mDateDatas));
		// 设置可见条目数量
		// mViewProvince.setVisibleItems(7);
		// mViewCity.setVisibleItems(7);
		// mViewDistrict.setVisibleItems(7);

		String[] strs = currentData.split(" ");
		String str = "";
		if (strs.length > 0)
			str = strs[0];
		int i = mDateDatas.length - 1;
		for (; i > 0; --i) {
			if (str.equals(mDateDatas[i]))
				break;
		}
		mDate.setCurrentItem(i);
		if (level > 1) {
			updateHours();
		}

		if (level > 2 && i > 0) {
			updateMinutes();
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (isFinished) {
			return;
		}
		if (wheel == mDate) {
			updateHours();
		}
		else if (wheel == mHour) {
			updateMinutes();
		}
		else if (wheel == mMinute) {

		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateMinutes() {
		if (mDate.getCurrentItem() == 0) {
			if (mHour.getCurrentItem() == 0) {
				mMinute.setViewAdapter(new WeelAdapter(getActivity(), new String[0]));
				return;
			}
			else if (mHour.getCurrentItem() == 1){
				int currentMinute = (new Date().getMinutes() / 5 + 1) * 5;
				
				int size = (56 - currentMinute) / 5 + 1;
				mMinuteDatasMap = new String[size];
				if (size == 1) {
					mMinuteDatasMap = new String[] {"00"};
				}
				else {
					for (int j = currentMinute; j < 56; j = j + 5) {
						mMinuteDatasMap[(j - currentMinute) / 5] = j + "";
						// System.out.println((j - currentMinute)/5 + ":" + j + "\n");
					}
				}
			}
			else {
				mMinuteDatasMap = getResources().getStringArray(R.array.appointMinute);
			}
		}
		else {
			mMinuteDatasMap = getResources().getStringArray(R.array.appointMinute);
		}
		mMinute.setViewAdapter(new WeelAdapter(getActivity(), mMinuteDatasMap));
		mMinute.setCurrentItem(0);
	}

	/**
	 * 根据当前小时更新
	 */
	private void updateHours() {
		int pCurrent = mDate.getCurrentItem();
		if (pCurrent == 0) {
			int currentHour = new Date().getHours();
			if (new Date().getMinutes() > 55) {
				currentHour++;
			}
			int size = 24 - currentHour + 1;
			mHourDatasMap = new String[size];
			
			mHourDatasMap[0] = "立即取件";
			for (int j = currentHour; j < 24; j++) {
				mHourDatasMap[j - currentHour + 1] = j + "";
			}
		}
		else {
			mHourDatasMap = getResources().getStringArray(R.array.appointHour);
		}
		mHour.setViewAdapter(new WeelAdapter(getActivity(), mHourDatasMap));
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
			case R.id.txtSubmit :
				isFinished = true;
				callback.submit(getVal());
				break;
			case R.id.txtCancel :
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
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		int myDate = date.getDate();
		if (0 == mDate.getCurrentItem())
			myDate++;
		str = str + s.format(new Date()) + "-" + myDate + " ";
		if (level > 1) {
			str += " " + mHourDatasMap[mHour.getCurrentItem()];
		}
		if (level > 2) {
			str += " :" + mMinuteDatasMap[mMinute.getCurrentItem()];
		}
		return str;
	}

}
