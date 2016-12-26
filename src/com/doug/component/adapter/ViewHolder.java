package com.doug.component.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewHolder {

	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private MyClickHandler handler;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 *
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null || convertView.getTag() == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}

		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 *
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 通过控件的Id给控件加上点击事件
	 *
	 * @param viewId
	 */
	public void addClick(int viewId) {
		View v = getView(viewId);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.viewClick(v.getId());
			}
		});
	}

	/**
	 * 为TextView设置字符串
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text, boolean click) {
		TextView view = getView(viewId);
		view.setText(text);
		if (click) {
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					handler.viewClick(v.getId());
				}
			});
		}
		return this;
	}

	public void setHandler(MyClickHandler handler) {
		this.handler = handler;
	}

	public interface MyClickHandler {
		void viewClick(int id);
	}

	public int getPosition() {
		return mPosition;
	}

}
