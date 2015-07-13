package com.myoffice.myapp.support;

import java.util.List;

import com.myoffice.myapp.models.dto.Document;

public class ListDocOut {
	
	private Long count;
	private List<Document> docList;
	private String url;
	private int step = 10;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public List<Document> getDocList() {
		return docList;
	}
	public void setDocList(List<Document> docList) {
		this.docList = docList;
	}
	
	
}
