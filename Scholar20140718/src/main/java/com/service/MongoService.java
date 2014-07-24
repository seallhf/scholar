package com.service;

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
import com.pojo.Authors;
import com.pojo.Paper;

@Service
public class MongoService {

	@Resource
	public MongoDao mongoDao;

	public final String AUTHOR = "authors";

	public final String PAPER = "papers";

	public final String INDEX = "authorrank";

	/**
	 * 查找全部的作者
	 * 
	 * @return
	 */
	public Map<String, DBObject> getAllAuthor() {
		return mongoDao.find(AUTHOR, "aid");
	}
	
	public Map<String, DBObject> getAllPaper() {
		return mongoDao.find(PAPER, "pid");
	}


	public Author findAuthor(String aid) {
		Map<String, String> query = new HashMap<String, String>();
		query.put("aid", aid);
		Author a = (Author) JSONObject.toJavaObject(
				(JSONObject) JSONObject.toJSON(mongoDao.find("author", query)),
				Author.class);
		return a;
	}
	
	public Authors findAuthorN(String aid) {
		Map<String, String> query = new HashMap<String, String>();
		query.put("aid", aid);
		Authors a = (Authors) JSONObject.toJavaObject(
				(JSONObject) JSONObject.toJSON(mongoDao.find(AUTHOR, query)),
				Authors.class);
		return a;
	}

	public Paper findPaper(String pid) {
		Map<String, String> query = new HashMap<String, String>();
		query.put("pid", pid);
		return (Paper) JSONObject.toJavaObject(
				(JSONObject) JSONObject.toJSON(mongoDao.find("paper", query)),
				Paper.class);
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
				.toJSON(newAuthor)).get(updateField));
		DBObject updatedSetValue = new BasicDBObject("$set", updatedValue);
		mongoDao.update("author", updateCondition, updatedSetValue);
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
				.toJSON(newPaper)).get(updateField));
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
			this.mongoDao.save("author", (JSONObject) JSONObject.toJSON(author));
	}	
	
	public void insertPaper(Paper paper) {
		// TODO Auto-generated method stub
		if (paper != null)
			this.mongoDao.save("paper", (JSONObject) JSONObject.toJSON(paper));
	}

	public static void main(String[] args) throws Exception {
		
		//需要将authors的数据类型操作完全删除

	}
}
