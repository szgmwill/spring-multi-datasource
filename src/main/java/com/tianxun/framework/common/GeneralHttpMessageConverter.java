package com.tianxun.framework.common;

import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
/**
 * 自定义SpringMVC中的json转换
 * 可以扩展使用自己封装好的json转换器进行处理
 * 关键是实现方法：readInternal,writeInternal
 * @author WillZhang
 *
 */
public class GeneralHttpMessageConverter extends
		AbstractHttpMessageConverter<Object> {

	@Override
	protected boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException {

		//TODO 这里进行json的转换处理
		return null;
	}

	@Override
	protected void writeInternal(Object o, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		//TODO 这里进行json的转换处理
	}

}
