package com.myoffice.myapp.models.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "document_recipient")
public class DocumentRecipient {

	@Id
	@Column(name = "doc_rec_id", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer docRecId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "document_id")
	private Document document;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "organ_id")
	private Organ organ;

	@Temporal(TemporalType.DATE)
	@Column(name = "receive_time", columnDefinition = "DATETIME")
	private Date receiveTime;

	@Column(name = "number")
	private Integer number;

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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getDocRecId() {
		return docRecId;
	}

}
