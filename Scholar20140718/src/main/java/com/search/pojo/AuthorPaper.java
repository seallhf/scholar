package com.search.pojo;

import org.springframework.stereotype.Component;


/**
 * 用户的全部论文发表情况
 * @author seal
 *
 */
@Component
public class AuthorPaper {

	private String aid;

	private int aconf;

	private int bconf;

	private int cconf;
	
	private int ajounal;

	private int bjounal;

	private int cjounal;

	public String getAid() {
		return aid;
	}

	public int getAconf() {
		return aconf;
	}

	public void setAconf(int aconf) {
		this.aconf = aconf;
	}

	public int getBconf() {
		return bconf;
	}

	public void setBconf(int bconf) {
		this.bconf = bconf;
	}

	public int getCconf() {
		return cconf;
	}

	public void setCconf(int cconf) {
		this.cconf = cconf;
	}

	public int getAjounal() {
		return ajounal;
	}

	public void setAjounal(int ajounal) {
		this.ajounal = ajounal;
	}

	public int getBjounal() {
		return bjounal;
	}

	public void setBjounal(int bjounal) {
		this.bjounal = bjounal;
	}

	public int getCjounal() {
		return cjounal;
	}

	public void setCjounal(int cjounal) {
		this.cjounal = cjounal;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	

}
