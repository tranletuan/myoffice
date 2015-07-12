package com.myoffice.myapp.support;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.User;

public class DocOutInfo {

	private Document doc;
	private User user;
	
	public DocOutInfo(Document doc, User user) {
		super();
		this.doc = doc;
		this.user = user;
	}
	
	
	public DocOutInfo() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
