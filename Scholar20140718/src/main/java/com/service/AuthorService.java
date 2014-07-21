package com.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pojo.Author;
import com.pojo.AuthorPaper;
import com.pojo.AuthorRank;
import com.pojo.Paper;
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
	public Author createAuthorPaper(String aid) {
		Author author = mongoService.findAuthor(aid);
		if (author != null) {
			return createAuthorPaper(author);
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
	public Author createAuthorPaper(Author author) {
		List<String> paperList = author.getPapers();
		AuthorPaper authorPaper = (AuthorPaper) SpringBeanFactory
				.getBean("authorPaper");
		int aconf = 0, bconf = 0, cconf = 0;
		int ajounal = 0, bjounal = 0, cjounal = 0;
		if (paperList != null)
			for (String pid : paperList) {
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
		author.setAuthorPaper(authorPaper);
		return author;
	}

	public AuthorRank getAuthorRank(String aid) {
		Author author = mongoService.findAuthor(aid);
		if (author != null) {
			return getAuthorRank(author);
		}
		return null;
	}

	public AuthorRank getAuthorRank(Author author) {
		List<String> paperList = author.getPapers();
		AuthorRank authorRank = (AuthorRank) SpringBeanFactory
				.getBean("authorRank");
		List<Paper> papers = new ArrayList<Paper>();
		if (paperList != null)
			for (String pid : paperList) {
				Paper paper = mongoService.findPaper(pid);					
				if (paper != null) {
					paper.setDate("1000/11/11");
					papers.add(paper);
				}
			}
		if (author.getAuthorPaper() != null) {
			authorRank.setApapers(author.getAuthorPaper().getAconf()
					+ author.getAuthorPaper().getAjounal());
			authorRank.setRank(caculateRank(author));
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
	private int caculateFirstYear(Author author) {
		List<Integer> list = new ArrayList<Integer>();
		if (author.getPapers() != null) {
			for (String pid : author.getPapers()) {
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
			Collections.sort(list);
			if (list.size() > 0)
				return list.get(0);
			return 1900;
		}
		return 1900;
	}

	private float caculateRank(Author author) {
		float rank = 0.01f
				* author.getCiteindex()
				+ 0.6f
				* (author.getAuthorPaper().getAconf() + author.getAuthorPaper()
						.getAjounal())
				+ 0.3f
				* (author.getAuthorPaper().getBconf() + author.getAuthorPaper()
						.getBjounal())
				+ 0.09f
				* (author.getAuthorPaper().getCconf() + author.getAuthorPaper()
						.getCjounal());
		return rank;
	}

	public static void main(String[] args) {
		AuthorService a = (AuthorService) SpringBeanFactory
				.getBean("authorService");
		AuthorRank rank = a.getAuthorRank("xrRQNgEAAAAJ");
		System.out.println(rank.getYear());
	}
}
