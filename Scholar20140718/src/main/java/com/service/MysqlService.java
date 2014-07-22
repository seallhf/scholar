package com.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dao.MysqlDao;
import com.mongodb.DBObject;
import com.pojo.Author;
import com.pojo.AuthorPage;
import com.pojo.AuthorPaper;
import com.utils.spring.SpringBeanFactory;

@Service
public class MysqlService {

	@Resource
	private MysqlDao mysqlDao;

	public AuthorPage insertAuthorPage(AuthorPage authorPage) {

		return mysqlDao.insert(authorPage);
	}

	public AuthorPaper insertAuthorPaper(AuthorPaper authorPaper) {

		return mysqlDao.insert(authorPaper);
	}

	public AuthorPaper findAuthorPaper(String aid) {
		List<AuthorPaper> list = mysqlDao.findAuthorPaper(aid);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}

	public AuthorPage findAuthorPage(String aid) {
		List<AuthorPage> list = mysqlDao.findAuthorPage(aid);
		if (list != null && list.size() > 0)
			return list.get(0);
		return null;
	}
	
	

	public static void main(String[] args) {
		MysqlService service = (MysqlService) SpringBeanFactory
				.getBean("mysqlService");
		MongoService mservice = (MongoService) SpringBeanFactory
				.getBean("mongoService");
		AuthorService aservice = (AuthorService) SpringBeanFactory
				.getBean("authorService");
		Map<String, DBObject> all = mservice.getAllAuthor();
		int i = 0;
		for (String aid : all.keySet()) {
			if (aid != null) {
				AuthorPage authorPage = (AuthorPage) SpringBeanFactory
						.getBean("authorPage");
				Author a = JSONObject.toJavaObject(
						(JSONObject) JSONObject.toJSON(all.get(aid)),
						Author.class);
				authorPage.setAid(aid);
				authorPage.setCiteindex(a.getCiteindex());
				authorPage.setCoBigAuthors(aservice.caculateBigCoauthor(a));
				authorPage.setCollege(a.getCollege());
				authorPage.setEmail(a.getEmail());
				authorPage.setHomePage(a.getHomePage());
				authorPage.setImgUrl(a.getImgUrl());
				authorPage.setMostFarmousPaper(aservice.caculateFamousPaper(a));
				authorPage.setName(a.getName());
				authorPage.setTags(a.getTags());
				authorPage.setYear(aservice.caculateFirstYear(a));
				AuthorPaper authorPaper = aservice.createAuthorPaper(a);
				service.insertAuthorPaper(authorPaper);
				service.insertAuthorPage(authorPage);
				System.out.println((i++) + ":" + aid);
			}
		}

	}
}
