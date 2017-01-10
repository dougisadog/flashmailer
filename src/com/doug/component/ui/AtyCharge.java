package com.doug.component.ui;

import com.doug.component.support.UIHelper;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AtyCharge extends KJActivity {
	
	@BindView(id = R.id.tel)
	private TextView tel;
	@BindView(id = R.id.llPriceTab)
	private LinearLayout llPriceTab;
	
	@BindView(id = R.id.llWX, click = true)
	private LinearLayout llWX;
	@BindView(id = R.id.llZFB, click = true)
	private LinearLayout llZFB;
	
	@BindView(id = R.id.wxCheck)
	private ImageView wxCheck;
	@BindView(id = R.id.zfbCheck)
	private ImageView zfbCheck;
	
	@BindView(id = R.id.btnCharge, click = true)
	private TextView mCharge;
	
	private EditText customizePrice;
	
	private int currenPayType = 0; // 0微信 1支付宝
	
	private int currentPrice = 1000;
	
	private int currentTabIndex = 0;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_charge);
		UIHelper.setTitleView(this, "", "充值");
		customizePrice = (EditText) findViewById(R.id.customizePrice);
		customizePrice.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (TextUtils.isEmpty(s.toString())) {
					refreshPriceTab(currentTabIndex);
				}
				else {
					refreshPriceTab(-1);
					try {
						currentPrice = Integer.parseInt(s.toString());
					} catch (NumberFormatException e) {
						currentPrice = -1;
						e.printStackTrace();
					}
				}
					
				
			}
		});
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.llWX :
				wxCheck.setImageResource(R.drawable.icon_red_check);
				zfbCheck.setImageResource(R.drawable.icon_red_check_none);
				currenPayType = 0;
				break;
			case R.id.llZFB :
				wxCheck.setImageResource(R.drawable.icon_red_check_none);
				zfbCheck.setImageResource(R.drawable.icon_red_check);
				currenPayType = 1;
				break;
			case R.id.btnCharge :
				Toast.makeText(this, "当前金额为：" + currentPrice + " 当前支付方式为" + currenPayType, Toast.LENGTH_LONG).show();
				break;
			default :
				break;
		}
	}
	

	@Override
	public void initData() {
		super.initData();
		initPriceTab();
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String[] prices = getResources().getStringArray(R.array.priceLevel);
			currentTabIndex = Integer.parseInt(v.getTag().toString());
			refreshPriceTab(currentTabIndex);
			customizePrice.setText("");
			currentPrice = Integer.parseInt(prices[currentTabIndex]);
			
		}
	};
	
	/**
	 * 初始化呢价格btn tab
	 */
	private void initPriceTab() {
		for (int i = 0; i < llPriceTab.getChildCount(); i++) {
			View v = llPriceTab.getChildAt(i);
			v.setTag(i);
			v.setOnClickListener(listener);
		}
	}
	
	private void refreshPriceTab(int index) {
		for (int i = 0; i < llPriceTab.getChildCount(); i++) {
			View v = llPriceTab.getChildAt(i);
			v.setBackgroundResource(index == i ? R.drawable.btn_blue: R.drawable.btn_empty);
		}
	}


}
