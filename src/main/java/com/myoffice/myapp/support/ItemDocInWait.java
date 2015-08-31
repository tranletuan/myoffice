package com.myoffice.myapp.support;

import java.util.Date;

import javax.swing.text.Utilities;

import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.utils.UtilMethod;

public class ItemDocInWait {

	private DocumentRecipient docRec;
	private User assignee;
	private User owner;
	private User candidate;
	private Date taskTime;
	private int duration;

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

	public User getOwner() {
		return owner;
	}

	public User getCandidate() {
		return candidate;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setCandidate(User candidate) {
		this.candidate = candidate;
	}

	public int getDuration() {
		return duration;
	}

	public Date getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
		this.duration = UtilMethod.betweenTwoDay(taskTime, new Date());
	}
}
