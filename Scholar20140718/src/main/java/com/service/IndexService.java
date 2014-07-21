package com.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dao.ElasticSearchDao;
import com.mongodb.DBObject;
import com.pojo.Author;
import com.pojo.AuthorRank;
import com.spider.ScholarSpider;
import com.utils.spring.SpringBeanFactory;

@Service
public class IndexService {

	@Resource
	ElasticSearchDao service;
	@Resource
	MongoService mservice;
	@Resource
	AuthorService aservice;
	@Resource
	ScholarSpider sservice;

	public void updateIndex() {
		AuthorRank authorRank;
		int i = 0;
		Map<String, DBObject> authors = mservice.getAllAuthor();
		for (String aid : authors.keySet()) {
			Author author = (Author) JSONObject.toJavaObject(
					(JSONObject) JSONObject.toJSON(authors.get(aid)),
					Author.class);
			if (author.getAid() != null && author.getIsYoungEnough()) {
				authorRank = aservice.getAuthorRank(author);
				author = aservice.createAuthorPaper(author);
				try {
					service.putAuthorRankIntoIndex(authorRank);
					//mservice.updateAuthorPaper(aid, author);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
//			if(i>=500)
//				break;
			System.out.println(i++);
		}
	}

	public void updateAuthorPaper(String aid) {

		Author author = aservice.createAuthorPaper(aid);
		// Author author = sservice.getAuthorDetail(aid);
		// author = aservice.createAuthorPaper(author);
		mservice.updateAuthorPaper(aid, author);
		// mservice.insertAuthor(author);
	}

	public static void main(String[] args) {
		IndexService index = (IndexService) SpringBeanFactory
				.getBean("indexService");
		index.updateIndex();
		//index.updateAuthorPaper("pGsO6EkAAAAJ");
	}

}
