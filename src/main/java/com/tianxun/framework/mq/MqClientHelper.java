package com.tianxun.framework.mq;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.tianxun.framework.utils.json.GsonUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class MqClientHelper {

    public Logger logger = Logger.getLogger(MqClientHelper.class);

    public ExecutorService threadPool;

    private JedisPool pool;

    public MqClientHelper(String address, String port, int poolSize) {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxActive(poolSize);
            config.setMaxWait(5000L);
            config.setMaxIdle(20);
            pool = new JedisPool(config, address, Integer.valueOf(port), 5000);
            threadPool = Executors.newFixedThreadPool(50);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void sendMessage(String queueName, String message) {
        Jedis redis = null;
        try {
            redis = pool.getResource();
            redis.lpush(queueName, message);
        } catch (Exception e) {
            if (redis != null) {
                pool.returnBrokenResource(redis);
            }
            logger.error(e.getMessage(), e);
        } finally {
            if (redis != null) {
                pool.returnResource(redis);
            }
        }
    }

    class MqSendThread implements Runnable {

        private String queueName;

        private String message;

        public MqSendThread(String queueName, String message) {
            this.queueName = queueName;
            this.message = message;
        }

        @Override
        public void run() {
            try {
                sendMessage(queueName, message);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

    }

    /**
     * 发送数据到指定的Q
     * 
     * @param data
     */
    public void sendDataToMQ(String queueName, String data) {
        MqSendThread thread = new MqSendThread(queueName, data);
        threadPool.execute(thread);
    }

    /**
     * 发送数据到指定的Q
     * 
     * @param <T>
     * 
     * @param data
     */
    public <T> void sendDataToMQ(String queueName, MqDataVo<T> data) {
        MqSendThread thread = new MqSendThread(queueName, GsonUtils.toJson(data));
        threadPool.execute(thread);
    }

    /**
     * spring加载初始化时执行
     */
    public void initMethod() {
        logger.info("initMethod ===");
    }

    /**
     * 程序退出,spring销毁该bean前调用
     */
    public void destroy() {
        logger.info("===start to shutdown memcachedClient===");
        try {
            if (threadPool != null) {
                threadPool.shutdown();
            }
            if (pool != null) {
                pool.destroy();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        logger.info("===successed shutdown memcachedClient===");
    }

}
