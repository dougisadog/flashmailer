package com.doug.component.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;

import com.doug.AppConstants;
import com.doug.component.adapter.CommonAdapter;
import com.doug.component.adapter.ViewHolder;
import com.doug.component.bean.Announce;
import com.doug.component.bean.AnnounceList;
import com.doug.component.support.UIHelper;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//关于一理财页面
public class AtyCoupon extends KJActivity {
	@BindView(id = R.id.btnTrade, click = true)
	private TextView btnTrade;
	@BindView(id = R.id.idCoupon, click = true)
	private EditText idCoupon;
	@BindView(id = R.id.listview)
	private KJListView listview;
	@BindView(id = R.id.empty)
	private TextView mEmpty;
	
	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Announce> adapter;
	private static List<Announce> data = new ArrayList<Announce>();

	private int page = 1;
	private boolean noMoreData;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_coupon);
		UIHelper.setTitleView(this, "", "优惠券");
		UIHelper.setBtnRight(this, R.drawable.detail_desc, this);
		findViewById(R.id.flright).setOnClickListener(this);
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.btnTrade :
				String code = idCoupon.getText().toString();
				String text = "";
				if (TextUtils.isEmpty(code)) {
					text = "请输入优惠码";
				}
				else {
					text = "兑换优惠券失败";
				}
				Toast.makeText(this, text, Toast.LENGTH_LONG).show();
				break;
			case R.id.flright :
				startActivity(new Intent(AtyCoupon.this, AtyCouponDetail.class));
				break;
			default :
				break;
		}
	}

	@Override
	public void initData() {
		super.initData();
		http = new KJHttp();
		params = new HttpParams();
		getData(page);
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", "");
		http.post(AppConstants.ANNOUNCE, params, httpCallback);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new CommonAdapter<Announce>(AtyCoupon.this,
				R.layout.item_anno) {
			@Override
			public void click(int id, List<Announce> list, int position,
					ViewHolder viewHolder) {
			}

			@Override
			public void canvas(ViewHolder holder, Announce item) {
				holder.addClick(R.id.item_anno);
				holder.setText(R.id.data, item.getDateline(), false);
				holder.setText(R.id.title, item.getTitle(), false);
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(refreshListener);
		listview.setEmptyView(findViewById(R.id.empty));
	}
	
	
	public void refreshData() {
		getData(1);
	}

	private KJRefreshListener refreshListener = new KJRefreshListener() {
		@Override
		public void onRefresh() {
			getData(1);
		}

		@Override
		public void onLoadMore() {
			if (!noMoreData) {
				getData(page + 1);
			}
		}
	};

	private HttpCallBack httpCallback = new HttpCallBack(this) {
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("articles");
				page = articles.getInt("currentPage");
//				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				JSONArray arr = articles.getJSONArray("items");
				if (null == arr || arr.length() == 0) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				System.out.println("当前页面=====>" + page);
				if (page == 1) {
					data = new AnnounceList(arr)
							.getAnnounces();
				} else {
					data = new AnnounceList(data, arr).getAnnounces();
				}
				mEmpty.setVisibility(data.size() > 0 ? View.INVISIBLE : View.VISIBLE);
				System.out.println("data=========>" + data);
				adapter.setList(data);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("数据解析错误。");
				Toast.makeText(AtyCoupon.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
		}
	};

}
