package com.tianxun.framework.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * md5 util.
 * 
 * @author eric
 */
public final class Md5Util {
	
	private final static Logger logger = Logger.getLogger(Md5Util.class);

	/**
	 * 除去数组中的空值和签名参数
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("")
					|| key.equalsIgnoreCase("sign")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 */
	public static String getSign(Map<String, String> params, String key) {
		Map<String, String> paramsNew = paraFilter(params);
		String str = createLinkString(paramsNew);
		str = str + "&key=" + key;
		return MD5Encode(str);
	}
	
	/**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     */
    public static String getSignNoAnd(Map<String, String> params, String key) {
        Map<String, String> paramsNew = paraFilter(params);
        String str = createLinkString(paramsNew);
        str = str + key;
        return MD5Encode(str);
    }

	/**
	 * 对字符串 进行 MD5加密
	 * 
	 * @param value
	 * @return
	 */
	public static String MD5Encode(String value) {
		String result = null;
		try {
			byte[] valueByte = value.getBytes("utf-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(valueByte);
			result = toHex(md.digest()).toUpperCase();
		} catch (NoSuchAlgorithmException e1) {
		    ExceptionLogUtil.logExceptionInfo(logger, e1);
		}catch (UnsupportedEncodingException e2) {
		    ExceptionLogUtil.logExceptionInfo(logger, e2);
        }
		return result;
	}

	public static String toHex(byte[] buffer) {
		StringBuffer sb = new StringBuffer(buffer.length * 2);
		for (int i = 0; i < buffer.length; i++) {
			sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
			sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
		}
		return sb.toString();
	}
}
