package com.tianxun.framework.aop.service;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tianxun.framework.utils.sys.ConfigUtil;

@Service
public class StatsdService implements InitializingBean {
	private static final Logger log = LoggerFactory.getLogger(StatsdService.class);
	
	//区分当前处理消息的statsd通道,也是用于区分不同机器发送的metric
	private static String statsD_id = "db-exec-";
	
	//当前服务的名称
	private static String serviceName = "";
	
	//当前本地的ip
	private static String localIp;
	
	//statsd的客户端,注意正确使用,实例个数,关闭资源等
	private static StatsdClient client;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(client == null) {
			int statsdPort = 8125;
			String statsdHost = ConfigUtil.get("statsD.host");
			String portStr = ConfigUtil.get("statsD.port");
			serviceName = ConfigUtil.get("service.name");
			log.info("Starting init StatsD Client ======> statsdHost[{}], Port[{}]", statsdHost, statsdPort);
			if (StringUtils.isNotBlank(portStr)
					&& NumberUtils.isNumber(portStr)) {
				statsdPort = Integer.parseInt(portStr);
			}
			
			if(StringUtils.isBlank(serviceName)) {
				log.warn("service.name not set, reset to unknown ===");
				serviceName = "unknown";
			}
			
			if (StringUtils.isBlank(statsdHost) || statsdPort <= 0) {
				log.error("No StatsD Setting Error!!!");
				return;
			}
			
			// 根据定义将Metric的名字按照要求拼接好
			// 因为host名字有"." ,特别处理一下
			String ipString = getLocalNetWorkIp();
			ipString = ipString.replaceAll("\\.", "-");
			
			statsD_id = statsD_id + ipString;
			log.info("================statsD_id====: {}", statsD_id);
			
			try {
				client = new StatsdClient(statsdHost, statsdPort);
			} catch (UnknownHostException e) {
				log.error("Init StatsdClient UnknownHostException", e);
			} catch (IOException e) {
				log.error("Init StatsdClient IOException", e);
			}
			
			log.info("StatsD Client Init Successfully ==== statsD_id[{}] host[{}], port[{}]", statsD_id, statsdHost, statsdPort);
		}

		if(client == null) {
			log.error("statsd server connect failed!!!");
		}
	}

	/**
	 * 监控数据库SQL操作的耗时
	 * 通过statsd客户端发送到statsd服务器进行统计
	 */
	public void monitorDBTimer(String ds, String classMethod, int time) {
		if(client != null) {
			//上报的最终标识符:serviceName(服务id).className(类名).methodName(方法名)
			StringBuffer sb = new StringBuffer();
			sb.append(statsD_id);
			sb.append(".").append(ds);
			sb.append(".").append(serviceName);
			if(StringUtils.isNotBlank(classMethod)) {
				sb.append(".").append(classMethod);
			}
			sb.append(".").append("time");
			
			String metricName = sb.toString();
			
			log.debug("metricName {}, value {}", metricName, time);

			//耗时一般采用timer类型
			client.timing(metricName, time);
		}
	}
	
	/**
	 * 监控数据库SQL操作的失败次数,每次调用默认+1
	 * 通过statsd客户端发送到statsd服务器进行统计
	 */
	public void monitorDBFailed(String ds, String classMethod) {
		if(client != null) {
			//上报的最终标识符:serviceName(服务id).className(类名).methodName(方法名)
			StringBuffer sb = new StringBuffer();
			sb.append(statsD_id);
			sb.append(".").append(ds);
			sb.append(".").append(serviceName);
			if(StringUtils.isNotBlank(classMethod)) {
				sb.append(".").append(classMethod);
			}
			sb.append(".").append(".failure");
			
			String metricName = sb.toString();
			
			log.debug("metricName {}, failure", metricName);

			//counter类型
			client.increment(metricName);
		}
	}
	
	@PreDestroy
	public void close() throws Exception {
		// TODO Auto-generated method stub
		if(client != null) {
			client.stopFlushTimer();
		}
	}
	
	/**
	 * 获取本机的网络IP
	 */
	public static String getLocalNetWorkIp() {
		if (localIp != null) {
			return localIp;
		}
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {// 遍历所有的网卡
				NetworkInterface ni = (NetworkInterface) netInterfaces
						.nextElement();
				if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()
						|| ni.isPointToPoint()) {
					continue;
				}
				Enumeration<InetAddress> addresss = ni.getInetAddresses();
				while (addresss.hasMoreElements()) {
					InetAddress address = addresss.nextElement();
					if (address instanceof Inet4Address) {// 这里暂时只获取ipv4地址
						ip = address;
						break;
					}
				}
				if (ip != null) {
					break;
				}
			}
			if (ip != null) {
				localIp = ip.getHostAddress();
			} else {
				localIp = "127.0.0.1";
			}
		} catch (Exception e) {
			localIp = "127.0.0.1";
		}
		return localIp;
	}
}
