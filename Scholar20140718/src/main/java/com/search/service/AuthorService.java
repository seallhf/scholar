package com.search.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pojo.Author;
import com.pojo.AuthorPage;
import com.pojo.AuthorPaper;
import com.pojo.AuthorRank;
import com.pojo.Paper;
import com.spider.service.MongoService;
import com.utils.spring.SpringBeanFactory;

@Service
public class AuthorService {

	@Resource
	private MongoService mongoService;

	/**
	 * 更新用户的论文等级
	 * 
	 * @param aid
	 *            作者ID
	 */
	public AuthorPaper createAuthorPaper(String aid, List<Paper> papers) {
		Author author = mongoService.findAuthor(aid);
		if (author != null) {
			return createAuthorPaper(author, papers);
		}
		return null;
	}

	/**
	 * 
	 * 将作者对象作为传入创建用户的文章对象
	 * 
	 * @param author
	 * @return
	 */
	public AuthorPaper createAuthorPaper(Author author, List<Paper> papers) {
		AuthorPaper authorPaper = (AuthorPaper) SpringBeanFactory
				.getBean("authorPaper");
		int aconf = 0, bconf = 0, cconf = 0;
		int ajounal = 0, bjounal = 0, cjounal = 0;
		if (papers == null) {
			papers = new ArrayList<Paper>();
			Map<String, String> paperList = author.getPapers();
			if (paperList != null)
				for (String pid : paperList.keySet()) {
					Paper paper = mongoService.findPaper(pid);
					papers.add(paper);
				}
		}
		for (Paper paper : papers) {
			if (paper != null) {
				String tag = paper.getTag();
				if (tag != null && !tag.equals("")) {
					String[] lines = tag.split("\t");
					String grade, type;
					if (lines.length > 4) {
						// 论文的等级
						grade = tag.split("\t")[0];
						// 论文的类型
						type = tag.split("\t")[4];
					} else {
						// System.out.println(tag);
						grade = tag.split("\t")[0];
						// 论文的类型
						type = tag.split("\t")[3];

					}
					if (type.equals("期刊")) {
						if (grade.equals("A"))
							ajounal++;
						else if (grade.equals("B"))
							bjounal++;
						else if (grade.equals("C"))
							cjounal++;
					} else if (type.equals("会议")) {
						if (grade.equals("A"))
							aconf++;
						else if (grade.equals("B"))
							bconf++;
						else if (grade.equals("C"))
							cconf++;
					}
				}
			}
		}
		authorPaper.setAconf(aconf);
		authorPaper.setAjounal(ajounal);
		authorPaper.setBconf(bconf);
		authorPaper.setBjounal(bjounal);
		authorPaper.setCconf(cconf);
		authorPaper.setCjounal(cjounal);
		authorPaper.setAid(author.getAid());
		return authorPaper;
	}

	public AuthorPage createAuthorPage(String aid, List<Paper> papers) {
		Author a = mongoService.findAuthor(aid);
		if (a != null) {
			return createAuthorPage(a, papers);
		}
		return null;
	}

	public AuthorPage createAuthorPage(Author a, List<Paper> papers) {
		AuthorPage authorPage = (AuthorPage) SpringBeanFactory
				.getBean("authorPage");
		if (papers == null) {
			papers = new ArrayList<Paper>();
			if (a.getPapers() != null) {
				for (String pid : a.getPapers().keySet()) {
					Paper paper = mongoService.findPaper(pid);
					if (paper != null && paper.getCiteIndex() != null) {
						papers.add(paper);
					}
				}
			}
		}
		authorPage.setAid(a.getAid());
		authorPage.setCiteindex(a.getCiteindex());
		authorPage.setCoBigAuthors(caculateBigCoauthor(a));
		authorPage.setCollege(a.getCollege());
		authorPage.setEmail(a.getEmail());
		authorPage.setHomePage(a.getHomePage());
		authorPage.setImgUrl(a.getImgUrl());
		authorPage.setMostFarmousPaper(caculateFamousPaper(papers));
		authorPage.setName(a.getName());
		authorPage.setTags(a.getTags());
		authorPage.setYear(caculateFirstYear(papers));
		return authorPage;
	}

	public AuthorRank createAuthorRank(String aid, List<Paper> papers) {
		Author author = mongoService.findAuthor(aid);
		if (author != null) {
			return createAuthorRank(author, papers);
		}
		return null;
	}

	public AuthorRank createAuthorRank(Author author, List<Paper> papers) {
		Map<String, String> paperList = author.getPapers();
		AuthorRank authorRank = (AuthorRank) SpringBeanFactory
				.getBean("authorRank");
		if (papers == null) {
			papers = new ArrayList<Paper>();
			if (paperList != null)
				for (String pid : paperList.keySet()) {
					Paper paper = mongoService.findPaper(pid);
					paper.setDate("1900/11/11");
					papers.add(paper);
				}
		}
		AuthorPaper authorPaper = mongoService.findAuthorPaper(author.getAid());
		if (authorPaper != null) {
			authorRank.setApapers(authorPaper.getAconf()
					+ authorPaper.getAjounal());
			authorRank.setRank(caculateRank(author, authorPaper));
		} else {
			authorRank.setApapers(0);
			authorRank.setRank(0f);
		}
		AuthorPage authorPage = mongoService.findAuthorPage(author.getAid());
		authorRank.setYear(authorPage.getYear());
		authorRank.setPapers(papers);
		authorRank.setAid(author.getAid());
		authorRank.setTags(author.getTags());
		authorRank.setLocation(author.getCollege());
		return authorRank;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private int caculateFirstYear(List<Paper> papers) {
		List<Integer> list = new ArrayList<Integer>();
		if (papers != null) {
			for (Paper paper : papers) {
				if (paper != null && paper.getDate() != null
						&& !paper.getDate().equals("")) {
					String years = paper.getDate();
					if (years.length() > 4)
						list.add(Integer.parseInt(years.substring(0, 4)));
					else
						list.add(Integer.parseInt(years));
				}
			}
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					return new Integer(String.valueOf(o1))
							.compareTo(new Integer(String.valueOf(o2)));
				}
			});
			if (list.size() > 0)
				return list.get(0);
			return 1900;
		}
		return 1900;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String caculateFamousPaper(List<Paper> papers) {
		List<Paper> list = new ArrayList<Paper>();
		if (papers != null) {
			for (Paper paper : papers) {
				if (paper != null && paper.getCiteIndex() != null) {
					list.add(paper);
				}
			}
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((Paper) o2).getCiteIndex().compareTo(
							((Paper) o1).getCiteIndex());
				}
			});
			if (list.size() > 0)
				return ((Paper) list.get(0)).getPid();
			return null;
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String caculateBigCoauthor(Author author) {
		List<Author> list = new ArrayList<Author>();
		if (author.getCoAuthors() != null) {
			for (String aid : author.getCoAuthors()) {
				Author a = mongoService.findAuthor(aid);
				if (a != null && a.getCiteindex() != null) {
					list.add(a);
				}
			}
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					return ((Author) o2).getCiteindex().compareTo(
							((Author) o1).getCiteindex());
				}
			});
			if (list.size() > 0)
				return ((Author) list.get(0)).getAid();
			return null;
		}
		return null;
	}

	private float caculateRank(Author author, AuthorPaper authorPaper) {
		float rank = 0.01f * author.getCiteindex() + 0.6f
				* (authorPaper.getAconf() + authorPaper.getAjounal()) + 0.3f
				* (authorPaper.getBconf() + authorPaper.getBjounal()) + 0.09f
				* (authorPaper.getCconf() + authorPaper.getCjounal());
		return rank;
	}

	public static void main(String[] args) {

	}
}
