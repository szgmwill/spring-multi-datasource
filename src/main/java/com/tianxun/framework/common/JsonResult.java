package com.tianxun.framework.common;

import java.io.Serializable;
/**
 * 通用Json返回结果
 * 接口的格式最好能标准化,所以统一按照以下json对象来定义系统间交互的数据格式
 * @author willzhang
 *
 */
public class JsonResult implements Serializable {
	
	private static final long serialVersionUID = 4088189846234880089L;
	public static final JsonResult EMPTY = new JsonResult();
	
	/**
	 * 返回的错误码:0-代表成功 , 其它代表失败,具体要业务自己确定
	 */
	private int code = 0;
	
	/**
	 * 返回的错误信息:根据业务自己定义
	 */
	private String msg = "成功";
	
	/**
	 * 返回的具体业务内容,通用的对象,可以是一个复杂的对象结构
	 */
	private Object data;
	
	/**
	 * 系统信息,当异常发生时,可以在这里输出一定量的日志,方便调试接口
	 * 接口调用方不会使用该字段
	 */
	private String sysMsg;

	public JsonResult() {
	}

	public JsonResult(Object data) {
		this.data = data;
	}

	public JsonResult(int code, String msg) {
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static JsonResult createErrorInstance(int code, String msg) {
		JsonResult result = new JsonResult(code, msg);
		return result;
	}

	public static JsonResult createInstance(Object object) {
		JsonResult result = new JsonResult();
		result.data = object;
		return result;
	}

	public String getSysMsg() {
		return sysMsg;
	}

	public void setSysMsg(String sysMsg) {
		this.sysMsg = sysMsg;
	}

	
}
