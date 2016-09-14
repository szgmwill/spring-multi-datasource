package com.tianxun.framework.aop.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源选择器
 * 只需要扩展Spring的AbstractRoutingDataSource类
 * 然后传入数据源的标识符key,这个key是配置数据源的xml文件里定义的
 * 这样就可实现每次sql操作前先输入数据源的id,再动态使用该数据源进行数据库操作
 * Created by willzhang on 2016/1/21.
 */
public class DynamicDataSource extends AbstractRoutingDataSource {


    /**
     * 通过key决定使用哪个数据源操作数据
     * @return database
     */
    @Override
    protected Object determineCurrentLookupKey() {
    	//当前线程的数据源标识已经在前面方法拦截的时候设置好了,只需要取出即可
        return DynamicDataSourceHolder.getDataSouce();
    }
}
