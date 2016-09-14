package com.tianxun.framework.aop.cache;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 缓存的切面定义和处理
 * 将需要缓存拦截处理的各个情况在这里处理
 * 
 * @author willzhang
 *
 */

// 指定切面的优先级，当有多个切面时，数值越小优先级越高
//@Order(1)
//把这个类声明为一个切面：需要把该类放入到IOC容器中。再声明为一个切面.
//比如: <context:component-scan base-package="com.tianxun.x.aop" />
//<aop:aspectj-autoproxy proxy-target-class="false" />
//@Aspect
//@Component
public class CacheAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheAspect.class);
	
	//只针对缓存使用的专用memcache
	@Autowired
	private AutoMemcachedService cacheService;
	
	/**
	 * 声明切入点表达式，一般在该方法中不再添加其他代码。 使用@Pointcut来声明切入点表达式。 后面的通知直接使用方法名来引用当前的切入点表达式。
	 * 可以定义多个pointcut点
	 * com.tianxun.*..mapper*..*
	 * execution(* com.tianxun.*..mapper*..*(..))
	 */
	@Pointcut("execution(* com.tianxun.*..mapper*..*(..))")//拦截所有mapper的接口方法
	public void cachePointCutForDao() {
	}
	
	//拦截所有service的公共方法
	@Pointcut("execution(* com.tianxun.*..service*..*(..))")
	public void cachePointCutForService() {
	}
	
	@Around("cachePointCutForDao()")
	public Object aroundDao(ProceedingJoinPoint pjp) throws Throwable {
		return doCacheAround(pjp, true);
	}
	
	@Around("cachePointCutForService()")
	public Object aroundService(ProceedingJoinPoint pjp) throws Throwable {
		return doCacheAround(pjp, false);
	}
	
	/**
	 * 环绕通知类似于动态代理的全过程，ProceedingJoinPoint类型的参数可以决定是否执行目标方法。
	 * 
	 * @param point
	 *            环绕通知需要携带ProceedingJoinPoint类型的参数。
	 * @return 目标方法的返回值。必须有返回值。
	 * @throws Throwable 
	 */
	public Object doCacheAround(ProceedingJoinPoint pjp, boolean isInterface) throws Throwable {
		LOGGER.debug("doCacheAround is running ===========");
		
		// start stopwatch
		Object result = null;//最终返回的结果对象
		int expiration = CacheTimeType.all.getDuration();
		String cacheKey = null;
		try {
		
			Object target = pjp.getTarget();
			Signature signature =  pjp.getSignature();
			//得到拦截的方法的返回类型
			MethodSignature ms = (MethodSignature) signature;
//			Class<?> returnType = ms.getReturnType();//通过得到返回类型,可以实现将对象序列化然后保存到cache,并且反序列化回来后返回该类型
			String methodName = signature.getName();
			Method m = null;
			String className = null;
			if(isInterface) {
				//接口(一般针对DAO)
				Class<?>[] classz = target.getClass().getInterfaces();
				
				Class<?>[] parameterTypes = ms.getMethod().getParameterTypes();
			
				m = classz[0].getMethod(methodName, parameterTypes);
				className = m.getDeclaringClass().getSimpleName();
			} else {
				//普通的实现类
				m = ms.getMethod();
				className = target.getClass().getSimpleName();
			}

			if(m != null && className != null) {
				
				if (m.isAnnotationPresent(Cache.class)) {
					Cache cache = m.getAnnotation(Cache.class);
					if (cache != null) {
						cacheKey = this.getKey(cache.keyPrefix(), className, m.getName(), pjp.getArgs());
						expiration = cache.expireTime();
						if(cacheKey != null) {
							LOGGER.debug("cacheKey---------------:"+cacheKey);
							//先从缓存读取是否有值
							result = cacheService.get(cacheKey);
						}
						
					}
				} else if (m.isAnnotationPresent(Flush.class)) {//如果是需要更新缓存的话
					Flush flush = m.getAnnotation(Flush.class);
					if (flush != null) {
						String prefix = flush.keyPrefix();
						if(StringUtils.isNotBlank(prefix)) {
							//寻找所有这个打头的key列表
							//TO DO
							
							//删除这些key,即让缓存失效
							//TO DO
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("doCacheAround error:", e);
		}
		
		if(result == null) {
			//正常往后调用
			result = pjp.proceed();
			
			//查询为空是否需要放入缓存?
            if(cacheKey != null && result != null) {
            	
            	if(result instanceof Serializable) {
            		//如果已经实现序列化,可网络传输直接放入cache中
            		//被缓存的对象必须这现序列化接口,而且安全起见最好判断一下大小,以免太大
                	//判断大小 TO DO
            		LOGGER.debug("put to cache @@@@@@: {}", result);
                	 cacheService.set(cacheKey, expiration, result);
            	} else {
            		//如果没有序列化,则可先将其json化后放入
            		//TO DO
            	}
            }
		} else {
			LOGGER.debug("cacheKey[{}] go cache...", cacheKey);
			
			if(result instanceof Serializable) {
				//可直接返回
			} else {
				//放入的时候是先json后的,所以还得还原为原来的对象,根据方法的返回类型json反序列化.注意复杂对象的深度处理问题
				// TO DO
			}
		}
		
		LOGGER.debug("End doCacheAround ===========");
		
		// stop stopwatch
		return result;
	}

	/**
	 * 组装key值
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	private String getKey(String prefix, String className, String methodName, Object[] args) {
		if(className != null && methodName != null) {
			StringBuffer sb = new StringBuffer();
			sb.append(className).append("_");
			sb.append(methodName);
			
			if (args != null && args.length > 0) {
				for (Object arg : args) {
					sb.append("_").append(arg);
				}
			}
			
			//用户自定义的前缀
			if(StringUtils.isNotBlank(prefix)) {
				return prefix + "_" + sb.toString();
			}
			
			return sb.toString();
		}
		return null;
	}
}