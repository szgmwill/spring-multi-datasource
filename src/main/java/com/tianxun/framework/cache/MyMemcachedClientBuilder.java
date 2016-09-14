package com.tianxun.framework.cache;

import org.apache.commons.lang3.StringUtils;

import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class MyMemcachedClientBuilder extends XMemcachedClientBuilder {

    public MyMemcachedClientBuilder(String serverAddr, String username, String password, int poolSize) {
        super(AddrUtil.getAddresses(serverAddr));

        //云服务器要密码认证, 测试环境不需要
        if (StringUtils.isNotBlank(username)){
            addAuthInfo(AddrUtil.getOneAddress(serverAddr), AuthInfo.plain(username, password));    
        }
        
        setSessionLocator(new KetamaMemcachedSessionLocator());
        //连接池,默认是1
        if(poolSize > 0) {
            setConnectionPoolSize(poolSize);
        }
        //需要认证的情况下,只能用BinaryCommandFactory
        setCommandFactory(new BinaryCommandFactory());
        
    }
}
