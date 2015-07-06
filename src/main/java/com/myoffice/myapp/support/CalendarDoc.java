package com.myoffice.myapp.support;

import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.User;

public class CalendarDoc {

	private DocumentRecipient docRec;
	private User assignee;
	public DocumentRecipient getDocRec() {
		return docRec;
	}
	public void setDocRec(DocumentRecipient docRec) {
		this.docRec = docRec;
	}
	public User getAssignee() {
		return assignee;
	}
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}
	
	
	
}
