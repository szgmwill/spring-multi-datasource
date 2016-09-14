package com.tianxun.framework.aop.cache;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存更新注解控制
 * @author willzhang
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Flush {
	/**
	 * 用户自定义缓存key的前缀
	 * 必须指出需要控制的缓存key匹配前缀,将会把所有这个前缀下的key缓存失效
	 */
	String keyPrefix();
}
