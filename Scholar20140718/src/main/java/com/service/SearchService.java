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

@Service
public class SearchService {

	@Resource
	private ElasticSearchDao eSService;

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
		return eSService.findByPage(query, 0, 1).getHits().getTotalHits();
	}

	/**
	 * 获得从start页开始的前length个作者列表 根据作者相关数据进行排序
	 * 
	 * @param start
	 * @param length
	 * @param query
	 * @return
	 */
	public List<Author> getAuthors(String query,String sortBy, long start, long length) {
		List<Author> list = new ArrayList<Author>();
		Map<String, String> terms = new HashMap<String, String>();
		//terms.put("location", "student");
		SearchResponse response;
		if(sortBy.equals("defualt"))
			response = eSService.findReScoreWithFilterByTerm(query,
				terms, (start - 1) * length, length);
		else if(sortBy.equals("aType"))
			response = eSService.findReScoreWithFilterByTerm(query,
					terms, (start - 1) * length, length);
		else
			response = eSService.findReScoreWithFilterByTerm(query,
					terms, (start - 1) * length, length);
		allcounts = response.getHits().totalHits();
		for (SearchHit sh : response.getHits()) {
			String id = (String) sh.getSource().get("aid");
			Author author = mongoService.findAuthor(id);
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
