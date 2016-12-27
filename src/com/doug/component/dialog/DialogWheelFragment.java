package com.doug.component.dialog;

import com.doug.component.adapter.WeelAdapter;
import com.doug.flashmailer.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import wheel.OnWheelClickedListener;
import wheel.WheelView;


public class DialogWheelFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogWheel {
		
		public void cancel();
		public void submit(int tag, String data);
		
	}
	
	private WheelView wv;
	private TextView txtDialogTitle, txtSubmit, txtCancel;
	private CallBackDialogWheel callback;
	
	private int tag;
	private String title;
	private String[] datas;
	private String currentData;
	
	public DialogWheelFragment() {
		super();
	}
	
	public DialogWheelFragment(int tag, CallBackDialogWheel callback, String title, String[] datas, String currentData) {
		super();
		this.callback = callback;
		this.title = title;
		this.datas = datas;
		this.currentData = currentData;
		this.tag = tag;
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE, theme = 0; 
    	setStyle(style, theme);
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_wheel, null);
		wv = (WheelView) v.findViewById(R.id.wv1);
		wv.setShadowColor(0xefffffff, 0xcfffffff, 0x3fffffff);
		wv.setVisibleItems(7);
		wv.setWheelBackground(R.drawable.wheel_bg_holo);
		wv.setWheelForeground(R.drawable.wheel_val_holo);
		wv.setViewAdapter(new WeelAdapter(v.getContext(), datas));
		int i = datas.length - 1;
		for (; i > 0; --i) {
			if (datas[i].equals(currentData))
				break;
		}
		wv.setCurrentItem(i);
		wv.addClickingListener(new OnWheelClickedListener() {
			
			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
//				wv.setCurrentItem(itemIndex);
				if (wheel.getCurrentItem() != itemIndex) {
					wv.scroll(itemIndex - wheel.getCurrentItem(), 1000);
					return;
				}
				callback.submit(tag, datas[wv.getCurrentItem()]);
			}
		});
		
		txtDialogTitle = (TextView) v.findViewById(R.id.txtDialogTitle);
		txtDialogTitle.setText(title);
		txtSubmit = (TextView) v.findViewById(R.id.txtSubmit);
		txtSubmit.setOnClickListener(this);
		txtCancel = (TextView) v.findViewById(R.id.txtCancel);
		txtCancel.setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtSubmit:
			callback.submit(tag, datas[wv.getCurrentItem()]);
			break;
		case R.id.txtCancel:
			callback.cancel();
			break;
		}
	}
	
}
