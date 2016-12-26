package com.doug.component.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class FormatUtils {

	public static String priceFormat(long price) {
		String st = "";
		if (price < 0) {
			price = -price;
			st = "-";
		}
		long a = price / 100;
		long b = price % 100;
		if (b == 0) {
			st += a + ".00";
		} else if (b < 10) {
			st += a + ".0" + b;
		} else if (b > 9) {
			st += a + "." + b;
		}
		return st;
	}

	public static String priceFormat(int price) {
		String st = "";
		long a = price / 100;
		long b = price % 100;
		if (b == 0) {
			st = a + ".00";
		} else if (b < 10) {
			st = a + ".0" + b;
		} else if (b > 9) {
			st = a + "." + b;
		}
		return st;
	}

	public static String getAmount(String s) {
		String a;
		float i = Float.parseFloat(s);
		if (i > 1000000) {
			if (i % 1000000 == 0) {
				a = ((long) i / 1000000) + "万";
			} else {
				a = (i / 1000000) + "万";
			}
		} else {
			if (i % 100 == 0) {
				a = ((long) i / 100) + "元";
			} else {
				a = (i / 100) + "元";
			}
		}
		return a;
	}

	public static ArrayList<String> getNewAmount(String s) {
		String a;
		String b;
		float i = Float.parseFloat(s);
		if (i > 1000000) {
			if (i % 1000000 == 0) {
				a = ((long) i / 1000000) + "";
			} else {
				a = (i / 1000000) + "";
			}
			b = "万";
		} else {
			if (i % 100 == 0) {
				a = ((long) i / 100) + "";
			} else {
				a = (i / 100) + "";
			}
			b = "元";
		}
		ArrayList<String> ss = new ArrayList<String>();
		ss.add(a);
		ss.add(b);
		return ss;
	}

	public static String urlFormat(String url) {
		if (url.indexOf("http") == -1) {
			url = "http://" + url;
		}
		return url;
	}

	// 处理浮点数显示的时候后面会出现.0的问题
	public static String numFormat(float num) {
		String str = String.valueOf(num);
		String[] s = str.split("\\.");
		Log.d("numFormat", s.length + "");
		Log.d("numFormat", str);
		for (int i = 0; i < s.length; i++) {
			Log.d("numFormat", s[i]);
		}
		if (s.length > 1 && s[1].equals("0")) {
			return s[0];
		} else {
			return str;
		}
	}
	
	/**
	 * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
	 *
	 * @param str
	 *            无逗号的数字
	 * @return 加上逗号的数字
	 */
	public static String fmtMicrometer(String text) {
		DecimalFormat df = null;
		if (text.indexOf(".") > 0) {
			if (text.length() - text.indexOf(".") - 1 == 0) {
				df = new DecimalFormat("###,##0.");
			} else if (text.length() - text.indexOf(".") - 1 == 1) {
				df = new DecimalFormat("###,##0.0");
			} else {
				df = new DecimalFormat("###,##0.00");
			}
		} else {
			df = new DecimalFormat("###,##0");
		}
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}
	
	/**
	 * jsons数据解析成为对应类
	 * @param json
	 * @param clazz 所属class
	 * @return
	 */
	public static <T> T jsonParse(String json, Class<T> clazz) {
		T result;
		try {
			Gson gson = new GsonBuilder().create();
			result = gson.fromJson(json, clazz);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
		return result;
		
	}

}
