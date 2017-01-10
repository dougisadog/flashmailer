package com.doug.component.ui;

import java.util.HashMap;

import com.doug.component.bean.jsonbean.User;
import com.doug.component.cache.CacheBean;
import com.doug.component.error.DebugPrinter;
import com.doug.component.support.UIHelper;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.analytics.social.UMPlatformData.UMedia;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

//关于一理财页面
public class AtyInvite extends KJActivity {
	
	@BindView(id = R.id.wx, click = true)
	private TextView wx;
	@BindView(id = R.id.wxCircle)
	private TextView wxCircle;
	@BindView(id = R.id.weibo)
	private TextView weibo;
	

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_bill);
		UIHelper.setTitleView(this, "", "分享");
		findViewById(R.id.flright).setOnClickListener(this);
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.wx :
				shareWx();
				break;
			case R.id.wxCircle :
				shareWxCircle();
				break;
			case R.id.weibo :
				shareWeibo();
				break;
			default :
				break;
		}
	}
	
	
	private PlatformActionListener platformActionListener = new PlatformActionListener() {
		
		@Override
		public void onError(Platform platform, int action, Throwable arg2) {
		}
		
		@Override
		public void onComplete(Platform platform, int action,
				HashMap<String, Object> arg2) {
			UMedia uMedia = null;
			if (platform.getName().equals("SinaWeibo")) {
				uMedia = UMedia.SINA_WEIBO;
			}
			else if (platform.getName().equals("Wechat")) {
				uMedia = UMedia.WEIXIN_FRIENDS;
			}
			else if (platform.getName().equals("WechatMoments")) {
				uMedia = UMedia.WEIXIN_CIRCLE;
			}
			User user = CacheBean.getInstance().getUser();
			if (null != uMedia && null != user) {
				UMPlatformData platformData = new UMPlatformData(uMedia, user.getPhone());
				platformData.setName(user.getName());
				MobclickAgent.onSocialEvent(AtyInvite.this, platformData);
				DebugPrinter.d("platform:" + platform.getName() + " action:" + action + "\n");
			}
		}
		
		@Override
		public void onCancel(Platform platform, int action) {
		}
	};
	
	private void shareWeibo() {
		ShareParams sp = new ShareParams();
		sp.setText("Kpor分享");
		sp.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		weibo.setPlatformActionListener(platformActionListener); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);
	}
	
	private void shareWx() {
		ShareParams sp = new ShareParams();
		sp.setText("Kpor分享");
		sp.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

		Platform wx = ShareSDK.getPlatform(WechatMoments.NAME);
		wx.setPlatformActionListener(platformActionListener); // 设置分享事件回调
		// 执行图文分享
		wx.share(sp);
	}

	private void shareWxCircle() {
		ShareParams sp = new ShareParams();
		sp.setText("Kpor分享");
		sp.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		weibo.setPlatformActionListener(platformActionListener); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);
	}

}
