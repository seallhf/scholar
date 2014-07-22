package com.spider;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.pojo.Author;
import com.pojo.Authors;
import com.pojo.Paper;
import com.service.AuthorService;
import com.service.MongoService;
import com.utils.IOUtil;
import com.utils.spring.SpringBeanFactory;

@Service
public class SpiderService {

	@Resource
	ScholarSpider spider;
	@Resource
	MongoService service;
	@Resource
	AuthorService aservice;

	public void updateAuthors() {
		Map<String, DBObject> authors = service.getAllAuthor();
		int i = 0;
		for (String aid : authors.keySet()) {
			if (aid != null) {
				Authors as = JSONObject.toJavaObject(
						(JSONObject) JSONObject.toJSON(authors.get(aid)),
						Authors.class);
				if (service.findAuthorN(aid) == null) {
					Author a = (Author) SpringBeanFactory.getBean("author");
					a.setAid(as.getAid());
					a.setCiteindex(as.getCiteindex());
					a.setCoAuthors(as.getCoAuthors());
					a.setCollege(as.getCollege());
					a.setEmail(as.getEmail());
					a.setHomePage(as.getHomePage());
					a.setImgUrl(as.getImgUrl());
					a.setName(as.getName());
					a.setTags(as.getTags());
					Map<String, String> papers = new HashMap<String, String>();
					List<String> paperlist = as.getPapers();
					if (paperlist != null && paperlist.size() > 0)
						for (String pid : paperlist) {
							Paper paper = spider.getPaperDetail(aid, pid);
							if (paper != null) {
								papers.put(paper.getPid(), pid);
								if (service.findPaperN(paper.getPid()) == null)
									service.insertPaper(paper);
								System.out.println(aid + ":" + pid);
							}
						}
					a.setPapers(papers);
					service.insertAuthor(a);
				}
				System.out.println((i++) + ":" + aid);
			}
		}
	}
	
	

	public void getNewAuthors() throws IOException {
		// Map<String, String> authors = spider.getSeniorUser("chinese");
		Map<String, String> authors = IOUtil.read2Map("d:/data.txt");
		int i = 0;
		for (String id : authors.keySet()) {
			if (id != null) {
				Author _author;
				if (service.findAuthor(id) == null) {
					_author = spider.getAuthorDetail(id);
					service.insertAuthor(_author);
				} else {
					_author = service.findAuthor(id);
				}
				if (_author.getPapers() != null) {
					for (String pid : _author.getPapers().keySet()) {
						if (service.findPaper(pid) == null) {
							Paper paper = spider.getPaperDetail(
									_author.getAid(),
									_author.getPapers().get(pid));
							if (paper != null)
								service.insertPaper(paper);
						}
						System.out.println((i) + "_" + _author.getAid() + ":"
								+ pid);
					}
				}
			}
			System.out.println((i++) + ":" + id);
		}
	}

	public static void main(String[] args) {
		SpiderService service = (SpiderService) SpringBeanFactory
				.getBean("spiderService");
		// service.updatePapers();
		service.updateAuthors();
	}
}
