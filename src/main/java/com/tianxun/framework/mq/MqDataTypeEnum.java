package com.tianxun.framework.mq;

public enum MqDataTypeEnum {

    dailyMin("1"), dailyMinIntl("2"), AirportPair("3"), flightInfo("4"), emailLog("5"), smsLog("6"), redirect("7"), appException("8");

    private String value;

    private MqDataTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
