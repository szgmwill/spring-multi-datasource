package com.tianxun.framework.entity.vo;

public class BaseRequest {

	private String uid; // 联盟号
	
	private String requestType; //请求类别,监控,正常,爬虫
	
	private String source; // 请求来源 详见CommonConstant.SOURCE
	
	private String userSessionId; //客户端sessionId
	
	private String deviceId; // 客户端设备ID
	
	private String deviceType; // 设备类型
	
	private String userIp; // 终端用户端IP
	
	private String appVersion; // APP 版本号

	private String debugArgs; // 是否调试模式
	
	private String sign; //签名
	
	private String abTestType; // ab test type
	
	private boolean signPass; //本地签名验证结果
	
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUserSessionId() {
		return userSessionId;
	}

	public void setUserSessionId(String userSessionId) {
		this.userSessionId = userSessionId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getDebugArgs() {
		return debugArgs;
	}

	public void setDebugArgs(String debugArgs) {
		this.debugArgs = debugArgs;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public boolean isSignPass() {
		return signPass;
	}

	public void setSignPass(boolean signPass) {
		this.signPass = signPass;
	}

    public String getAbTestType() {
        return abTestType;
    }

    public void setAbTestType(String abTestType) {
        this.abTestType = abTestType;
    }

}
