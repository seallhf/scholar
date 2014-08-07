package com.spider.pojo;

import org.springframework.stereotype.Component;

@Component
public class Paper {

	private String pid;

	private String title;

	private String authors;

	private String date;

	private String jounalName;

	private String brief;

	private String url;
	
	private String tag;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private Integer citeIndex;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getJounalName() {
		return jounalName;
	}

	public void setJounalName(String jounalName) {
		this.jounalName = jounalName;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getCiteIndex() {
		return citeIndex;
	}

	public void setCiteIndex(Integer citeIndex) {
		this.citeIndex = citeIndex;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	
}
