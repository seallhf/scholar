package com.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pojo.Page;
import com.utils.TypeCastUtil;

@Service
public class PageService {

	public Page page;
	public long start;
	public long allcounts;

	@Resource
	private SearchService searchService;

	public void init(int start, String query, String sortBy) {
		page = new Page();
		this.start = start;
		if (page.getList() == null) {
			List<?> list = searchService.getAuthors(query, sortBy, start,
					page.getPageSize());
			page.setList(list);
			allcounts = searchService.getAllAuthorsCount(query);
		}
		setRowCount(); // 设置page的总数量
		setTotalPage(); // 设置page的总页数
		setCurrentPage(); // 设置page的当前页
		setPrePage(); // 设置page的上一页
		setNextPage(); // 设置page的下一页
		setPreOrNextBoolean(); // 判断page是否有上一页或下一页

	}

	/**
	 * 获得当前页面的数据
	 * 
	 * @return
	 */
	public Page getPage() {
		return page;

	}

	/** 获得数据的总数量 */
	public long getRowCount() {
		return allcounts;
	}

	/**
	 * 判断是否有下一页
	 * */
	public void setPreOrNextBoolean() {
		if (page.getCurrentPage() <= 1) {
			page.setHasPreviousPage(false);
		} else {
			page.setHasPreviousPage(true);
		}

		if (page.getCurrentPage() >= page.getTotalPage()) {
			page.setHasNextPage(false);
		} else {
			page.setHasNextPage(true);
		}

	}

	/**
	 * 设置当前查询页
	 * */
	public void setCurrentPage() {
		if (start < 1) {
			page.setCurrentPage(1);
		} else if (start > page.getTotalPage()) {
			page.setCurrentPage(page.getTotalPage());
		} else {
			page.setCurrentPage(start);
		}

	}

	/**
	 * 设置上一页的页数
	 * */
	public void setPrePage() {
		page.setPrePage(page.getCurrentPage() - 1);

	}

	/**
	 * 设置下一页的页数
	 * */
	public void setNextPage() {
		page.setNextPage(page.getCurrentPage() + 1);

	}

	/**
	 * 计算总页数
	 * */
	public void setTotalPage() {
		long rowCount = getRowCount();
		long pageSize = page.getPageSize();

		if (rowCount > pageSize) {
			if (rowCount % pageSize == 0) {
				page.setTotalPage(TypeCastUtil.castLong2Integer(rowCount
						/ pageSize));
			} else {
				page.setTotalPage(1 + TypeCastUtil.castLong2Integer(rowCount
						/ pageSize));
			}
		} else {
			page.setTotalPage(1);
		}

	}

	public void setRowCount() {
		page.setRowCount(getRowCount());

	}

	/**
	 * 设置查询数据的起始位置
	 * */
	public long getStartIndex() {
		long startIndex = 0;
		if (start < 0) {
			startIndex = 0;
		} else {
			if (start > page.getTotalPage()) {
				startIndex = page.getPageSize() * (page.getTotalPage() - 1);
			} else {
				startIndex = page.getPageSize() * (start - 1);
			}
		}

		return startIndex;

	}
}
