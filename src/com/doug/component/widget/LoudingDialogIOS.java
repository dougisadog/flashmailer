package com.doug.component.widget;

import com.doug.flashmailer.R;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * IOS系统样式dialog模仿
 * @author Doug
 *
 */
public class LoudingDialogIOS {
	Context context;
	android.app.AlertDialog ad;
	TextView titleView;
	TextView messageView;
	LinearLayout buttonLayout;
	RelativeLayout loudingLayout;
	TextView loudingtext;

	public LoudingDialogIOS(Context context) {
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.setCancelable(false);
		ad.setOnKeyListener(onKeyListener);
		ad.show();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(R.layout.loudingdialog_ios);
		titleView = (TextView) window.findViewById(R.id.title);
		messageView = (TextView) window.findViewById(R.id.message);
		buttonLayout = (LinearLayout) window.findViewById(R.id.buttonLayout);
		loudingLayout = (RelativeLayout) window.findViewById(R.id.louding);
		loudingtext = (TextView) window.findViewById(R.id.loudtext);
		titleView.setVisibility(View.GONE);
		messageView.setVisibility(View.GONE);
	}
	
	public void show() {
		if (null != ad)
			ad.show();
	}
	
    /**
     * add a keylistener for progress dialog
     */
    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

	public void setTitle(int resId, int resColor) {
		titleView.setText(resId);
		titleView.setTextColor(context.getResources().getColor(resColor));
		titleView.setVisibility(View.VISIBLE);
	}

	public void setTitle(String resId, int resColor) {
		titleView.setText(resId);
		titleView.setTextColor(context.getResources().getColor(resColor));
		titleView.setVisibility(View.VISIBLE);
	}

	public void setMessage(int resId, int resColor) {
		messageView.setText(resId);
		messageView.setTextColor(context.getResources().getColor(resColor));
		messageView.setVisibility(View.VISIBLE);
	}

	public void setMessage(String resId, int resColor) {
		messageView.setText(resId);
		messageView.setTextColor(context.getResources().getColor(resColor));
		messageView.setVisibility(View.VISIBLE);
	}

	public void setMessageicon(int resId) {
		Drawable icon;
		Resources res = context.getResources();
		icon = res.getDrawable(resId);
		// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
		icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
		messageView.setCompoundDrawables(icon, null, null, null); // 设置左图标
		messageView.setCompoundDrawablePadding(10);
	}

	/**
	 * 设置按钮
	 *
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text, Integer bg, final View.OnClickListener listener) {
		Button button = new Button(context);
		LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		button.setLayoutParams(params);
		//暂时屏蔽所有其他背景
//		if (null == bg) {
			button.setBackgroundResource(R.color.white);
//		}
//		else {
//			button.setBackgroundResource(bg);
//		}
		button.setGravity(Gravity.CENTER);
		button.setText(text);
		button.setTextColor(context.getResources().getColor(R.color.blue_v2));
		button.setTextSize(16);
		button.setOnClickListener(listener);
		if (buttonLayout.getChildCount() > 0) {
			button.setLayoutParams(params);
			buttonLayout.addView(button, 1);
		} else {
			button.setLayoutParams(params);
			buttonLayout.addView(button);
		}
	}

	/**
	 * 设置按钮
	 *
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,
			final View.OnClickListener listener) {
		Button button = new Button(context);
		LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		button.setLayoutParams(params);
		button.setBackgroundResource(R.color.white);
		button.setGravity(Gravity.CENTER);
		button.setText(text);
		button.setTextColor(context.getResources().getColor(R.color.blue_v2));
		button.setTextSize(16);
		button.setOnClickListener(listener);
		if (buttonLayout.getChildCount() > 0) {
			button.setLayoutParams(params);
			buttonLayout.addView(button, 1);
		} else {
			button.setLayoutParams(params);
			buttonLayout.addView(button);
		}

	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}

	/**
	 * 显示温馨提示对话框,带确定
	 */
	public void showConfirmHint(int textId) {
		setTitle(R.string.dialog_title, R.color.black);
		setMessage(textId, R.color.black);
		setPositiveButton(
				context.getResources().getString(R.string.dialog_confirm),
				null, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dismiss();
					}
				});
	}

	public void showConfirmHint(String textId) {
		setTitle(R.string.dialog_title, R.color.black);
		setMessage(textId, R.color.black);
		setPositiveButton(
				context.getResources().getString(R.string.dialog_confirm),
				null, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dismiss();
					}
				});
	}

	/**
	 * 显示温馨提示对话框,带确定
	 */
	public void showConfirmHintAndFinish(int textId) {
		setTitle(R.string.dialog_title, R.color.black);
		setMessage(textId, R.color.black);
		setPositiveButton(
				context.getResources().getString(R.string.dialog_confirm),
				null, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dismiss();
						Activity activity = (Activity) context;
						activity.finish();
					}
				});
	}

	public void showConfirmHintAndFinish(String textId) {
		setTitle(R.string.dialog_title, R.color.black);
		setMessage(textId, R.color.black);
		setPositiveButton(
				context.getResources().getString(R.string.dialog_confirm),
				null, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dismiss();
						Activity activity = (Activity) context;
						activity.finish();
					}
				});
	}

	/**
	 * 显示操作对话框,带确定
	 */
	public void showOperateMessage(int textId) {
		setTitle(R.string.dialog_title, R.color.black);
		setMessage(textId, R.color.black);
		setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	public void showOperateMessage(String textId) {
		setTitle(R.string.dialog_title, R.color.black);
		setMessage(textId, R.color.black);
		setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	/**
	 * 显示温馨提示对话框，带icon
	 */
	public void showIconHint(int textId) {
		setTitle(R.string.dialog_title, R.color.black);
		setMessage(textId, R.color.black);
		setMessageicon(R.drawable.dialog_icon_confirm);
	}

	public void showIconHint(String textId) {
		setTitle(R.string.dialog_title, R.color.black);
		setMessage(textId, R.color.black);
		setMessageicon(R.drawable.dialog_icon_confirm);
	}

	/**
	 * 显示正在加载对话框,带小菊花
	 */
	public void showLouding(int textId) {
		titleView.setVisibility(View.GONE);
		messageView.setVisibility(View.GONE);
		buttonLayout.setVisibility(View.GONE);
		loudingtext.setText(textId);
		loudingLayout.setVisibility(View.VISIBLE);
	}

	public void showLouding(String textId) {
		titleView.setVisibility(View.GONE);
		messageView.setVisibility(View.GONE);
		buttonLayout.setVisibility(View.GONE);
		loudingtext.setText(textId);
		loudingLayout.setVisibility(View.VISIBLE);
	}

}
