package com.tianxun.framework.quartz;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;

import com.google.common.base.Strings;

/**
 * 根据IP和机器名生成Quartz服务器的ID。
 * 
 * 这样做可以通过数据库表的状态实时监控当前运行任务落在哪台业务机器上执行,包括日志也可以打出相关信息
 * 
 * @author zhangweiui
 */
public class QuartzSchedulerInstanceIdGenerator implements InstanceIdGenerator {

    private static final Logger log = Logger.getLogger(QuartzSchedulerInstanceIdGenerator.class);

    @Override
    public String generateInstanceId() throws SchedulerException {
        String id = null;

        if (isWindows()) {
            id = getHostName();
        } else {
            id = getLocalNetWorkIp();
        }

        if (id == null) {
            if (isWindows()) {
                id = getLocalNetWorkIp();
            } else {
                id = getHostName();
            }
        }

        if (id == null) {
            id = "QRTZ-Sched-" + System.nanoTime();
        }

        log.info("Quartz Scheduler " + id + " is starting...");

        return id;
    }
    
    /**
     * 获取当前主机名称
     * @return
     */
    private String getHostName() {
        String hostName = null;
        try {
            List<String> lines = IOUtils.readLines(Runtime.getRuntime().exec(isWindows() ? "cmd.exe /c \"set computername\"" : "hostname").getInputStream());
            if (lines != null && !Strings.isNullOrEmpty(lines.get(0))) {
                hostName = lines.get(0);

                if (hostName.indexOf('=') > 0) {
                    hostName = hostName.substring(hostName.indexOf('=') + 1);
                }
            }
        } catch (Throwable ex) {
            log.error("获取机器名失败！", ex);
        }

        return hostName;
    }

    private boolean isWindows() {
//        return StringUtils.indexOfIgnoreCase(System.getProperty("os.name"), "Windows") >= 0;
    	if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
    		return true;
    	}
    	return false;
    }
    
    
    
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
    
}
