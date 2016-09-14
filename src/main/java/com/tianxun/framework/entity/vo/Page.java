package com.tianxun.framework.entity.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询分页的通用对象
 * 
 * @author willzhang
 *
 * @param <T>
 */
public class Page<T> {

	/**
	 * 每一页的大小
	 */
	private int pageSize;

	/**
	 * 当前页的页码索引:从1开始
	 */
	private int pageIndex;

	/**
	 * 返回的总记录数
	 */
	private int totalCount;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 具体的业务结果列表
	 */
	private List<T> result;

	/**
	 * 初始化时默认分配的信息
	 */
	public Page() {
        pageSize = 15;
        pageIndex = 1;
        totalCount = 0;
        totalPage = 0;
        result = new ArrayList<T>(0);
    }
    
    public Page(List<T> result, int pageIndex, int pageSize, int totalCount) {
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.totalCount = totalCount;
        this.totalPage = totalCount % pageSize > 0 ? (totalCount / pageSize) + 1 : totalCount / pageSize;
        this.result = result;
    }

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

}
