package com.tianxun.framework.utils.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.tianxun.framework.metric.MetricLog;
import com.tianxun.framework.metric.MetricUtil;

public class HttpClientUtils {
	private final static Logger logger = Logger.getLogger(HttpClientUtils.class);

	private final static String DEFAULT_CHARSET = "UTF-8";

	private static int DEFAULT_TIMEOUT_CONN = 10 * 1000;

	private static int DEFAULT_TIMEOUT_READ = 30 * 1000;

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, DEFAULT_CHARSET);
	}

	/**
	 * @param url
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url, String charsetName) {
		return httpRequestGet(url, charsetName, DEFAULT_TIMEOUT_READ);
	}

	/**
	 * 
	 * @param url
	 * @param charsetName
	 * @param timeout
	 * @return
	 */
	public static String doGet(String url, String charsetName, int timeout) {
		return httpRequestGet(url, charsetName, timeout);
	}

	public static String doGetWithMetric(String url, String charsetName, MetricLog metricLog) {
	    MetricUtil.sendData(metricLog);
	    return httpRequestGet(url, charsetName, DEFAULT_TIMEOUT_READ);
	}
	
	public static String doPost(String url, Map<String, String> paramMap) {
		return doPost(url, paramMap, DEFAULT_CHARSET);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param paramMap
	 * @param charsetName
	 * @return
	 */
	public static String doPost(String url, Map<String, String> paramMap, String charsetName) {
		try {
			return httpRequestForPost(url, paramMap, charsetName, DEFAULT_TIMEOUT_READ);
		} catch (Exception e) {
			logger.error("httpRequestPost :", e);
		}
		return null;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param paramMap
	 * @param charsetName
	 * @return
	 */
	public static String doPost(String url, Map<String, String> paramMap, String charsetName, int timeout) {
		try {
			return httpRequestForPost(url, paramMap, charsetName, timeout);
		} catch (Exception e) {
			logger.error("httpRequestPost :", e);
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @param charsetName
	 * @param timeout
	 * @param hostIp
	 * @param port
	 * @param user
	 * @param password
	 * @return
	 */
	private static String httpRequestGet(String url, String charsetName, int timeout) {

		// 设置连接超时时间(单位毫秒) 设置读数据超时时间(单位毫秒)
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(DEFAULT_TIMEOUT_CONN)
				.setConnectTimeout(DEFAULT_TIMEOUT_READ).setSocketTimeout(DEFAULT_TIMEOUT_READ).build();

		String responseStr = null;
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet method = new HttpGet(url);
		try {
			method.setConfig(requestConfig);
			CloseableHttpResponse response = client.execute(method);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
			    logger.error("statusCode=" + statusCode);
				logger.error("Method failed: " + response.getStatusLine().getReasonPhrase());
                logger.error("url: " + url);
				return responseStr;
			}
			responseStr = EntityUtils.toString(response.getEntity(), charsetName);
		} catch (IOException e) {
			logger.error("httpRequest :", e);
		} finally {
			try {
				method.releaseConnection();
				client.close();
			} catch (Exception e) {
				logger.error("httpRequest :", e);
			}
		}
		return responseStr;
	}

	/**
	 * 请求数据(post)
	 * 
	 * @param url
	 * @param charsetName
	 * @param hostIp
	 * @param port
	 * @param user
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static String httpRequestForPost(String url, Map<String, String> paramMap, String charsetName, int timeout)
			throws Exception {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		// 设置连接超时时间(单位毫秒) 设置读数据超时时间(单位毫秒)
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout)
				.setConnectTimeout(timeout).setSocketTimeout(timeout).build();

		HttpPost method = new HttpPost(url);
		method.setConfig(requestConfig);
		String responseStr = null;
		try {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();  
	        for (String key : paramMap.keySet()) {  
	            pairs.add(new BasicNameValuePair(key, paramMap.get(key)));  
	        }  
	        method.setEntity(new UrlEncodedFormEntity(pairs, charsetName));
	        CloseableHttpResponse response = client.execute(method);
	        int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + response.getStatusLine().getReasonPhrase());
	            logger.error("地址: " + url);
				return responseStr;
			}
			
			responseStr = EntityUtils.toString(response.getEntity(), charsetName);
		} catch (IOException e) {
			logger.error("httpRequestForPost :", e);
		} finally {
			try {
				method.releaseConnection();
				client.close();
			} catch (Exception e) {
				logger.error("httpRequest :", e);
			}
		}
		return responseStr;
	}
	
	public static void main(String[] args) {
		String url = "http://localhost:9999/flights/pricing/createSession/intl/v1/";
		String result = HttpClientUtils.doGet(url);
		System.out.println(result);
	}
}
