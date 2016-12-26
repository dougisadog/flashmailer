package com.doug.component.dialog;

import com.doug.emojihelper.R;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class DialogConfirm3BtnFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirm {
		
		public void onKeyBack();
		public void onBtn1();
		public void onBtn2();
		public void onBtn3();
		
	}
	
	private String titleStrEn, titleStrCn;
	private CallBackDialogConfirm callback;
	private TextView txtDialogTitleCn, txtDialogTitleEn;
	
	public DialogConfirm3BtnFragment() {
		super();
	}
	
	public DialogConfirm3BtnFragment(CallBackDialogConfirm callback, String titleStrEn, String titleStrCn) {
		super();
		this.callback = callback;
		this.titleStrEn = titleStrEn;
		this.titleStrCn = titleStrCn;
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE; 
    	setStyle(style, 0);
	}
	
	public DialogConfirm3BtnFragment(CallBackDialogConfirm callback, String titleStrEn, String titleStrCn, int theme) {
		super();
		this.callback = callback;
		this.titleStrEn = titleStrEn;
		this.titleStrCn = titleStrCn;
		setCancelable(true);
		int style = DialogFragment1.STYLE_NO_TITLE; 
		setStyle(style, theme);
	}
	
	public void showDialog(FragmentManager fragmentManager) {
		if (isAdded()) {
			return;
		}
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, "dialog");
        ft.commitAllowingStateLoss();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_confirm_3btn, null);// 得到加载view
		txtDialogTitleEn = (TextView) v.findViewById(R.id.txtDialogTitleEn);
		txtDialogTitleEn.setText(titleStrEn);
		txtDialogTitleCn = (TextView) v.findViewById(R.id.txtDialogTitleCn);
		txtDialogTitleCn.setText(titleStrCn);
		v.findViewById(R.id.flBtn1).setOnClickListener(this);
		v.findViewById(R.id.flBtn2).setOnClickListener(this);
		v.findViewById(R.id.flBtn3).setOnClickListener(this);
		return v;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		int gravity = Gravity.CENTER;
		if (getTheme() == R.style.DialogTopToBottomTheme) {
			gravity = Gravity.TOP;
		}
		else if (getTheme() == R.style.DialogBottomToTopTheme)  {
			gravity = Gravity.BOTTOM;
		}
		dialog.getWindow().setGravity(Gravity.LEFT | gravity);
		return dialog;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getDialog().getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.flBtn1:
			callback.onBtn1();
			break;
		case R.id.flBtn2:
			callback.onBtn2();
			break;
		case R.id.flBtn3:
			callback.onBtn3();
			break;
		}
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		if (null != callback) {
			callback.onKeyBack();
		}
	}
	
}
