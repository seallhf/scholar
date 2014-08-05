package com.spider.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.Author;
import com.pojo.AuthorPage;
import com.pojo.AuthorPaper;
import com.pojo.AuthorRank;
import com.pojo.Authors;
import com.pojo.Paper;
import com.search.service.AuthorService;
import com.spider.dao.MongoDao;
import com.utils.spring.SpringBeanFactory;

@Service
public class MongoService {

	@Resource
	public MongoDao mongoDao;

	public final String AUTHOR = "author";

	public final String PAPER = "paper";

	public final String AUTHORPAPER = "author_paper";

	public final String AUTHORPAGE = "author_page";

	public final String INDEX = "authorrank";

	/**
	 * 查找全部的作者
	 * 
	 * @return
	 */
	public Map<String, DBObject> getAllAuthor() {
		return mongoDao.find(AUTHOR, "aid");
	}

	public List<String> getAllAuthorId() {
		return mongoDao.findIds(AUTHOR, "aid");
	}

	public Map<String, DBObject> getAllAuthorOld() {
		return mongoDao.find("authors", "aid");
	}

	public Map<String, DBObject> getAllPaper() {
		return mongoDao.find(PAPER, "pid");
	}

	public Author findAuthor(String aid) {
		Map<String, String> query = new HashMap<String, String>();
		query.put("aid", aid);
		Author a = (Author) JSONObject.toJavaObject(
				(JSONObject) JSONObject.toJSON(mongoDao.find(AUTHOR, query)),
				Author.class);
		return a;
	}

	public AuthorPage findAuthorPage(String aid) {
		Map<String, String> query = new HashMap<String, String>();
		query.put("aid", aid);
		AuthorPage a = (AuthorPage) JSONObject
				.toJavaObject((JSONObject) JSONObject.toJSON(mongoDao.find(
						AUTHORPAGE, query)), AuthorPage.class);
		return a;
	}

	public AuthorPaper findAuthorPaper(String aid) {
		Map<String, String> query = new HashMap<String, String>();
		query.put("aid", aid);
		AuthorPaper a = (AuthorPaper) JSONObject.toJavaObject(
				(JSONObject) JSONObject.toJSON(mongoDao
						.find(AUTHORPAPER, query)), AuthorPaper.class);
		return a;
	}

	public Authors findAuthorOld(String aid) {
		Map<String, String> query = new HashMap<String, String>();
		query.put("aid", aid);
		Authors a = (Authors) JSONObject
				.toJavaObject((JSONObject) JSONObject.toJSON(mongoDao.find(
						"authors", query)), Authors.class);
		return a;
	}

	public Paper findPaper(String pid) {
		Map<String, String> query = new HashMap<String, String>();
		query.put("pid", pid);
		return (Paper) JSONObject.toJavaObject(
				(JSONObject) JSONObject.toJSON(mongoDao.find(PAPER, query)),
				Paper.class);
	}

	/**
	 * 更新作者的相应信息
	 * 
	 * @param aid
	 * @param updateField
	 * @param newAuthor
	 */
	public void updateAuthorData(String aid, String updateField,
			Author newAuthor) {
		DBObject updateCondition = new BasicDBObject();
		// where name='fox'
		updateCondition.put("aid", aid);
		DBObject updatedValue = new BasicDBObject();
		updatedValue.put(updateField,
				((JSONObject) JSONObject.toJSON(newAuthor)).get(updateField));
		DBObject updatedSetValue = new BasicDBObject("$set", updatedValue);
		mongoDao.update("author", updateCondition, updatedSetValue);
	}

	/**
	 * 更新文章的相应信息
	 * 
	 * @param pid
	 * @param updateField
	 * @param newPaper
	 */
	public void updatePaperData(String pid, String updateField, Paper newPaper) {
		DBObject updateCondition = new BasicDBObject();
		// where name='fox'
		updateCondition.put("pid", pid);
		DBObject updatedValue = new BasicDBObject();
		updatedValue.put(updateField,
				((JSONObject) JSONObject.toJSON(newPaper)).get(updateField));
		DBObject updatedSetValue = new BasicDBObject("$set", updatedValue);
		mongoDao.update(PAPER, updateCondition, updatedSetValue);
	}

	/**
	 * 更新文章的相应信息
	 * 
	 * @param pid
	 * @param updateField
	 * @param newPaper
	 */
	public void updateAuthorPaperData(String aid, String updateField,
			AuthorPaper authorPaper) {
		DBObject updateCondition = new BasicDBObject();
		// where name='fox'
		updateCondition.put("aid", aid);
		DBObject updatedValue = new BasicDBObject();
		updatedValue.put(updateField,
				((JSONObject) JSONObject.toJSON(authorPaper)).get(updateField));
		DBObject updatedSetValue = new BasicDBObject("$set", updatedValue);
		mongoDao.update(AUTHORPAPER, updateCondition, updatedSetValue);
	}

	/**
	 * 更新文章的相应信息
	 * 
	 * @param pid
	 * @param updateField
	 * @param newPaper
	 */
	public void updateAuthorPageData(String aid, String[] updateFields,
			AuthorPage authorPage) {
		DBObject updateCondition = new BasicDBObject();
		// where name='fox'
		updateCondition.put("aid", aid);
		DBObject updatedValue = new BasicDBObject();
		for (String updateField : updateFields) {
			updatedValue.put(updateField, ((JSONObject) JSONObject
					.toJSON(authorPage)).get(updateField));
		}
		DBObject updatedSetValue = new BasicDBObject("$set", updatedValue);
		mongoDao.update(AUTHORPAGE, updateCondition, updatedSetValue);
	}

	public void insertIndex(AuthorRank authorRank) {
		// TODO Auto-generated method stub
		if (authorRank != null)
			this.mongoDao.save(INDEX,
					(JSONObject) JSONObject.toJSON(authorRank));
	}

	public void insertAuthor(Author author) {
		// TODO Auto-generated method stub
		if (author != null)
			this.mongoDao
					.save("author", (JSONObject) JSONObject.toJSON(author));
	}

	public void insertPaper(Paper paper) {
		// TODO Auto-generated method stub
		if (paper != null)
			this.mongoDao.save("paper", (JSONObject) JSONObject.toJSON(paper));
	}

	public void insertAuthorPaper(AuthorPaper authorPaper) {
		// TODO Auto-generated method stub
		if (authorPaper != null)
			this.mongoDao.save("author_paper",
					(JSONObject) JSONObject.toJSON(authorPaper));
	}

	public void insertAuthorPage(AuthorPage authorPage) {
		// TODO Auto-generated method stub
		if (authorPage != null)
			this.mongoDao.save("author_page",
					(JSONObject) JSONObject.toJSON(authorPage));
	}

	public static void main(String[] args) throws Exception {

	}
}
