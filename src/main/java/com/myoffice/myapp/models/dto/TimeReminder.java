package com.myoffice.myapp.models.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.myoffice.myapp.models.service.DataConfig;
import com.myoffice.myapp.utils.UtilMethod;

@Entity
@Table(name = "time_reminder")
public class TimeReminder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "remind_id", nullable = false, unique = true)
	private Integer remindId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "remind_time", nullable = false)
	private Date remindTime;
	
	@Column(name = "remind_subject", nullable = false)
	private String remindSubject;
	
	@Column(name = "remind_content", nullable = false)
	private String remindContent;
	
	@Column(name = "pre_task_mail", nullable = false)
	private String preTaskMail;
	
	@Column(name = "cur_task_mail", nullable = false)
	private String curTaskMail;
	
	@Column(name = "completed")
	private Boolean completed = false;

	public Date getRemindTime() {
		return remindTime;
	}
	
	public String getRemindeTimeString() {
		return UtilMethod.dateToString(remindTime, DataConfig.DATE_FORMAT_STRING);
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

	public String getRemindContent() {
		return remindContent;
	}

	public void setRemindContent(String remindContent) {
		this.remindContent = remindContent;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Integer getRemindId() {
		return remindId;
	}

	public String getPreTaskMail() {
		return preTaskMail;
	}

	public void setPreTaskMail(String preTaskMail) {
		this.preTaskMail = preTaskMail;
	}

	public String getCurTaskMail() {
		return curTaskMail;
	}

	public void setCurTaskMail(String curTaskMail) {
		this.curTaskMail = curTaskMail;
	}

	public String getRemindSubject() {
		return remindSubject;
	}

	public void setRemindSubject(String remindSubject) {
		this.remindSubject = remindSubject;
	}
	
	
	
	
}
