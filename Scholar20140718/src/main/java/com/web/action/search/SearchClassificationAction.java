package com.web.action.search;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.search.service.SearchService;
import com.utils.spring.SpringBeanFactory;
import com.web.action.base.BaseAction;

@SuppressWarnings("serial")
public class SearchClassificationAction extends BaseAction {

	private JSONObject classificationData;

	private String query;

	private Map<String, List<String>> terms;

	public JSONObject getClassificationData() {
		return classificationData;
	}

	public void setClassificationData(JSONObject classificationData) {
		this.classificationData = classificationData;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Map<String, List<String>> getTerms() {
		return terms;
	}

	public void setTerms(Map<String, List<String>> terms) {
		this.terms = terms;
	}

	public String json() {
		SearchService search = (SearchService) SpringBeanFactory
				.getBean("searchService");
		List<String> company = Arrays.asList(new String[]{"microsoft","google","ibm","facebook","apple"});
		List<String> position = Arrays.asList(new String[]{"phd","researcher","professor"});
		List<String> system = Arrays.asList(new String[]{"beijing university","tinghua university","chinese academy of sciences"});
		Map<String,List<String>> maps = new HashMap<String,List<String>>();
		maps.put("company", company);
		maps.put("position", position);
		maps.put("system", system);
		setTerms(maps);
		setQuery(query);
		JSONObject result = new JSONObject();
		for (String type : terms.keySet()) {
			List<String> list = terms.get(type);
			Map<String,Long> map = new HashMap<String,Long>();
			for (String term : list) {
				long counts = search.getAuthorsCountWithFilter(query, term);
				map.put(term, counts);
			}
			result.put(type, map);
		}
		setClassificationData(result);
		return SUCCESS;
	}
}
