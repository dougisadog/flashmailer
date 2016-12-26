package com.doug;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;

import com.doug.component.bean.Update;
import com.doug.component.cache.CacheBean;
import com.doug.component.dialog.DialogConfirmFragment;
import com.doug.component.error.DebugPrinter;
import com.doug.component.support.ApkInfo;
import com.doug.component.support.UpdateManager;
import com.doug.component.support.YYBManager;
import com.doug.component.support.UpdateManager.CheckVersionInterface;
import com.doug.component.support.UpdateManager.OnCheckDoneListener;
import com.doug.component.support.UpdateManager.OnPatchUpdateListener;
import com.doug.component.support.YYBManager.UpdateCallBack;
import com.doug.component.utils.ApplicationUtil;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.tencent.tmapkupdatesdk.model.ApkUpdateDetail;
import com.tencent.tmselfupdatesdk.ITMSelfUpdateListener;
import com.tencent.tmselfupdatesdk.TMSelfUpdateManager;
import com.tencent.tmselfupdatesdk.YYBDownloadListener;
import com.tencent.tmselfupdatesdk.model.TMSelfUpdateUpdateInfo;
import com.xiaomi.market.sdk.UpdateResponse;
import com.xiaomi.market.sdk.UpdateStatus;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;
import com.xiaomi.market.sdk.XiaomiUpdateListener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

public class AutoUpdateManager {

	private static AutoUpdateManager instance = null;
	
	private KJHttp http;
	private JSONObject versionInfo;
	private UpdateManager updateManager;
	private Update u;
	
	public static AutoUpdateManager getInstance() {
		if (null == instance) {
			instance = new AutoUpdateManager();
			instance.setUpdateCallback(new UpdateCallback(){

				@Override
				public void onNoUpdate() {
				}

				@Override
				public void onUpdated() {
				}

				@Override
				public void onBeforeUpdate() {
				}});
		}
		return instance;
	}
	
	private UpdateCallback updateCallback;
	
	public UpdateCallback getUpdateCallback() {
		return updateCallback;
	}

	public void setUpdateCallback(UpdateCallback updateCallback) {
		this.updateCallback = updateCallback;
	}
	
	/**
	 * 通用更新回调
	 * @author Doug
	 *
	 */
	public static interface UpdateCallback {
		
		//无更新 暂未使用
		public void onNoUpdate();
		
		//更新完成
		public void onUpdated();	
		
		public void onBeforeUpdate();	
	}
	
	/**
	 * 弹出框点击回调
	 * @author Doug
	 *
	 */
	public static interface UpdateConfirm {
		
		public void submit();
		
		public void cancel();
	}
	
	/**
	 * 官方更新流程
	 * @param context
	 */
	public void initLocalUpdate(final Context context) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				http = new KJHttp();
				updateManager = UpdateManager.getUpdateManager();
				HttpParams params = new HttpParams();

				params.put("sid", AppVariables.sid);
				http.post(AppConstants.UPDATE, params, new HttpCallBack(
						context, false) {

					@Override
					public void failure(JSONObject ret) {
						System.out.println("fail");
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
//						updateDone();
						updateCallback.onUpdated();
					};

					public void onSuccess(String t) {
						try {
							versionInfo = new JSONObject(t);
							checkUpdate(context);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				
			}
		}, 500); //修改本地更新请求延迟 2.9s-> 0.5s
	}
	
	//本地更新是否提示信息  特指 当前为最新版本时 是否提示
	private boolean showMsg = false;
	

	public boolean isShowMsg() {
		return showMsg;
	}

	public void setShowMsg(boolean showMsg) {
		this.showMsg = showMsg;
	}
	/**
	 * 检测更新
	 * @param context
	 */
	private void checkUpdate(Context context) {
		updateManager.setOnCheckDoneListener(new OnCheckDoneListener() {
			@Override
			public void onCheckDone() {
				updateCallback.onUpdated();
			}
		});
		CheckVersionInterface update = new CheckVersionInterface() {
			
			@Override
			public Update checkVersion() throws Exception {
				try {
					u = new Update(versionInfo);
					int currentVersion = CacheBean.getInstance().getApkInfo().versionCode;
					if (currentVersion < u.getVersionCode())
					CacheBean.getInstance().getRedConditions().put("lastVersion", u.getVersionCode() + "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return u;
			}
		};
		updateManager.checkAppUpdate(context, showMsg, update);
	}

	/**
	 * 新版应用宝自动更新
	 * @param context
	 * @param apkUpdateDetail 应用宝 查询更新内容{@link ApkUpdateDetail}
	 */
	private void yybUpdate(final Context context ,final ApkUpdateDetail apkUpdateDetail) {
		updateManager = UpdateManager.getUpdateManager();
		updateManager.setOnCheckDoneListener(new OnCheckDoneListener() {
			@Override
			public void onCheckDone() {
				updateCallback.onUpdated();
			}
		});
		
		CheckVersionInterface update = new CheckVersionInterface() {
			
			@Override
			public Update checkVersion() throws Exception {
				Update up = new Update();
				try {
//					String url = "http://dd.myapp.com/16891/a7d39e4b08bae5d6e0c4b72117d1d414.patch?fsname=com.nangua.xiaomanjflc_1.1.2_10102.patch1";
//					up.setDownloadURL(url);
					up.setDownloadURL(apkUpdateDetail.url);
					up.setForceUpdate(false);
					up.setVersionCode(apkUpdateDetail.versioncode);
					up.setVersionName(apkUpdateDetail.versionname);
					
					int currentVersion = CacheBean.getInstance().getApkInfo().versionCode;
					if (currentVersion < apkUpdateDetail.versioncode)
					CacheBean.getInstance().getRedConditions().put("lastVersion", apkUpdateDetail.versioncode + "");
					
					String patchTitle = "";
					if (apkUpdateDetail.patchsize > 0)
						patchTitle = "增量更新";
					up.setVersionDesc(patchTitle + " " + apkUpdateDetail.newFeature);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return up;
			}
		};
		//当为应用宝增量更新时 使用patch组包apk安装
		if (apkUpdateDetail.patchsize > 0) {
			updateManager.setOnPatchUpdateListener(new OnPatchUpdateListener() {
				
				@Override
				public void onPatchUpdate() {
					YYBManager.getInstance().startPatch(context, apkUpdateDetail.url);
				}
			});
		}
		updateManager.checkAppUpdate(context, showMsg, update);
	}
	
	/**
	 * 初始化应用宝自动更新
	 * @param c
	 */
	public void initYYB(final Context c) {
		YYBManager.getInstance().checkUpdate(c, new UpdateCallBack() {
			
			@Override
			public void onUpdateRecieved(ApkUpdateDetail apkUpdateDetail) {
				yybUpdate(c, apkUpdateDetail);
			}

			@Override
			public void noUpdate() {
				updateCallback.onUpdated();
			}
		});
	}
	
	/**
	 * 小米自动更新
	 * @param context
	 * @param debug true沙盒  false 正式
	 */
	public void initXiaomi(final Context context, boolean debug) {
		XiaomiUpdateAgent.setCheckUpdateOnlyWifi(true);
		
		XiaomiUpdateAgent.setUpdateAutoPopup(false);
		XiaomiUpdateAgent.setUpdateListener(new XiaomiUpdateListener() {

		    @Override
		    public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
		        switch (updateStatus) {
		            case UpdateStatus.STATUS_UPDATE:
		                 // 有更新， UpdateResponse为本次更新的详细信息
		                 // 其中包含更新信息，下载地址，MD5校验信息等，可自行处理下载安装
		            	 updateCallback.onBeforeUpdate();

		         		StringBuffer sb = new StringBuffer();
		         		double size = new   BigDecimal(1d * updateInfo.apkSize /1000 /1000).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
		         		sb.append("apk大小为" + size + "MB\n");
		         		sb.append("最新版本为" + updateInfo.versionName + "\n");
		         		sb.append("最新版本号为" + updateInfo.versionCode + "\n");
		         		sb.append("本次更新内容:" + updateInfo.updateLog + "\n");
		         		
						int currentVersion = CacheBean.getInstance().getApkInfo().versionCode;
						if (currentVersion < updateInfo.versionCode)
		         		CacheBean.getInstance().getRedConditions().put("lastVersion", updateInfo.versionCode + "");
		         		//旧弹出框UI
//		            	AlertDialog.Builder builder = new Builder(context);
//		         		builder.setTitle("软件版本更新");
//		         		builder.setMessage(sb.toString());
//		         		builder.setPositiveButton("立即更新", new OnClickListener() {
//		         			@Override
//		         			public void onClick(DialogInterface dialog, int which) {
//		         				dialog.dismiss();
//		         				XiaomiUpdateAgent.arrange();
//		         				updateCallback.onUpdated();
//		         			}
//		         		});
//		         		builder.setNegativeButton("以后再说", new OnClickListener() {
//		         			@Override
//		         			public void onClick(DialogInterface dialog, int which) {
//		         				updateCallback.onUpdated();
//		         				dialog.dismiss();
//		         			}
//		         		});
//
//		         		Dialog noticeDialog = builder.create();
//		         		noticeDialog.show();
		         		showDialog(context, sb.toString(), new UpdateConfirm() {
							
							@Override
							public void submit() {
								// 如果希望 SDK继续接管下载安装事宜，可调用
								StartApplication.parse = true;
								XiaomiUpdateAgent.arrange();
//		         				updateCallback.onUpdated();
							}
							
							@Override
							public void cancel() {
								updateCallback.onUpdated();
							}
						});
		                 break;
		             case UpdateStatus.STATUS_NO_UPDATE:
		                // 无更新， UpdateResponse为null
		            	 updateCallback.onUpdated();
		                break;
		            case UpdateStatus.STATUS_NO_WIFI:
		                // 设置了只在WiFi下更新，且WiFi不可用时， UpdateResponse为null
		            	updateCallback.onUpdated();
		                break;
		            case UpdateStatus.STATUS_NO_NET:
		                // 没有网络， UpdateResponse为null
		            	updateCallback.onUpdated();
		                break;
		            case UpdateStatus.STATUS_FAILED:
		                // 检查更新与服务器通讯失败，可稍后再试， UpdateResponse为null
		            	updateCallback.onUpdated();
		                break;
		            case UpdateStatus.STATUS_LOCAL_APP_FAILED:
		                // 检查更新获取本地安装应用信息失败， UpdateResponse为null
		                break;
		            default:
		                break;
		        }
		    }
		});
		XiaomiUpdateAgent.update(context, debug);
	}
	
	
	private DialogConfirmFragment updateConfirmDialog;
	/**
	 * 弹出对话框
	 * @param context 上下文 需要为fragmentActivity
	 * @param content 内容
	 * @param updateConfirm 按钮回调 {@link UpdateConfirm}
	 */
	private void showDialog(Context context, String content, final UpdateConfirm updateConfirm) {
		if (context instanceof FragmentActivity) {
			if (null == updateConfirmDialog) {
				updateConfirmDialog = new DialogConfirmFragment(new DialogConfirmFragment.CallBackDialogConfirm() {
					@Override
					public void onSubmit(int position) {
						if (null == updateConfirmDialog) {
							return;
						}
						updateConfirm.submit();
						updateConfirmDialog.dismiss();
						updateConfirmDialog = null;
					}
					
					@Override
					public void onKeyBack() {
						updateConfirm.cancel();
						updateConfirmDialog.dismiss();
						updateConfirmDialog = null;
					}
					
					@Override
					public void onCancel() {
						updateConfirm.cancel();
						updateConfirmDialog.dismiss();
						updateConfirmDialog = null;
					}
				}, content, " ", 0, "下载更新", "取消");
			}
			if (updateConfirmDialog.isVisible()) {
				return;
			}
			updateConfirmDialog.setCancelable(false);
			updateConfirmDialog.showDialog(((FragmentActivity)context).getSupportFragmentManager());
			
		}
		else {
			DebugPrinter.e("Dialog needs FragmentActivity");
		}
		
	}

	/**
	 * 百度自动更新
	 * @param context
	 */
//	public void initBaidu(final Context context) {
//		
//		BDAutoUpdateSDK.uiUpdateAction(context, new UICheckUpdateCallback() {
//
//			@Override
//			public void onCheckComplete() {
//				new Handler().post(new Runnable() {
//					
//					@Override
//					public void run() {
//						BDAutoUpdateSDK.cpUpdateCheck(context, new CPCheckUpdateCallback() {
//							
//							@Override
//							public void onCheckUpdateCallback(AppUpdateInfo appInfo,
//									AppUpdateInfoForInstall install) {
//								if (null == appInfo) return;
//								String name = appInfo.getAppSname();
//								int code = appInfo.getAppVersionCode();
//								String versionName = appInfo.getAppVersionName();
//								DebugPrinter.d("name = " + name + " code = " + code + " vesion = " + versionName);
//								int currentVersion = CacheBean.getInstance().getApkInfo().versionCode;
//								if (currentVersion < code)
//								CacheBean.getInstance().getRedConditions().put("lastVersion", code + "");
//								
//							}
//						});
//						
//					}
//				});
//				updateCallback.onUpdated();
//			}
//			
//		});
//	}


}
