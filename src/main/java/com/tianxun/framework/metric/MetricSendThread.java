package com.tianxun.framework.metric;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.tianxun.framework.utils.ExceptionLogUtil;
import com.tianxun.framework.utils.http.HttpClientUtils;

public class MetricSendThread implements Runnable {


	private final static Logger logger = Logger.getLogger(MetricSendThread.class);
	
	private String metricAddress;
	
	private Map<String, String> paramsMap;
	
	
	public MetricSendThread (String metricAddress, Map<String, String> paramsMap) {
		this.metricAddress = metricAddress;
		this.paramsMap = paramsMap;
	}
	
	@Override
	public void run() {
		try{
		    if(StringUtils.isNotEmpty(metricAddress)) {
		        HttpClientUtils.doPost(metricAddress, paramsMap, "UTF-8", 30);
		    }
		}catch(Exception e) {
			ExceptionLogUtil.logExceptionInfo(logger, e);
		}
	}
}
