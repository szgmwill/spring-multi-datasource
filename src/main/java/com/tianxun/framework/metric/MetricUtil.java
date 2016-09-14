package com.tianxun.framework.metric;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.tianxun.framework.utils.ExceptionLogUtil;
import com.tianxun.framework.utils.sys.ConfigUtil;

public class MetricUtil {

    private final static Logger logger = Logger.getLogger(MetricUtil.class);

    private static String metricType_timer = "timer";

    private static String metricTopic = "platform";

    private static String metricAddress = ConfigUtil.get("metric.address");

    private static ExecutorService sendDataPool = Executors.newFixedThreadPool(50);

    private static Map<String, String> channelMap = new HashMap<String, String>();

    static {
        channelMap.put("1", "flights");
        channelMap.put("2", "hotel");
        channelMap.put("3", "travel");
        channelMap.put("5", "carhire");
    }

    public static void sendDate(String topic, String message) {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("topic", topic);
        paramsMap.put("message", message);
        MetricSendThread thread = new MetricSendThread(metricAddress, paramsMap);
        sendDataPool.execute(thread);
    }
    
    public static void sendData(MetricLog partnerLog) {
        try {
            String message = getMetricData(partnerLog, metricType_timer);
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("topic", metricTopic);
            paramsMap.put("message", message);
            MetricSendThread thread = new MetricSendThread(metricAddress, paramsMap);
            sendDataPool.execute(thread);
        } catch (Exception e) {
            ExceptionLogUtil.logExceptionInfo(logger, e);
        }
    }

    public static void sendData(int channel, String partner, String service, long totalTime, int hasData, int state) {
        try {
            MetricLog partnerLog = new MetricLog();
            partnerLog.setChannel(channel);
            partnerLog.setCallPartner(partner);
            partnerLog.setRequestType(service);
            partnerLog.setRequestTime(0);
            partnerLog.setResponseTime(totalTime);
            partnerLog.setHasData(hasData);
            partnerLog.setReturnState(state);
            String message = getMetricData(partnerLog, metricType_timer);
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("topic", metricTopic);
            paramsMap.put("message", message);
            MetricSendThread thread = new MetricSendThread(metricAddress, paramsMap);
            sendDataPool.execute(thread);
        } catch (Exception e) {
            ExceptionLogUtil.logExceptionInfo(logger, e);
        }
    }

    private static String getMetricData(MetricLog partnerLog, String metricType) {
        JSONObject jo = new JSONObject();
        jo.put("biz", channelMap.get(String.valueOf(partnerLog.getChannel())));
        jo.put("service", partnerLog.getRequestType());
        jo.put("host", getHostName());
        jo.put("metricName", getMetricName(partnerLog));
        jo.put("metricValue", partnerLog.getResponseTime() - partnerLog.getRequestTime());
        jo.put("metricType", metricType);
        jo.put("timestamp", System.currentTimeMillis());
        jo.put("remark", "platform");
        return jo.toString();
    }

    private static String getMetricName(MetricLog partnerLog) {
        StringBuilder name = new StringBuilder();
        name.append(channelMap.get(String.valueOf(partnerLog.getChannel())));
        name.append(".").append(partnerLog.getCallPartner());
        name.append(".").append(partnerLog.getRequestType());
        name.append(".").append(partnerLog.getReturnState() == 1 ? "success" : "fail");
        name.append(".").append(partnerLog.getHasData() == 1 ? "hasData" : "noData");
        return name.toString();
    }

    public static String getHostName() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            String address = addr.getHostName().toString();// 获得本机名称
            return address;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public static String getHostIp(){
    	InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress();
			return ip;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	return "";
    }
    
    /**
     * 
     * 获取本机ip地址
     * 
     * @return
     */
    public static List<String> getLocalIpAddress() {
        List<String> liIp = new ArrayList<String>(4);
        try {
            Enumeration<?> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<?> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        String address = ip.getHostAddress();
                        if (!"127.0.0.1".equals(address)) {
                            liIp.add(address);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ExceptionLogUtil.logExceptionInfo(logger, ex);
        }
        return liIp;
    }
}
