package com.web.action.search;

import com.search.pojo.AuthorPaper;
import com.spider.service.MongoService;
import com.utils.spring.SpringBeanFactory;
import com.web.action.base.BaseAction;

@SuppressWarnings("serial")
public class PaperTypeAction extends BaseAction{

	private AuthorPaper authorPaper;
	
	private String aid;

	public AuthorPaper getAuthorPaper() {
		return authorPaper;
	}

	public void setAuthorPaper(AuthorPaper authorPaper) {
		this.authorPaper = authorPaper;
	}
	
	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String json()
	{
		MongoService mongo = (MongoService) SpringBeanFactory
				.getBean("mongoService");
		authorPaper = mongo.findAuthorPaper(aid);
		return SUCCESS;
	}
	
}
