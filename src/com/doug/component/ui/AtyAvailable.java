package com.doug.component.ui;

import com.doug.component.support.UIHelper;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

//关于一理财页面
public class AtyAvailable extends KJActivity {
	@BindView(id = R.id.btnCharge, click = true)
	private TextView mCharge;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_available);
		UIHelper.setTitleView(this, "", "明细","账户余额", 0);
		findViewById(R.id.flright).setOnClickListener(this);
		
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.btnCharge :
				startActivity(new Intent(AtyAvailable.this, AtyCharge.class));
				break;
			case R.id.flright :
				startActivity(new Intent(AtyAvailable.this, AtyTransDetail.class));
				break;
			default :
				break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
