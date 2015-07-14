package com.myoffice.myapp.models.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.myoffice.myapp.utils.UtilMethod;

@Entity
@Table(name = "document_recipient")
public class DocumentRecipient implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "document_id", nullable = false)
	private Document document;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organ_id", nullable = false)
	private Organ organ;

	@Temporal(TemporalType.DATE)
	@Column(name = "receive_time", columnDefinition = "DATETIME")
	private Date receiveTime;

	@Column(name = "number")
	private Integer number;
	
	@Column(name = "processId", nullable = false, unique = true)
	private String processInstanceId;
	
	@Column(name = "completed")
	private boolean completed = false;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "assign_content_id")
	private AssignContent assignContent;
	
	public DocumentRecipient() {
		super();
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	public String getReceiveTimeString() {
		return UtilMethod.dateToString(receiveTime, "dd-MM-yyyy");
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public AssignContent getAssignContent() {
		return assignContent;
	}

	public void setAssignContent(AssignContent assignContent) {
		this.assignContent = assignContent;
	}

	

}
