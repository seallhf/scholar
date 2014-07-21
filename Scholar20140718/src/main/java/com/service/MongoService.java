package com.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dao.MongoDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.Author;
import com.pojo.AuthorRank;
import com.pojo.Paper;
import com.utils.spring.SpringBeanFactory;

@Service
public class MongoService {

	@Resource
	public MongoDao mongoDao;

	public final String AUTHOR = "authors";

	public final String PAPER = "papers";

	public final String INDEX = "authorrank";

	public long getLastAuthorId() {
		DBObject findMaxValue = this.mongoDao.findMaxValue(AUTHOR, "_id");
		if (findMaxValue != null && findMaxValue.containsField("_id")) {
			return Long.parseLong(findMaxValue.get("_id").toString());
		} else {
			return 0;
		}
	}

	/**
	 * 查找最后一次更新的时间
	 * 
	 * @return
	 */
	public Date getLastVideoUpdate() {
		DBObject findMaxValue = this.mongoDao.findMaxValue(PAPER, "_id");
		if (findMaxValue != null) {
			Object object = findMaxValue.get("update_time");
			return (Date) object;
		} else
			return null;
	}

	/**
	 * 查找全部的作者
	 * 
	 * @return
	 */
	public Map<String, DBObject> getAllAuthor() {
		return mongoDao.find(AUTHOR, "aid");
	}

	/**
	 * 获得最大的值
	 * 
	 * @return
	 */
	public DBObject findMaxValue(String tableName, String column) {
		return mongoDao.findMaxValue(tableName, column);

	}

	public Object findObject(String tableName, String column, String value) {
		Map<String, String> query = new HashMap<String, String>();
		query.put(column, value);
		return mongoDao.find(tableName, query);
	}

	public Object findAllObject(String tableName, String column, String value) {
		Map<String, String> query = new HashMap<String, String>();
		query.put(column, value);
		return mongoDao.find(tableName, query);
	}

	public Author findAuthor(String aid) {
		Map<String, String> query = new HashMap<String, String>();
		query.put("aid", aid);
		Author a = (Author) JSONObject.toJavaObject(
				(JSONObject) JSONObject.toJSON(mongoDao.find(AUTHOR, query)),
				Author.class);
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
	 * 更新作者的文章统计
	 * @param aid
	 * @param newAuthor
	 */
	public void updateAuthorPaper(String aid, Author newAuthor) {
		DBObject updateCondition = new BasicDBObject();
		// where name='fox'
		updateCondition.put("aid", aid);
		DBObject updatedValue = new BasicDBObject();
		updatedValue.put("authorPaper",
				JSONObject.toJSON(newAuthor.getAuthorPaper()));
		DBObject updatedSetValue = new BasicDBObject("$set", updatedValue);
		mongoDao.update(AUTHOR, updateCondition, updatedSetValue);
	}

	/**
	 * 更新作者的相应信息
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
		updatedValue.put(updateField, ((JSONObject) JSONObject
				.toJSON(newAuthor)).getString(updateField));
		DBObject updatedSetValue = new BasicDBObject("$set", updatedValue);
		mongoDao.update(AUTHOR, updateCondition, updatedSetValue);
	}
	
	/**
	 * 更新文章的相应信息
	 * @param pid
	 * @param updateField
	 * @param newPaper
	 */
	public void updatePaperData(String pid, String updateField,
			Paper newPaper) {
		DBObject updateCondition = new BasicDBObject();
		// where name='fox'
		updateCondition.put("pid", pid);
		DBObject updatedValue = new BasicDBObject();
		updatedValue.put(updateField, ((JSONObject) JSONObject
				.toJSON(newPaper)).getString(updateField));
		DBObject updatedSetValue = new BasicDBObject("$set", updatedValue);
		mongoDao.update(PAPER, updateCondition, updatedSetValue);
	}

	public void insert(String tableName, DBObject dbo) {
		// TODO Auto-generated method stub
		this.mongoDao.save(tableName, dbo);
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
			this.mongoDao.save(AUTHOR, (JSONObject) JSONObject.toJSON(author));
	}

	public void insertPaper(Paper paper) {
		// TODO Auto-generated method stub
		if (paper != null)
			this.mongoDao.save(PAPER, (JSONObject) JSONObject.toJSON(paper));
	}

	public static void main(String[] args) throws Exception {

		MongoService mongo = (MongoService) SpringBeanFactory
				.getBean("mongoService");
		AuthorService aservice = (AuthorService) SpringBeanFactory
				.getBean("authorService");
		Map<String, DBObject> authors = mongo.getAllAuthor();
		int i =0 ;
		for (String aid : authors.keySet()) 
		{
			Author author = JSONObject.toJavaObject(
					((JSONObject) JSONObject.toJSON(authors.get(aid))),
					Author.class);
			author = aservice.createAuthorPaper(author);
			mongo.updateAuthorPaper(aid, author);
			System.out.println((i++)+":"+aid);
		}
	}
}
