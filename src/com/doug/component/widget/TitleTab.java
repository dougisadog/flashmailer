package com.doug.component.widget;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * tab空间
 * Demo
 * 
 *   <com.nangua.xiaomanjflc.widget.TitleTab
        android:id="@+id/mytab"
                android:layout_width="match_parent"
                android:layout_height="40dp"
                android:orientation="horizontal"
                android:background = "@color/white">
        </com.nangua.xiaomanjflc.widget.TitleTab>
        
 * 		titleTab = (TitleTab) v.findViewById(R.id.mytab);
		final List<String> names = new ArrayList<String>();
		names.add("aasd");
		names.add("bbb");
		names.add("ccc");
		names.add("ddd");
		titleTab.setDatas(names, new ItemCallBack() {

			@Override
			public void onItemClicked(int position) {

				for (int i = 0; i < titleTab.getChildCount(); i++) {
					TextView tv = titleTab.getTextView(i);
					if (null != tv)
						tv.setTextColor(getResources().getColor(position == i ? R.color.orange : R.color.grey));
				}
			}

		});
		
		titleTab.clickItem(0);
		
 * @author Doug
 *
 */
public class TitleTab extends LinearLayout {

	public TitleTab(Context context, List<String> datas) {
		super(context);
	}

	public TitleTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TitleTab(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}
	
	

	private List<String> datas;

	private ItemCallBack itemCallBack;
	
	private int currentPosition = -1;

	public List<String> getDatas() {
		return datas;
	}

	public void setDatas(List<String> datas, ItemCallBack itemCallBack) {
		this.datas = datas;
		this.itemCallBack = itemCallBack;
		this.removeAllViews();
		buildTabs();
	}

	public static interface ItemCallBack {
		public void onItemClicked(int position);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (null != v.getTag() && null != itemCallBack) {
				int position = (Integer) v.getTag();
				itemCallBack.onItemClicked(position);
				currentPosition = position;

			}

		}
	};
	
	public TextView getTextView(int position) {
		if (position < this.getChildCount()) {
			LinearLayout tab = (LinearLayout) this.getChildAt(position);
			if (tab.getChildCount() > 0 && tab.getChildAt(0) instanceof TextView) {
				TextView tv = (TextView) tab.getChildAt(0);
				return tv;
			}
		}
		return null;
	}
	
	public void clickItem(int position) {
			itemCallBack.onItemClicked(position);
			currentPosition = position;
	}

	private void buildTabs() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		params.setMargins(0, 0, 0, 0);

		for (int i = 0; i < datas.size(); i++) {
			LinearLayout tab = new LinearLayout(getContext());
			tab.setLayoutParams(params);
			if (null != itemCallBack) {
				TextView tv = new TextView(getContext());
				LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				paramsTv.setMargins(0, 0, 0, 0);
				tv.setLayoutParams(paramsTv);
				tv.setGravity(Gravity.CENTER);
				tv.setText(datas.get(i));
				tv.setTextSize(14);
				tab.addView(tv);
				tab.setTag(i);
				tab.setOnClickListener(onClickListener);
			}
			this.addView(tab);
		}

	}

	public int getCurrentPosition() {
		return currentPosition;
	}

}
