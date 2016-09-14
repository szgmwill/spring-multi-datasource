/**
 * 
 */
package com.tianxun.framework.utils;

import org.apache.log4j.Logger;

/**
 * @author ken.ma
 *
 */
public class ExceptionLogUtil {

	/**
	 * 将Exception信息写入log4j里面
	 * @param logger
	 * @param e
	 */
	public static void logExceptionInfo(Logger logger, Throwable e){	
	    if(logger != null) {
	        logger.error(e == null ? "" : e.getMessage(), e);
	    }
        e.printStackTrace();		
	}
	
	
	
	/**
	 * 抽出异常中的核心信息<br>
	 * 1.输出异常种类，如nullpoint，time out<br>
	 * 2.如果有异常栈，则输出抛出异常的类、方法、行号
	 * 
	 * @param e
	 * @return
	 */
	public static String traceException(Exception e) {
		if (e == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder(e.toString());
		StackTraceElement[] stackTrace = e.getStackTrace();
		if (stackTrace != null && stackTrace.length > 0) {
			StackTraceElement stackTraceElement = stackTrace[0];
			sb.append(" Class:").append(stackTraceElement.getClassName())
					.append(" Method:")
					.append(stackTraceElement.getMethodName()).append(" Line:")
					.append(stackTraceElement.getLineNumber());
		} else {
			sb.append("no stackTrace");
		}

		return sb.toString();
	}

	/**
	 * 抽出异常中的最小信息集合<br>
	 * 1.仅输出异常种类，如nullpoint，time out<br>
	 * 
	 * @param e
	 * @return
	 */
	public static String traceExceptionMini(Exception e) {
		if (e == null) {
			return "";
		}
		return e.toString();
	}
}
