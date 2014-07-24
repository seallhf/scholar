package com.spider.service;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mongodb.DBObject;
import com.pojo.Author;
import com.pojo.Paper;
import com.search.service.AuthorService;
import com.spider.ScholarSpider;
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
			if (aid != null && !aid.equals("")) {
				spideAuthor(aid);
				// break;
			}
			System.out.println((i++) + ":" + aid);
		}
	}

	public void spideAuthor(String aid) {
		Author a = service.findAuthor(aid);
		if (a == null) {
			a = spider.getAuthorDetail(aid);
			if (a != null) {
				service.insertAuthor(a);
				System.out.println("author :" + aid + "---->inserted!");
			}
		} else if (a.getPapers() == null) {
			a = spider.getAuthorDetail(aid);
			if (a != null) {
				service.updateAuthorData(aid, "papers", a);
				System.out.println("author :" + aid + "---->updated!");
			}
		}
		// Map<String, String> papers = a.getPapers();
		// if (papers != null && papers.size() > 0)
		// for (String pid : papers.keySet()) {
		// if (service.findPaper(pid) == null) {
		// Paper paper = spider.getPaperDetail(aid,
		// papers.get(pid));
		// if (paper != null) {
		// service.insertPaper(paper);
		// System.out.println(aid + ":" + pid);
		// }
		// }
		// }
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
		service.updateAuthors();
	}
}
