package com.spider;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DBObject;
import com.pojo.Author;
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

	public void updatePapers() {
		Map<String, DBObject> authors = service.getAllAuthor();
		int i = 0;
		for (String id : authors.keySet()) {
			if (id != null) {
				Author author = JSONObject.toJavaObject(
						(JSONObject) JSONObject.toJSON(authors.get(id)),
						Author.class);
				if (author.getPapers() != null) {
					for (String pid : author.getPapers()) {
						System.out.println((i) + "_" + author.getAid() + ":"
								+ pid);
						if (service.findPaper(pid) == null) {
							Paper paper = spider.getPaperDetail(
									author.getAid(), pid);
							if (paper != null)
								service.insertPaper(paper);

						}
					}
				}
				i++;
			}
		}
	}

	public void updateAuthors() {
		Map<String, DBObject> authors = service.getAllAuthor();
		int i = 0;
		for (String id : authors.keySet()) {
			if (id != null) {
				Author author = JSONObject.toJavaObject(
						(JSONObject) JSONObject.toJSON(authors.get(id)),
						Author.class);
				if (author.getCoAuthors() != null) {
					for (String coaid : author.getCoAuthors()) {
						System.out.println((i++) + ":" + coaid);
						if (service.findAuthor(coaid) == null) {
							Author _author = spider.getAuthorDetail(coaid);
							service.insertAuthor(_author);
							if (_author.getIsYoungEnough()) {
								List<String> paperList = _author.getPapers();
								if (paperList != null) {
									for (String pid : paperList) {
										if (service.findObject(service.PAPER,
												"pid", pid) == null) {
											Paper paper = spider
													.getPaperDetail(id, pid);
											service.insertPaper(paper);
											System.out.println(pid);
										}
									}
								}
							}
						}
					}
				}
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
					for (String pid : _author.getPapers()) {
						if (service.findPaper(pid) == null) {
							Paper paper = spider.getPaperDetail(
									_author.getAid(), pid);
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

	public void insertNewAuthor(String aid) {
		Author _author;
		if (service.findAuthor(aid) == null) {
			_author = spider.getAuthorDetail(aid);
		} else {
			_author = service.findAuthor(aid);

		}
		if (_author.getPapers() != null) {
			for (String pid : _author.getPapers()) {
				if (service.findPaper(pid) == null) {
					Paper paper = spider.getPaperDetail(_author.getAid(), pid);
					if (paper != null)
						service.insertPaper(paper);
				}
				System.out.println(_author.getAid() + ":" + pid);
			}
		}
		// 添加用户的论文评级
		_author = aservice.createAuthorPaper(_author);

		service.insertAuthor(_author);
	}

	public static void main(String[] args) {
		SpiderService service = (SpiderService) SpringBeanFactory
				.getBean("spiderService");
		// service.updatePapers();
		// service.updateAuthors();
		try {
			service.getNewAuthors();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
