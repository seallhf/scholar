package com.spider.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.pojo.Author;
import com.pojo.AuthorPage;
import com.pojo.Paper;
import com.search.service.AuthorService;
import com.search.service.IndexService;
import com.spider.ScholarSpider;
import com.utils.spring.SpringBeanFactory;

@Service
public class SpiderService {

	@Resource
	ScholarSpider spider;
	@Resource
	MongoService mongo;
	@Resource
	IndexService index;
	@Resource
	AuthorService authorService;

	public void updateAuthors() {
		Map<String, DBObject> authors = mongo.getAllAuthor();
		int i = 0;
		for (String aid : authors.keySet()) {
			if (aid != null && !aid.equals("")) {
				Integer index = (Integer) authors.get(aid).get("citeindex");
				if (index < 1000)
					spideAuthor(aid);
				// break;
			}
			System.out.println((i++) + ":" + aid);
		}
	}

	public Author spideAuthor(String aid) {
		Author a = mongo.findAuthor(aid);
		AuthorPage aPage = mongo.findAuthorPage(aid);
		if (a == null) {
			a = spider.getAuthorDetail(aid);
			if (a != null) {
				mongo.insertAuthor(a);
				System.out.println("author :" + aid + "---->inserted!");
			}
		} else if (a.getPapers() == null) {
			a = spider.getAuthorDetail(aid);
			if (a != null) {
				mongo.updateAuthorData(aid, "papers", a);
				System.out.println("author :" + aid + "---->updated!");
			}
		}

		// 合理化authorPage的数据交互
		else if (aPage.getYear().equals("1900")) {
			//a = spider.getAuthorDetail(aid);
			long start = System.currentTimeMillis();
			List<Paper> lists = new ArrayList<Paper>();
			for (String pid : a.getPapers().keySet()) {
				Paper paper = mongo.findPaper(pid);
				lists.add(paper);
			}
			if (a != null) {
				mongo.updateAuthorPageData(aid, new String[] { "year",
						"mostFarmousPaper" },
						authorService.createAuthorPage(a, lists));
				System.out.println("authorPage :" + aid + "---->updated!");
			}
			System.out.println("get Papaers cost "+(System.currentTimeMillis() - start)+" ms");
		}
		return a;
	}

	public void getNewAuthors() throws IOException {
		Map<String, DBObject> authors = mongo.getAllAuthorOld();
		// Map<String, String> authors = IOUtil.read2Map("d:/data.txt");
		Map<String, String> coAuthors = new HashMap<String, String>();
		for (String id : authors.keySet()) {
			if (!coAuthors.containsKey(id))
				coAuthors.put(id, null);
		}
		for (String id : authors.keySet()) {
			List<String> coAuthor = (List<String>) ((JSONObject) JSONObject
					.toJSON(authors.get(id))).get("coAuthors");
			if (coAuthor != null)
				for (String aid : coAuthor) {
					if (!coAuthors.containsKey(aid))
						coAuthors.put(aid, null);
				}
		}
		System.out.println("all author counts :" + coAuthors.size());
		int i = 0;
		for (String id : coAuthors.keySet()) {
			getNewAuthor(id);
			System.out.println((i++) + ":" + id);
		}
	}
	
	public void getAuthorsUpdates() throws IOException {
		Map<String, DBObject> authors = mongo.getAllAuthorOld();
		// Map<String, String> authors = IOUtil.read2Map("d:/data.txt");
		Map<String, String> coAuthors = new HashMap<String, String>();
		for (String id : authors.keySet()) {
			if (!coAuthors.containsKey(id))
				coAuthors.put(id, null);
		}
		for (String id : authors.keySet()) {
			List<String> coAuthor = (List<String>) ((JSONObject) JSONObject
					.toJSON(authors.get(id))).get("coAuthors");
			if (coAuthor != null)
				for (String aid : coAuthor) {
					if (!coAuthors.containsKey(aid))
						coAuthors.put(aid, null);
				}
		}
		System.out.println("all author counts :" + coAuthors.size());
		int i = 0;
		for (String id : coAuthors.keySet()) {
			getNewAuthorTest(id);
			System.out.println((i++) + ":" + id);
		}
	}

	public void getNewAuthor(String id) {
		if (id != null) {
			if (mongo.findAuthor(id) == null) {
				// 向数据库添加抓取的用户和文章数据,
				// 并添加索引,写在spider中
				Author a = spideAuthor(id);
				// 向索引数据库添加缩影数据
				// index.updateIndex(a);
			} else {
				// 爬虫数据库中已经存在的数据，对其进行索引的添加
				Author a = mongo.findAuthor(id);
				index.updateIndex(a);
			}
		}
	}

	public void getNewAuthorTest(String id) {
		if (id != null) {
			// if (mongo.findAuthor(id) == null) {
			// 向数据库添加抓取的用户和文章数据,
			// 并添加索引,写在spider中
			Author a = spideAuthor(id);
			// 向索引数据库添加缩影数据
			// index.updateIndex(a);
		}
		// else
		// {
		// //爬虫数据库中已经存在的数据，对其进行索引的添加
		// Author a = mongo.findAuthor(id);
		// index.updateIndex(a);
		// }

	}

	public static void main(String[] args) throws IOException {
		SpiderService service = (SpiderService) SpringBeanFactory
				.getBean("spiderService");
		//service.getNewAuthorTest("Iss3wgUAAAAJ");
		service.getAuthorsUpdates();
	}
}
