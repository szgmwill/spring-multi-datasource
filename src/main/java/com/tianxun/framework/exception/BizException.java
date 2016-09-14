package com.tianxun.framework.exception;
/**
 * Base Exception for Business
 * 只针对业务异常使用
 * @author WillZhang
 *
 */
public class BizException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7742547237828451230L;

	/**
	 * 异常信息
	 */
	protected String msg;

	/**
	 * 具体异常码
	 */
	protected int code;
	
	
	public BizException(int code, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.code = code;
		this.msg = String.format(msgFormat, args);
	}
	
	public BizException(int code, String message) {
		this.code = code;
		this.msg = message;
	}
	
	public BizException() {
		super();
	}

	public String getMsg() {
		return msg;
	}

	public int getCode() {
		return code;
	}

	/**
	 * 实例化异常
	 * 
	 * @param msgFormat
	 * @param args
	 * @return
	 */
	public BizException newInstance(String msgFormat, Object... args) {
		return new BizException(this.code, msgFormat, args);
	}

	public BizException(String message, Throwable cause) {
		super(message, cause);
	}

	public BizException(Throwable cause) {
		super(cause);
	}

	public BizException(String message) {
		super(message);
	}
}
