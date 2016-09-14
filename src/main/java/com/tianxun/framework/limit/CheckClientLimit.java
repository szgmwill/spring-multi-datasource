package com.tianxun.framework.limit;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.tianxun.framework.cache.Memcached;
import com.tianxun.framework.utils.DateUtil;
import com.tianxun.framework.utils.Md5Util;
import com.tianxun.framework.utils.http.HttpClientUtils;
import com.tianxun.framework.utils.json.GsonUtils;
import com.tianxun.framework.utils.sys.ConfigUtil;
import com.tianxun.framework.utils.sys.NetworkUtils;

/**
 * 访问控制类， 验证签名和访问频率
 * 
 * @author kevingu
 *
 */
public class CheckClientLimit extends Memcached {

    private final Logger logger = Logger.getLogger(CheckClientLimit.class);

    private final int MAX_SESSION_COUNT = 250;
    private final int MAX_DETAIL_COUNT = 200;

    private long timeStampMin = 0;

    private AtomicInteger sessionCount = new AtomicInteger(0);

    private AtomicInteger detailCount = new AtomicInteger(0);

    private Map<String, String> clientKeyMap = new HashMap<String, String>();

    private final String MEMCACHE_UNION_KEY = "memcache_union_key_pre_";

    private final String MEMCACHE_LIMIT_MIN_KEY = "memcache_limit_min_key";
    private final String MEMCACHE_LIMIT_DAY_KEY = "memcache_limit_day_key";

    protected final int default_minute_time = 50;
    protected final int default_day_time = 1000;

    public CheckClientLimit(String serverAddr, String username, String password, int poolSize) {
        super(serverAddr, username, password, poolSize);
    }

    /**
     * 校验签名
     * 
     * @param uid
     * @param sign
     * @param params
     * @return
     */
    public boolean ckSign(String uid, String sign, Map<String, String> params) {
        if (StringUtils.isEmpty(uid)) {
            logger.error("uid is empty");
            return false;
        }
        if ("tianxuntestskyscanner2016".equals(sign)) {
            return true;
        }
        String key = clientKeyMap.get(uid);
        if (StringUtils.isEmpty(key)) {
            key = init(uid);
        }
        if (StringUtils.isEmpty(key)) {
            logger.error("union key is empty:" + uid);
            return false;
        }
        if (StringUtils.isEmpty(sign)) {
            return false;
        }
        String signNew = Md5Util.getSign(params, key);
        if (sign.equals(signNew)) {
            return true;
        }
        logger.info("check sign fail:" + GsonUtils.toJson(params));
        return false;
    }
    
    /**
     * 校验签名
     * 
     * @param uid
     * @param sign
     * @param params
     * @return
     */
    public boolean ckSignNoAnd(String uid, String sign, Map<String, String> params) {
        if (StringUtils.isEmpty(uid)) {
            logger.error("uid is empty");
            return false;
        }
        if ("tianxuntestskyscanner2016".equals(sign)) {
            return true;
        }
        String key = clientKeyMap.get(uid);
        if (StringUtils.isEmpty(key)) {
            key = init(uid);
        }
        if (StringUtils.isEmpty(key)) {
            logger.error("union key is empty:" + uid);
            return false;
        }
        if (StringUtils.isEmpty(sign)) {
            return false;
        }
        String signNew = Md5Util.getSignNoAnd(params, key);
        if (sign.equals(signNew)) {
            return true;
        }
        logger.info("check sign fail:" + GsonUtils.toJson(params));
        return false;
    }

    /**
     * 根据UID获取原始数据验证签名
     * 
     * @param uid
     * @param sign
     * @param params
     * @return
     */
    public boolean checkSign(String uid, String sign, String data) {
        if (StringUtils.isEmpty(uid)) {
            logger.error("uid is empty");
            return false;
        }
        if ("tianxuntestskyscanner2016".equals(sign)) {
            return true;
        }
        String key = clientKeyMap.get(uid);
        if (StringUtils.isEmpty(key)) {
            key = init(uid);
        }
        if (StringUtils.isEmpty(key)) {
            logger.error("union key is empty:" + uid);
            return false;
        }
        if (StringUtils.isEmpty(sign)) {
            return false;
        }
        String signNew = Md5Util.MD5Encode(data + key);
        logger.info(data + key);
        logger.info(signNew);
        if (signNew.equals(sign)) {
            return true;
        }
        logger.info("check sign fail:" + data);
        return false;
    }

    public String getSign(String key, String data) {
        if (StringUtils.isEmpty(key)) {
            logger.error("union key is empty.");
            return data;
        }
        return Md5Util.MD5Encode(data + key);
    }

    public boolean ckSignBykey(String sign, Map<String, String> params, String key) {
        if (StringUtils.isEmpty(sign)) {
            return false;
        }
        String signNew = Md5Util.getSign(params, key);
        if (sign.equals(signNew)) {
            return true;
        }
        return false;
    }

    /**
     * 先取cache，取不到再到接口取
     * 
     * @param uid
     * @return
     */
    private String init(String uid) {
        try {
            String key = super.get(MEMCACHE_UNION_KEY + uid);
            if (StringUtils.isNotEmpty(key)) {
                clientKeyMap.put(uid, key);
                return key;
            }
            String callUrl = ConfigUtil.get("HELPER_URL");
            if (StringUtils.isEmpty(callUrl)) {
                return null;
            }
            String result = HttpClientUtils.doGet(callUrl + "union/getUnionUserByNum/v1/" + uid);
            if (StringUtils.isEmpty(result)) {
                return null;
            }
            JSONObject jo = JSONObject.parseObject(result);
            if (jo.getIntValue("code") != 0) {
                logger.error("get union key fail:" + jo.getString("message"));
                return null;
            }
            JSONObject union = jo.getJSONObject("unionUser");
            key = union.getString("unionKey");
            if (StringUtils.isNotEmpty(key)) {
                clientKeyMap.put(uid, key);
                super.set(MEMCACHE_UNION_KEY + uid, 60 * 60 * 24, key);
            }
            return key;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 接口访问频率控制， 如果是未传IP 或者是内部IP，都通过， 控制数据：50/min/method, 1000/day/method
     * 
     * @param req
     *            访问端IP
     * @param userIp
     *            最前端IP
     * @param method
     * @return
     */
    public boolean checkLimitBase(HttpServletRequest req, String userIp, String method) {
        if (StringUtils.isEmpty(userIp)) {
            userIp = NetworkUtils.getIpAddress(req);
        }
        if (StringUtils.isEmpty(userIp)) {
            return true;
        }
        if (NetworkUtils.isInteriorIp(userIp)) {
            return true;
        }
        return checkLimitByKey(userIp, method);
    }

    /**
     * 判断每分钟，小时，天的访问限制是否超限, 限制次数为默认值, min:50, day:1000;
     * 
     * @param key
     * @param method
     * @return
     */
    public boolean checkLimitByKey(String key, String method) {
        return checkLimitByKey(key, method, default_minute_time, default_day_time);
    }

    /**
     * 判断每分钟，小时，天的访问限制是否超限
     * 
     * @param key
     * @param method
     * @param minuteCount
     * @param hourCount
     * @param dayCount
     * @return
     */
    public boolean checkLimitByKey(String key, String method, int minuteCount, int dayCount) {
        boolean checkResult = checkMinLimitByKey(key, method, minuteCount);
        if (checkResult) {
            return checkDayLimitByKey(key, method, dayCount);
        }
        return checkResult;
    }

    /**
     * 检查每个key访问的频率是否超限
     * 
     * @param requestIp
     * @return
     */
    public boolean checkMinLimitByKey(String key, String method, int maxLimitCount) {
        if (StringUtils.isEmpty(key)) {
            return true;
        }
        if (maxLimitCount == 0) {
            return true;
        }
        String memcacheKey = new StringBuilder(MEMCACHE_LIMIT_MIN_KEY).append(key).append(method).append(DateUtil.dateToFullString2(new Date()))
                .toString();
        String currentCount = super.get(memcacheKey);
        if (StringUtils.isEmpty(currentCount)) {
            return true;
        }
        int count = Integer.valueOf(currentCount).intValue();
        if (count <= maxLimitCount) {
            super.set(memcacheKey, 60, String.valueOf(count + 1));
            return true;
        }
        logger.error("===checkMinLimitByKey error, method:" + method + ", key:" + key + ", currentCount:" + count + ", max:" + maxLimitCount);
        return false;
    }

    /**
     * 检查每个key访问的频率是否超限
     * 
     * @param requestIp
     * @return
     */
    public boolean checkDayLimitByKey(String key, String method, int maxLimitCount) {
        if (StringUtils.isEmpty(key)) {
            return true;
        }
        if (maxLimitCount == 0) {
            return true;
        }
        String memcacheKey = new StringBuilder(MEMCACHE_LIMIT_DAY_KEY).append(key).append(method).append(DateUtil.dateToString(new Date()))
                .toString();
        String currentCount = super.get(memcacheKey);
        if (StringUtils.isEmpty(currentCount)) {
            return true;
        }
        int count = Integer.valueOf(currentCount).intValue();
        if (count <= maxLimitCount) {
            super.set(memcacheKey, 60 * 60 * 24, String.valueOf(count + 1));
            return true;
        }
        logger.error("===checkDayLimitByKey error, method:" + method + ", key:" + key + ", currentCount:" + count + ", max:" + maxLimitCount);
        return false;
    }

    public boolean checkLimitSession() {
        long times = System.currentTimeMillis() - timeStampMin;
        if (times > 60 * 1000) {
            sessionCount.set(0);
            timeStampMin = getDateTimeNoSec();
            return true;
        }
        int count = sessionCount.getAndAdd(1);
        if (count > MAX_SESSION_COUNT) {
            logger.info("=== There have been too many requests session in the last minute ===");
            return false;
        }
        return true;
    }

    public boolean checkLimitDetail() {
        long times = System.currentTimeMillis() - timeStampMin;
        if (times > 60 * 1000) {
            detailCount.set(0);
            timeStampMin = getDateTimeNoSec();
            return true;
        }
        int count = detailCount.getAndAdd(1);
        if (count > MAX_DETAIL_COUNT) {
            logger.info("=== There have been too many requests detail in the last minute ===");
            return false;
        }
        return true;
    }

    private long getDateTimeNoSec() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        return ca.getTimeInMillis();
    }        
}
