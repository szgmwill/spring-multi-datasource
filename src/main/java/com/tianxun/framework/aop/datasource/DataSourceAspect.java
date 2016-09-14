package com.tianxun.framework.aop.datasource;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.tianxun.framework.aop.service.StatsdService;

/**
 * 通过AOP实现自动选择不同数据库操作数据库 切面类
 * 
 * @author willzhang
 *
 */
// 指定切面的优先级，当有多个切面时，数值越小优先级越高
@Order(2)//该优先级在缓存之后
// 把这个类声明为一个切面：需要把该类放入到IOC容器中。再声明为一个切面.
// 比如: <context:component-scan base-package="com.tianxun.x.aop" />
// <aop:aspectj-autoproxy proxy-target-class="false" />
@Aspect
@Component
public class DataSourceAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    //监控上报,通过statsd客户端上传监控指标数据
    @Autowired
    private StatsdService statsdService;
    
	/**
	 * 声明切入点表达式，一般在该方法中不再添加其他代码。 使用@Pointcut来声明切入点表达式。 后面的通知直接使用方法名来引用当前的切入点表达式。
	 * 可以定义多个pointcut点
	 * com.tianxun.*..mapper*..*
	 * execution(* com.tianxun.*..mapper*..*(..))
	 */
    //拦截所有包路径mapper下的所有类
	@Pointcut("execution(* com.tianxun.*..mapper*..*(..))")
	public void selectDaoTargetDataSourcePointCut() {}

	/**
	 * 前置通知，在目标方法开始之前执行。
	 * // 这里使用切入点表达式即可。后面的可以都改成切入点表达式。如果这个切入点表达式在别的包中，在前面加上包名和类名即可。
	 * @Before("selectDaoTargetDataSourcePointCut") //这样写可以指定特定的方法。
	 * @Before("execution(* com.tianxun.flight.dao.*.*(..))") //这样写可以指定特定的拦截包路径
	 * @param joinpoint
	 */
	@Before("selectDaoTargetDataSourcePointCut()")
	public void beforeDaoMethod(JoinPoint joinpoint) {
		logger.debug("beforeDaoMethod running.....");
		
		//记录DAO操作的开始时间,以便后置通知里可以读取来判断操作耗时
		DynamicDataSourceHolder.putStartTime(System.currentTimeMillis());
		try {
			//因为mapper类都是接口,按接口处理,类的处理和接口有点不一样
			Object target = joinpoint.getTarget();
			String methodName = joinpoint.getSignature().getName();
	
			Class<?>[] classz = target.getClass().getInterfaces();
			
			//判断有没有实现接口方法,一般mybatis mapper是会实现一个接口代理
			if(classz == null || classz.length < 1) {
				logger.warn("class not impl any interface...");
				return;
			}
			
			Class<?>[] parameterTypes = ((MethodSignature) joinpoint.getSignature())
					.getMethod().getParameterTypes();
		
			//这里要判断注解是针对整个DAO的接口类,还是只针对某个方法
//			Class<?> targetClass = classz[0];
			//目标类或方法上的注解
			String dsKey = null;//要执行的数据源
			DataSource ds = null;
			
			Method m = classz[0].getMethod(methodName, parameterTypes);
			if(m != null) {
				
				String className = m.getDeclaringClass().getSimpleName();
				//记录下执行的唯一标识
				DynamicDataSourceHolder.putIdentity(className + "." + methodName);
				
				//1.优先判断方法上的注解
				ds = m.getAnnotation(DataSource.class);
				if(ds == null) {
					//2.其次判断接口类上的注解
					ds = m.getDeclaringClass().getAnnotation(DataSource.class);
				}
				
				if(ds != null) {
					dsKey = ds.value();
				}

				if(StringUtils.isNotBlank(dsKey)) {
					logger.debug("Using DataSource ====> {}", dsKey);
					DynamicDataSourceHolder.putDataSource(dsKey);//数据源标识,一定要注意跟配置文件上的对应
				} else {
					//没有写注解的话,不作处理,使用默认数据源
				}
			}
		} catch (Exception e) {
			//不管什么错误,都不应该阻碍正常的sql执行
			logger.error("beforeDaoMethod ex:", e);
		}
	}
	
	/**
	 * 后置通知，在目标方法执行之后开始执行，无论目标方法是否抛出异常。 在后置通知中不能访问目标方法执行的结果。
	 * 
	 * @param joinpoint
	 */
	@After("selectDaoTargetDataSourcePointCut()")
	public void afterDaoMethod(JoinPoint joinpoint) {
		logger.debug("afterDaoMethod running.....");
		//一般是处理后的finally{}
		//TO DO
	}

	/**
	 * 返回通知，在方法正常结束之后执行。 可以访问到方法的返回值,即入参:Object result
	 * 
	 * @param joinpoint
	 * @param result
	 *            目标方法的返回值
	 */
	@AfterReturning(value = "selectDaoTargetDataSourcePointCut()", returning = "result")
	public void afterReturnning(JoinPoint joinpoint, Object result) {
		logger.debug("afterReturnning running.....");
		//记录DAO操作的耗时
		long endTime = System.currentTimeMillis();
		
		//在前置通知里记下的开始时间
		long startTime = DynamicDataSourceHolder.getStartTime();
		
		String ds = DynamicDataSourceHolder.getDataSouce();
		if(StringUtils.isBlank(ds)) {
			//默认源
			ds = DynamicDataSourceHolder.getDefaultDS();
		}
		
		//当前请求的标识：类名.方法名
		String classMethod = DynamicDataSourceHolder.getIdentity();
		long costTime = endTime - startTime;
		
		//监控上报
		statsdService.monitorDBTimer(ds, classMethod, new Long(costTime).intValue());
		
		//清理
		DynamicDataSourceHolder.clean();
		
		logger.debug("afterReturnning ==> ds[{}], servicekey[{}], startTime[{}], endTime[{}], costime[{}]", ds,classMethod,startTime,endTime,costTime);
	}

	/**
	 * 异常通知。目标方法出现异常的时候执行，可以访问到异常对象，可以指定在出现特定异常时才执行。
	 * 假如把参数写成NullPointerException则只在出现空指针异常的时候执行。
	 * 可以根据异常的类型自己判断处理方式
	 * 
	 * @param joinpoint
	 * @param e
	 */
	@AfterThrowing(value = "selectDaoTargetDataSourcePointCut()", throwing = "e")
	public void afterThrowing(JoinPoint joinpoint, Exception e) throws Exception {
		logger.debug("afterThrowing running.....");
		//记录DAO失败的次数
		String ds = DynamicDataSourceHolder.getDataSouce();
		if(StringUtils.isBlank(ds)) {
			//默认源
			ds = DynamicDataSourceHolder.getDefaultDS();
		}
		
		//当前请求的标识：类名.方法名
		String classMethod = DynamicDataSourceHolder.getIdentity();
//		long costTime = endTime - startTime;
		
		//监控上报
		statsdService.monitorDBFailed(ds, classMethod);
		
		//清理
		DynamicDataSourceHolder.clean();
		
		//记录error日志并且返回异常
		logger.error("afterThrowing Exception ==> ", e);
		throw e;
	}

	/**
	 * 环绕通知类似于动态代理的全过程，ProceedingJoinPoint类型的参数可以决定是否执行目标方法。
	 * 
	 * @param point
	 *            环绕通知需要携带ProceedingJoinPoint类型的参数。
	 * @return 目标方法的返回值。必须有返回值。
	 */
//	@Around("com.xyz.myapp.SystemArchitecture.businessService()")
//    public Object doBasicProfiling(ProceedingJoinPoint pjp) {
//        // start stopwatch
//        Object retVal = pjp.proceed();
//        // stop stopwatch
//        return retVal;
//    }
	
	/**
	 * After (finally) advice
	 */
//	@After("com.xyz.myapp.SystemArchitecture.dataAccessOperation()")
//    public void doReleaseLock() {
//        // ...
//    }
}
