package com.tianxun.framework.utils;

/**
 * 发送短信
 * @author kevingu
 *
 */
public class MessageUtil {

//	public static Logger logger = Logger.getLogger(MessageUtil.class);
//	
//	public static boolean sendMessage(String mobile, String message) {
//		try{
//			StringBuilder url = new StringBuilder();
//			url.append(ConfigUtil.get("SENDMESSAGE_URL")).append("?moblies=").append(mobile).append("&message=").append(URLEncoder.encode(message.toString(),"utf-8"));
//			String result = HttpClientUtils.doGet(url.toString());
//			if("0".equals(result)) {
//				return true;
//			}
//		} catch (Exception e) {
//			ExceptionLogUtil.logExceptionInfo(logger, e);
//		}
//		return false;
//	}
}
