package com.pojo;

import java.util.List;

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

	private List<String> papers;

	private String homePage;

	private boolean isYoungEnough;

	private AuthorPaper authorPaper;

	public AuthorPaper getAuthorPaper() {
		return authorPaper;
	}

	public void setAuthorPaper(AuthorPaper authorPaper) {
		this.authorPaper = authorPaper;
	}

	public void setYoungEnough(boolean isYoungEnough) {
		this.isYoungEnough = isYoungEnough;
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

	public List<String> getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(List<String> coAuthors) {
		this.coAuthors = coAuthors;
	}

	public List<String> getPapers() {
		return papers;
	}

	public void setPapers(List<String> papers) {
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

	public boolean getIsYoungEnough() {
		return isYoungEnough;
	}

	public void setIsYoungEnough(boolean isYoungEnough) {
		this.isYoungEnough = isYoungEnough;
	}
}
