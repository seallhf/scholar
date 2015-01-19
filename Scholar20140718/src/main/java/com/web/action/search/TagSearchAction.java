package com.web.action.search;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.struts2.ServletActionContext;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.search.dao.SearchDao;
import com.spider.dao.MongoDao;
import com.utils.IOUtil;
import com.utils.TypeCastUtil;
import com.utils.spring.SpringBeanFactory;
import com.web.action.base.BaseAction;

@Service
@SuppressWarnings("serial")
public class TagSearchAction extends BaseAction {

	private String terms;

	private int from;

	private JSONObject CV;

	private int to;

	private JSONObject list;

	private String db;

	private String filter;

	public JSONObject getCV() {
		return CV;
	}

	public void setCV(JSONObject cV) {
		CV = cV;
	}

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

	public JSONObject getList() {
		return list;
	}

	public void setList(JSONObject list) {
		this.list = list;
	}

	public String getDb() {
		return db;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setDb(String db) {
		if (db.equals("linkedin"))
			this.db = "linkedin_tag";
		else if (db.equals("zhihu"))
			this.db = "zhihu_tags";
	}

	/**
	 * 获得从start页开始的前length个作者列表 根据作者相关数据进行排序
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	private Map<String, Float> searchByTerm(String query,
			Map<String, Float> list) {
		Map<String, Float> _list = list;
		SearchDao esDao = (SearchDao) SpringBeanFactory.getBean("searchDao");
		esDao.setIndexName(db);
		long allcounts = esDao.findAllCounts(query, "tag");
		Map<String, Float> votes = new HashMap<String, Float>();
		Map<String, Integer> counts = new HashMap<String, Integer>();
		SearchResponse response = esDao.findDefault(query, "tag", allcounts);
		for (SearchHit sh : response.getHits()) {
			JSONObject json = (JSONObject) JSONObject.toJSON(sh.getSource());
			String name = json.getString("name");
			Float vote = json.getFloat("vote");
			// Float score = (float) (vote * (1 - Math.pow(Math.E,
			// 0d - sh.getScore())));
			// Float score = (float) (vote * sh.getScore());
			Float score = (float) (1 - Math.pow(Math.E,
					0d - (sh.getScore() * Math.log(vote + 2))));
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
			if (_list != null) {
				if (_list.containsKey(name))
					_list.put(
							name,
							list.get(name)
									+ (votes.get(name) / (counts.get(name))));
				else
					_list.put(name, (votes.get(name) / (counts.get(name))));

			} else {
				_list = new HashMap<String, Float>();
				_list.put(name, (votes.get(name) / (counts.get(name))));
			}
		}
		return _list;
	}

	/**
	 * 获得从start页开始的前length个作者列表 根据作者相关数据进行排序
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	private Map<String, Float> filtering(String query, Map<String, Float> list) {
		Map<String, Float> _list = new HashMap<String, Float>();
		SearchDao esDao = (SearchDao) SpringBeanFactory.getBean("searchDao");
		esDao.setIndexName("zhihu_infos");
		long allcounts = esDao.findAllCounts(query, "location");
		SearchResponse response = esDao.findDefault(query, "location",
				allcounts);
		for (SearchHit sh : response.getHits()) {
			JSONObject json = (JSONObject) JSONObject.toJSON(sh.getSource());
			String name = json.getString("name");
			if (list.containsKey(name)) {
				_list.put(name, list.get(name));
			}
		}
		return _list;
	}

	private long allcounts;

	public String searchService() {
		Map<String, String> map = new HashMap<String, String>();
		String line = ServletActionContext.getRequest().getQueryString();
		String[] lines = line.split("&");
		for (String l : lines) {
			if (l.split("=")[0].equals("terms"))
				try {
					terms = java.net.URLDecoder
							.decode(l.split("=")[1], "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else if (l.split("=")[0].equals("from"))
				from = Integer.parseInt(l.split("=")[1]);
			else if (l.split("=")[0].equals("to"))
				to = Integer.parseInt(l.split("=")[1]);
		}
		String[] tags = terms.split(";");
		for (String tag : tags) {
			map.put(tag, null);
		}
		List<Map.Entry<String, Float>> list = mutiplSearch(map, from, to);
		JSONObject object = new JSONObject();
		object.put("id_list", list);
		object.put("allcounts", allcounts);
		this.setList(object);
		return SUCCESS;
	}

	private Map<String, Float> contentSearch(String query,
			Map<String, Float> list) {
		Map<String, Float> _list = list;
		SearchDao esDao = (SearchDao) SpringBeanFactory.getBean("searchDao");
		esDao.setIndexName("zhihu_infos");
		long allcounts = esDao.findAllMatchCounts(query);
		SearchResponse response = esDao.findDefaultAll(query, allcounts);
		for (SearchHit sh : response.getHits()) {
			JSONObject json = (JSONObject) JSONObject.toJSON(sh.getSource());
			String name = json.getString("name");
			if (!_list.containsKey(name)) {
				_list.put(name, sh.getScore());
			} else {
				_list.put(name, sh.getScore() + _list.get(name));
			}
		}
		return _list;
	}

	private List<Map.Entry<String, Float>> mutiplSearch(
			Map<String, String> terms, int from, int to) {
		Map<String, Float> list = null;
		for (String term : terms.keySet()) {
			list = searchByTerm(term, list);
			list = contentSearch(term, list);
		}
		String[] filters = this.filter.split(";");
		for (String f : filters) {
			list = filtering(f, list);
		}
		List<Map.Entry<String, Float>> _list = new ArrayList<Map.Entry<String, Float>>(
				list.entrySet());
		Collections.sort(_list, new ComparatorRank());
		allcounts = _list.size();
		if (from <= to && from >= 0 && to >= 0) {
			if (to >= _list.size() && from < _list.size())
				return _list.subList(from, _list.size() - 1);
			else if (from >= _list.size())
				return new ArrayList<Entry<String, Float>>();
			else
				return _list.subList(from, to);
		}
		return null;
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

	public String searchCV() {
		String name = ServletActionContext.getRequest().getQueryString()
				.split("=")[1];
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("name", name);
		MongoDao dao = (MongoDao) SpringBeanFactory.getBean("mongoDao");
		this.setCV((JSONObject) JSONObject.toJSON(dao.find("zhihu_user",
				condition)));
		return SUCCESS;
	}

	public static void main(String[] args) {
		TagSearchAction t = (TagSearchAction) SpringBeanFactory
				.getBean("tagSearchAction");
		MongoDao dao = (MongoDao) SpringBeanFactory.getBean("mongoDao");
		String line = "";
		t.setFilter("成都");
		HashMap<String, String> terms = new HashMap<String, String>();
		terms.put("理财", "");
		terms.put("金融", "");
		terms.put("银行", "");
		terms.put("经济", "");
		List<Entry<String, Float>> list = t.mutiplSearch(terms, 0, 500);
		for (Entry<String, Float> entry : list) {
			Map<String, String> query = new HashMap<String, String>();
			query.put("name", entry.getKey());
			line += entry.getKey() + "\t" + "http://www.zhihu.com/people/"
					+ entry.getKey() + "\t"
					+ dao.find("zhihu_user", query).get("weiboId") + "\r\n";
		}
		IOUtil.write2File("e:\\user.txt", line);
	}
}
