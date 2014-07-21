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

	/**
	 * 向elasticsearch添加所有用户的索引
	 * 
	 */
	public void updateIndex() {
		AuthorRank authorRank;
		int i = 0;
		Map<String, DBObject> authors = mservice.getAllAuthor();
		for (String aid : authors.keySet()) {
			Author author = (Author) JSONObject.toJavaObject(
					(JSONObject) JSONObject.toJSON(authors.get(aid)),
					Author.class);
			if (author.getAuthorPaper() == null) {
				author = aservice.createAuthorPaper(author);
				mservice.updateAuthorPaper(aid, author);
			}
			if (author.getAid() != null) {
				authorRank = aservice.getAuthorRank(author);
				try {
					if (authorRank.getYear() >= 2007) {
						service.putAuthorRankIntoIndex(authorRank);
						author.setIsYoungEnough(true);
						mservice.updateAuthorData(author.getAid(),
								"isYoungEnough", author);
					} else {
						service.putAuthorRankIntoIndex(authorRank);
						author.setIsYoungEnough(false);
						mservice.updateAuthorData(author.getAid(),
								"isYoungEnough", author);
					}
					// mservice.updateAuthorPaper(aid, author);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// if(i>=10)
			// break;
			System.out.println((i++) + ":" + aid);
		}
	}

	/**
	 * 根据用户的ID更新用户的论文等级
	 * 
	 * @param aid
	 */
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
		// index.updateAuthorPaper("pGsO6EkAAAAJ");
	}

}
