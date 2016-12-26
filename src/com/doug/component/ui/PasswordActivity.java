package com.doug.component.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doug.AppConstants;
import com.doug.AppVariables;
import com.doug.component.support.UIHelper;
import com.doug.component.utils.ApplicationUtil;
import com.doug.component.widget.LoudingDialogIOS;
import com.doug.emojihelper.R;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.utils.StringUtils;

public class PasswordActivity extends KJActivity {

	@BindView(id = R.id.pwd_old, click = true)
	private EditText mPwd_old;
	@BindView(id = R.id.pwd_new, click = true)
	private EditText mPwd_new;
	@BindView(id = R.id.pwd_confirm, click = true)
	private EditText mPwd_confirm;
	@BindView(id = R.id.confirm, click = true)
	private TextView mConfirm;

	private String pwd_old;
	private String pwd_new;
	private String pwd_confirm;

	private KJHttp kjh;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_password_v2);
		UIHelper.setTitleView(this, "账户中心", "修改密码");
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.confirm:
			comfirm();
			break;
		}
	}

	private void comfirm() {
		pwd_old = mPwd_old.getText().toString();
		pwd_new = mPwd_new.getText().toString();
		pwd_confirm = mPwd_confirm.getText().toString();
		if (StringUtils.isEmpty(pwd_old) || StringUtils.isEmpty(pwd_new)) {
			LoudingDialogIOS ld = new LoudingDialogIOS(PasswordActivity.this);
			ld.showConfirmHint("请输入完整信息。");
			return;
		}
		if (pwd_new.length() < 8 || pwd_new.length() > 20) {
			LoudingDialogIOS ld = new LoudingDialogIOS(PasswordActivity.this);
			ld.showConfirmHint("密码长度必须大于等于8位小于等于20位");
			return;
		}
		if (!pwd_new.equals(pwd_confirm)) {
			LoudingDialogIOS ld = new LoudingDialogIOS(PasswordActivity.this);
			ld.showConfirmHint("两次输入密码不一致");
			return;
		}
		if (pwd_new.trim().equals(pwd_old.trim())) {
			LoudingDialogIOS ld = new LoudingDialogIOS(PasswordActivity.this);
			ld.showConfirmHint("新旧密码不能相同");
			return;
		}
		kjh = new KJHttp();
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		params.put("originPassword", pwd_old);
		params.put("newPassword", pwd_new);
		params.put("confirmNewPassword", pwd_confirm);
		kjh.post(AppConstants.CHANGEPWD, params, new HttpCallBack(
				PasswordActivity.this) {
			
			
			@Override
			public void failure(JSONObject ret) {
					super.failure(ret);
				if (!ret.isNull("msg")) {
					try {
						String msg = ret.getString("msg");
							if (msg.equals("not login")) {
								ApplicationUtil.restartApplication(PasswordActivity.this);
							} else {
								if ("".equals(msg))
									msg = "密码修改失败";
								LoudingDialogIOS ldc = new LoudingDialogIOS(PasswordActivity.this);
								ldc.showConfirmHint(msg);
							}
					} catch (JSONException e) {
						Toast.makeText(PasswordActivity.this, R.string.app_data_error,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(PasswordActivity.this, R.string.app_exception, Toast.LENGTH_SHORT)
							.show();
				}
			}


			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				LoudingDialogIOS ld = new LoudingDialogIOS(PasswordActivity.this);
				ld.showConfirmHintAndFinish("设置成功。");
				//修改成功后强制登出
				clearinfo();
				
			}
		});
	}
	
	private void clearinfo() {
		AppVariables.clear();
	}
	
}
