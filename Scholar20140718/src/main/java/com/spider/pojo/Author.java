package com.spider.pojo;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Author {

	private String imgUrl;

	private String aid;

	private String name;

	private String college;

	private String email;

	private String tags;

	private Integer citeindex;

	private List<String> coAuthors;

	// google中论文的访问地址列表
	private Map<String, String> papers;

	private String homePage;

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Integer getCiteindex() {
		return citeindex;
	}

	public void setCiteindex(Integer citeindex) {
		this.citeindex = citeindex;
	}

	public List<String> getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(List<String> coAuthors) {
		this.coAuthors = coAuthors;
	}

	public Map<String, String> getPapers() {
		return papers;
	}

	public void setPapers(Map<String, String> papers) {
		this.papers = papers;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
}
