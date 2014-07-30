package com.web.action.search;

import com.pojo.Author;
import com.spider.service.MongoService;
import com.utils.spring.SpringBeanFactory;
import com.web.action.base.BaseAction;

@SuppressWarnings("serial")
public class CoAuthorAction extends BaseAction {

	private Author author;

	private String aid;

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String json() {
		MongoService mongo = (MongoService) SpringBeanFactory
				.getBean("mongoService");
		author = mongo.findAuthor(aid);
		return SUCCESS;
	}

}
