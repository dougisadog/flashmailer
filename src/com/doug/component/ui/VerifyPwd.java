package com.doug.component.ui;

import java.util.Date;
import java.util.List;

import com.doug.AppVariables;
import com.doug.component.bean.database.UserConfig;
import com.doug.component.support.InfoManager;
import com.doug.component.support.InfoManager.TaskCallBack;
import com.doug.component.widget.LoudingDialogIOS;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VerifyPwd extends KJActivity {

	@BindView(id = R.id.tel)
	private EditText mTel;
	@BindView(id = R.id.pwd)
	private EditText mPwd;
	@BindView(id = R.id.verification)
	private EditText mVrify;
	@BindView(id = R.id.verifyimage, click = true)
	private ImageView mVrifyImage;
	@BindView(id = R.id.signin, click = true)
	private TextView mSignin;
	@BindView(id = R.id.verify1)
	private LinearLayout mVrify1;
	@BindView(id = R.id.hint)
	private TextView mHint;

	private String tel;
	private String pwd;
	private String code;
	private String sid;
	private int uid;

	private KJHttp kjh;
	private KJDB kjdb;
	
	private boolean reset = false;
	public static final int SUCCESS = 1;
	public static final int FAIL = -1;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_verify_pwd);
		reset = getIntent().getBooleanExtra("reset", false);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		mTel.setText(AppVariables.tel);
		TextView btnLeft = null;
		TextView titleTv = null;
		ImageView titleImage = null;
		btnLeft = (TextView) findViewById(R.id.title_left);
		titleTv = (TextView) findViewById(R.id.title_center);
		titleImage = (ImageView) findViewById(R.id.title_image);
		btnLeft.setText(" ");
		titleImage.setVisibility(View.GONE);
		btnLeft.setVisibility(View.VISIBLE);
		titleTv.setVisibility(View.VISIBLE);
		titleTv.setText("验证登录");
		btnLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				loginout();
				setResult(FAIL);
				finish();
			}
		});
	}
	
	/**
	 * 暂时弃用登出逻辑 0817
	 */
	private void loginout() {
		final LoudingDialogIOS ld = new LoudingDialogIOS(VerifyPwd.this);
		ld.showOperateMessage("是否退出登录？");
		ld.setPositiveButton("确定", R.drawable.dialog_positive_btn,
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						InfoManager.getInstance().clearinfo(VerifyPwd.this);
						ld.dismiss();
						finish();
					}
				});

	}

	@Override
	public void initData() {
		super.initData();
		sid = "";
		kjdb = KJDB.create(this);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.signin:
			tel = mTel.getText().toString();
			pwd = mPwd.getText().toString();
			code = mVrify.getText().toString();
			if (StringUtils.isEmpty(tel) || StringUtils.isEmpty(pwd)) {
				mHint.setVisibility(View.VISIBLE);
				mHint.setText(R.string.sign_hint);
			} else {
				// 注册成功后登录
				setResult(FAIL);
				InfoManager.getInstance().loginNormal(VerifyPwd.this, tel, pwd, new TaskCallBack() {
					
					@Override
					public void taskSuccess() {
						if (reset) {
							List<UserConfig> userConfigs = kjdb.findAllByWhere(UserConfig.class, "uid=" + AppVariables.uid);
							UserConfig userConfig = null;
							if (userConfigs.size() > 0) {
								userConfig = userConfigs.get(0);
								userConfig.setLastGestureCheckTime(new Date().getTime());
								userConfig.setNeedGesture(false);
								userConfig.setHandPwd("");
								kjdb.update(userConfig);
								AppVariables.needGesture = false;
							}
							if (userConfig == null) {
								userConfig = new UserConfig();
								userConfig.setUid(uid);
								userConfig.setNeedGesture(false);
								userConfig.setHandPwd("");
								userConfig.setLastGestureCheckTime(new Date().getTime());
								kjdb.save(userConfig);
							}
						}
						setResult(SUCCESS);
						VerifyPwd.this.finish();
					}
					
					@Override
					public void afterTask() {
					}

					@Override
					public void taskFail(String err, int type) {
//						if (type == TaskCallBack.TXT) {
//							Toast.makeText(VerifyPwd.this, "数据解析错误", Toast.LENGTH_LONG).show();
//						}
//						else if (type == TaskCallBack.JSON) {
//							Toast.makeText(VerifyPwd.this, "数据拉取失败", Toast.LENGTH_LONG).show();
//						}
					}
				});
//				post();
				
			}
			break;
		}
	}

	@Override
	public void onBackPressed() {
		setResult(FAIL);
		finish();
	}


}
