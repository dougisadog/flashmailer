package com.doug.component.ui;

import static java.lang.System.out;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import cn.smssdk.EventHandler;
//import cn.smssdk.OnSendMessageHandler;
//import cn.smssdk.SMSSDK;

import com.doug.AppConstants;
import com.doug.component.support.InfoManager;
import com.doug.component.support.UIHelper;
import com.doug.component.support.InfoManager.TaskCallBack;
//import com.doug.component.support.MopMessageManager;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;

public class SignupActivity extends KJActivity {

	@BindView(id = R.id.tel)
	private EditText mTel;
	@BindView(id = R.id.tel_verify)
	private EditText mTelVerify;
	@BindView(id = R.id.pwd)
	private EditText mPwd;
	@BindView(id = R.id.pwdconfirm)
	private EditText mPwdConfirm;
	@BindView(id = R.id.verification)
	private EditText mVrify;
	@BindView(id = R.id.verifyimage, click = true)
	private ImageView mVrifyImage;
	@BindView(id = R.id.code, click = true)
	private TextView mCode;
	@BindView(id = R.id.signup, click = true)
	private TextView mSignup;
	@BindView(id = R.id.verify1)
	private LinearLayout mVrify1;
	@BindView(id = R.id.verify2)
	private LinearLayout mVrify2;
	@BindView(id = R.id.hint)
	private TextView mHint;
	@BindView(id = R.id.protocol, click = true)
	private TextView mProtocol;

	private String tel;
	private String code;
	private String pwd;
	private String pwdc;
	private String sid;
	private String captcha;
	private boolean hascode;

	private KJHttp kjh;
	private KJDB kjdb;
	private String currentCode;
	
//	private EventHandler eh = new EventHandler(){
//
//		@Override
//		public void afterEvent(int event, int result, Object data) {
//			System.out.println(data.toString());
//
//		   if (result == SMSSDK.RESULT_COMPLETE) {
//			//回调完成
//			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//
//            //提交验证码成功
//			}else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
//				
//		    //获取验证码成功
//			}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
//            //返回支持发送验证码的国家列表
//            } 
//          }else{                                                                 
//             ((Throwable)data).printStackTrace(); 
//      }
//  } 
//}; 

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_signup_v2);
		UIHelper.setTitleView(this, "返回", "注册");
	}

	@Override
	public void initData() {
		super.initData();
//		String[] country = MopMessageManager.getInstance().getCurrentCountry();
//		if (country != null) {
//			currentCode = country[1];
//		}
		
		hascode = false;
		sid = "";
		captcha = "";
		kjh = new KJHttp();
		kjdb = KJDB.create(this);
	}
	
	

	public void onResume() {
//		SMSSDK.registerEventHandler(eh);
		super.onResume();
	}

	public void onDestroy() {
//		SMSSDK.unregisterEventHandler(eh);
		super.onDestroy();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.code:
			tel = mTel.getText().toString();
			captcha = mVrify.getText().toString();
			if (StringUtils.isEmpty(tel) || (tel.length() < 11)) {
				mHint.setVisibility(View.VISIBLE);
				mHint.setText(R.string.signup_code);
			} else {
				mHint.setVisibility(View.GONE);
				getCode();
			}
			break;
		case R.id.signup:
			tel = mTel.getText().toString();
			code = mTelVerify.getText().toString();
			pwd = mPwd.getText().toString();
			pwdc = mPwdConfirm.getText().toString();
			captcha = mVrify.getText().toString();
			if (StringUtils.isEmpty(tel) || (tel.length() < 11)) {
				mHint.setVisibility(View.VISIBLE);
				mHint.setText(R.string.signup_code);
			} 
			else if (!hascode) {
				mHint.setVisibility(View.VISIBLE);
				mHint.setText(R.string.signup_hascode);
			}
			else if (StringUtils.isEmpty(code)) {
				mHint.setVisibility(View.VISIBLE);
				mHint.setText("短信验证码不能为空");
			}
			else if (StringUtils.isEmpty(pwd)) {
				mHint.setVisibility(View.VISIBLE);
				mHint.setText("密码不能为空");
			}
			else if (!StringUtils.isPasswordStrength(pwd)) {
				mHint.setVisibility(View.VISIBLE);
				mHint.setText("密码格式不正确");
			}
			else if (!pwd.equals(pwdc)) {
				mHint.setVisibility(View.VISIBLE);
				mHint.setText("两次输入密码不一致。");
			} 
			else {
				mHint.setVisibility(View.GONE);
				signup();
			}
			break;
		}
	}

//	private boolean pwdPassed(String pwd) {
//		if (pwd.length() < 8)
//			return false;
//		return true;
//	}

	private void signup()
	{
		
		mSignup.setClickable(false);
		mSignup.setBackgroundResource(R.drawable.btn_tender_gray);
		
		out.println("My: 进入注册功能");
		
		HttpParams params = new HttpParams();
		params.put("sid", sid);
		params.put("phone", tel);
		params.put("password", pwd);
		params.put("phoneCode", code);
		params.put("captcha", captcha);
		
		out.println("My: params => " + params);
		
		kjh.post(AppConstants.SIGNUP, params, new HttpCallBack(
				SignupActivity.this) {
			@Override
			public void onFinish() {
				mSignup.setClickable(true);
				mSignup.setBackgroundResource(R.drawable.btn_blue);
				super.onFinish();
			}

			@Override
			public void failure(JSONObject ret) {
				super.failure(ret);
				try {
					ret.getJSONObject("body");
					sid = ret.getString("sid");
//					if (o.getBoolean("needCaptcha")) {
//						mVrify1.setVisibility(View.VISIBLE);
//						mVrify1.setVisibility(View.VISIBLE);
//						getCapture();
//					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				mHint.setVisibility(View.VISIBLE);
				mHint.setText("注册成功。");
				Toast.makeText(SignupActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				// 注册成功后登录
				InfoManager.getInstance().loginNormal(SignupActivity.this, tel, pwd, new TaskCallBack() {
					
					@Override
					public void taskSuccess() {
					}
					
					@Override
					public void afterTask() {
//						showActivity(SignupActivity.this, AccountActivity.class);
						setResult(AppConstants.SUCCESS);
						SignupActivity.this.finish();
					}

					@Override
					public void taskFail(String err, int type) {
						if (type == TaskCallBack.TXT) {
							Toast.makeText(SignupActivity.this, "数据解析错误", Toast.LENGTH_LONG).show();
						}
						else if (type == TaskCallBack.JSON) {
							Toast.makeText(SignupActivity.this, "数据拉取失败", Toast.LENGTH_LONG).show();
						}
						
					}
				});
			}
		});
	}
	
	
//	private void checkCode() {
//		SMSSDK.submitVerificationCode(currentCode, tel, code);
//	}
	

	private void getCode()
	{
		out.println("My: 获取手机验证码");
		
//		SMSSDK.getVerificationCode(currentCode, tel, new OnSendMessageHandler() {
//			
//			@Override
//			public boolean onSendMessage(String arg0, String arg1) {
//						runOnUiThread(new Runnable() {
//							public void run() {
//								out.println("My: 获取手机验证码成功");
//								mHint.setVisibility(View.VISIBLE);
//								mHint.setText(R.string.signup_code_success);
//								hascode = true;
//								buttonHandle.post(buttonControl);
//							}
//						});
//				return false;
//			}
//		});
		
	}

	Runnable buttonControl = new Runnable() {
		int sec = 60;

		@Override
		public void run() {
			Message msg = buttonHandle.obtainMessage();
			sec -= 1;
			msg.arg1 = sec;
			buttonHandle.sendMessage(msg);
			if (sec == 0) {
				sec = 60;// 读完秒 按下重新获取之后把sec重新设定为60
			}
		}
	};

	@SuppressLint("HandlerLeak")
	Handler buttonHandle = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mCode.setText(msg.arg1 + "秒后重发");
//			mCode.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//	                getResources().getDimensionPixelSize(R.dimen.font_hint_small));
			if (msg.arg1 == 0) {
				buttonHandle.removeCallbacks(buttonControl);
				mCode.setBackgroundResource(R.drawable.btn_blue);
				mCode.setText("获取验证码");
//				mCode.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//		                getResources().getDimensionPixelSize(R.dimen.font_hint));
				mCode.setClickable(true);
			} else {
				mCode.setClickable(false);
				mCode.setBackgroundResource(R.drawable.btn_grey);
				buttonHandle.postDelayed(buttonControl, 1000);
			}

		}

		;
	};
	
}
