package com.tianxun.framework.utils.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.tianxun.framework.utils.ExceptionLogUtil;
/**
 * 
 * http请求工具类
 * 
 * @author michael.chen
 *
 */
public class HttpUtil {

	public static Logger logger = Logger.getLogger(HttpUtil.class);	
	
    public final static int GET = 0;
    public final static int POST_JSON = 1;
    public final static int POST = 2;
    public final static int PUT = 3;
    
    /**
     * 
     * 发送GET的http请求数据
     * 
     * @param reqUrl
     * @return
     */
    public static String callGetHttpUrl(String reqUrl) {        
        return HttpUtil.callHttpUrl(reqUrl, "GET", null, true, null, null, 30000, 120000, null);        
    }	
    
    /**
     * 
     * 发送GET的http请求数据
     * 
     * @param reqUrl
     * @param charset
     * @return
     */
    public static String callGetHttpUrl(String reqUrl, String charset) {        
        return HttpUtil.callHttpUrl(reqUrl, "GET", null, true, null, null, 30000, 120000, charset);        
    }       
    
    /**
     * 
     * 发送POST的http请求数据
     * 
     * @param reqUrl
     * @param data
     * @return
     */
    public static String callPostHttpUrl(String reqUrl, String data) {        
        return HttpUtil.callHttpUrl(reqUrl, "POST", null, true, null, data, 30000, 120000, null);        
    }       
    
    /**
     * 
     * 发送webservice的POST的http请求数据
     * 
     * @param reqUrl
     * @param soapAction
     * @param data
     * @return
     */
    public static String callSoapPostHttpUrl(String reqUrl, String soapAction, String data) {        
        return HttpUtil.callHttpUrl(reqUrl, "POST", null, true, soapAction, data, 30000, 120000, null);        
    }        
	
    /**
     * 
     * 发送POST的auth的ssl https请求数据
     * 
     * @param reqUrl
     * @param trustManager
     * @param auth
     * @param data
     * @return
     */
    public static String callPostAuthSSLHttpsUrl(String reqUrl, TrustManager[] trustManager, 
            String auth, String data) {        
        return HttpUtil.callSSLHttpsUrl(reqUrl, trustManager, "POST", auth, true, 
                null, data, 30000, 120000);        
    }  
    
    /**
     * 
     * 获取指定url的内容
     * 
     * @param reqUrl 请求的url
     * @param method GET or POST
     * @param authorization 认证
     * @param bKeepAlive 
     * @param soapAction
     * @param data 发送的数据
     * @param timeoutConn 
     * @param timeoutRead
     * @param charset
     * @return
     */
    public static String callHttpUrl(String reqUrl, 
            String method, String authorization, boolean bKeepAlive, 
            String soapAction, String data, 
            int timeoutConn, int timeoutRead, String charset) {      
        
        HttpURLConnection httpConn = null;
        String result = null;
        BufferedReader br = null;
        OutputStream os = null;
        try {   
            URL url = new URL(reqUrl);
            httpConn = (HttpURLConnection) url.openConnection();
            if(0 < timeoutConn) {
                httpConn.setConnectTimeout(timeoutConn); // 30000   
            }            
            if(0 < timeoutRead) {
                httpConn.setReadTimeout(timeoutRead); // 120000
            }
            httpConn.setRequestProperty("Content-Type",
                    "text/xml; charset=utf-8");
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            
            // Authorization
            if(null != authorization) {
                httpConn.setRequestProperty("Authorization", authorization);
            }
            
            // soapAction
            if(null != soapAction) {
                httpConn.setRequestProperty("SOAPAction", soapAction);
            }            
            
            // keep-alive
            if(bKeepAlive) {
                httpConn.setRequestProperty("Connection", "Keep-Alive");   
            }

            httpConn.setRequestMethod(method);
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(null == data ? false : true);
            httpConn.setDoInput(true);
            
            // 发送请求参数
            if(null != data) {                
                os = httpConn.getOutputStream();
                os.write(data.getBytes("utf-8"));
                os.flush();
            }            
            
            // 接收返回信息            
            InputStream in = httpConn.getInputStream();
            String contentEncoding = httpConn.getHeaderField("Content-Encoding");
            if(null != contentEncoding && contentEncoding.toLowerCase().contains("gzip")) {
                in = new GZIPInputStream(in);    
            }           
            br = new BufferedReader(new InputStreamReader(in, null == charset ? "utf-8" : charset));
            
            // 获取响应字符串
            StringBuilder xmlBuf = new StringBuilder();
            String str = null;
            while (null != (str = br.readLine())) {
                xmlBuf.append(str);
            }             
            result = xmlBuf.toString();
        } catch (Exception ex) {
            ExceptionLogUtil.logExceptionInfo(logger, ex);
        } finally {
            try {
                if(null!=os) {  os.close(); }
                if(null!=br) {  br.close(); }   
            } catch(Exception e) {
                ExceptionLogUtil.logExceptionInfo(logger, e);
            }
        }
        if(StringUtils.isEmpty(result)) {
            logger.error("请求接口返回为空 , 地址: " + reqUrl + ", 参数: " + data);
        }
        return result;
    }	
    /**
     * 
     * 发送POST的http请求数据
     * 
     * @param reqUrl
     * @param data
     * @param  contentType
     * @return
     */
    public static String callPostHttpUrl(String reqUrl, String data, String contentType) {
        return HttpUtil.callHttpUrl(reqUrl, "POST", null, true, null, data, 30000, 120000, null, contentType);
    }

    /**
     * 
     * 获取指定url的内容
     * 
     * @param reqUrl 请求的url
     * @param method GET or POST
     * @param authorization 认证
     * @param bKeepAlive 
     * @param soapAction
     * @param data 发送的数据
     * @param timeoutConn 
     * @param timeoutRead
     * @param charset
     * @param contentType
     * @return
     */
    public static String callHttpUrl(String reqUrl, 
            String method, String authorization, boolean bKeepAlive, 
            String soapAction, String data, 
            int timeoutConn, int timeoutRead, String charset, String contentType) {      
        
        HttpURLConnection httpConn = null;
        String result = null;
        BufferedReader br = null;
        OutputStream os = null;
        try {   
            URL url = new URL(reqUrl);
            httpConn = (HttpURLConnection) url.openConnection();
            if(0 < timeoutConn) {
                httpConn.setConnectTimeout(timeoutConn); // 30000   
            }            
            if(0 < timeoutRead) {
                httpConn.setReadTimeout(timeoutRead); // 120000
            }
            httpConn.setRequestProperty("Content-Type",contentType);
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            
            // Authorization
            if(null != authorization) {
                httpConn.setRequestProperty("Authorization", authorization);
            }
            
            // soapAction
            if(null != soapAction) {
                httpConn.setRequestProperty("SOAPAction", soapAction);
            }            
            
            // keep-alive
            if(bKeepAlive) {
                httpConn.setRequestProperty("Connection", "Keep-Alive");   
            }

            httpConn.setRequestMethod(method);
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(null == data ? false : true);
            httpConn.setDoInput(true);
            
            // 发送请求参数
            if(null != data) {                
                os = httpConn.getOutputStream();
                os.write(data.getBytes("utf-8"));
                os.flush();
            }            
            
            // 接收返回信息            
            InputStream in = httpConn.getInputStream();
            String contentEncoding = httpConn.getHeaderField("Content-Encoding");
            if(null != contentEncoding && contentEncoding.toLowerCase().contains("gzip")) {
                in = new GZIPInputStream(in);    
            }           
            br = new BufferedReader(new InputStreamReader(in, null == charset ? "utf-8" : charset));
            
            // 获取响应字符串
            StringBuilder xmlBuf = new StringBuilder();
            String str = null;
            while (null != (str = br.readLine())) {
                xmlBuf.append(str);
            }             
            result = xmlBuf.toString();
        } catch (Exception ex) {
            ExceptionLogUtil.logExceptionInfo(logger, ex);
        } finally {
            try {
                if(null!=os) {  os.close(); }
                if(null!=br) {  br.close(); }   
            } catch(Exception e) {
                ExceptionLogUtil.logExceptionInfo(logger, e);
            }
        }
        if(StringUtils.isEmpty(result)) {
            logger.error("请求接口返回为空 , 地址: " + reqUrl + ", 参数: " + data);
        }
        return result;
    }	
    
    
    /**
     * 
     * 获取指定url的https内容
     * 
     * @param reqUrl 请求的url
     * @param trustManager 管理证书
     * @param method GET or POST
     * @param authorization 认证
     * @param bKeepAlive 
     * @param soapAction
     * @param data 发送的数据
     * @param timeoutConn 
     * @param timeoutRead
     * @return
     */
    public static String callSSLHttpsUrl(String reqUrl, TrustManager[] trustManager,  
            String method, String authorization, boolean bKeepAlive, 
            String soapAction, String data, 
            int timeoutConn, int timeoutRead) {      
        
        HttpsURLConnection httpConn = null;
        String result = null;
        BufferedReader br = null;
        OutputStream os = null;
        try {   
            
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化          
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, trustManager, new java.security.SecureRandom());

            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();            
            
            URL url = new URL(reqUrl);
            httpConn = (HttpsURLConnection) url.openConnection();
            if(0 < timeoutConn) {
                httpConn.setConnectTimeout(timeoutConn); // 30000   
            }            
            if(0 < timeoutRead) {
                httpConn.setReadTimeout(timeoutRead); // 120000
            }
            httpConn.setRequestProperty("Content-Type",
                    "text/xml; charset=utf-8");
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            
            // Authorization
            if(null != authorization) {
                httpConn.setRequestProperty("Authorization", authorization);
            }
            
            // soapAction
            if(null != soapAction) {
                httpConn.setRequestProperty("SOAPAction", soapAction);
            }            
            
            // keep-alive
            if(bKeepAlive) {
                httpConn.setRequestProperty("Connection", "Keep-Alive");   
            }
            
                                    
            httpConn.setRequestMethod(method);
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(null == data ? false : true);
            httpConn.setDoInput(true);
            httpConn.setSSLSocketFactory(ssf);
            
            // 发送请求参数
            if(null != data) {                
                os = httpConn.getOutputStream();
                os.write(data.getBytes("utf-8"));
                os.flush();
            }            
            
            // 接收返回信息            
            InputStream in = httpConn.getInputStream();
            String contentEncoding = httpConn.getHeaderField("Content-Encoding");
            if(null != contentEncoding && contentEncoding.toLowerCase().contains("gzip")) {
                in = new GZIPInputStream(in);    
            }           
            br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            
            // 获取响应字符串
            StringBuilder xmlBuf = new StringBuilder();
            String str = null;
            while (null != (str = br.readLine())) {
                xmlBuf.append(str);
            }             
            result = xmlBuf.toString();
        } catch (Exception ex) {
            ExceptionLogUtil.logExceptionInfo(logger, ex);
        } finally {
            try {
                if(null!=os) {  os.close(); }
                if(null!=br) {  br.close(); }   
            } catch(Exception e) {
                ExceptionLogUtil.logExceptionInfo(logger, e);
            }
        }
        return result;
    }       
	
    /**
     * Post请求 
     * @param reqUrl
     * @param data
     * @return
     */
    public static String callHttpPost(String reqUrl,  String data ) {      
        
        HttpURLConnection httpConn = null;
        String result = null;
        BufferedReader br = null;
        OutputStream os = null;
        try {   
            URL url = new URL(reqUrl);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setConnectTimeout(30000);   
            httpConn.setReadTimeout(30000); 
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpConn.setRequestProperty("Connection", "Keep-Alive");   
            
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            
            // 发送请求参数
            if(null != data) {                
                os = httpConn.getOutputStream();
                os.write(data.getBytes("utf-8"));
                os.flush();
            }            
            
            // 接收返回信息            
            InputStream in = httpConn.getInputStream();
            String contentEncoding = httpConn.getHeaderField("Content-Encoding");
            if(null != contentEncoding && contentEncoding.toLowerCase().contains("gzip")) {
                in = new GZIPInputStream(in);    
            }           
            br = new BufferedReader(new InputStreamReader(in, "utf-8"));
            // 获取响应字符串
            StringBuilder xmlBuf = new StringBuilder();
            String str = null;
            while (null != (str = br.readLine())) {
                xmlBuf.append(str);
            }             
            result = xmlBuf.toString();
        } catch (Exception ex) {
            ExceptionLogUtil.logExceptionInfo(logger, ex);
        } finally {
            try {
                if(null!=os) {  os.close(); }
                if(null!=br) {  br.close(); }   
            } catch(Exception e) {
                ExceptionLogUtil.logExceptionInfo(logger, e);
            }
        }
        return result;
    }   
    /**
     * 
     * 获取指定url的https内容
     * 
     * @param reqUrl
     *            请求的url
     * @param method
     *            GET or POST
     * @param authorization
     *            认证
     * @param bKeepAlive
     * @param soapAction
     * @param data
     *            发送的数据
     * @param timeoutConn
     * @param timeoutRead
     * @return
     */
    public static String callSSLHttpsUrl(String reqUrl, String method, String authorization, boolean bKeepAlive, String soapAction, String data,
            int timeoutConn, int timeoutRead) {

        HttpsURLConnection httpConn = null;
        String result = null;
        BufferedReader br = null;
        OutputStream os = null;
        try {

            URL url = new URL(reqUrl);
            httpConn = (HttpsURLConnection) url.openConnection();

            httpConn.setHostnameVerifier(Holder.hv);

            if (0 < timeoutConn) {
                httpConn.setConnectTimeout(timeoutConn); // 30000
            }
            if (0 < timeoutRead) {
                httpConn.setReadTimeout(timeoutRead); // 120000
            }
            if ("POST".equals(method)) {
                httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            } else {
                httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            }
            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            if (data != null) {
                httpConn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            }

            // Authorization
            if (null != authorization) {
                httpConn.setRequestProperty("Authorization", authorization);
            }

            // soapAction
            if (null != soapAction) {
                httpConn.setRequestProperty("SOAPAction", soapAction);
            }

            // keep-alive
            if (bKeepAlive) {
                httpConn.setRequestProperty("Connection", "Keep-Alive");
            }

            httpConn.setRequestMethod(method);
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(null == data ? false : true);
            httpConn.setDoInput(true);
            httpConn.setSSLSocketFactory(Holder.ssf);

            // 发送请求参数
            if (null != data) {
                os = httpConn.getOutputStream();
                os.write(data.getBytes("utf-8"));
                os.flush();
            }

            // 接收返回信息
            InputStream in = httpConn.getInputStream();
            String contentEncoding = httpConn.getHeaderField("Content-Encoding");
            if (null != contentEncoding && contentEncoding.toLowerCase().contains("gzip")) {
                in = new GZIPInputStream(in);
            }
            br = new BufferedReader(new InputStreamReader(in, "utf-8"));

            // 获取响应字符串
            StringBuilder xmlBuf = new StringBuilder();
            String str = null;
            while (null != (str = br.readLine())) {
                xmlBuf.append(str);
            }
            result = xmlBuf.toString();
        } catch (Exception ex) {
            ExceptionLogUtil.logExceptionInfo(logger, ex);
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != br) {
                    br.close();
                }
            } catch (Exception e) {
                ExceptionLogUtil.logExceptionInfo(logger, e);
            }
        }
        return result;
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

            Enumeration<?> allNetInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
                        .nextElement();                
                Enumeration<?> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address) {
                        String address = ip.getHostAddress();
                        if(!"127.0.0.1".equals(address)) {
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
    
    private static class Holder {
        static SSLSocketFactory ssf = null;

        static TrustAnyHostnameVerifier hv = new TrustAnyHostnameVerifier();

        static class TrustAnyHostnameVerifier implements HostnameVerifier {
            public boolean verify(String hostname, SSLSession session) {
                // 直接返回true
                return true;
            }
        }

        static {
            try {

                X509TrustManager tm = new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1) throws CertificateException {
                    }
                };

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[] { tm }, new java.security.SecureRandom());
                ssf = sslContext.getSocketFactory();
            } catch (Exception e) {
                ExceptionLogUtil.logExceptionInfo(logger, e);
            }
        }
    }
    
}
