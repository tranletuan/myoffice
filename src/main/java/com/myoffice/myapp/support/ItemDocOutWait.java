package com.myoffice.myapp.support;

import java.util.Date;

import org.joda.time.Days;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.utils.UtilMethod;

public class ItemDocOutWait {

	private Document doc;
	private User user;
	private Date taskTime;
	private int duration;
	public ItemDocOutWait(Document doc, User user, Date taskTime) {
		super();
		this.doc = doc;
		this.user = user;
		this.taskTime = taskTime;
	}
	
	public ItemDocOutWait() {
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

	public Date getTaskTime() {
		return taskTime;
	}

	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
		this.duration = UtilMethod.betweenTwoDay(taskTime, new Date());
	}
	
	public String getTaskTimeString() {
		return UtilMethod.dateToString(taskTime, "HH:mm:ss dd-MM-yyyy");
	}

	public int getDuration() {
		return duration;
	}
	
}
