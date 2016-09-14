package com.tianxun.framework.utils;

/**
 * 校验请求限制辅助类
 * @author Jack.liu
 *
 */
public class State {
	private long endTime;

	private int count;

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
