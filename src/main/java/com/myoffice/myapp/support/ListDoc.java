package com.myoffice.myapp.support;

import java.util.List;

import com.myoffice.myapp.models.dto.Document;

public class ListDoc {
	
	private int count;
	private List<Document> docList;
	private String url;
	private int step = 10;
	private int currentNumber = 0;
	
	public int getCurrentNumber() {
		return currentNumber;
	}
	public void setCurrentNumber(int currentNumber) {
		this.currentNumber = currentNumber;
	}
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
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Document> getDocList() {
		return docList;
	}
	public void setDocList(List<Document> docList) {
		this.docList = docList;
	}
	
	
}
