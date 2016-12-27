package com.doug.component.dialog;

import java.util.HashMap;
import java.util.List;

import com.doug.component.adapter.ImagePagerAdapter;
import com.doug.component.bean.CItem;
import com.doug.component.cache.CacheBean;
import com.doug.component.utils.CommonUtils;
import com.doug.flashmailer.R;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class ImagePager extends Fragment implements OnClickListener {

	public static interface CallBackDialogConfirm {
		
		public void onKeyBack();
		public void onSubmit(HashMap<String, String> values);
		
	}
	
	private CallBackDialogConfirm callback;
	
	private static ViewGroup vg;
	private ViewPager id_viewpager;
	private ImagePagerAdapter adapter;
	private int position = 0;
	private boolean showed;
	
	public ImagePager(ViewGroup vg, CallBackDialogConfirm callback, ImagePagerAdapter adapter) {
		super();
		this.callback = callback;
		ImagePager.vg = vg;
		this.adapter = adapter;
	}
	
	public void show(FragmentManager fragmentManager, int position) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(vg.getId(), this);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
        showed = true;
        this.position = position;
	}
	
	
	public void hide(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.remove(this);
		ft.commit();
		showed = false;
	}
	
	private HashMap<String, String> values = new HashMap<String, String>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_image_pager, null);// 得到加载view
		id_viewpager = (ViewPager) v.findViewById(R.id.id_viewpager);
		id_viewpager.setAdapter(adapter);
		List<CItem> items = CacheBean.getInstance().getItems();
		if (null != items && items.size() > 0)
			setDatas(items);
		handler.post(r);
		return v;
	}
	
	private Handler handler = new Handler();
	private Runnable r = new Runnable() {
		
		@Override
		public void run() {
			if (isAdded()) {
//				CommonUtils.controlViewPagerSpeed(getActivity(), id_viewpager, 0);//设置你想要的时间
				id_viewpager.setCurrentItem(position, false);
			}
			else {
				handler.postDelayed(this, 100);
			}
		}
	};
	
	
	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.confirm:
			callback.onSubmit(values);
			break;
		}
	}

	public void setDatas(List<CItem> items) {
		adapter.setDatas(items);
	}

	public boolean isShowed() {
		return showed;
	}
	
}
