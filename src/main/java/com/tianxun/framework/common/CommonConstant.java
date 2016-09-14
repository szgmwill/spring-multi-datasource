package com.tianxun.framework.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共常量类
 * 
 * @author kevingu
 *
 */
public class CommonConstant {

    /**
     * @ClassName: RequestType
     * @Description: TODO(这里用一句话描述这个类的作用)
     * @author Charles.guo
     * @date 2016年4月25日 上午10:56:10
     * 
     */
    public enum RequestType {
        NORMAL("normal", "正常"), MONITOR("monitor", "监控"), SPIDER("spider", "爬虫"), ALERT("alert", "价格提醒"), OTHERS("others", "其他");

        private String value;

        private String label;

        private RequestType(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        public List<RequestType> getList() {
            return Arrays.asList(RequestType.values());
        }

        public Map<String, String> getMaps() {
            Map<String, String> map = new HashMap<String, String>();
            for (RequestType object : RequestType.values()) {
                map.put(object.getValue(), object.getLabel());
            }
            return map;
        }
    }

    /**
     * 请求来源
     * 
     * @author kevingu
     *
     */
    public enum SOURCE {
        WEB_SITE(1, "webSite"), ANDROID_APP(21, "AndroidApp"), IOS_APP(22, "IOSApp"), ANDROID_WAP(31, "AndroidWAP"), IOS_WAP(32, "IOSWAP");

        private Integer value;
        private String label;

        private SOURCE(Integer value, String label) {
            this.value = value;
            this.label = label;
        }

        public Integer getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        public List<SOURCE> getList() {
            return Arrays.asList(SOURCE.values());
        }

        public Map<Integer, String> getMaps() {
            Map<Integer, String> map = new HashMap<Integer, String>();
            for (SOURCE object : SOURCE.values()) {
                map.put(object.getValue(), object.getLabel());
            }
            return map;
        }
    }

    /**
     * 设备类型
     * 
     * @author kevingu
     *
     */
    public enum DEVICT_TYPE {
        DESKTOP(1, "desktop"), MOBILE(2, "mobile"), TABLET(3, "tablet"), WATCH(4, "watch"), TV(4, "TV"), OTHER(9, "other");

        private Integer value;
        private String label;

        private DEVICT_TYPE(Integer value, String label) {
            this.value = value;
            this.label = label;
        }

        public Integer getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        public List<DEVICT_TYPE> getList() {
            return Arrays.asList(DEVICT_TYPE.values());
        }

        public Map<Integer, String> getMaps() {
            Map<Integer, String> map = new HashMap<Integer, String>();
            for (DEVICT_TYPE object : DEVICT_TYPE.values()) {
                map.put(object.getValue(), object.getLabel());
            }
            return map;
        }
    }

    /**
     * 查询状态
     * 
     * @author kevingu
     *
     */
    public enum SEARCH_STATUS {
        COMPLETED("completed"), PENDING("pending"), CURRENT("current"), FAIL("fail"), EXPIRE("expire");

        private String value;

        private SEARCH_STATUS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    /**
     * boolean value
     * 
     * @author kevingu
     *
     */
    public enum BOOLEAN_VALUE {
        TRUE(1, "是"), FALSE(0, "否");

        private int value;
        private String label;

        private BOOLEAN_VALUE(int value, String label) {
            this.value = value;
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

    }

    public enum AB_TEST_TYPE {
        A("A"), B("B");

        private String value;

        private AB_TEST_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    
    /**
     * 跳转类型
     * @author kevingu
     *
     */
    public enum REDIRECT_TYPE {
        DEEPLINK("DpL"), FaB("FaB"), AsB("AsB");

        private String value;

        private REDIRECT_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
