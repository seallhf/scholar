package com.service;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dao.ElasticSearchDao;
import com.mongodb.DBObject;
import com.pojo.Author;
import com.pojo.AuthorRank;
import com.utils.spring.SpringBeanFactory;

@Service
public class IndexService {

	@Resource
	ElasticSearchDao elastic;
	@Resource
	MongoService mongo;
	@Resource
	MysqlService mysql;
	@Resource
	AuthorService authorService;

	/**
	 * 向elasticsearch添加所有用户的索引
	 * 
	 */
	public void updateIndex() {
		int i = 0;
		Map<String, DBObject> authors = mongo.getAllAuthor();
		for (String aid : authors.keySet()) {
			Author author = (Author) JSONObject.toJavaObject(
					(JSONObject) JSONObject.toJSON(authors.get(aid)),
					Author.class);
			updateIndex(author);
			// if(i>=10)
			// break;
			System.out.println((i++) + ":" + aid);
		}
	}

	/**
	 * 添加目标用户数据库索引
	 * @param author
	 */
	public void updateIndex(Author author) {
		if (mysql.findAuthorPaper(author.getAid()) == null) {
			// 创建AuthorPaper并插务数据库
			mysql.insertAuthorPaper(authorService.createAuthorPaper(author));
			// 创建AuthorPage并插入数据库
			mysql.insertAuthorPage(authorService.createAuthorPage(author));
		}
		if (author.getAid() != null) {
			AuthorRank authorRank = authorService.createAuthorRank(author);
			try {
				if (authorRank.getYear() >= 2007) {
					// 添加搜索索引
					elastic.putAuthorRankIntoIndex(authorRank);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		IndexService index = (IndexService) SpringBeanFactory
				.getBean("indexService");
		index.updateIndex();
		// index.updateAuthorPaper("pGsO6EkAAAAJ");
	}

}
