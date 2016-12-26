package com.doug.component.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.doug.component.adapter.ImageGridAdapter;
import com.doug.component.adapter.ImagePagerAdapter;
import com.doug.component.adapter.ImagePagerAdapter.LongClickCallBack;
import com.doug.component.bean.CItem;
import com.doug.component.bean.database.UserSearch;
import com.doug.component.cache.CacheBean;
import com.doug.component.dialog.DialogOrderTimeFragment;
import com.doug.component.dialog.DialogOrderTimeFragment.CallBackDialogCitiesWheel;
import com.doug.component.dialog.DialogWheelFragment;
import com.doug.component.dialog.ImagePager;
import com.doug.component.dialog.ImagePager.CallBackDialogConfirm;
import com.doug.component.receiver.AppReceiver;
import com.doug.component.support.HtmlParser;
import com.doug.component.support.HtmlParser.ParserCallBack;
import com.doug.component.support.UIHelper;
import com.doug.component.utils.ImageUtils;
import com.doug.component.utils.KeyboardUitls;
import com.doug.component.widget.LoudingDialogIOS;
import com.doug.emojihelper.R;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;

public class MainActivity extends MenuActivity {

	// 再按一次退出程序
	private long firstTime = 0;

	public static final int REQUEST_CODE = 10001;
	
	private TextView city, txtWeight, orderTime;
	private ImageView plus,minus;
	
	private DialogOrderTimeFragment dialogWheel;
	
	private String[] orderWeight;
	private int currentWeightIndex = 0;

	@Override
	protected void initData() {
		super.initData();
		setContentView(R.layout.activity_main_v2);
		ImageView left = (ImageView) findViewById(R.id.img_left);
		left.setBackgroundResource(R.drawable.account_idcard);
		left.setOnClickListener(listener);
		TextView title_center = (TextView) findViewById(R.id.title_center);
		title_center.setText("跑男");
		findViewById(R.id.rlright).setOnClickListener(listener);
		
		city = (TextView) findViewById(R.id.city);
		ImageView mainAD = (ImageView) findViewById(R.id.mainAD);
		mainAD.setOnClickListener(listener);
		txtWeight = (TextView) findViewById(R.id.txtWeight);
		plus = (ImageView) findViewById(R.id.plus);
		plus.setOnClickListener(listener);
		minus = (ImageView) findViewById(R.id.minus);
		minus.setOnClickListener(listener);
		orderTime = (TextView) findViewById(R.id.orderTime);
		orderTime.setOnClickListener(listener);
		
		orderWeight = getResources().getStringArray(R.array.orderWeight);
	}

	@Override
	protected void initRequestData() {

		txtWeight.setText(orderWeight[0]);
		currentWeightIndex = 0;
		super.initRequestData();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			KeyboardUitls.hideKeyboard(MainActivity.this);
			switch (v.getId()) {
				case R.id.img_left : //左拉
					break;
				case R.id.rlright : //选城市
					break;
				case R.id.mainAD :  //广告
					break;
				case R.id.plus :    //加重
					if (currentWeightIndex == orderWeight.length -1) {
						Toast.makeText(MainActivity.this, "最大", Toast.LENGTH_LONG).show();
						return;
					}
					currentWeightIndex = currentWeightIndex++;
					txtWeight.setText(orderWeight[currentWeightIndex]);
					break;
				case R.id.minus :   //减重
					if (currentWeightIndex == 0) {
						Toast.makeText(MainActivity.this, "最大", Toast.LENGTH_LONG).show();
						return;
					}
					currentWeightIndex = currentWeightIndex--;
					txtWeight.setText(orderWeight[currentWeightIndex]);
					break;
				case R.id.orderTime :   //预约时间
					if (null == dialogWheel)
						dialogWheel = new DialogOrderTimeFragment(new CallBackDialogCitiesWheel() {
							
							@Override
							public void submit(String data) {
								orderTime.setText(data);
								dialogWheel.dismiss();
							}
							
							@Override
							public void cancel() {
								dialogWheel.dismiss();
							}
						}, "取件时间", "", true, 3);
					dialogWheel.showDialog(getSupportFragmentManager());
					break;
				default :
					break;
			}

		}
	};
	

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	// 再按一次退出程序
	@Override
	public void onBackPressed() {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
			}
			else { // 两次按键小于2秒时，退出应用
				finish();
			}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void finish() {
		super.finish();
	}


}
