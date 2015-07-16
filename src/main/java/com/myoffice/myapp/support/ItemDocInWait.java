package com.myoffice.myapp.support;

import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.User;

public class ItemDocInWait {

	private DocumentRecipient docRec;
	private User user;
	public ItemDocInWait(DocumentRecipient docRec, User user) {
		super();
		this.docRec = docRec;
		this.user = user;
	}
	public DocumentRecipient getDocRec() {
		return docRec;
	}
	public void setDocRec(DocumentRecipient docRec) {
		this.docRec = docRec;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
