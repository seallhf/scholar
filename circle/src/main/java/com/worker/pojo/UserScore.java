package com.worker.pojo;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;

@Repository
public class UserScore {

	//对比的用户ID
	public String uid;
	//相似用户的兴趣得分
	public Map<String,Double> scoreList;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Map<String, Double> getScoreList() {
		return scoreList;
	}

	public void setScoreList(Map<String, Double> scoreList) {
		this.scoreList = scoreList;
	}
	
	public JSONObject getScores()
	{
		JSONObject object = new JSONObject();
		object.put(uid, scoreList);
		return object;
	}
	
}
