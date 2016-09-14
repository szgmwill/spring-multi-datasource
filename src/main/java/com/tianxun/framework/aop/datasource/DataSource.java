package com.tianxun.framework.aop.datasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据源的key
 * 这个key是选择哪个数据源的关键,所以这个key值必须跟着数据源的选择一致:com.tianxun.framework.aop.datasource.DynamicDataSource
 * 一般是这样：
 * <bean id="multiDataSource" class="com.tianxun.framework.aop.datasource.DynamicDataSource">
		<property name="targetDataSources">
			<map key-type="java.lang.String">
				<!-- write with master  -->
				<entry key="Master" value-ref="dataSourceForMaster"/>
				<!-- read with slave -->
				<entry key="Slave" value-ref="dataSourceForSlave"/>
				<!-- 可以设置多个key对应多个数据源 -->
			</map>
		</property>
		<!-- 默认走主库配置 -->
		<property name="defaultTargetDataSource" ref="dataSourceForMaster"/>
	</bean>
 * @author WillZhang
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)      
@Documented      
@Inherited 
public @interface DataSource {
	//master slave 
	public String value() default "master";
}