/**
 * 对前端调用接口
 * 
 */

package com.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.pojo.AuthorPage;
import com.search.dao.ElasticSearchDao;
import com.spider.service.MongoService;

@Service
public class SearchService {

	@Resource
	private ElasticSearchDao esDao;

	@Resource
	private MongoService mongoService;

	public long allcounts;

	/**
	 * 获取全部的作者信息 通过query查询检索出相关的作者
	 * 
	 * @param query
	 * @return
	 */
	public long getAuthorsCount(String query) {
		return esDao.findByPage(query, 0, 1).getHits().getTotalHits();
	}

	/**
	 * 获取全部的作者信息 通过query查询检索出相关的作者
	 * 
	 * @param query
	 * @return
	 */
	public long getAuthorsCountWithFilter(String query, String filter) {
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("location", filter);
		return esDao.findReScoreWithFilterByTerm(query, filters, 0, 1)
				.getHits().getTotalHits();
	}

	public long getFilterAuthorsCount(String query, String terms) {
		Map<String, String> termFields = new HashMap<String, String>();
		if (terms != null && !terms.equals("")) {
			String[] lines = terms.split(";");
			for (String line : lines) {
				if (line != null) {
					String field = line.split(",")[0];
					String list;
					if (termFields.containsKey(field))
						list = termFields.get(field);
					else
						list = "";
					String value = line.split(",")[1];
					list += value+",";
					termFields.put(field, list);
				}
			}
		}
		return esDao.findReScoreWithFilterByTerm(query, termFields, 0, 1)
				.getHits().getTotalHits();
	}

	/**
	 * 获得从start页开始的前length个作者列表 根据作者相关数据进行排序
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	public List<AuthorPage> getAuthors(String query, String sortBy,
			String terms, long start, long length) {
		List<AuthorPage> list = new ArrayList<AuthorPage>();
		Map<String, String> termFields = new HashMap<String, String>();
		if (terms != null && !terms.equals("")) {
			String[] lines = terms.split(";");
			for (String line : lines) {
				if (line != null) {
					String field = line.split(",")[0];
					String _list;
					if (termFields.containsKey(field))
						_list = termFields.get(field);
					else
						_list = "";
					String value = line.split(",")[1];
					_list += value+",";
					termFields.put(field, _list);
				}
			}
		}
		SearchResponse response;
		if (sortBy.equals("apapers"))
			response = esDao.findByScoreAType(query, termFields, (start - 1)
					* length, length);
		else if (sortBy.equals("year"))
			response = esDao.findByScoreYear(query, termFields, (start - 1)
					* length, length);
		else
			response = esDao.findReScoreWithFilterByTerm(query, termFields,
					(start - 1) * length, length);
		allcounts = response.getHits().totalHits();
		for (SearchHit sh : response.getHits()) {
			String id = (String) sh.getSource().get("aid");
			AuthorPage authorPage = mongoService.findAuthorPage(id);
			list.add(authorPage);
		}
		return list;
	}

	public static void main(String[] args) {

		// MongoService search = (MongoService) SpringBeanFactory
		// .getBean("mongoService");
		// Author authors = search.findAuthor("1iE2ykkAAAAJ");
		// System.out.println(authors.toString());
	}
}
