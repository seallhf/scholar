package com.pojo;

import org.springframework.stereotype.Component;

@Component
public class AuthorPage {

	private Integer id;

	private String imgUrl;

	private String aid;

	private String name;

	private String college;

	private String email;

	private String tags;

	private Integer citeindex;

	// 最早发表论文的日期
	private String year;

	// 有合作关系的大节点
	private String coBigAuthors;

	// 最有影响力的论文
	private String famousPaper;

	private String homePage;

	// 是否是一个年轻的学者
	private boolean isYoungEnough;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

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

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCoBigAuthors() {
		return coBigAuthors;
	}

	public void setCoBigAuthors(String coBigAuthors) {
		this.coBigAuthors = coBigAuthors;
	}

	public String getMostFarmousPaper() {
		return famousPaper;
	}

	public void setMostFarmousPaper(String famousPaper) {
		this.famousPaper = famousPaper;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public boolean isYoungEnough() {
		return isYoungEnough;
	}

	public void setYoungEnough(boolean isYoungEnough) {
		this.isYoungEnough = isYoungEnough;
	}
}
