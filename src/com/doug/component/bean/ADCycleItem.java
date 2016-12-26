package com.doug.component.bean;

import com.doug.emojihelper.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ADCycleItem extends LinearLayout {
	
	private ImageView ad;

	/**
	 * 构建答案模板
	 * 
	 * @param context
	 * @param answer
	 *            答案内容
	 * @param position
	 *            答案所处位置
	 */
	public ADCycleItem(Context context, CycleData cycleData) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		LinearLayout fl = (LinearLayout) inflater.inflate(
				R.layout.item_ad_cycle, this);
		
		ad = (ImageView) fl.findViewById(R.id.adImage);
		ad.setVisibility(VISIBLE);
		ad.setImageResource(R.drawable.image_default);
		ad.setScaleType(ScaleType.FIT_XY);
		fl.findViewById(R.id.dataContent).setVisibility(View.GONE);

	}
	
	
	public ImageView getImageView() {
		return ad;
	}

}
