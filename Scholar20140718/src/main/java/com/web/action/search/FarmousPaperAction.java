package com.web.action.search;

import com.pojo.Paper;
import com.spider.service.MongoService;
import com.utils.spring.SpringBeanFactory;
import com.web.action.base.BaseAction;

@SuppressWarnings("serial")
public class FarmousPaperAction extends BaseAction{
	private Paper paper;

	private String pid;

	public Paper getPaper() {
		return paper;
	}

	public void setPaper(Paper paper) {
		this.paper = paper;
	}	
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String json() {
		MongoService mongo = (MongoService) SpringBeanFactory
				.getBean("mongoService");
		paper = mongo.findPaper(pid);
		return SUCCESS;
	}
}
