package com.doug.component.ui;

import org.json.JSONObject;

import com.doug.AppConfig;
import com.doug.AppVariables;
import com.doug.AutoUpdateManager;
import com.doug.AutoUpdateManager.UpdateCallback;
import com.doug.StartApplication;
import com.doug.component.bean.Update;
import com.doug.component.cache.CacheBean;
import com.doug.component.support.ApkInfo;
import com.doug.component.support.UIHelper;
import com.doug.component.support.UpdateManager.CheckVersionInterface;
import com.doug.component.utils.ApplicationUtil;
import com.doug.component.widget.LoudingDialogIOS;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AtyAbout extends KJActivity implements CheckVersionInterface{
	
	@BindView(id = R.id.txtVersion, click = true)
	private TextView txtVersion;
	private RelativeLayout report;
	@BindView(id = R.id.checkVersion, click = true)
	private RelativeLayout checkVersion;
	@BindView(id = R.id.iv_red_point, click = true)
	private ImageView iv_red_point;
	
	@BindView(id = R.id.comment, click = true)
	private RelativeLayout comment;
	
	
	
	private Update u;
	private JSONObject versionInfo;

	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_about_list);
		UIHelper.setTitleView(this, "", "关于");
	}
	
	@Override
	public void initWidget() {
		super.initWidget();
		ApkInfo apkInfo = ApplicationUtil.getApkInfo(this);
		txtVersion.setText("当前版本为" + apkInfo.versionName);
		
		String noUpdateVersion = AppConfig.getAppConfig(AtyAbout.this).get(
				AppConfig.NOT_UPDATE_DIALOG_VERSION);
		if (null == noUpdateVersion) {
			noUpdateVersion = apkInfo.versionCode + "";
			AppConfig.getAppConfig(AtyAbout.this).set(
					AppConfig.NOT_UPDATE_DIALOG_VERSION, noUpdateVersion);
		}
		String lastVersion = CacheBean.getInstance().getRedConditions().get("lastVersion");
		if (!StringUtils.isEmpty(lastVersion) && Integer.parseInt(lastVersion) > Integer.parseInt(noUpdateVersion)) {
			iv_red_point.setVisibility(View.VISIBLE);
		}
		else {
			iv_red_point.setVisibility(View.INVISIBLE);
		}
		boolean checkUpdate = getIntent().getBooleanExtra("update", false);
		if (checkUpdate) {
			checkUpdate();
		}
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.checkVersion :
				AppConfig.getAppConfig(AtyAbout.this).set(
						AppConfig.NOT_UPDATE_DIALOG_VERSION,
						CacheBean.getInstance().getApkInfo().versionCode + "");
				checkUpdate();
				break;
			case R.id.comment:
				final LoudingDialogIOS ldcall = new LoudingDialogIOS(AtyAbout.this);
				ldcall.showOperateMessage("确定拨打电话" + getResources().getString(R.string.support_tel_text) + "？");
				ldcall.setPositiveButton("确定", R.drawable.dialog_positive_btn,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(Intent.ACTION_CALL,
										Uri.parse("tel:" + getResources().getString(R.string.support_tel)));
								startActivity(intent);
								ldcall.dismiss();
							}
						});
				break;
			default :
				break;
		}
	}
	
	private void checkUpdate() {
		AutoUpdateManager.getInstance().setUpdateCallback(new UpdateCallback() {
			
			@Override
			public void onUpdated() {
			}
			
			@Override
			public void onNoUpdate() {
			}
			
			@Override
			public void onBeforeUpdate() {
			}
		});
//		AutoUpdateManager.getInstance().setShowMsg(true);
		StartApplication.parseChannel(AtyAbout.this);
		String lastVersion = CacheBean.getInstance().getRedConditions().get("lastVersion");
		if (lastVersion == null) {
			lastVersion = ApplicationUtil.getApkInfo(this).versionCode + "";
			LoudingDialogIOS ld = new LoudingDialogIOS(this);
			ld.showConfirmHint("当前为最新版本");
		}
		AppConfig.getAppConfig(AtyAbout.this).set(
				AppConfig.NOT_UPDATE_DIALOG_VERSION, lastVersion);
		AppVariables.forceUpdate = true;
		iv_red_point.setVisibility(View.INVISIBLE);
	}

	@Override
	public Update checkVersion() throws Exception {
		try {
			u = new Update(versionInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return u;
	}

}
