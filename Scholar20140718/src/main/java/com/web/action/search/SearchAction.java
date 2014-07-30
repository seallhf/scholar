package com.web.action.search;

import com.utils.spring.SpringBeanFactory;
import com.web.action.base.BaseAction;
import com.web.pojo.Page;
import com.web.service.PageService;

@SuppressWarnings("serial")
public class SearchAction extends BaseAction {

	private String query;
	private String sortby;
	private String terms;

	private int pno; // 查看的表单页数
	private Page myPage;
	private String stuName; // 传递的用户名

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
		this.query = query.trim();
	}

	public String getSortby() {
		return sortby;
	}

	public void setSortby(String sortby) {
		if (sortby != null)
			this.sortby = sortby;
		else
			this.sortby = "";
	}

	public String execute() {
		if (query.equals(""))
			return ERROR;
		else {
			setQuery(query);
			setSortby(sortby);
			if (pno == 0) {
				pno = 1;
			}
			setTerms(terms);
			PageService pageService = (PageService) SpringBeanFactory
					.getBean("pageService");
			pageService.init(pno, query, sortby, terms);
			myPage = pageService.getPage();
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

	public String getStuName() {
		return stuName;
	}

	public void setStuName(String stuName) {
		this.stuName = stuName;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}
}
