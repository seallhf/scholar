package com.dao;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.pojo.AuthorRank;
import com.utils.TypeCastUtil;
import com.utils.spring.SpringBeanFactory;

@Repository
public class ElasticSearchDao {

	@Autowired
	private Client client;

	/**
	 * 增加到索引
	 * 
	 * @param indexName
	 * @param typeName
	 * @param _id
	 * @param json
	 * @throws Exception
	 */
	public void addToIndex(String indexName, String typeName, String _id,
			JSONObject json) throws Exception {
		fixJson(json);
		IndexRequestBuilder build = client.prepareIndex(indexName, typeName,
				_id).setSource(json.toJSONString());
		long _start = System.currentTimeMillis();
		build.execute().actionGet(3000);
		System.out.println("put index costs"
				+ (System.currentTimeMillis() - _start) + " ms");
		System.out.println(_id + " indexed is ok!");
	}

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @param from
	 * @param size
	 * @return
	 */
	public SearchResponse findByPage(String query, long from, long size) {
		QueryBuilder qb = QueryBuilders.queryString(query);
		SearchResponse respons = client.prepareSearch("authorrank")
				.setTypes("authorrank")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb)
				.setFrom(TypeCastUtil.castLong2Integer(from))
				.setSize(TypeCastUtil.castLong2Integer(size)).execute()
				.actionGet();
		return respons;
	}

	/**
	 * 分页查询
	 * 
	 * @param query
	 * @param from
	 * @param size
	 * @return
	 */
	public SearchResponse findByField(String query, String field, long from,
			long size) {
		QueryBuilder qb = QueryBuilders.matchQuery(field, query);
		SearchResponse respons = client.prepareSearch("authorrank")
				.setTypes("authorrank")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb)
				.setFrom(TypeCastUtil.castLong2Integer(from))
				.setSize(TypeCastUtil.castLong2Integer(size)).execute()
				.actionGet();
		return respons;
	}

	/**
	 * 对列表进行重新排序
	 * 
	 * @param query
	 * @param from
	 * @param size
	 * @return
	 */
	public SearchResponse findByScoreDefault(String query,
			Map<String, String> terms, long from, long size) {
		String script = "doc.score * doc['rank'].value";
		QueryBuilder qb = QueryBuilders.queryString(query);
		SearchResponse respons = client
				.prepareSearch("authorrank")
				.setTypes("authorrank")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(qb)
				// .addSort("rank", SortOrder.DESC)
				.addSort(
						SortBuilders.scriptSort(script, "number").lang("mvel")
								.order(SortOrder.DESC))
				.setFrom(TypeCastUtil.castLong2Integer(from))
				.setSize(TypeCastUtil.castLong2Integer(size)).execute()
				.actionGet();
		return respons;
	}

	/**
	 * 对列表进行重新排序
	 * 
	 * @param query
	 * @param from
	 * @param size
	 * @return
	 */
	public SearchResponse findByScoreAType(String query,
			Map<String, String> terms, long from, long size) {
		String script = "doc['atype'].value";
		QueryBuilder qb = QueryBuilders.queryString(query);
		SearchResponse respons = client
				.prepareSearch("authorrank")
				.setTypes("authorrank")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(qb)
				// .addSort("rank", SortOrder.DESC)
				.addSort(
						SortBuilders.scriptSort(script, "number").lang("mvel")
								.order(SortOrder.DESC))
				.setFrom(TypeCastUtil.castLong2Integer(from))
				.setSize(TypeCastUtil.castLong2Integer(size)).execute()
				.actionGet();
		return respons;
	}

	/**
	 * 对列表进行重新排序
	 * 
	 * @param query
	 * @param from
	 * @param size
	 * @return
	 */
	public SearchResponse findByScoreYear(String query,
			Map<String, String> terms, long from, long size) {
		String script = "doc['year'].value";
		QueryBuilder qb = QueryBuilders.queryString(query);
		SearchResponse respons = client
				.prepareSearch("authorrank")
				.setTypes("authorrank")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(qb)
				// .addSort("rank", SortOrder.DESC)
				.addSort(
						SortBuilders.scriptSort(script, "number").lang("mvel")
								.order(SortOrder.DESC))
				.setFrom(TypeCastUtil.castLong2Integer(from))
				.setSize(TypeCastUtil.castLong2Integer(size)).execute()
				.actionGet();
		return respons;
	}

	/**
	 * 创建过滤查询条件
	 * 
	 * @param terms
	 * @return
	 */
	private FilterBuilder buildFilters(Map<String, String> terms) {
		FilterBuilder filter = null;
		for (String term : terms.keySet()) {
			filter = FilterBuilders.andFilter(FilterBuilders.termFilter(term,
					terms.get(term)));
		}
		return filter;
	}

	/**
	 * 对列表进行重新排序
	 * 
	 * @param query
	 * @param from
	 * @param size
	 * @return
	 */
	public SearchResponse findReScoreWithFilterByTerm(String query,
			Map<String, String> terms, long from, long size) {
		String script = "doc.score * doc['rank'].value";
		FilterBuilder filter = buildFilters(terms);
		QueryBuilder qb = QueryBuilders.queryString(query);
		SearchResponse respons = client
				.prepareSearch("authorrank")
				.setTypes("authorrank")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(qb)
				.setPostFilter(filter)
				// .addSort("rank", SortOrder.DESC)
				.addSort(
						SortBuilders.scriptSort(script, "number").lang("mvel")
								.order(SortOrder.DESC))
				.setFrom(TypeCastUtil.castLong2Integer(from))
				.setSize(TypeCastUtil.castLong2Integer(size)).execute()
				.actionGet();
		return respons;
	}

	private void fixJson(JSONObject json) {
		// TODO Auto-generated method stub
		Iterator<Entry<String, Object>> iterator = json.entrySet().iterator();
		Entry<String, Object> entry = null;
		while (iterator.hasNext()) {
			entry = iterator.next();
			if (entry.getValue() instanceof JSONObject) {
				json.put(entry.getKey(), entry.getValue().toString());
			}
		}
	}

	/**
	 * 将作者相关信息加入检索
	 * 
	 * @param authorRank
	 * @throws Exception
	 */
	public void putAuthorRankIntoIndex(AuthorRank authorRank) throws Exception {
		addToIndex("authorrank", "authorrank", authorRank.getAid(),
				(JSONObject) JSONObject.toJSON(authorRank));
	}

	public static void main(String[] args) throws Exception {

		ElasticSearchDao e = (ElasticSearchDao) SpringBeanFactory
				.getBean("elasticSearchDao");
		System.out.println(e.client);
	}

}
