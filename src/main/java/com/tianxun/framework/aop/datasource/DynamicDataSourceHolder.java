package com.tianxun.framework.aop.datasource;

/**
 * 多个数据源的标签
 * Created by willzhang on 2016/1/21.
 */
public class DynamicDataSourceHolder {
	
	//线程变量类型,因为每次sql操作的动作都对应一个独立的线程,所以每个线程必须有自己的数据源标识变量
    private static final ThreadLocal<String> dsHolder = new ThreadLocal<String>();
    
    //线程变量类型,保存每次查询数据库之前的开始时间,后面可以算出每次执行SQL的耗时,作为一个监控采集项
    private static final ThreadLocal<Long> timeHolder = new ThreadLocal<Long>();
	
    //请求的标识符:类名+方法名
    private static final ThreadLocal<String> classMethodHolder = new ThreadLocal<String>();
    
    //如果没有定义走哪个数据源,则默认源,注意默认源必须和xml配置文件里的默认源一致
    private static final String DEFAULT = "master";
    
    public static void putDataSource(String name) {
    	dsHolder.set(name);
    }

    public static String getDataSouce() {
        return dsHolder.get();
    }

    public static void putStartTime(long startTime) {
    	timeHolder.set(startTime);
    }
    
    public static long getStartTime() {
    	return timeHolder.get();
    }
    
    public static void putIdentity(String identity) {
    	classMethodHolder.set(identity);
    }
    
    public static String getIdentity() {
    	return classMethodHolder.get();
    }
    
    public static String getDefaultDS() {
    	return DEFAULT;
    }
    
    //记得要清除上一次的,要不同一个线程中将会有上一次的结果
    public static void clean() {
    	dsHolder.remove();
    	timeHolder.remove();
    	classMethodHolder.remove();
    }
}
