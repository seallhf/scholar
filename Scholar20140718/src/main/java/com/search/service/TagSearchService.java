/**
 * 对前端调用接口
 * 
 */

package com.search.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import com.alibaba.fastjson.JSONObject;
import com.search.dao.SearchDao;
import com.utils.spring.SpringBeanFactory;

public class TagSearchService {	

	private String terms;

	private int from;

	private int to;	
	
	private String db;

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	/**
	 * 获得从start页开始的前length个作者列表 根据作者相关数据进行排序
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	private Map<String, Float> searchByTerm(String query, Map<String, Float> list) {
		Map<String, Float> _list = new HashMap<String, Float>();
		SearchDao esDao = (SearchDao) SpringBeanFactory.getBean("searchDao");
		long allcounts = esDao.findAllCounts(query, "tag");
		Map<String, Float> votes = new HashMap<String, Float>();
		Map<String, Integer> counts = new HashMap<String, Integer>();
		SearchResponse response = esDao.findDefault(query, "tag", allcounts);
		for (SearchHit sh : response.getHits()) {
			JSONObject json = (JSONObject) JSONObject.toJSON(sh.getSource());
			String name = json.getString("name");
			Float vote = json.getFloat("vote");
			Float score = (float) (vote * (1 - Math.pow(Math.E,
					0d - sh.getScore())));
			if (votes.containsKey(name))
				votes.put(name, votes.get(name) + score);
			else
				votes.put(name, score);
			if (counts.containsKey(name))
				counts.put(name, counts.get(name) + 1);
			else
				counts.put(name, 1);
		}
		for (String name : votes.keySet()) {
			if (list != null) {
				if (list.containsKey(name))
					_list.put(
							name,
							list.get(name)
									+ (votes.get(name) / (counts.get(name))));
			} else {
				_list.put(name, (votes.get(name) / (counts.get(name))));
			}
		}
		return _list;
	}

	public JSONObject searchService() {
		Map<String, String> map = new HashMap<String, String>();
		String[] tags = terms.split(";");
		for (String tag : tags) {
			map.put(tag, null);
		}
		List<Map.Entry<String, Float>> list = mutiplSearch(map, from, to);
		JSONObject object = new JSONObject();
		object.put("id_list", list);
		return object;
	}

	private List<Map.Entry<String, Float>> mutiplSearch(
			Map<String, String> terms, int from, int to) {
		Map<String, Float> list = null;
		for (String term : terms.keySet()) {
			list = searchByTerm(term, list);
		}
		List<Map.Entry<String, Float>> _list = new ArrayList<Map.Entry<String, Float>>(
				list.entrySet());
		Collections.sort(_list, new ComparatorRank());
		return _list.subList(from, to);
	}

	public class ComparatorRank implements Comparator<Entry<String, Float>> {
		public int compare(Entry<String, Float> arg0, Entry<String, Float> arg1) {
			if (arg1.getValue() > arg0.getValue())
				return 1;
			if (arg1.getValue() < arg0.getValue())
				return -1;
			return 0;
		}
	}

	public static void main(String[] args) {

		
	}
}
