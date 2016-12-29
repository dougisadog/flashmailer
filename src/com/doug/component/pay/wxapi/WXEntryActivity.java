//package com.doug.component.pay.wxapi;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.doug.AppConstants;
//import com.doug.component.cache.CacheBean;
//import com.louding.frame.KJActivity;
//import com.tencent.mm.sdk.constants.ConstantsAPI;
//import com.tencent.mm.sdk.modelbase.BaseReq;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.tencent.mm.sdk.modelmsg.SendAuth;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//
//import android.accounts.AccountManager;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//public class WXEntryActivity extends KJActivity implements
//		IWXAPIEventHandler {
//
//	private static String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
//	private static String Get_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
//	private String get_access_token;
//	private IWXAPI api;
//	private String code;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		api = WXAPIFactory.createWXAPI(this, AppConstants.WX_APP_ID);
//		if (!api.isWXAppInstalled()) {
//			Toast.makeText(WXEntryActivity.this, "请先安装微信应用", Toast.LENGTH_SHORT)
//					.show();
//			return;
//		}
//		api.registerApp(AppConstants.WX_APP_ID);
//		api.handleIntent(getIntent(), this);
//	}
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		setIntent(intent);
//		api.handleIntent(intent, this);
//	}
//
//	@Override
//	public void onReq(BaseReq arg0) {
//
//	}
//
//	@Override
//	public void onResp(BaseResp resp) {
//		Log.e("微信登录", " " + resp.errCode);
//		// Toast.makeText(WXEntryActivity.this, "微信登录" +"[ "+ resp.errCode +
//		// "] "+ resp.getType(), Toast.LENGTH_SHORT).show();
//		switch (resp.errCode) {
//		case BaseResp.ErrCode.ERR_OK:
//			if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
//				if (null != callback)
//					callback.shareSuccess();
//				// shareSuccess();
//				CacheBean.getInstance().setWxResp(resp);
//				finish();
//				break;
//			}
//			code = ((SendAuth.Resp) resp).code;
//			// Toast.makeText(WXEntryActivity.this, "微信登录" +"[ "+ code + "] ",
//			// Toast.LENGTH_SHORT).show();
//			CacheBean.getInstance().setWxResp(resp);
//			onReq();
//			// get_access_token = getTokenRequest(code);
//			// Thread thread=new Thread(downloadRun);
//			// thread.start();
//			// try {
//			// thread.join();
//			// } catch (InterruptedException e) {
//			// e.printStackTrace();
//			// }
//
//			break;
//		case BaseResp.ErrCode.ERR_USER_CANCEL:
//			finish();
//			// Toast.makeText(WXEntryActivity.this, "用户取消[" +
//			// BaseResp.ErrCode.ERR_USER_CANCEL + "] ",
//			// Toast.LENGTH_SHORT).show();
//			break;
//		case BaseResp.ErrCode.ERR_AUTH_DENIED:
//			finish();
//			// Toast.makeText(WXEntryActivity.this, "用户拒绝授权[" +
//			// BaseResp.ErrCode.ERR_AUTH_DENIED + "] ",
//			// Toast.LENGTH_SHORT).show();
//			break;
//		case BaseResp.ErrCode.ERR_COMM:
//			finish();
//			// Toast.makeText(WXEntryActivity.this, "分享失败[" +
//			// BaseResp.ErrCode.ERR_COMM + "] ", Toast.LENGTH_SHORT).show();
//			break;
//		}
//	}
//
//	private void onReq() {
//		Log.e("微信登录", "onReq ");
//		BaseResp resp = CacheBean.getInstance().getWxResp();
//		// Toast.makeText(WXEntryActivity.this, "resp:"+ resp,
//		// Toast.LENGTH_SHORT).show();
//		if (resp == null) {
//			return;
//		}
//		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
//			String weixinCode = ((SendAuth.Resp) resp).code;
//
//			get_access_token = getTokenRequest(weixinCode);
//			// Toast.makeText(WXEntryActivity.this,"token:"+ get_access_token,
//			// Toast.LENGTH_SHORT).show();
//			// WXGetAccessToken();
//			Thread thread = new Thread(downloadRun);
//			thread.start();
//			try {
//				thread.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	private void login() {
//		AccountManager.getInstance().loginByOAuth(new ICallback() {
//			
//			@Override
//			public void onComplete() {
//				hideLoading();
//			}
//			
//			@Override
//			public void onSuccess() {
//				AtyHomeNew.startAty(WXEntryActivity.this);
//			}
//			
//			@Override
//			public void onError() {
//				hideLoading();
//			}
//
//		});
//	}
//
//	private static String getTokenRequest(String code) {
//		String tokenRequest = GET_ACCESS_TOKEN
//				.replace("APPID", AppConstants.WX_APP_ID.trim())
//				.replace("SECRET", AppConstants.WX_API_KEY.trim())
//				.replace("CODE", code);
//		return tokenRequest;
//	}
//
//	private static String getUserInfo(String token, String openid) {
//		String url = Get_USER_INFO.replace("ACCESS_TOKEN", token).replace(
//				"OPENID", openid);
//		return url;
//	}
//
//	public Runnable downloadRun = new Runnable() {
//
//		@Override
//		public void run() {
//			WXGetAccessToken();
//
//		}
//	};
//
//	private void WXGetAccessToken() {
//		// Log.e("微信登录", "WXGetAccessToken ");
//		// Toast.makeText(WXEntryActivity.this, "WXGetAccessToken ",
//		// Toast.LENGTH_SHORT).show();
//		HttpClient get_access_token_httpClient = new DefaultHttpClient();
//		HttpClient get_user_info_httpClient = new DefaultHttpClient();
//		String access_token = "";
//		String openid = "";
//		try {
//			// HttpGet getMethod = new HttpGet(get_access_token);
//			// HttpResponse response =
//			// get_access_token_httpClient.execute(getMethod);
//			HttpPost postMethod = new HttpPost(get_access_token);
//			HttpResponse response = get_access_token_httpClient
//					.execute(postMethod); // 执行POST方法
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//				InputStream is = response.getEntity().getContent();
//				BufferedReader br = new BufferedReader(
//						new InputStreamReader(is));
//				String str = "";
//				StringBuffer sb = new StringBuffer();
//				while ((str = br.readLine()) != null) {
//					sb.append(str);
//				}
//				is.close();
//				String josn = sb.toString();
//				// String josn = EntityUtils.toString(response.getEntity());
//				JSONObject json1 = new JSONObject(josn);
//
//				access_token = json1.getString("access_token");// (String)
//																// json1.get("access_token");
//				// GlobalApp.getInstance().showErrMsg("get_access:"
//				// +access_token);
//				openid = json1.getString("openid");
//
//			} else {
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// GlobalApp.getInstance().showErrMsg("JSONException"
//			// +e.getMessage());
//			e.printStackTrace();
//		}
//
//		String get_user_info_url = getUserInfo(access_token, openid);
//		// GlobalApp.getInstance().showErrMsg(get_user_info_url);
//		WXGetUserInfo(get_user_info_url);
//	}
//
//	private void WXGetUserInfo(String get_user_info_url) {
//		// Toast.makeText(this.getApplicationContext(),"info_url:"+
//		// get_user_info_url, Toast.LENGTH_SHORT).show();
//		HttpClient get_access_token_httpClient = new DefaultHttpClient();
//		String openid = "";
//		String nickname = "";
//		String headimgurl = "";
//		try {
//			HttpGet getMethod = new HttpGet(get_user_info_url);
//			HttpResponse response = get_access_token_httpClient
//					.execute(getMethod); // 执行GET方法
//			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//
//				// InputStream is = response.getEntity().getContent();
//				// BufferedReader br = new BufferedReader(
//				// new InputStreamReader(is));
//				// String str = "";
//				// StringBuffer sb = new StringBuffer();
//				// while ((str = br.readLine()) != null) {
//				// sb.append(str);
//				// }
//				// is.close();
//				// String josn = sb.toString();
//				String josn = EntityUtils.toString(response.getEntity());
//				Log.e("josn", josn);
//				// Toast.makeText(this.getApplicationContext(),"josn:"+ josn,
//				// Toast.LENGTH_SHORT).show();
//				// GlobalApp.getInstance().showErrMsg(josn);
//				JSONObject json1 = new JSONObject(josn);
//				openid = (String) json1.get("openid");
//				nickname = (String) json1.get("nickname");
//				headimgurl = (String) json1.get("headimgurl");
//				GlobalReqDatas.getInstance().setRequestName(nickname);
//				GlobalReqDatas.getInstance().setRequestImgHeadUrl(headimgurl);
//				GlobalReqDatas.getInstance().setRequestSex(
//						(Integer) json1.get("sex"));
//				GlobalReqDatas.getInstance().setRequestUID(openid);
//				GlobalReqDatas.getInstance().setRequestUIDType(3);
//				login();
//
//			} else {
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSONException e) {
//			e.printStackTrace();
//			// GlobalApp.getInstance().showErrMsg("JSONException:"+
//			// e.getMessage());
//		}
//
//	}
//	
//	public static IWXShareCallback callback;
//	
//	public static void setShareCallback(IWXShareCallback callback) {
//		WXEntryActivity.callback = callback;
//	}
//	
//	public static interface IWXShareCallback {
//		
//		public void shareSuccess();
//		public void shareFail();
//		
//	}
//
//	@Override
//	public void setRootView() {
//		// TODO Auto-generated method stub
//		
//	}
//	
//}
