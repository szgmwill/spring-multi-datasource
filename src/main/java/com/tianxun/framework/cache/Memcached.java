/**
 * 
 */
package com.tianxun.framework.cache;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * mecached工具类
 * @author ken.ma
 *
 */
public class Memcached {

	private Logger logger = LoggerFactory.getLogger(Memcached.class); 
	
	private MemcachedClient client = null;
	
	public Memcached(String serverAddr, String username, String password, int poolSize) {
	    
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(serverAddr));
		//云服务器要密码认证, 测试环境不需要
        if (StringUtils.isNotBlank(username)){
		    builder.addAuthInfo(AddrUtil.getOneAddress(serverAddr), AuthInfo.plain(username, password));    
		}
        builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		//连接池,默认是1
		if(poolSize > 0) {
			builder.setConnectionPoolSize(poolSize);
		}
		
		builder.setCommandFactory(new BinaryCommandFactory());
		
		builder.getTranscoder().setCompressionThreshold(1024);
		
		try {
			client = builder.build();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 存值
	 * @param key
	 * @param validTime
	 * @param value
	 */
	public  boolean set(String key, int validTime, String value){
		if(logger.isDebugEnabled()){
			logger.debug("memcached key=" + key);
		}
		try {
			client.setWithNoReply(key, validTime, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}
	
	/**
	 * 存值
	 * @param key
	 * @param validTime
	 * @param value
	 */
	public  boolean set(String key, int validTime, Object value){
		if(logger.isDebugEnabled()){
			logger.debug("memcached key=" + key);
		}
		try {
			client.setWithNoReply(key, validTime, value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return true;
	}
    /**
     * 存值
     * @param key
     * @param validTime
     * @param value
     */
    public  boolean setBlock(String key, int validTime, String value){
        if(logger.isDebugEnabled()){
            logger.debug("memcached key=" + key);
        }
        try {
            client.set(key, validTime, value);
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
        return true;
    }	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public  String get(String key){
		String rst = null;
		try {
			rst = client.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rst = null;
		}
		return rst;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public  Object getObject(String key){
		Object rst = null;
		try {
			rst = client.get(key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			rst = null;
		}
		return rst;
	}
	/**
	 * @param key
	 */
	public  void del(String key){
	    try {
	        client.delete(key);
	    } catch (Exception e) {
	    	logger.error(e.getMessage(), e);
	    }
	}
	
	/**
	 * spring加载初始化时执行
	 */
	public void initMethod() {
		logger.info("initMethod ===");
	}
	
	/**
	 * 程序退出,spring销毁该bean前调用
	 */
	public void destroy() {
		logger.info("===start to shutdown memcachedClient===");
		try {
			client.shutdown();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("===successed shutdown memcachedClient===");
	}	
}
