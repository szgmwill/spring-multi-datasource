package com.tianxun.framework.aop.cache;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.rubyeye.xmemcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 自动缓存服务
 * @author willzhang
 *
 */
//@Service
public class AutoMemcachedService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AutoMemcachedService.class);
	
	/**
	 * 缓存客户端实例
	 */
	@Autowired
	@Qualifier("memcachedClientForAuto") //指定要哪个bean id
	private MemcachedClient client;

	/**
	 * 在加载实例时调用
	 * 初始化memcache客户端
	 */
	@PostConstruct
	public void initMemcache() {
		LOGGER.info("initMemcache ~~~~~~~");
	}
	
	/**
	 * 实例被销毁前调用
	 */
	@PreDestroy
	public void destroy() {
		LOGGER.info("destroy ~~~~~~~");
		
		if(client != null && !client.isShutdown()) {
			try {
				client.shutdown();
			} catch (IOException e) {
				LOGGER.error("Shutdown Memcache Client Exception:", e);
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 存值
	 * @param key
	 * @param validTime
	 * @param value
	 * 注意对象的大小问题
	 */
	public boolean set(String key, int validTime, Object value) {
		
		try {
			client.setWithNoReply(key, validTime, value);
		} catch (Exception e) {
			LOGGER.error("set key:"+key, e);
		}
		return true;
	}
	
	public Object get(String key) {
		try {
			return client.get(key);
		} catch (Exception e) {
			LOGGER.error("get key:"+key, e);
		}
		return null;
	}
	
	/**
	 * @param key
	 */
	public void del(String key) {
	    try {
	        client.delete(key);
	    } catch (Exception e) {
	    	LOGGER.error("delete key:"+key, e);
	    }
	}
}
