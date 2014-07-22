package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import com.dao.ElasticSearchDao;
import com.pojo.Author;
import com.pojo.Authors;

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
	public long getAllAuthorsCount(String query) {
		return esDao.findByPage(query, 0, 1).getHits().getTotalHits();
	}

	/**
	 * 获得从start页开始的前length个作者列表 根据作者相关数据进行排序
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	public List<Authors> getAuthors(String query,String sortBy, long start, long length) {
		List<Authors> list = new ArrayList<Authors>();
		Map<String, String> terms = new HashMap<String, String>();
		//terms.put("location", "student");
		SearchResponse response;
		if(sortBy.equals("apapers"))
			response = esDao.findByScoreAType(query,
					terms, (start - 1) * length, length);
		else if(sortBy.equals("year"))
			response = esDao.findByScoreYear(query,
					terms, (start - 1) * length, length);
		else
			response = esDao.findReScoreWithFilterByTerm(query,
				terms, (start - 1) * length, length);
		allcounts = response.getHits().totalHits();
		for (SearchHit sh : response.getHits()) {
			String id = (String) sh.getSource().get("aid");
			Authors author = mongoService.findAuthorN(id);
			list.add(author);
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
