package com.web.pojo;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class Page {
	private long pageSize; // 每页的数量
	private long totalPage; // 总页数
	private long rowCount; // 总数量
	private long currentPage; // 当前页数
	private long prePage; // 上一页
	private long nextPage; // 下一页
	private boolean hasNextPage; // 是否有下一页
	private boolean hasPreviousPage; // 是否有下一页
	private List<?> list; // 返回的数据

	public Page() {
		this.pageSize = 10;
	}

	public long getPageSize() {
		return pageSize;

	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;

	}

	public long getTotalPage() {
		return totalPage;

	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;

	}

	public long getRowCount() {
		return rowCount;

	}

	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;

	}

	public long getCurrentPage() {
		return currentPage;

	}

	public void setCurrentPage(long currentPage) {
		this.currentPage = currentPage;

	}

	public long getPrePage() {
		return prePage;

	}

	public void setPrePage(long prePage) {
		this.prePage = prePage;

	}

	public long getNextPage() {
		return nextPage;

	}

	public void setNextPage(long nextPage) {
		this.nextPage = nextPage;

	}

	public boolean isHasNextPage() {
		return hasNextPage;

	}

	public void setHasNextPage(boolean hasNextPage) {
		this.hasNextPage = hasNextPage;

	}

	public boolean isHasPreviousPage() {
		return hasPreviousPage;

	}

	public void setHasPreviousPage(boolean hasPreviousPage) {
		this.hasPreviousPage = hasPreviousPage;

	}

	public List<?> getList() {
		return list;

	}

	public void setList(List<?> list) {
		this.list = list;

	}
	
}
