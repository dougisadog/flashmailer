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
package com.louding.frame.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包<br>
 * 
 * <b>创建时间</b> 2014-8-14
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.1
 */
public class StringUtils {
    private final static Pattern emailer = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern phone = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     */
    public static boolean isJsonEmpty(CharSequence input) {
        if (input == null || "".equals(input)||"null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(CharSequence email) {
        if (isEmpty(email))
            return false;
        return emailer.matcher(email).matches();
    }

    /**
     * 判断是不是一个合法的手机号码
     */
    public static boolean isPhone(CharSequence phoneNum) {
        if (isEmpty(phoneNum))
            return false;
        return phone.matcher(phoneNum).matches();
    }

    /**
     * 字符串转整数
     * 
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * String转long
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * String转double
     * 
     * @param obj
     * @return 转换异常返回 0
     */
    public static double toDouble(String obj) {
        try {
            return Double.parseDouble(obj);
        } catch (Exception e) {
        }
        return 0D;
    }

    /**
     * 字符串转布尔
     * 
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 判断一个字符串是不是数字
     */
    public static boolean isNumber(CharSequence str) {
        try {
            Integer.parseInt(str.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * byte[]数组转换为16进制的字符串。
     * 
     * @param data
     *            要转换的字节数组。
     * @return 转换后的结果。
     */
    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            int v = b & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    /**
     * 16进制表示的字符串转换为字节数组。
     * 
     * @param s
     *            16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return d;
    }
    

	private StringUtils() {
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static long toLong(String str, long defaultVal) {
		long ret = defaultVal;
		try {
			ret = Long.parseLong(str);
		} catch (NumberFormatException e) {
		}
		return ret;
	}

	public static String fill(String str, int count) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; ++i) {
			sb.append(str);
		}
		return sb.toString();
	}

	/**
	 * Get hex string from byte array
	 */
	public static String toHexString(byte[] res) {
		StringBuffer sb = new StringBuffer(res.length << 1);
		for (int i = 0; i < res.length; i++) {
			String digit = Integer.toHexString(0xFF & res[i]);
			if (digit.length() == 1) {
				digit = '0' + digit;
			}
			sb.append(digit);
		}
		return sb.toString();
	}

	public static String getDirectoryDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	public static String denominateAppID(String contextPath) {

		if (StringUtils.isEmpty(contextPath)) {
			contextPath = "root";
		} else {
			contextPath = contextPath.substring(1);
		}

		return contextPath;
	}

	/**
	 * 对参数的字符串进行  native2ascii 处理的方法。<br>
	 * 第二个参数设置 null的话、使用系统属性 file.encoding设定。
	 *
	 * @param str 变换的字符串
	 * @param encoding 字符编码
	 * @return native2ascii 字符串
	 * @throws UnsupportedEncodingException 指定不受支持的字符编码
	 */
	public static String native2Ascii(String str)
		throws UnsupportedEncodingException {
		StringBuffer buf = new StringBuffer(str.length() * 6);
		char[] carray = str.toCharArray();
		for (int i = 0; i < carray.length; i++) {
			if (carray[i] > '\u007f') {
				buf.append("\\u");
				String hex4 = Integer.toHexString((int)carray[i]);
				if (hex4.length() == 3) {
					hex4 = "0" + hex4;
				} else if (hex4.length() == 2) {
					hex4 = "00" + hex4;
				} else if (hex4.length() == 1) {
					hex4 = "000" + hex4;
				}
				buf.append(hex4);
			}
			else {
				buf.append(carray[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * 通过传递指定的编码返回转换后的 Unicode 转义字符串的方法。
	 *
	 * @param str 转义的 Unicode ASCII 字符串
	 * @param encoding 字符编码
	 * @return ascii 变换的字符串
	 * @throws UnsupportedEncodingException 指定不受支持的字符编码
	 */
	public static String ascii2Native(String str, String encoding)
		throws UnsupportedEncodingException {
		String sysEnc = System.getProperty("file.encoding");
		if (encoding == null) {
			encoding = sysEnc;
		}
		System.setProperty("file.encoding", encoding);

		StringBuffer buf = new StringBuffer();
		char[] carray = str.toCharArray();
		for (int i = 0; i < carray.length; i++) {
			// 字符串是 \u9999 格式的情况
			if (carray[i] == '\\'
				&& i + 1 < carray.length
				&& carray[i + 1] == 'u' && i + 5 < carray.length
				&& "0123456789abcdefABCDEF".indexOf(carray[i + 2]) > -1
				&& "0123456789abcdefABCDEF".indexOf(carray[i + 3]) > -1
				&& "0123456789abcdefABCDEF".indexOf(carray[i + 4]) > -1
				&& "0123456789abcdefABCDEF".indexOf(carray[i + 5]) > -1
			) {
				StringBuffer hex4 = new StringBuffer(4);
				for (int k = i + 2; k < i + 6; k++) {
					hex4.append(carray[k]);
				}
				buf.append((char)Integer.parseInt(hex4.toString(), 16));
				i = i + 5;
			} else {
				buf.append(carray[i]);
			}
		}

		System.setProperty("file.encoding", sysEnc);
		return buf.toString();
	}

	/**
	 * 取得UUID。
	 *
	 * @return UUID
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	/**
	 * 生成随机登录密码（默认8位长度）
	 * @return 登录密码的字符串
	 */
	public static String genUserPassword() {
		return genRandomNum(8);
	}

	/**
	 * 生成随机支付密码（默认8位长度）
	 * @return 支付密码的字符串
	 */
	public static String genPaymentPassword() {
		return genRandomNum(8);
	}

	/**
	 * 生成随机密码
	 * @param pwdLen 生成的密码的总长度
	 * @return 密码的字符串
	 */
	public static String genRandomNum(int pwdLen) {
		//35是因为数组是从0开始的，26个字母+10个 数字
		final int maxNum = 36;
		int i;  //生成的随机数
		int count = 0; //生成的密码的长度
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
				'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

		StringBuffer pwd = new StringBuffer("");
		Random r = new Random();
		while(count < pwdLen){
			//生成随机数，取绝对值，防止 生成负数，
			i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1
			if (i >= 0 && i < str.length) {
				if (i%2 ==0) {
					pwd.append(Character.toUpperCase(str[i]));
				} else {
					pwd.append(str[i]);
				}
				count ++;
			}
		}

		return pwd.toString();
	}

	

	/**
	 * 字符串前后空格（全角，半角）的trim
	 *
	 * @param orgstr 文字列
	 * @return trim后的文字列
	 */
	public static String trim(String orgstr) {
		while (orgstr.startsWith(" ") || orgstr.startsWith("　")) {
			orgstr = orgstr.substring(1);
		}
		while (orgstr.endsWith(" ") || orgstr.endsWith("　")) {
			orgstr = orgstr.substring(0, orgstr.length() - 1);
		}
		return orgstr;
	}


	public static boolean isSettingNum(String str) {
		String regex = "^([1-9]\\d*)$|^(0|[1-9]\\d*)\\.(\\d{1,2})$|^0$";
		return match(regex, str);
	}

	public static boolean isAnnualRate(String str) {
//		String regex = "^[1-9][0-9]?(\\.\\d)?$";
//		String regex = "^[1-9][0-9]?([.]{1}[0-9]{1,2}){0,1}$";
//		String regex = "^[1-9]?[0-9]{1}([.]{1}[0-9]{1,2}){0,1}$";
		String regex = "^([1-9]\\d{0,1})$|^(0|[1-9]\\d{0,1})\\.(\\d{1,2})$|^0$|^100$";
		return match(regex, str);
	}

	public static boolean isPermillage(String str) {
//		String regex = "^[1-9][0-9]?(\\.\\d)?$";
//		String regex = "^[1-9][0-9]?([.]{1}[0-9]{1,2}){0,1}$";
		String regex = "^([1-9]\\d{0,2})$|^(0|[1-9]\\d{0,2})\\.(\\d{1,2})$|^0$|^1000$";
		return match(regex, str);
	}

	/**
	* @param regex
	* 正则表达式字符串
	* @param str
	* 要匹配的字符串
	* @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	*/
	public static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public static boolean isLoginId(String str) {
		String regex = "^[a-zA-z][a-zA-Z0-9_]{3,15}$";
		return match(regex, str);
	}

	public static boolean isPasswordStrength(String str) {
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![`~!@#$%^&*()_+=\\\\|{}\\[\\]'\":;,.<>/?-]+$)[`~!@#$%^&*()_+=\\\\|{}\\[\\]'\":;,.<>/?0-9a-zA-Z-]{8,20}$";
		return match(regex, str);
	}
	/**
	 * 密码验证
	 * @param str 输入的密码
	 * @param len 密码最小长度
	 * @return
	 */
	public static boolean isPasswordStrength(String str, int len) {

		if (len < 1) {
			len = 8;
		}
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![`~!@#$%^&*()_+=\\\\|{}\\[\\]'\":;,.<>/?-]+$)[`~!@#$%^&*()_+=\\\\|{}\\[\\]'\":;,.<>/?0-9a-zA-Z-]{" + len +",20}$";
		return match(regex, str);
	}


	/**
	 * 功能：判断字符串是否为日期格式
	 *
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMobileNum(String str) {
		String regex = "^13[0-9]{9}$|14[0-9]{9}|15[0-9]{9}$|17[0-9]{9}$|18[0-9]{9}$";
		return match(regex, str);
	}


	public static boolean isMoney(String str) {
		String regex = "^[0-9]+(.[0-9]{1,2})?$";
		return match(regex, str);
	}


	public static boolean isInArray(String[] arr, String val) {
		return Arrays.asList(arr).contains(val);
	}

	/**
	 * 字符串是数值(含小数)的判定方法。
	 *
	 * @param str 字符串
	 * @return 数值(含小数) true
	 */
	public static boolean isNumeric(String str) {
		if (StringUtils.isEmpty(str)) {
			return false;
		}
		Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 字符串替换。
	 *
	 * @return 替换后字符串
	 */
	public static String replaceStr(String strInput, String strBefore, String strAfter) {
		if (strInput == null || "".equals(strInput)) {
			return "";
		} else {
			return strInput.replace(strBefore, strAfter);
		}
	}

	public static boolean isUrl(String str) {
		String regex = "^(\\w+:\\/\\/)?\\w+(\\.\\w+)+.*$";
		return match(regex, str);
	}

	public static boolean isTelNum(String str) {
		String regex = "\\d{3,5}-\\d{7,8}(-\\d{1,})?$";
		return match(regex, str);
	}

	/**
	 * 手机号中间4位用*显示。
	 *
	 * @return 替换后手机号
	 */
	public static String encryptMobile(String mobile) {
		if (mobile == null || "".equals(mobile)) {
			return "";
		} else {
			String ret = mobile.substring(0,3);
			ret = ret + "****";
			ret = ret + mobile.substring(7,11);
			return ret;
		}
	}

	public static String encryptEmail(String email) {
		if (email == null || "".equals(email)) {
			return "";
		} else {
			String ret = email.substring(0,1);
			ret = ret + "****";
			ret = ret + email.substring(email.length() - 1);
			return ret;
		}
	}

	public static String encryptIdCard(String idCard) {
		if (idCard == null || "".equals(idCard)) {
			return "";
		} else {
			String ret = idCard.substring(0,3);
			ret = ret + "***************";
			return ret;
		}
	}

	public static String newEncryptIdCard(String idCard) {
		if (idCard == null || "".equals(idCard)) {
			return "";
		} else {
			String ret = idCard.substring(0,6);
			ret = ret + "********";
			ret = ret + idCard.substring(14);
			return ret;
		}
	}

	/**
	 * 字符串截取固定Byte。
	 *
	 * @return 截取后字符串
	 */
	public static String getSubString(String str, int length) {
		int count = 0;
		int offset = 0;
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] > 256) {
				offset = 2;
				count += 2;
			} else {
				offset = 1;
				count++;
			}
			if (count == length) {
				int countRemain = 0;
				for (int j = i + 1; j < c.length; j++) {
					if (c[i] > 256) {
						countRemain += 2;
					} else {
						countRemain++;
					}
				}
				if (countRemain <= 3) {
					return str;
				} else {
					return str.substring(0, i + 1) + "...";
				}
			}
			if ((count == length + 1 && offset == 2)) {
				int countRemain = 0;
				for (int j = i + 1; j < c.length; j++) {
					if (c[i] > 256) {
						countRemain += 2;
					} else {
						countRemain++;
					}
				}
				if (countRemain <= 3) {
					return str;
				} else {
					return str.substring(0, i) + "...";
				}
			}
		}
		return str;
	}
	
	/**
	 * 字符串截取固定Byte。(不要...)
	 *
	 * @return 截取后字符串
	 */
	public static String getSubString2(String str, int length) {
		int count = 0;
		int offset = 0;
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] > 256) {
				offset = 2;
				count += 2;
			} else {
				offset = 1;
				count++;
			}
			if (count == length) {
				int countRemain = 0;
				for (int j = i + 1; j < c.length; j++) {
					if (c[i] > 256) {
						countRemain += 2;
					} else {
						countRemain++;
					}
				}
				if (countRemain <= 3) {
					return str;
				} else {
					return str.substring(0, i + 1);
				}
			}
			if ((count == length + 1 && offset == 2)) {
				int countRemain = 0;
				for (int j = i + 1; j < c.length; j++) {
					if (c[i] > 256) {
						countRemain += 2;
					} else {
						countRemain++;
					}
				}
				if (countRemain <= 3) {
					return str;
				} else {
					return str.substring(0, i);
				}
			}
		}
		return str;
	}


	public static String digitalUnitDisplay(BigDecimal digital) {
		String digitalStyleDisp = "元";
		if (digital.compareTo(new BigDecimal("0")) == 0) {
			return digitalStyleDisp;
		}

		// 大于亿的场合,以亿为单位;大于万的以万为单位;否则以元为单位
		if (digital.compareTo(new BigDecimal("100000000")) >= 0) {
			digitalStyleDisp = "亿";
		} else if (digital.compareTo(new BigDecimal("10000")) >= 0) {
			digitalStyleDisp = "万";
		} else {
			digitalStyleDisp = "元";
		}

		return digitalStyleDisp;
	}

	public static boolean isEmail(String str) {
		String regex = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
		return match(regex, str);
	}


	/**
     * 根据身份证的号码算出当前身份证持有者的性别和年龄 18位身份证 <br>
     *
     * key：  sex         男，女
     * key：  sexValue    1 ，0
     * key：  age
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getCarInfo(String CardCode)
            throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String year = CardCode.substring(6).substring(0, 4);// 得到年份
        String yue = CardCode.substring(10).substring(0, 2);// 得到月份
        // String day=CardCode.substring(12).substring(0,2);//得到日
        String sex = "";
        String sexValue = "";
        if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {// 判断性别
            sex = "女";
            sexValue = "1";
        } else {
            sex = "男";
            sexValue = "0";
        }
        Date date = new Date();// 得到当前的系统时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fyear = format.format(date).substring(0, 4);// 当前年份
        String fyue = format.format(date).substring(5, 7);// 月份
        // String fday=format.format(date).substring(8,10);
        int age = 0;
        if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) { // 当前月份大于用户出身的月份表示已过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
        } else {// 当前用户还没过生
            age = Integer.parseInt(fyear) - Integer.parseInt(year);
        }
        map.put("sex", sex);
        map.put("sexValue", sexValue);
        map.put("age", age);
        return map;
    }

}
