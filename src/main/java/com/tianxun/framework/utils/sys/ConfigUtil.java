/**
 * 
 */
package com.tianxun.framework.utils.sys;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;
import com.tianxun.framework.utils.encrypt.EncryptUtil;


/**
 * @author ken.ma
 *
 */
public class ConfigUtil {
	
	private static final Logger logger = Logger.getLogger(ConfigUtil.class);
	
	private static final Properties pros = new Properties();
	//加解密的key,安全一点的做法是放到某个地方,这里通过反编译是可以得到的
    private static final String encryp_key = "LmMGStGtOpTiAnxuNyvYt54EQ";  
    
    //key的配置规则,即出现"pattern"将进行解密,这个可以自定义
    private static final String pattern_all = ".cipher";
    private static final String pattern_pw = ".password";
	
	private static String configFileName = "config.properties";
	
	private static void initEnv() {
		String env = System.getenv("env");
		if (Strings.isNullOrEmpty(env)) {
			env = System.getProperty("env");
			if (Strings.isNullOrEmpty(env)) {
				logger.warn("current env setting is empty");
			}
		}
		if (!Strings.isNullOrEmpty(env)) {
			logger.info("current env setting is : " + env);
			configFileName = "config_" + env + ".properties";
		}
	}
	
	static {
		initEnv();
		
		InputStream ins = ConfigUtil.class.getClassLoader().getResourceAsStream(configFileName);
		try {
			pros.load(new InputStreamReader(ins,"utf-8"));
			// 解密
			//遍历所有的key,看下是否有需要解密的
            Enumeration<?> keyenu = pros.propertyNames();
            while(keyenu.hasMoreElements()) {
                String key = (String) keyenu.nextElement();
                if(key != null && (key.toLowerCase().contains(pattern_all) || key.toLowerCase().contains(pattern_pw))) {
                    String ciphertext = pros.getProperty(key);
                    if(ciphertext != null) {
                        //自动解密后将结果存回去,这样使用方不必改变即可读取
                        String sourceValue = EncryptUtil.decrypt(ciphertext, encryp_key);
                        if(sourceValue != null) {
                            pros.setProperty(key, sourceValue);
                        }
                    }
                }
            }
		} catch (IOException e) {
			logger.error("load Properties file error", e);
		} catch (Exception e) {
			logger.error("load Properties Exception", e);
		} finally {
		    try {
		        ins.close();
		    } catch(Exception e) {
		    	logger.error("close Properties file error", e);
		    }
		}
	}
	
	/**
	 * 根据key获取系统配置
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		if(pros != null) {
			return pros.getProperty(key);
		}
		return null;
	}
}
