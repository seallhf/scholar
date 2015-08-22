package com.web.action;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.web.action.base.BaseAction;
import com.worker.pojo.UserScore;

@Service
@SuppressWarnings("serial")
public class UserSimilarityAction extends BaseAction {

	private String uid;

	private JSONObject ulist;

	public String getUserList() {
		if (uid != null) {
			ulist = getUserScore(uid);
			return SUCCESS;
		}
		else
			return ERROR;
	}
	
	public JSONObject getUserScore(String uid)
	{
		Map<String,Double> scores = new HashMap<String,Double>();
		scores.put("food", 0.64548);
		scores.put("travel", 0.2348);
		scores.put("sports", 0.21548);
		scores.put("sea", 0.14548);
		UserScore user = new UserScore();
		user.setScoreList(scores);
		user.setUid(uid);
		return user.getScores();
	}
}
