package com.doug.component.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.tts.BaiduTTSManager;
import com.bumptech.glide.Glide;
import com.doug.AppConstants;
import com.doug.component.bean.ADCycleItem;
import com.doug.component.bean.CycleData;
import com.doug.component.bean.ShopADData;
import com.doug.component.bean.ShopADData.ShopADType;
import com.doug.component.cache.CacheBean;
import com.doug.component.dialog.DialogOrderTimeFragment;
import com.doug.component.dialog.DialogOrderTimeFragment.CallBackDialogCitiesWheel;
import com.doug.component.utils.ApplicationUtil;
import com.doug.component.utils.KeyboardUitls;
import com.doug.component.widget.MutiCycleViewHome;
import com.doug.flashmailer.R;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.StringUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class AtyHome extends MenuActivity {

	// 再按一次退出程序
	private long firstTime = 0;

	public static final int REQUEST_CODE = 10001;
	
	private TextView city, txtWeight, orderTime, txtFrom, txtTo;
	private ImageView plus,minus, mainAD;
	
	private DialogOrderTimeFragment dialogWheel;
	
	private String[] orderWeight;
	private int currentWeightIndex = 0;
	
	private LinearLayout llFrom, llTo;
	
	private LinearLayout adContainer;
	private MutiCycleViewHome mcv;
	private int adHeight;
	private String currentArea = "";
	

	@Override
	protected void initData() {
		super.initData();
		setContentView(R.layout.activity_main_v2);
		ImageView left = (ImageView) findViewById(R.id.img_left);
		left.setBackgroundResource(R.drawable.account_idcard);
		
		FrameLayout flleft = (FrameLayout) findViewById(R.id.flleft);
		flleft.setOnClickListener(listener);
		TextView title_center = (TextView) findViewById(R.id.title_center);
		title_center.setText("跑男");
		findViewById(R.id.llright).setOnClickListener(listener);
		
		city = (TextView) findViewById(R.id.city);
		mainAD = (ImageView) findViewById(R.id.mainAD);
		mainAD.setVisibility(View.GONE);
		
		txtWeight = (TextView) findViewById(R.id.txtWeight);
		plus = (ImageView) findViewById(R.id.plus);
		plus.setOnClickListener(listener);
		minus = (ImageView) findViewById(R.id.minus);
		minus.setOnClickListener(listener);
		orderTime = (TextView) findViewById(R.id.orderTime);
		orderTime.setOnClickListener(listener);
		
		llFrom = (LinearLayout) findViewById(R.id.llFrom);
		llFrom.setOnClickListener(listener);
		llTo = (LinearLayout) findViewById(R.id.llTo);
		llTo.setOnClickListener(listener);
		
		txtFrom = (TextView) findViewById(R.id.txtFrom);
		txtTo = (TextView) findViewById(R.id.txtTo);
		
		
		orderWeight = getResources().getStringArray(R.array.orderWeight);
		adContainer = (LinearLayout) findViewById(R.id.adContainer);
		
		int width = ApplicationUtil.getApkInfo(this).width;
		adHeight = (int) (width /AppConstants.BANNER_SCALE);
		LinearLayout.LayoutParams params = new   LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,adHeight);
		
		mcv = new MutiCycleViewHome(this);
		mcv.setLayoutParams(params);
		adContainer.addView(mcv);
	}

	@Override
	protected void initRequestData() {

		txtWeight.setText(orderWeight[0]);
		currentWeightIndex = 0;
		

		loadCycleADDatas(mcv);
		super.initRequestData();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			KeyboardUitls.hideKeyboard(AtyHome.this);
			switch (v.getId()) {
				case R.id.llFrom : //起始
					startActivityForResult(new Intent(AtyHome.this, AtyLocationSearch.class),  999);
					break;
				case R.id.llTo : //终止
					startActivityForResult(new Intent(AtyHome.this, AtyLocationSearch.class),  998);
					break;
				case R.id.flleft : //左拉
					toggle();
					BaiduTTSManager.getInstance().speak("你好");
					break;
				case R.id.llright : //选城市
					Intent i = new Intent(AtyHome.this, AtyCity.class);
					i.putExtra("area", currentArea);
					startActivityForResult(i,  1000);
					break;
				case R.id.plus :    //加重
					if (currentWeightIndex == orderWeight.length -1) {
						Toast.makeText(AtyHome.this, "最大", Toast.LENGTH_LONG).show();
						return;
					}
					currentWeightIndex++;
					txtWeight.setText(orderWeight[currentWeightIndex]);
					break;
				case R.id.minus :   //减重
					if (currentWeightIndex == 0) {
						Toast.makeText(AtyHome.this, "最大", Toast.LENGTH_LONG).show();
						return;
					}
					currentWeightIndex--;
					txtWeight.setText(orderWeight[currentWeightIndex]);
					break;
				case R.id.orderTime :   //预约时间
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
	
	/**
	 * 初始化轮播 控件
	 * @param cycleView  控件view
	 * @param cycleDatas 轮播信息
	 */
	private void initCycleViewData(MutiCycleViewHome cycleView, List<CycleData> cycleDatas) {
		final List<CycleData> showCycleDatas = new ArrayList<CycleData>();
		showCycleDatas.addAll(cycleDatas);
		
		cycleView.setImageResources(showCycleDatas, new MutiCycleViewHome.ImageCycleViewListener() {
			
			@Override
			public void onImageClick(int position, View imageView) {
				//根据类型执行 商品内部跳转 和外链跳转 
				CycleData cycleData = showCycleDatas.get(position);
	            if (cycleData.getType() == ShopADType.goods) {
//	            	CacheShop.getInstance().setFromType(CacheShop.home_banner);
//	            	CacheShop.getInstance().reqShopFrom(null);
//					AtyShopListNew.startAty(AtyHomeNew.this,cycleData.getLinkUrl());
	            }
	            else if (cycleData.getType() == ShopADType.url) {
	            	Intent intent = new Intent();  
	                intent.setAction(Intent.ACTION_VIEW);  
	                intent.addCategory(Intent.CATEGORY_BROWSABLE); 
	            	intent.setData(Uri.parse(cycleData.getLinkUrl()));
	            	startActivity(intent);
	            }
			}
			
			@Override
			public void displayImage(final CycleData cycleData, final ADCycleItem adCycleItem) {
				//TODO 修正图片显示
				adCycleItem.getImageView().setScaleType(ScaleType.FIT_XY);
				Glide.with(AtyHome.this).load(cycleData.getUrl()).into(adCycleItem.getImageView());
			}
		});
		cycleView.startImageTimerTask();
	} 
	
	/**
	 * 获取排序队列
	 * @param adDatas 
	 * @return
	 */
	private List<CycleData> getCycleADs(List<ShopADData> adDatas) {
		List<CycleData> shopADs = new ArrayList<CycleData>();
		Collections.sort(adDatas, new Comparator<ShopADData>() {

			@Override
			public int compare(ShopADData lhs, ShopADData rhs) {
				//首页广告为sortNo 从大到小排列
//				return rhs.getSortNo().compareTo(lhs.getSortNo());
				//首页广告为sortNo 从小到大排列
				return lhs.getSortNo().compareTo(rhs.getSortNo());
			}
		});
		shopADs.addAll(adDatas);
		return shopADs;
	}
	
	
	/**
	 * 拉取轮播广告
	 * @param mcv
	 * @param positon 0 top 1 bottom
	 */
	private void loadCycleADDatas(final MutiCycleViewHome mcv) {
		List<ShopADData> ads = CacheBean.getInstance().getShopADDatas();
		String adUrl = AppConstants.GET_SLIDE_IMAGE;
		if (null != ads && ads.size() > 0) {
			ads = CacheBean.getInstance().getShopADDatas();
			initCycleViewData(mcv, getCycleADs(ads));
			mainAD.setVisibility(View.GONE);
			return;
		}
		
			// 一步任务获取图片
			KJHttp kjh = new KJHttp();
			HttpParams params = new HttpParams();
			kjh.post(adUrl, params, new HttpCallBack(this, false) {

				@Override
				public void success(JSONObject ret) {
					try {
						List<ShopADData> shopADDatas = new ArrayList<ShopADData>();
						JSONArray ja = ret.getJSONArray("data");
						for (int i = 0; i < ja.length(); i++) {
							ShopADData ad = new ShopADData();
							ad.setSortNo((double) i);
							ad.setImgUrl(ja.getJSONObject(i).getJSONObject("extra")
									.getString("img"));
							ad.setAndroidUrl(ja.getJSONObject(i).getJSONObject("extra")
									.getString("url"));
							ad.setType(ShopADType.url);
							shopADDatas.add(ad);
						}
//						shopADDatas.clear();
						if (shopADDatas.size() > 0) {
							CacheBean.getInstance().setShopADDatas(shopADDatas);
							initCycleViewData(mcv, getCycleADs(shopADDatas));
						}
						else {
							mcv.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					super.success(ret);
				}
				
			});
		}
	

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
	protected void onActivityResult(int req, int res, Intent intent) {
		if (null == intent) return;
		switch (req) {
		case 1000:
			String cityName = intent.getStringExtra("city");
			if (!StringUtils.isEmpty(cityName)) {
				currentArea = cityName;
				city.setText(cityName);
			}
			break;
		case 999:
			String from = intent.getStringExtra("finalResult");
			if (!StringUtils.isEmpty(from)) {
				txtFrom.setText(from);
			}
			break;
		case 998:
			String to = intent.getStringExtra("finalResult");
			if (!StringUtils.isEmpty(to)) {
				txtTo.setText(to);
			}
			break;
			

		default:
			break;
		}
		
		// TODO Auto-generated method stub
		super.onActivityResult(req, res, intent);
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
