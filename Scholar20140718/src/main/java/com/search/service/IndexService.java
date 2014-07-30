package com.search.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.pojo.Author;
import com.pojo.AuthorPage;
import com.pojo.AuthorRank;
import com.pojo.Paper;
import com.search.dao.ElasticSearchDao;
import com.spider.service.MongoService;
import com.utils.spring.SpringBeanFactory;

@Service
public class IndexService {

	@Resource
	ElasticSearchDao elastic;
	@Resource
	MongoService mongo;
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
	 * 
	 * @param author
	 */
	public void updateIndex(Author author) {
		if (mongo.findAuthorPaper(author.getAid()) == null) {
			// 创建AuthorPaper并插务数据库
			mongo.insertAuthorPaper(authorService.createAuthorPaper(author,
					null));
			// 创建AuthorPage并插入数据库
			mongo.insertAuthorPage(authorService.createAuthorPage(author, null));
		}
		if (author.getAid() != null) {
			AuthorPage authorPage = mongo.findAuthorPage(author.getAid());
			if (authorPage != null) {
				AuthorRank authorRank = authorService.createAuthorRank(author,null);
				try {
					{
						// 添加搜索索引
						elastic.putAuthorRankIntoIndex(authorRank);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 添加目标用户数据库索引
	 * 
	 * @param author
	 */
	public void updateIndex(Author author,List<Paper> papers) {
		if (mongo.findAuthorPaper(author.getAid()) == null) {
			// 创建AuthorPaper并插务数据库
			mongo.insertAuthorPaper(authorService.createAuthorPaper(author,papers));
			// 创建AuthorPage并插入数据库
			mongo.insertAuthorPage(authorService.createAuthorPage(author,papers));
		}
		if (author.getAid() != null) {
			AuthorPage authorPage = mongo.findAuthorPage(author.getAid());
			if (authorPage != null) {
				AuthorRank authorRank = authorService.createAuthorRank(author,papers);
				try {
					{
						// 添加搜索索引
						elastic.putAuthorRankIntoIndex(authorRank);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
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
