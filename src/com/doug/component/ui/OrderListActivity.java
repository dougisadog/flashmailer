package com.doug.component.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.doug.AppConstants;
import com.doug.AppVariables;
import com.doug.component.adapter.CommonAdapter;
import com.doug.component.adapter.ViewHolder;
import com.doug.component.bean.Invest;
import com.doug.component.bean.InvestList;
import com.doug.component.support.UIHelper;
import com.doug.component.widget.TitleTab;
import com.doug.component.widget.TitleTab.ItemCallBack;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderListActivity extends KJActivity {

	@BindView(id = R.id.listview)
	private KJListView listview;
	@BindView(id = R.id.mytab)
	private TitleTab titleTab;

	private KJHttp http;
	private HttpParams params;

	private CommonAdapter<Invest> adapter;
	private List<Invest> data;

	private int page = 1;
	private String url;
	private int state;
	private boolean noMoreData;
	private boolean clickable = true;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_order_list);
		UIHelper.setTitleView(this, "", "我的订单");
		UIHelper.setBtnRight(this, R.drawable.icon_search_on, this);
	}

	@Override
	public void initData() {
		super.initData();
		url = AppConstants.INVEST_ORDER;
		state = getIntent().getIntExtra("type", 0);
		data = new ArrayList<Invest>();
		http = new KJHttp();
		params = new HttpParams();
	}

	private void getData(int page) {
		params.put("page", page);
		params.put("sid", AppVariables.sid);
		http.post(url, params, httpCallback);
	}
	
	private void search() {
		Toast.makeText(this, "search", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.flright :
				search();
				break;

			default :
				break;
		}
		super.onClick(v);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		final List<String> names = new ArrayList<String>();
		names.add("全部");
		names.add("待抢单");
		names.add("闪送中");
		names.add("待评价");
		names.add("取消单");
		titleTab.setDatas(names, new ItemCallBack() {

			@Override
			public void onItemClicked(int position) {
				for (int i = 0; i < titleTab.getChildCount(); i++) {
					
					TextView tv = titleTab.getTextView(i);
					if (null != tv)
						tv.setTextColor(getResources().getColor(position == i ? R.color.orange : R.color.grey));
				}
//				if (titleTab.getCurrentPosition() != position) {
//					if (position == 0) {
//						url = AppConstants.INVEST_ORDER;
//						state = 1;
//					}
//					else if (position == 1) {
//						url = AppConstants.INVEST_CHARGE;
//						state = 2;
//					}
//					else if (position == 2) {
//						url = AppConstants.INVEST_RETURN;
//						state = 3;
//					}
//					getData(1);
//				}
			}

		});
		
		titleTab.clickItem(state);
		adapter = new CommonAdapter<Invest>(OrderListActivity.this,
				R.layout.item_order) {
			@Override
			public void canvas(ViewHolder holder, Invest item) {
				holder.addClick(R.id.invest_protocol);
				TextView t = holder.getView(R.id.statusText);
				holder.setText(R.id.name, item.getName(), false);
				holder.setText(R.id.price, item.getPrice() + "元", false);
				holder.setText(R.id.rate, item.getRate(), false);
				LinearLayout l = holder.getView(R.id.add_v);
				TextView gainCent = holder.getView(R.id.gainCent);
				gainCent.setVisibility(View.VISIBLE);
				if (!StringUtils.isEmpty(item.getActivityRate())) {
					gainCent.setVisibility(View.GONE);
					l.setVisibility(View.VISIBLE);
					holder.setText(R.id.add,item.getActivityRate(), false);
				} else {
					l.setVisibility(View.GONE);
				}
				switch (state) {
				case 1:
					holder.setText(R.id.txthint, "回款本息", false);
					holder.setText(R.id.lastReturn,
							item.getPrincipalAndInterest() + "元", false);
					holder.setText(R.id.repayTime,
							"下期回款日：" + item.getRepayTime(), false);
					t.setVisibility(View.VISIBLE);
					t.setText("期数：" + item.getStatusText());
					break;
				case 2:
					holder.setText(R.id.txthint, "状态", false);
					holder.setText(R.id.lastReturn, item.getStatusText(), false);
					holder.setText(R.id.repayTime,
							"投资日期：" + item.getCreateDate(), false);
					t.setVisibility(View.VISIBLE);
					String beginDate = item.getInterestBeginDate();
					if (StringUtils.isEmpty(beginDate))
						t.setVisibility(View.GONE);
					else
					t.setText("起息：" + item.getInterestBeginDate());
					break;
				case 3:
					holder.setText(R.id.txthint, "回款总额", false);
					holder.setText(R.id.lastReturn, item.getLastReturn() + "元", false);
					holder.setText(R.id.repayTime,
							"起息：" + item.getInterestBeginDate(), false);
					t.setVisibility(View.VISIBLE);
					t.setText("结清：" + item.getEndDate());
					break;
				case 4:
					holder.setText(R.id.txthint, "状态", false);
					holder.setText(R.id.lastReturn, item.getStatusText(), false);
					holder.setText(R.id.repayTime,
							"投资日期：" + item.getCreateDate(), false);
					t.setVisibility(View.GONE);
					break;

				}
			}

			@Override
			public void click(int id, List<Invest> list, int position,
					ViewHolder viewHolder) {
				switch (id) {
				case R.id.invest_protocol:
				}
			}
		};
		adapter.setList(data);
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(refreshListener);
		listview.setEmptyView(findViewById(R.id.empty));
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

	private HttpCallBack httpCallback = new HttpCallBack(OrderListActivity.this) {
		public void success(org.json.JSONObject ret) {
			try {
				JSONObject articles = ret.getJSONObject("orders");
				page = articles.getInt("currentPage");
				int maxPage = articles.getJSONObject("pager").getInt("maxPage");
				if (page >= maxPage) {
					listview.hideFooter();
					noMoreData = true;
				} else {
					listview.showFooter();
					noMoreData = false;
				}
				if (page < 2) {
					data = new InvestList(articles.getJSONArray("items"))
							.getInvests();
				} else {
					data = new InvestList(data, articles.getJSONArray("items"))
							.getInvests();
				}
				adapter.setList(data);
			} catch (Exception e) {
				if (page > 0) {
					page = page - 1;
				}
				e.printStackTrace();
				Toast.makeText(OrderListActivity.this, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onFinish() {
			super.onFinish();
			listview.stopRefreshData();
			clickable = true;
		}
	};
}
