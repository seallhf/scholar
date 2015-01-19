package com.search.dao;

import java.util.Iterator;
import java.util.Map.Entry;

import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.utils.TypeCastUtil;
import com.utils.spring.SpringBeanFactory;

@Repository
public class SearchDao {

	Settings settings = ImmutableSettings.settingsBuilder()
			.put("cluster.name", "haofang").build();

	private Client client = new TransportClient(settings)
			.addTransportAddress(new InetSocketTransportAddress(
					"192.168.2.221", 9300));

	private String indexName = "linkedin_tag";	

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

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

	public long findAllCounts(String query, String field) {
		CountResponse response = client.prepareCount(indexName)
				.setQuery(QueryBuilders.queryString(query).field(field))
				.execute().actionGet();
		return response.getCount();
	}
	
	public long findAllMatchCounts(String query) {
		CountResponse response = client.prepareCount(indexName)
				.setQuery(QueryBuilders.multiMatchQuery(query,"brief","title","descrpition"))
				.execute().actionGet();
		return response.getCount();
	}

	public SearchResponse findBase(QueryBuilder qb, long length) {
		SearchResponse respons;
		respons = client.prepareSearch(indexName).setTypes(indexName)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(qb)
				.setFrom(0).setSize(TypeCastUtil.castLong2Integer(length))
				.execute().actionGet();
		return respons;
	}

	public SearchResponse findDefault(String query, String field, Long length) {
		QueryBuilder qb = QueryBuilders.queryString(query).field(field);
		return findBase(qb, length);
	}
	
	public SearchResponse findDefaultAll(String query, Long length) {
		QueryBuilder qb = QueryBuilders.multiMatchQuery(query,"brief","title","descrpition");
		return findBase(qb, length);
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
	public void putAnswerIntoIndex(JSONObject json) throws Exception {
		addToIndex(indexName, indexName, json.getString("id"), json);
	}

	public static void main(String[] args) throws Exception {
		SearchDao e = (SearchDao) SpringBeanFactory
				.getBean("searchDao");
		System.out.println(e);
	}

}
