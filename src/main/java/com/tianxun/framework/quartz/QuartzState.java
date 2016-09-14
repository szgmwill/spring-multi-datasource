package com.tianxun.framework.quartz;

public enum QuartzState {
	
	/**
	 * 等待中
	 */
	READY(0),
	
	/**
	 * 运行中
	 */
	RUNNING(1);

	private QuartzState(int value) {
		this.value = value;
	}

	private int value;
	
	public int value() {
		return value;
	}
}
