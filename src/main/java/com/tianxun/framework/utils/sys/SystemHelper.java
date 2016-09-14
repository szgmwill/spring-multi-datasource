package com.tianxun.framework.utils.sys;

import com.google.common.base.Strings;


/**
 * 系统,网络等工具类
 */
public class SystemHelper {
	
	/**
	 * 判断当前操作系统是否windows
	 */
	public static boolean isWindows() {
		if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断当前是否生产环境
	 * 只有设置了环境变量env=prod才是生产环境
	 */
	public static boolean isProduction() {
		String env = System.getenv("env");
		if (Strings.isNullOrEmpty(env)) {
			env = System.getProperty("env");
		}
		if (!Strings.isNullOrEmpty(env) && "prod".equalsIgnoreCase(env)) {
			return true;
		}
		
		return false;
	}
}
