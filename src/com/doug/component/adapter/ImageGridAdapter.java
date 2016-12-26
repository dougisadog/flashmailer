package com.doug.component.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.doug.component.bean.CItem;
import com.doug.emojihelper.R;
import com.louding.frame.utils.DensityUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class ImageGridAdapter extends BaseAdapter {  
    private List<CItem> mNameList = new ArrayList<CItem>();  
    private LayoutInflater mInflater;  
    private Context mContext;  
    AbsListView.LayoutParams params;  
    
    private ItemCallBack itemCallBack;
    
    public void setItemCallBack(ItemCallBack itemCallBack) {
    	this.itemCallBack = itemCallBack;
    }
    
    public interface ItemCallBack {
    	public void onItemClick(int position, CItem item);
    }
    
    public ImageGridAdapter(Context context, List<CItem> nameList) {  
        mNameList = nameList;  
        mContext = context;  
        mInflater = LayoutInflater.from(context);  
          
//        int h = DensityUtils.dip2px(context, 100);
//        params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, h);  
    } 
    
    public void setDatas(List<CItem> mNameList) {
    	this.mNameList = mNameList;
    	notifyDataSetChanged();
    }
    
    
    public int getCount() {  
        return mNameList.size();  
    }  
  
    public CItem getItem(int position) {  
        return mNameList.get(position);  
    }  
  
    public long getItemId(int position) {  
        return position;  
    }  
  
    public View getView(int position, View convertView, ViewGroup parent) {  
        ItemViewTag viewTag;  
          
        if (convertView == null)  
        {  
            convertView = mInflater.inflate(R.layout.item_city, null);  
            convertView.setLayoutParams(params);
            // construct an item tag   
            viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.tabImg), (TextView) convertView.findViewById(R.id.tabName));  
            convertView.setTag(viewTag);  
        } else  
        {  
            viewTag = (ItemViewTag) convertView.getTag();  
        }  
        View ll = convertView.findViewById(R.id.item);
        ll.setTag(position);
        ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (null != itemCallBack && null != v.getTag()) {
					int pos = Integer.parseInt(v.getTag().toString());
					itemCallBack.onItemClick(pos, getItem(pos));
				}
			}
		});
        // set name   
        viewTag.mName.setText(mNameList.get(position).getName());
        CItem item = mNameList.get(position);
        viewTag.mName.setText(item.getName());
        viewTag.mIcon.setScaleType(ScaleType.FIT_CENTER);
        Glide.with(mContext).load(item.getValue()).placeholder(R.drawable.empty).into(viewTag.mIcon);
//        convertView.setLayoutParams(params);
        return convertView;  
    }  
      
    class ItemViewTag  
    {  
        protected ImageView mIcon;  
        protected TextView mName;  
          
        /** 
         * The constructor to construct a navigation view tag 
         *  
         * @param name 
         *            the name view of the item 
         * @param size 
         *            the size view of the item 
         * @param icon 
         *            the icon view of the item 
         */  
        public ItemViewTag(ImageView icon, TextView name)  
        {  
            this.mName = name;  
            this.mIcon = icon;  
        }  
    }  
  
}  
