package com.tianxun.framework.aop.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存读取注解控制
 * @author willzhang
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cache {
	/**
	 * 用户自定义缓存key的前缀
	 * 如果不指定,则默认用类名+方法名+参数值
	 */
	String keyPrefix() default "";
	
	/**
	 * 缓存失效时间(秒)
	 * 默认值会根据缓存的类型决定
	 */
	int expireTime() default 30*60;
}
