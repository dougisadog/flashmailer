package com.doug.component.support;

import java.io.File;
import java.util.ArrayList;

import com.doug.component.error.DebugPrinter;
import com.doug.component.file.FileUtils;
import com.doug.component.utils.ApplicationUtil;
import com.louding.frame.ui.AnnotateUtil;
import com.louding.frame.utils.StringUtils;
import com.tencent.tmapkupdatesdk.ApkUpdateListener;
import com.tencent.tmapkupdatesdk.ApkUpdateManager;
import com.tencent.tmapkupdatesdk.model.ApkUpdateDetail;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 应用宝管理类
 * @author Administrator
 *
 */
public class YYBManager {
	
	private HandlerThread mHandlerThread = new HandlerThread("sdk_call_thread");
	
	// 主线程:ui线程
	private Handler mhandler;

	// 调用service的子线程
	private Handler mSDKHandler;
	
	//包名
	private String pkgnamString;
	
	private static YYBManager yyb  = null;
	
	public static YYBManager getInstance() {
		if (yyb == null)
			yyb = new YYBManager();
		return yyb; 
	}
	
	private  YYBManager() {
		mHandlerThread.start();
		mSDKHandler = new Handler(mHandlerThread.getLooper());
		Looper looper = Looper.myLooper();
		mhandler = new Handler(looper);
		// sdk必须用子线程调用
	}
	
	private UpdateCallBack updateCallBack;
	
	/**
	 * 更新检测回调
	 *
	 */
	public interface UpdateCallBack {
		/**
		 * 更新回调
		 * @param apkUpdateDetail {@link ApkUpdateDetail}
		 */
		public void onUpdateRecieved(ApkUpdateDetail apkUpdateDetail);
		
		/**
		 * 无更新
		 */
		public void noUpdate();
	}

	
	/**
	 * 查询更新
	 * @param context
	 * @param updateCallBack {@link UpdateCallBack}
	 */
	public void checkUpdate(Context context, UpdateCallBack updateCallBack) {
		this.updateCallBack = updateCallBack;
		ApkUpdateManager apkUpdataManager = ApkUpdateManager.getInstance();
		apkUpdataManager.init(context);
		apkUpdataManager.addListener(mApkUpdateListener);
		ArrayList<String> nameList = new ArrayList<String>();
		pkgnamString = ApplicationUtil.getApkInfo(context).packageName;
		nameList.add(pkgnamString);

		ApkUpdateManager.getInstance().checkUpdate(nameList);
	}
	
	/**
	 * 更新检查结果回调listener
	 */
	private ApkUpdateListener mApkUpdateListener = new ApkUpdateListener() {

		@Override
		public void onCheckUpdateSucceed(final ArrayList apkUpdateDetailList) {
			ArrayList<String> nameList = new ArrayList<String>();
			DebugPrinter.d("更新检测接收");
			final StringBuffer sb = new StringBuffer("");
			int count = 1;
			for (int i = 0;i<apkUpdateDetailList.size();i++) {
			    try {
					ApkUpdateDetail de = (ApkUpdateDetail)apkUpdateDetailList.get(i);
						sb.append("\n\t").append(count).append("、");
						sb.append("packageName=" + de.packageName + "; ");
						sb.append("versioncode=" + de.versioncode + "; ");
						String s = "";
						if (de.updatemethod == ApkUpdateDetail.UpdateMethod_Normal) {
							s = "普通更新[" + de.updatemethod + "]";
						} else if (de.updatemethod == ApkUpdateDetail.UpdateMethod_ByPatch) {
							s = "增量更新[" + de.updatemethod + "]";
						} else {
							s = "[" + de.updatemethod + "]";
						}
						sb.append("updatemethod=" + s + "; ");
						sb.append("url=" + de.url + "; ");
						sb.append("fileMd5=" + de.fileMd5 + "; ");
						sb.append("sigMd5=" + de.sigMd5 + "; ");
						sb.append("overwriteChannelid=" + de.overwriteChannelid + "; ");
						sb.append("\n\t");
						
						DebugPrinter.d(sb.toString());

						count++;
						nameList.add(de.packageName);
						
					}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//判断若有当前更新则
			int index = nameList.indexOf(pkgnamString);
			if (null != updateCallBack && -1 != index) {
				updateCallBack.onUpdateRecieved((ApkUpdateDetail) apkUpdateDetailList.get(index));
			}
			else {
				updateCallBack.noUpdate();
			}
		}

		@Override
		public void onCheckUpdateFailed(String errMsg) {
			// 更新ui 必须用主线程handler
			mhandler.post(new Runnable() {
				@Override
				public void run() {
					DebugPrinter.d("更新检测失败");
				}
			});
		}
	};

	/**
	 * 增量包已经下载完成，开始合成新包
	 * @param context
	 * @param patchUrl 增量包下载的地址
	 */
	public void startPatch(final Context context, final String patchUrl) {
		// 调用service必须用子线程
		mSDKHandler.post(new Runnable() {
			public void run() {

				try {
					String patchpath = UpdateManager.getUpdateManager().getApkPath();
					if (null != pkgnamString && StringUtils.isEmpty(pkgnamString)) {
						pkgnamString = ApplicationUtil.getApkInfo(context).packageName;
					}
					final String newGenApkPath = patchpath + "_" + pkgnamString
							+ "_new.apk";

					ApkUpdateManager.getInstance().patchNewApk(
								pkgnamString, patchpath, newGenApkPath);
					// 更新ui 必须用主线程handler
					mhandler.post(new Runnable() {
						@Override
						public void run() {
							File apkfile = new File(newGenApkPath);
							if (!apkfile.exists()) {
								return;
							}
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							i.setAction(android.content.Intent.ACTION_VIEW);
							i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
									"application/vnd.android.package-archive");
							context.startActivity(i);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					FileUtils.writeErr(e.getMessage(), "patchErr");
				}
			}
		});
	}

}
