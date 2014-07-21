package com.action.search;

import java.util.List;

import javax.annotation.Resource;

import com.action.base.BaseAction;
import com.pojo.Page;
import com.service.MongoService;
import com.service.PageService;
import com.utils.spring.SpringBeanFactory;

@SuppressWarnings("serial")
public class SearchAction extends BaseAction {

	private String query;
	private String sortBy;

	private int pno; // 查看的表单页数
	private Page myPage;
	private List<?> mylist;
	private String stuName; // 传递的用户名

	@Resource
	private MongoService mongoService;

	@Resource
	private PageService pageService;

	/**
	 * @return the message
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String execute() {
		if (query.equals(""))
			return ERROR;
		else {
			setQuery(query);
			if (pno == 0) {
				pno = 1;
			}
			PageService pageService = (PageService) SpringBeanFactory
					.getBean("pageService");
			pageService.init(pno, query, "default");
			myPage = pageService.getPage();
			mylist = pageService.getPage().getList();
			return SUCCESS;
		}
	}

	public String filterSearch() {
		if (query.equals(""))
			return ERROR;
		else {
			setQuery(query);
			if (pno == 0) {
				pno = 1;
			}
			pageService.init(pno, query, "default");
			myPage = pageService.getPage();
			mylist = pageService.getPage().getList();
			return SUCCESS;
		}
	}

	public String getSearch() throws Exception {
		String html = "";

		return html;
	}

	public int getPno() {
		return pno;
	}

	public void setPno(int pno) {
		this.pno = pno;
	}

	public Page getMyPage() {
		return myPage;
	}

	public void setMyPage(Page myPage) {
		this.myPage = myPage;
	}

	public List<?> getMylist() {
		return mylist;
	}

	public void setMylist(List<?> mylist) {
		this.mylist = mylist;
	}

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

}
