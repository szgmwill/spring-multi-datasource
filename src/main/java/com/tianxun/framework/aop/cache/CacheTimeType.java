package com.tianxun.framework.aop.cache;
/**
 * 缓存时间类别
 * @author willzhang
 *
 */
public enum CacheTimeType {
	all(30*60),
	
	price(5*60),
	
	base(6*3600);
	
	private int duration;
	
	private CacheTimeType(int expire) {
		this.duration = expire;
	}

	public int getDuration() {
		return duration;
	}
}
