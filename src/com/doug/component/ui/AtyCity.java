package com.doug.component.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bumptech.glide.Glide;
import com.doug.component.adapter.ImageGridAdapter;
import com.doug.component.adapter.ImageGridAdapter.ItemCallBack;
import com.doug.component.bean.CItem;
import com.doug.component.bean.MainAD;
import com.doug.component.cache.CacheBean;
import com.doug.component.utils.ImageUtils;
import com.doug.emojihelper.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.louding.frame.KJActivity;
import com.louding.frame.utils.StringUtils;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * 
 * @author doug
 *
 */
public class AtyCity extends KJActivity implements OnClickListener {
	
	private PullToRefreshGridView mCity;
	private boolean isRefreshing = false;
	private List<CItem> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        init();
		mCity = (PullToRefreshGridView) findViewById(R.id.emoji);
		mCity.setMode(Mode.DISABLED);
//		emoji.setOnRefreshListener(new OnRefreshListener<GridView>() {
//
//			@Override
//			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
//			}
//
//		});
		ImageGridAdapter adapter = new ImageGridAdapter(this, cities);
		adapter.setItemCallBack(new ItemCallBack() {
			
			@Override
			public void onItemClick(int position, CItem item) {
				// TODO Auto-generated method stub
				
			}
		});
		mCity.setAdapter(adapter);
    }
    

    private void init() {
    	cities = new ArrayList<CItem>();
    	String[] a = getResources().getStringArray(R.array.areas);
    	for (int i = 0; i < a.length; i++) {
    		cities.add(new CItem(a[0], a[0], i + ""));
		}
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AD:
                break;
            default:
                break;
        }

    }

	@Override
	public void setRootView() {
		// TODO Auto-generated method stub
		
	}
    
}
