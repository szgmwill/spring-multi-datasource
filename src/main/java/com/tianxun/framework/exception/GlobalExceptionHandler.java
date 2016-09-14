package com.tianxun.framework.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.tianxun.framework.common.ErrorCode;
import com.tianxun.framework.common.JsonResult;
import com.tianxun.framework.utils.ExceptionLogUtil;

/**
 * 全局错误异常统一处理入口
 * @author Will Zhang
 *
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {
	private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * 利用全局已经配置好的json转换框架处理
	 */
	@Autowired
	private GsonHttpMessageConverter gsonHttpMessageConverter;


	/**
	 * 针对json异常统一处理返回
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
		
		//这里的处理要区分是接口类型还是视图类型,才能决定统一返回怎样的错误信息
		
		//目前的请求应该都是json方式的
		return doResolveExceptionForJson(request, response, exception);
	}
	
	/**
	 * 处理json异常返回
	 * @param exception
	 * @return
	 */
	private ModelAndView doResolveExceptionForJson(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		JsonResult result = new JsonResult();
		result.setCode(ErrorCode.SERVER_SYS_ERROR.getCode());
		
		//返回给用户展示的错误信息
		result.setMsg(ErrorCode.SERVER_SYS_ERROR.getMsg());
		ex.printStackTrace();
		//用于系统调试的错误信息
		result.setSysMsg(ExceptionLogUtil.traceExceptionMini(ex));
		
		//回写到返回结果里
		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		//返回的编码格式强制化utf-8,以防出现中文乱码
		webRequest.getResponse().setCharacterEncoding("UTF-8");
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
		
		try {
			gsonHttpMessageConverter.write(result, MediaType.APPLICATION_JSON, outputMessage);
		} catch (Exception jsonEx) {
			log.error("Error rendering json response!", jsonEx);
		}
		return new ModelAndView();
	}
	
}
