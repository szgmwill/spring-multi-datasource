package com.tianxun.framework.utils.sys;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;


/**
 * 网络帮助类
 * 
 * @author willzhang
 *
 */
public class NetworkUtils {
	/**
	 * 当前本机的网络IP
	 */
	private static String localIp;

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
	
	public final static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");                
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");                
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");                
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");                
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();                
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
    
    /**
     * 验证IP是否是内容IP
     * @param request
     * @return
     */
    public static boolean isInteriorIp(HttpServletRequest request) {
        String ip = getIpAddress(request);
        if(StringUtils.isEmpty(ip)) {            
            return false;
        }
        return isInteriorIp(ip);
    }
    
    /**
     * 判断IP是否在白名单中
     * 
     * @param request
     * @return
     */
    public static boolean isInteriorIp(String requestIp) {
        if (StringUtils.isEmpty(requestIp)) {
            return false;
        }
        String trustIps = ConfigUtil.get("TRUST_IPS");
        if(StringUtils.isEmpty(trustIps)) {
            //如果没有设置IP，则通过
            return true;
        }
        if(trustIps.indexOf(requestIp) > -1) {
            return true;
        }        
        return false;
    }
}
