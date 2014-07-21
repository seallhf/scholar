package com.pojo;

/**
 * 不同领域中发表的论文情况
 * @author seal
 */
import org.springframework.stereotype.Component;

@Component
public class PaperType {

	private String domain;

	private int aconf;

	private int bconf;

	private int cconf;

	private int ajounal;

	private int bjounal;

	private int cjounal;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
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

}
