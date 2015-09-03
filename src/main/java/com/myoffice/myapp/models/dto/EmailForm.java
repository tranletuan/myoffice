package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "email_form")
public class EmailForm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "form_id", unique = true, nullable = false)
	private Integer formId;
	
	@Column(name = "subject", nullable = false)
	private String subject;
	
	@Column(name = "body", nullable = false, columnDefinition = "VARCHAR(2500)")
	private String body;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Integer getFormId() {
		return formId;
	}
	
	
	
}
