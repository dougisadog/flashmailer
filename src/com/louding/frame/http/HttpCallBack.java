/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.louding.frame.http;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.doug.component.utils.ApplicationUtil;
import com.doug.component.widget.LoudingDialogIOS;
import com.doug.flashmailer.R;
import com.louding.frame.utils.KJLoger;

/**
 * Http请求回调类<br>
 * <p/>
 * <b>创建时间</b> 2014-8-7
 *
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.3
 */
public abstract class HttpCallBack {

	private Context context;
	private LoudingDialogIOS ld;
	private LoudingDialogIOS ldc;
	private boolean loading = true;

	public HttpCallBack(Context context) {
		super();
		this.context = context;
	}
	
	public HttpCallBack(Context context, boolean loading) {
		super();
		this.context = context;
		this.loading = loading;
	}

	/**
	 * Http请求开始前回调
	 */
	public void onPreStart() {
		KJLoger.debug("Http请求开始。。。");
		if (context != null && loading) {
//			ld = new LoudingDialogIOS(context);
			ld = new LoudingDialogIOS(context);
			ld.showLouding(R.string.load_ing);
		}
	}
	
	private void dissmiss() {
		if (ld != null && loading) {
			ld.dismiss();
		}
	}

	;

	/**
	 * 进度回调，仅支持Download时使用
	 *
	 * @param count
	 *            总数
	 * @param current
	 *            当前进度
	 */
	public void onLoading(long count, long current) {
	}

	/**
	 * Http请求成功时回调
	 *
	 * @param t
	 */
	public void onSuccess(String t) {
		KJLoger.debug("Http请求成功。。。");
		System.out.println(t);
		try {
			JSONObject ret = new JSONObject(t);
			int state = ret.getInt("status");
			if (state != 0) {
				failure(ret);
			} else {
				success(ret);
			}
		} catch (JSONException e) {
			System.out.println("json数据解析错误。");
			Toast.makeText(context, R.string.app_data_error, Toast.LENGTH_SHORT)
					.show();
			dissmiss();
		}
		dissmiss();
	}

	public void failure(JSONObject ret) {
		if (!ret.isNull("msg")) {
			try {
				String msg = ret.getString("msg");
				if (context != null) {
					if (msg.equals("not login")) {
						ApplicationUtil.restartApplication(context);
						// context.startActivity(new Intent(context,
						// SigninActivity.class));
					} else {
						ldc = new LoudingDialogIOS(context);
						ldc.showConfirmHint(msg);
					}
				}
			} catch (JSONException e) {
				Toast.makeText(context, R.string.app_data_error,
						Toast.LENGTH_SHORT).show();
				dissmiss();
			}
		} else {
			Toast.makeText(context, R.string.app_exception, Toast.LENGTH_SHORT)
					.show();
		}
		failNext(ret);
	}

	public void failNext(JSONObject ret) {

	}

	public void success(JSONObject ret) {
		KJLoger.debug("Http请求成功。。。");
	}

	/**
	 * Http下载成功时回调
	 */
	public void onSuccess(File f) {
		KJLoger.debug("文件下载成功。。。");
	}

	/**
	 * Http请求失败时回调
	 *
	 * @param t
	 * @param errorNo
	 *            错误码
	 * @param strMsg
	 *            错误原因
	 */
	public void onFailure(Throwable t, int errorNo, String strMsg) {
		if (context != null) {
			Toast.makeText(context, "网络错误，请重试。。。", Toast.LENGTH_SHORT).show();
		}
		KJLoger.debug("Http请求失败。。。" + strMsg);
	}

	/**
	 * Http请求结束后回调
	 */
	public void onFinish() {
		KJLoger.debug("Http请求结束。。。");
		dissmiss();
	}
}
