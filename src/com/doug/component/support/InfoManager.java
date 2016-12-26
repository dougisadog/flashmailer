package com.doug.component.support;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.doug.AppConfig;
import com.doug.AppConstants;
import com.doug.AppVariables;
import com.doug.component.bean.database.UserConfig;
import com.doug.component.bean.jsonbean.Account;
import com.doug.component.bean.jsonbean.User;
import com.doug.component.cache.CacheBean;
import com.doug.component.utils.ApplicationUtil;
import com.doug.component.utils.FormatUtils;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

import android.content.Context;

/**
 * 处理登录  个人信息拉取
 * @author Doug
 *
 */
public class InfoManager {

	private static InfoManager instance = null;
	
	public static InfoManager getInstance() {
		if (null == instance) {
			instance = new InfoManager();
		}
		return instance;
	}
	
	/**
	 * http请求回调
	 * @author Doug
	 *
	 */
	public static interface TaskCallBack {
		
		//返回信息类型
		public static int JSON = 0;
		public static int TXT = 1;
		
		public void taskSuccess();
		
		public void taskFail(String err, int type);
		
		public void afterTask();
		
	}
	
	/**
	 * 注册请求
	 * @param context
	 * @param account 账号
	 * @param pwd	密码
	 * @param taskCallBack 回调
	 */
	public void loginNormal(final Context context, final String account , final String pwd, 
			final TaskCallBack taskCallBack) {
		// 注册成功后登录
			KJHttp kjh = new KJHttp();
			HttpParams params = new HttpParams();
			params.put("sid", "");
			params.put("account", account);
			params.put("passwd", pwd);
			params.put("captcha", "");
			String verionName = ApplicationUtil.getApkInfo(context).versionName;
			params.put("loginVersionName", "Android" + verionName);
			kjh.post(AppConstants.SIGNIN, params, new HttpCallBack(context) {
				@Override
				public void success(JSONObject ret) {
					super.success(ret);
					try {
						JSONObject o = ret.getJSONObject("body");
						
						//基础参数更新
						String sid = o.getString("sid");
						int uid = o.getInt("uid");
						AppVariables.uid = uid;
						AppVariables.sid = sid;
						AppVariables.tel = account;
						AppVariables.isSignin = true;
						AppConfig.getAppConfig(context).set(AppConfig.SID, sid);
						
						//账号数据本地数据库存储
						KJDB kjdb = KJDB.create(context);
						List<UserConfig> userConfigs = kjdb.findAllByWhere(UserConfig.class, "uid=" + AppVariables.uid);
						UserConfig userConfig = null;
						if (userConfigs.size() > 0) {
							userConfig = userConfigs.get(0);
							userConfig.setTel(account);
							userConfig.setLastGestureCheckTime(new Date().getTime());
							kjdb.update(userConfig);
							AppVariables.needGesture = userConfig.isNeedGesture();
						}
						if (userConfig == null) {
							userConfig = new UserConfig();
							userConfig.setUid(uid);
							userConfig.setTel(account);
							userConfig.setNeedGesture(false);
							userConfig.setLastGestureCheckTime(new Date().getTime());
							kjdb.save(userConfig);
						}
						
						//清空webview的cookie
						CacheBean.syncCookie(context);
						AppVariables.forceUpdate = true;
						//Umeng账号统计
						taskCallBack.taskSuccess();
//						getInfo(context, taskCallBack);
					} catch (JSONException e) {
						this.onFinish();
						taskCallBack.taskFail(e.toString(), TaskCallBack.TXT);
						e.printStackTrace();
					}
				}

				@Override
				public void onFinish() {
					taskCallBack.afterTask();
					super.onFinish();
				}

				@Override
				public void failure(JSONObject ret) {
					taskCallBack.taskFail(ret.toString(), TaskCallBack.JSON);
					super.failure(ret);
				}
				
			});
		}
	
	/**
	 * 及时拉取服务器基本信息
	 */
	
	public void getInfo(Context context, final TaskCallBack taskCallBack) {
		getInfo(context, taskCallBack, true);
	}
	
	
	/**
	 * 个人信息请求
	 * @param context
	 * @param taskCallBack 回调
	 * @param needLoading  显示loading
	 */
	public void getInfo(Context context, final TaskCallBack taskCallBack, boolean needLoading) // 实名认证、绑定银行卡
	{
		if (!AppVariables.isSignin) return;
		HttpParams params = new HttpParams();
		params.put("sid", AppVariables.sid);
		KJHttp kjh = new KJHttp();
		kjh.post(AppConstants.BASICINFO, params, new HttpCallBack(context, needLoading) {
			
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					JSONObject user = ret.getJSONObject("user");
					JSONObject account = ret.getJSONObject("account");
					
					//储存在缓存中
					User u = FormatUtils.jsonParse(user.toString(), User.class);
					CacheBean.getInstance().setUser(u);
					Account a = FormatUtils.jsonParse(account.toString(), Account.class);
					CacheBean.getInstance().setAccount(a);
					taskCallBack.taskSuccess();
				} catch (JSONException e) {
					taskCallBack.taskFail(e.toString(), TaskCallBack.TXT);
					e.printStackTrace();
				}
			}

			@Override
			public void failure(JSONObject ret) {
				taskCallBack.taskFail(ret.toString(), TaskCallBack.JSON);
				super.failure(ret);
			}

			@Override
			public void onFinish() {
				taskCallBack.afterTask();
				super.onFinish();
			}
			
			
		});
	}
	
	/**
	 * 清空用户数据 和缓存
	 * @param context
	 */
	public void clearinfo(Context context) {
		AppVariables.clear();
	}
}
