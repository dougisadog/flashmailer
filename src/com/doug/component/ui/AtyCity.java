package com.doug.component.ui;

import java.util.ArrayList;
import java.util.List;
import com.doug.component.adapter.ImageGridAdapter;
import com.doug.component.adapter.ImageGridAdapter.ItemCallBack;
import com.doug.component.bean.CItem;
import com.doug.component.support.UIHelper;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

/**
 * 
 * @author doug
 *
 */
public class AtyCity extends KJActivity implements OnClickListener {
	
	private GridView mCity;
	private List<CItem> cities;
	private String area = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        UIHelper.setTitleView(this, "", "请选择你所在的市区");
        area = getIntent().getStringExtra("area");
        init();
		mCity = (GridView) findViewById(R.id.emoji);
		ImageGridAdapter adapter = new ImageGridAdapter(this, cities);
		adapter.setItemCallBack(new ItemCallBack() {
			
			@Override
			public void onItemClick(int position, CItem item) {
				Intent i = getIntent();
				i.putExtra("city", item.getValue());
				setResult(10000, i);
				finish();
				
			}
		});
		mCity.setAdapter(adapter);
    }
    

    private void init() {
    	cities = new ArrayList<CItem>();
    	String[] a = getResources().getStringArray(R.array.areas);
    	for (int i = 0; i < a.length; i++) {
    		int selected = 0;
    		if (area.equals(a[i])) {
    			selected = 1;
    		}
    		cities.add(new CItem(a[i], a[i], selected + ""));
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
