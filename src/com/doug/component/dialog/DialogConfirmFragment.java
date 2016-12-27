package com.doug.component.dialog;

import java.lang.reflect.Field;

import com.doug.flashmailer.R;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment1;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DialogConfirmFragment extends DialogFragment1 implements OnClickListener {

	public static interface CallBackDialogConfirm {
		
		public void onKeyBack();
		public void onCancel();
		public void onSubmit(int position);
		
	}
	
	private String titleStrEn, titleStrCn, btnStrSubmit, btnStrExit;
	private CallBackDialogConfirm callback;
	private TextView txtDialogTitleCn, txtDialogTitleEn;
	private FrameLayout flDialogCancel, flDialogSubmit;
	
	private int position;
	
	public DialogConfirmFragment() {
		super();
	}
	
	public DialogConfirmFragment(CallBackDialogConfirm callback, String titleStrEn, String titleStrCn, int position ) {
		super();
		this.callback = callback;
		this.titleStrEn = titleStrEn;
		this.titleStrCn = titleStrCn;
		this.position = position;
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE; 
    	setStyle(style, 0);
	}
	
	public DialogConfirmFragment(CallBackDialogConfirm callback, String titleStrEn, String titleStrCn, int position, int theme) {
		super();
		this.callback = callback;
		this.titleStrEn = titleStrEn;
		this.titleStrCn = titleStrCn;
		this.position = position;
		setCancelable(true);
		int style = DialogFragment1.STYLE_NO_TITLE; 
		setStyle(style, theme);
	}
	
	public DialogConfirmFragment(CallBackDialogConfirm callback, String titleStrEn, String titleStrCn, int position, String btnStrSubmit, String btnStrExit ) {
		super();
		this.callback = callback;
		this.titleStrEn = titleStrEn;
		this.titleStrCn = titleStrCn;
		this.position = position;
		this.btnStrSubmit = btnStrSubmit;
		this.btnStrExit = btnStrExit;
		setCancelable(true);
    	int style = DialogFragment1.STYLE_NO_TITLE; 
    	setStyle(style, 0);
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
		View v = inflater.inflate(R.layout.dialog_confirm, null);// 得到加载view
		TextView txtSubmit,txtExit;
		txtDialogTitleEn = (TextView) v.findViewById(R.id.txtDialogTitleEn);
		txtDialogTitleEn.setText(titleStrEn);
		txtDialogTitleCn = (TextView) v.findViewById(R.id.txtDialogTitleCn);
		txtDialogTitleCn.setText(titleStrCn);
		flDialogSubmit = (FrameLayout) v.findViewById(R.id.flDialogSubmit);
		txtSubmit = (TextView)flDialogSubmit.findViewById(R.id.txtDialogSubmitEn);
		flDialogCancel = (FrameLayout) v.findViewById(R.id.flDialogCancel);
		txtExit = (TextView)flDialogCancel.findViewById(R.id.txtDialogCancelEn);
		if (!TextUtils.isEmpty(btnStrSubmit)) {
			txtSubmit.setText(btnStrSubmit);
		}
		if (!TextUtils.isEmpty(btnStrExit)) {
			txtExit.setText(btnStrExit);
		}
		flDialogSubmit.setOnClickListener(this);
		flDialogCancel.setOnClickListener(this);
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
		case R.id.flDialogSubmit:
			callback.onSubmit(position);
			break;
		case R.id.flDialogCancel:
			callback.onCancel();
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
	
	@Override
	public void onDetach() {
	    super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}
	
}
