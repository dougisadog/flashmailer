package com.doug.component.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alipay.sdk.app.PayTask;
import com.doug.AppConstants;
import com.doug.component.bean.pay.OrderData;
import com.doug.component.bean.pay.ZfbPayResult;
import com.doug.component.utils.pay.SignUtils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class ALIPayManager {
	
	
	public static interface ALIPayCallBack {
		public void success(String resultStatus);
		public void confirming(String resultStatus);
		public void fail(String resultStatus);
		
	}
	
	private ALIPayCallBack callBack;
	public void setCallBack(ALIPayCallBack callBack) {
		this.callBack = callBack;
	}


	private static ALIPayManager manager;
	
	private ALIPayManager() {
		
	}
	public static ALIPayManager getInstance() {
		if (null == manager) 
			manager = new ALIPayManager();
		return manager;
	}
	
	
	private static final int SDK_PAY_FLAG = 1;

	//支付宝支付 Handler,里面有支付的返回码
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				ZfbPayResult zfbPayResult = new ZfbPayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultStatus = zfbPayResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					callBack.success(resultStatus);
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				} else if (TextUtils.equals(resultStatus, "8000")) {
					callBack.confirming(resultStatus);
				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					callBack.fail(resultStatus);
				}
				break;
			}
			default:
				break;
			}
		}
		
		/**
		 * 组装数据,并向 支付宝 发送数据
		 */
		public void assembleDataAndSendDataToZFBServer(OrderData data,String price, final Activity activity) {
			// 订单
			String orderInfo = "";
			String goodsName = data.getGoodsName();
			String cost = String.valueOf(data.getCost());
			if( !(TextUtils.isEmpty(goodsName) && TextUtils.isEmpty(cost))) {
				orderInfo = getOrderInfo(goodsName, goodsName, cost, price);	
			}
			//String orderInfo = getOrderInfo("大象", "哈哈哈哈", "0.01");  出现Ali59的错误,是因为传递数据出现了问题,从上面获取到正常的数据 就可以了.
			//Log.i(TAG,"getOutTradeNo :"+ getOutTradeNo().toString());
			// 对订单做RSA 签名
			String sign = sign(orderInfo);
			try {
				// 仅需对sign 做URL编码
				sign = URLEncoder.encode(sign, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// 完整的符合支付宝参数规范的订单信息
			final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
					+ getSignType();

			Runnable payRunnable = new Runnable() {

				@Override
				public void run() {
					// 构造PayTask 对象
					PayTask alipay = new PayTask(activity);
					// 调用支付接口，获取支付结果
					String result = alipay.pay(payInfo);

					Message msg = new Message();
					msg.what = SDK_PAY_FLAG;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			};

			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();
		}
	};
	
	
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price, String orderNo) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + AppConstants.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + AppConstants.SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";	
		
		// 服务器异步通知页面路径    经过了截取路径的操作
		String url = AppConstants.HOST;
		orderInfo += "&notify_url=" + "\"" + url +AppConstants.ALI_NOTIFY_URL
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 *//*
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}*/

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, AppConstants.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
}
