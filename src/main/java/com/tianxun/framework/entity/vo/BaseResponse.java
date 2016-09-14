package com.tianxun.framework.entity.vo;

import com.tianxun.framework.common.ResponseCode;

public class BaseResponse {

	private int code;
	
	private String message;
	
	public BaseResponse(){};
	
	public BaseResponse(ResponseCode responseCode) {
	    this.code = responseCode.getValue();
	    this.message = responseCode.getLabel();
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
