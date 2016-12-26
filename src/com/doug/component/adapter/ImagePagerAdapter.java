package com.doug.component.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.doug.component.bean.CItem;
import com.doug.component.widget.ZoomImageView;
import com.doug.component.widget.ZoomImageView.LongPressCallBack;
import com.doug.emojihelper.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ImagePagerAdapter extends PagerAdapter{
	
	private Context context;
	private List<CItem> items = new ArrayList<CItem>();
	private LayoutInflater inflater;
	
	private LongClickCallBack longClickCallBack;
	
	public static interface LongClickCallBack {
		public void longClick(int position);
	}
	
	public ImagePagerAdapter(Context context, List<CItem> items, LongClickCallBack longClickCallBack) {
		this.context = context;
		this.items = items;
		inflater = LayoutInflater.from(context); 
		this.longClickCallBack = longClickCallBack;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position)
	{
		View v = inflater.inflate(R.layout.item_image_pager, null);// 得到加载view
		TextView title = (TextView) v.findViewById(R.id.title);
		title.setText(items.get(position).getName());
		ZoomImageView imageView = (ZoomImageView) v.findViewById(R.id.img);
		imageView.setLongPressCallBack(new LongPressCallBack() {
			
			@Override
			public void longClick() {
				if (null != longClickCallBack)
					longClickCallBack.longClick(position);
			}
		});
		Glide.with(context).load(items.get(position).getValue())
	    .placeholder(R.drawable.empty).into(imageView);
		((ViewPager) container).addView(v, 0);
		v.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (null != longClickCallBack)
					longClickCallBack.longClick(position);
				return false;
			}
		});
		return v;
	}
	
	

	@Override
	public void destroyItem(View container, int position, Object object) {
		
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == arg1;
	}

	@Override
	public int getCount()
	{
		return items.size();
	}
	
	public void setDatas(List<CItem> items) {
		this.items = items;
		notifyDataSetChanged();
	}

}
