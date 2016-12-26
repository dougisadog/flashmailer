package com.doug;

/**
 * @description:
 * @author: Liu wei
 * @mail: i@liuwei.co
 * @date: 2014-3-12
 */
public class AppConstants {
	
	public final static String USER_DB_NAME = "xiaomanjf_user.db"; // 数据库名字
	
	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";// App唯一标识
	
	public final static String IPS_PACKAGE_NAME = "com.ips.p2p"; // 数据库名字

	public static String PATH_LOG_PATH = "/yilicai/Log/";// 日志默认保存目录

	public static String PATH_UPDATE_APK = "/yilicai/Update/";// 软件更新默认保存目录

	public static final int TIME = 120* 1000;// 手势密码出现持续时间  60秒

	public static final int PRODUCT_STATUS_REPAY = 1;// 还款中

	public static final int PRODUCT_STATUS_SOLD_OUT = 2;// 已售罄

	public static final int PRODUCT_STATUS_APPOINTMENT = 3;// 预约

	public static final int PRODUCT_STATUS_END = 4;// 已结束

	public static final int PRODUCT_STATUS_ON_SALE = 5;// 正在售卖
	
	public static final float BANNER_SCALE = 2.0f; // 长宽比

	/**
	 * 返回成功
	 */
	public static final int SUCCESS = 1;

	/**
	 * 返回失败
	 */
	public static final int FAILED = 2;
	
	
	
	//本地外网
	public final static String HOST = "http://nangua.webok.net:9973/p2p-mapp";
	public final static String HOST_IMAGE = "http://nangua.webok.net:9973/p2p-mapp/docroot/upload/images";
	public final static String SPECIALHOST = "http://nangua.webok.net:9973";
	
	public final static String UBIAOQING = "http://www.ubiaoqing.com/";
	public final static String MADAN = "http://md.itlun.cn/";

	public final static String HTTP = "http://";

	/**
	 * 登录
	 */
	public static final String SIGNIN = HOST + "/login/noOauth";

	/**
	 * 注册账号
	 */
	public static final String SIGNUP = HOST + "/register/noOauth";
	

	/**
	 * 发送手机验证码
	 */
	public static final String GETCODE = HOST + "/reg/sendcode";

	/**
	 * 获取验证码
	 */
	public static final String CAPTCHA = HOST + "/etc/captcha";

	/**
	 * 验证是否登录
	 */
	public static final String ISSIGNIN = HOST + "/islogin";

	/**
	 * 找回登陆密码时发送验证码
	 */
	public static final String SENDCODE = HOST + "/findback/sendcode";

	/**
	 * 找回登陆密码时验证手机验证码
	 */
	public static final String VERIFY_CODE = HOST + "/findback/find";

	/**
	 * 找回登陆密码
	 */
	public static final String GET_LOSE = HOST + "/findback/reset";

	public static final String GET_SLIDE_IMAGE = null;

	protected static final String UPDATE = null;

	public static final String BASICINFO = null;

	public static final String CHANGEPWD = null;


}
