package com.tianxun.framework.common;
/**
 * 定义全局的错误码和错误信息
 * 标准的错误码定义必须是严格的,比如说固定多不位,每位代表的意义等
 * @author WillZhang
 *
 */
public enum ErrorCode {
	
	SERVER_SYS_ERROR(-99999, "系统出小差了 ..."),
	
	RESULT_EMPTY_ERROR(-2, "查询结果为空");
	
	private int code;
	
	private String msg;
	
	private ErrorCode (int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
