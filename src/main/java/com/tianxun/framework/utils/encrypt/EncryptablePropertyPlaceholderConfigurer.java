package com.tianxun.framework.utils.encrypt;

import java.util.Enumeration;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
/**
 * 对加载的properties配置文件中出现的密文进行自动解密
 * 只对key中包含某个特定说明的key进行解密:比如：
 * 加解密的Key暂时hard code
 * 
 * @author WillZhang
 *
 */
public class EncryptablePropertyPlaceholderConfigurer extends
		PropertyPlaceholderConfigurer {
	//加解密的key,安全一点的做法是放到某个地方,这里通过反编译是可以得到的
	private static final String encryp_key = "LmMGStGtOpTiAnxufdfdf54EQ";  
	
	//key的配置规则,即出现"pattern"将进行解密,这个可以自定义
	private static final String pattern_all = ".cipher";
	private static final String pattern_pw = ".password";
	
	
	@Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)  
        throws BeansException {  
            try {
            	//遍历所有的key,看下是否有需要解密的
            	Enumeration<?> keyenu = props.propertyNames();
                while(keyenu.hasMoreElements()) {
                    String key = (String) keyenu.nextElement();
                    if(key != null && (key.toLowerCase().contains(pattern_all) || key.toLowerCase().contains(pattern_pw))) {
                    	String ciphertext = props.getProperty(key);
                    	if(ciphertext != null) {
                    		//自动解密后将结果存回去,这样使用方不必改变即可读取
                        	String sourceValue = EncryptUtil.decrypt(ciphertext, encryp_key);
                        	if(sourceValue != null) {
                        		props.setProperty(key, sourceValue);
                        	}
                    	}
                    }
                }
                super.processProperties(beanFactory, props);  
            } catch (Exception e) {  
                e.printStackTrace();  
                throw new BeanInitializationException(e.getMessage());  
            }  
        }  
}
