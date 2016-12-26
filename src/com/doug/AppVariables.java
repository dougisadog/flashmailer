package com.doug;

import com.doug.component.cache.CacheBean;

/**
 * 内存中的当前运行参数
 * @author Doug
 *
 */
public class AppVariables {
	
	public static Long cacheLiveTime = (long) (5 * 60 * 1000); //5分钟 数据更新缓存时间

    public static int uid = 0; //主键

    public static String sid = ""; //通信登录id sid

    public static boolean isSignin = false; //已登录

    public static String tel = ""; //电话

    public static long lastTime = 0; //上次登录时间
    
    //开启强制更新
    public static boolean  forceUpdate = false;

    //当前是否强制弹出手势验证
    public static boolean needGesture = true;
    
    //不再提示 的 弹出判定 true 弹出 false不弹
    public static boolean dismissVersionUpdate = true;

    /**
     * 清空缓存
     */
    public static void clear() {
        uid = 0;
        sid = "";
        isSignin = false;
        tel = "";
        forceUpdate = false;
        needGesture = true;
        AppConfig.getAppConfig(FlashApplication.getInstance().getApplicationContext()).set(AppConfig.SID, "");
        CacheBean.getInstance().clear();
    }

}
