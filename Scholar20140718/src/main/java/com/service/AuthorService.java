package com.service;

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
import com.utils.spring.SpringBeanFactory;

@Service
public class AuthorService {

	@Resource
	private MongoService mongoService;

	@Resource
	private MysqlService mysqlService;

	/**
	 * 更新用户的论文等级
	 * 
	 * @param aid
	 *            作者ID
	 */
	public AuthorPaper createAuthorPaper(String aid) {
		Author author = mongoService.findAuthor(aid);
		if (author != null) {
			return createAuthorPaper(author);
		}
		return null;
	}

	public AuthorPage createAuthorPage(Author a) {
		AuthorPage authorPage = (AuthorPage) SpringBeanFactory
				.getBean("authorPage");
		authorPage.setAid(a.getAid());
		authorPage.setCiteindex(a.getCiteindex());
		authorPage.setCoBigAuthors(caculateBigCoauthor(a));
		authorPage.setCollege(a.getCollege());
		authorPage.setEmail(a.getEmail());
		authorPage.setHomePage(a.getHomePage());
		authorPage.setImgUrl(a.getImgUrl());
		authorPage.setMostFarmousPaper(caculateFamousPaper(a));
		authorPage.setName(a.getName());
		authorPage.setTags(a.getTags());
		authorPage.setYear(caculateFirstYear(a));
		return authorPage;
	}

	/**
	 * 
	 * 将作者对象作为传入创建用户的文章对象
	 * 
	 * @param author
	 * @return
	 */
	public AuthorPaper createAuthorPaper(Author author) {
		Map<String, String> paperList = author.getPapers();
		AuthorPaper authorPaper = (AuthorPaper) SpringBeanFactory
				.getBean("authorPaper");
		int aconf = 0, bconf = 0, cconf = 0;
		int ajounal = 0, bjounal = 0, cjounal = 0;
		if (paperList != null)
			for (String pid : paperList.keySet()) {
				Paper paper = mongoService.findPaper(pid);
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

	public AuthorRank createAuthorRank(String aid) {
		Author author = mongoService.findAuthor(aid);
		if (author != null) {
			return createAuthorRank(author);
		}
		return null;
	}

	public AuthorRank createAuthorRank(Author author) {
		Map<String, String> paperList = author.getPapers();
		AuthorRank authorRank = (AuthorRank) SpringBeanFactory
				.getBean("authorRank");
		List<Paper> papers = new ArrayList<Paper>();
		if (paperList != null)
			for (String pid : paperList.keySet()) {
				Paper paper = mongoService.findPaper(pid);
				if (paper != null) {
					paper.setDate("1000/11/11");
					papers.add(paper);
				}
			}
		AuthorPaper authorPaper = mysqlService.findAuthorPaper(author.getAid());
		if (authorPaper != null) {
			authorRank.setApapers(authorPaper.getAconf()
					+ authorPaper.getAjounal());
			authorRank.setRank(caculateRank(author, authorPaper));
		} else {
			authorRank.setApapers(0);
			authorRank.setRank(0f);
		}
		authorRank.setYear(caculateFirstYear(author));
		authorRank.setPapers(papers);
		authorRank.setAid(author.getAid());
		authorRank.setTags(author.getTags());
		authorRank.setLocation(author.getCollege());

		return authorRank;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int caculateFirstYear(Author author) {
		List<Integer> list = new ArrayList<Integer>();
		if (author.getPapers() != null) {
			for (String pid : author.getPapers().keySet()) {
				Paper paper = mongoService.findPaper(pid);
				if (paper != null && paper.getDate() != null) {
					String years = paper.getDate();
					list.add(Integer.parseInt(years.substring(0, 4)));
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
	public String caculateFamousPaper(Author author) {
		List<Paper> list = new ArrayList<Paper>();
		if (author.getPapers() != null) {
			for (String pid : author.getPapers().keySet()) {
				Paper paper = mongoService.findPaper(pid);
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
	public String caculateBigCoauthor(Author author) {
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
		AuthorService a = (AuthorService) SpringBeanFactory
				.getBean("authorService");
		AuthorRank rank = a.createAuthorRank("xrRQNgEAAAAJ");
		System.out.println(rank.getYear());
	}
}
