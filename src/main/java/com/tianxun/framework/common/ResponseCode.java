package com.tianxun.framework.common;

/**
 * 对外接口响应CODE
 * 1XXX: 公共业务错误
 * 2XXX: flight 业务错误
 * 3XXX: hotel 业务错误
 * 4XXX: carhire 业务错误
 * 5XXX: travel group 业务错误
 * 6XXX: 安全控制错误
 * 9XXX: 系统级别错误
 * @author kevingu
 *
 */
public enum ResponseCode {

	SUCCESS(0, "成功"), 
	
	// 1xxx, 公共业务错误
	SESSION_KEY_EMPTY(1000, "sessionKey不能为空不能为空"),
	SOURCE_EMPTY(1001,"source不能为空"),
	UID_EMPTY(1002,"uid不能为空"),
	USER_IP_EMPTY(1003,"useIp不能为空"),
	USER_SESSION_ID_EMPTY(1004,"userSessionId不能为空"),
	DEVICE_ID_EMPTY(1005,"deviceId不能为空"),
	DEVICE_TYPE_EMPTY(1006,"deviceType不能为空"),
	SIGN_EMPTY(1007, "sign不能为空"),
	SIGN_ERROR(1008, "签名不正确"),
	SESSION_VALUE_EMPTY(1009, "缓存为空，请重新查询"),
	RESULT_EMPTY(1010,"返回结果为空"),
	CHECK_LIMIT_ERROR(1011, "访问太频繁，超过限制"),
	REQUEST_PARAM_ERROR(1012,"请求参数的格式错误"),
	VERSION_ERROR(1013, "接口版本号不正确"),
	POLICY_ID_EMPTY(1014,"政策ID不能为空"),
	POLICY_EMPTY(1015,"没有对应的政策"),
	NOT_INNER_IP(1016,"非内部IP访问"),
	// 2XXX, 机票业务错误信息
	ADULTS_EMPTY(2001, "adults必须大于0或小于9"),
	CABIN_CLASS_EMPTY(2002, "cabinClass不能为空"),
	FLIGHT_LEGS_EMPTY(2003, "flightLegs不能为空"),
	TRIP_TYPE_EMPTY(2004, "tripType不能为空"),
	
	DEPART_DATE_EMPTY(2005,"出发日期不能为空"),
    DEPART_DATE_EXPIRATION(2006,"出发日期已过期"),
    RETURN_DATE_EXPIRATION(2007,"返程日期无效"),
	ORDER_CERTIFICATE_CODE_EMPTY(2008,"乘客证件号码不能为空"),
	ORDER_CERTIFICATE_TYPE_EMPTY(2009,"乘客证件类型不能为空"),
	ORDER_PASSENGER_TYPE_ERROR(2010,"乘机人类型错误"),
	FLIGHT_ORDER_EMPTY(2011,"订单信息不能为空"),
    ORDER_PASSENGER_NAME_EMPTY(2012,"订单中乘机人姓或名不能为空"),
    ORDER_PASSENGER_TYPE_EMPTY(2013,"乘机人类型不能为空"),
	
    ORDER_ORIGIN_AIRPORT_EMPTY(2014,"出发机场不能为空或不存在"),
	ORDER_DESTINATION_AIRPORT_EMPTY(2015,"到达机场不能为空或不存在"),
	ORDER_SEQNO_EMPTY(2016,"航班序列号不能为空"),
	ORDER_FLIGHTNO_EMPTY(2017,"航班号不能为空"),
	ORDER_ADULT_PRICE_ERROR(2018,"成人的票面价或者燃油税不合法"),
    ORDER_CHILD_PRICE_ERROR(2019,"儿童的票面价或者燃油税不合法"),
    SOURCE_SITE_ID_ERROR(2020,"sourceSiteId不正确"),
    FARE_BASIS(2021,"运价基础不能为空"),
    VERIFY_SEGMENTS(2022,"航段信息不能为空"),
    VERIFY_MARKETAIRLINE(2023,"市场航空公司不正确"),
    VERIFY_OPERAIRLINE(2024,"承运航空公司不正确"),
    VERIFY_FLIGHTNUMBER(2025,"航班号不正确"),
    VERIFY_CABIN(2026,"舱位不正确"),
    OCCUPY_SEAT(2027, "疑是占座订单"),
    PNR_CRATE_ERROR(2028,"PNR生成失败"),
    PNR_PRICE_VALID_ERROR(2029,"占座成功但验价失败"),
    PRICE_CHANGE_ERROR(2030,"价格已变动，请重新预定"),
    SOURCE_SITEID_ERROR(2031,"sourceSiteId不能为空"),
    ORDER_GOCHECK_DATE_ERROR(2032,"订单去程日期不能为空"),
    ORDER_CREATE_ERROR(2033,"数据操作失败"),
    ORDER_PASSENGER_EXPIRE_ERROR(2034,"乘机人证件过期时间不能为空"),
    ORDER_LINMKMAN_PHONE_ERROR(2035,"联系人手机号不能为空"),
    ORDER_LINMKMAN_ERROR(2036,"联系人姓名不能为空"),
    ORDER_POLICY_ERROR(2037,"包机产品座位不够"),
    CACHE_DEPCITY_EMPTY(2038,"查询出发城市不能为空"),
    CACHE_DEPDATE_EMPTY(2039,"出发时间需要起始值"),
    CACHE_DEPDATE_ERROR(2041,"时间格式不正确"),
    CACHE_TRAVEL_EMPTY(2040,"旅行时长需要起始值"),
    BLACK_LIST(2041,"会员为黑名单"),
    FAIL_ORDER(2042,"下单失败"),
    ORDER_WAITING(2043,"等待座位确认"),
    VERIFY_CABIN_1(2045,"舱位验证超时"),
    VERIFY_CABIN_2(2045,"舱位有无效航班号"),
    VERIFY_CABIN_3(2045,"舱位无位"),
    VERIFY_CABIN_4(2045,"没有这样的价格"),
    VERIFY_CABIN_5(2049,"舱位不可出售"),
    FARE_IS_EMPTY(2050,"政策不存在"),
    AMADEUS_VALID_ERROR(2051,"政策不存在"),
    NOT_FIND_ITINERARY(2052,"Itinerary行程不存在"),
    
    SHARE_PARAM_ERROR(2053,"分享参数不完整"),
    SHARE_FAIL(2054,"分享失败"),
    OPERATION_TYPE_EMPTY(2055,"操作类型不能为空"),
    OPERATOR_EMPTY(2056,"操作着不能为空"),
    OPERATION_LOG_EMPTY(2057,"操作日志不能为空"),
    ORDER_STATUS_ERROR(2058,"订单状态不正确"),
    ORDER_CANCEL_FAIL(2059,"订单取消失败"),
    ORDER_OVER_TIME(2060,"订单已过期,不能申请退改签"),
    DOME_NO_RETURNDAY(2061,"国内不提供往返查询"),
    ORDER_PASSENGER_AGE_ERROR1(2062,"乘机人年龄超过最大值"),
    ORDER_PASSENGER_AGE_ERROR2(2063,"乘机人年龄低于最小值"),
    ORDER_PASSENGER_AGE_ERROR3(2064,"政策不适合儿童"),
    ORDER_PASSENGER_NATION_ERROR1(2065,"乘机人国籍不符合政策要求"),
    ORDER_PASSENGER_NATION_ERROR2(2066,"乘机人国籍已被政策排除"),
    TRIP_TYPE_ERROR(2005, "tripType不能为空"),
    VALID_PRICE_ERROR(2067,"amadeus返回价格无效"),
    VALID_FARE_ERROR(2068,"政策条数错误"),
    //3XXX 酒店业务错误
    
    //4XXX 租车业务错误
    SUGGEST_NAME_EMPTY(4001, "输入内容不能为空"),
    SUGGEST_ID_EMPTY(4002, "输入ID不能为空"),
    
    // 5XXXX helper 模块错误
    ATTENTION_ERROR_EMAIL_BE_USED(5001, "邮箱也被其他会员使用"),
    ATTENTION_ERROR_EMAIL_ERROR(5002, "邮箱格式不正确"),
	// 9XXX: 系统级别错误
	SYSTEM_ERROR(9999, "系统异常");

	private int value;

	private String label;

	private ResponseCode(int value, String label) {
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
