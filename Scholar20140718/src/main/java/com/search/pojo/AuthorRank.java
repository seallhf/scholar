package com.search.pojo;

import java.util.List;

import org.springframework.stereotype.Component;

import com.spider.pojo.Paper;

@Component
public class AuthorRank {

	private String aid;

	private String tags;
		
	//作者的A类文章数
	private int apapers;
	
	//作者的第一篇文章年份
	private String year;

	private List<Paper> papers;
	
	private float rank;
	
	private String location;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<Paper> getPapers() {
		return papers;
	}

	public void setPapers(List<Paper> papers) {
		this.papers = papers;
	}

	public float getRank() {
		return rank;
	}

	public void setRank(float rank) {
		this.rank = rank;
	}

	public int getApapers() {
		return apapers;
	}

	public void setApapers(int apapers) {
		this.apapers = apapers;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
}
