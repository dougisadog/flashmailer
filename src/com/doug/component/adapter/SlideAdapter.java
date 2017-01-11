package com.doug.component.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;

import com.squareup.picasso.Picasso;
import com.doug.AppConstants;
import com.doug.component.adapter.ViewHolder.MyClickHandler;
import com.doug.component.bean.ADCycleItem;
import com.doug.component.bean.CycleData;
import com.doug.component.bean.ShopADData;
import com.doug.component.bean.ShopADData.ShopADType;
import com.doug.component.cache.CacheBean;
import com.doug.component.widget.MutiCycleViewHome;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

public abstract class SlideAdapter<T> extends BaseAdapter {

	protected List<T> list;
	protected Context context;
	protected ListView listview;
	private LayoutInflater inflater;
	protected final int itemLayoutId;

	private View slide;

	public SlideAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = mDatas;
		this.itemLayoutId = itemLayoutId;
	}

	public SlideAdapter(Context context, int itemLayoutId) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.itemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public T getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
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
//	            	Intent intent = new Intent();  
//	                intent.setAction(Intent.ACTION_VIEW);  
//	                intent.addCategory(Intent.CATEGORY_BROWSABLE); 
//	            	intent.setData(Uri.parse(cycleData.getLinkUrl()));
//	            	startActivity(intent);
	            }
			}
			
			@Override
			public void displayImage(final CycleData cycleData, final ADCycleItem adCycleItem) {
				//TODO 修正图片显示
				adCycleItem.getImageView().setScaleType(ScaleType.FIT_XY);
				Picasso.with(context).load(cycleData.getUrl()).into(adCycleItem.getImageView());
			}
		});
		cycleView.startImageTimerTask();
	} 
	
	private List<CycleData> getCycleADs() {
		List<CycleData> shopADs = new ArrayList<CycleData>();
		List<ShopADData> adDatas = CacheBean.getInstance().getShopADDatas();
		Collections.sort(adDatas, new Comparator<ShopADData>() {

			@Override
			public int compare(ShopADData lhs, ShopADData rhs) {
				//首页广告为sortNo 从大到小排列
				return rhs.getSortNo().compareTo(lhs.getSortNo());
			}
		});
		shopADs.addAll(adDatas);
		return shopADs;
	}
	
	private void loadDatas(final MutiCycleViewHome mcv) {
		List<ShopADData> ads = CacheBean.getInstance().getShopADDatas();
			if (null != ads && ads.size() > 0) {
				initCycleViewData(mcv, getCycleADs());
				return;
			}
		
			// 一步任务获取图片
			KJHttp kjh = new KJHttp();
			HttpParams params = new HttpParams();
			kjh.post(AppConstants.GET_SLIDE_IMAGE, params, new HttpCallBack(context, false) {

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
							shopADDatas.add(ad);
						}
//						shopADDatas.clear();
						if (shopADDatas.size() > 0) {
							CacheBean.getInstance().setShopADDatas(shopADDatas);
							initCycleViewData(mcv, getCycleADs());
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		Log.d("itemposition", position + "");
		if (position == 0) {
			if (slide == null) {
				slide = new RelativeLayout(context);
				//TODO 商品列表处的广告banner移动到首页
//				int width = ApplicationUtil.getApkInfo(context).width;
//				RelativeLayout.LayoutParams params = new   RelativeLayout.LayoutParams(
//						RelativeLayout.LayoutParams.MATCH_PARENT,
//						(int) (width /AppConstants.BANNER_SCALE));
//				
//				
//				MutiCycleViewHome mcv = new MutiCycleViewHome(context);
//				mcv.setLayoutParams(params);
//				((RelativeLayout) slide).addView(mcv);
//				loadDatas(mcv);
			}
			return slide;
		} else {
			final ViewHolder viewHolder = getViewHolder(position, convertView,
					parent);
			viewHolder.setHandler(new MyClickHandler() {
				@Override
				public void viewClick(int id) {
					click(id, list, position, viewHolder);
				}
			});
			canvas(viewHolder, getItem(position));
			return viewHolder.getConvertView();
		}
	}

	public abstract void canvas(ViewHolder holder, T item);

	public abstract void click(int id, List<T> list, int position,
			ViewHolder viewHolder);

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent) {
		return ViewHolder.get(context, convertView, parent, itemLayoutId,
				position);
	}

	public void setList(List<T> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public void setList(T[] list) {
		ArrayList<T> arrayList = new ArrayList<T>(list.length);
		for (T t : list) {
			arrayList.add(t);
		}
		setList(arrayList);
	}

	public List<T> getList() {
		return list;
	}

}
