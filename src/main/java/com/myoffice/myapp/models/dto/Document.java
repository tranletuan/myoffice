package com.myoffice.myapp.models.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "document")
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "document_id", nullable = false, unique = true)
	private Integer docId;

	@Column(name = "title", nullable = false, length = 60)
	private String title;
	
	@Column(name = "number_sign", nullable = false, length = 60, unique = true)
	private String numberSign;
	
	@Column(name = "number", nullable = false)
	private Integer number;
	
	@Column(name = "document_name", nullable = false, length = 100)
	private String docName;

	@Temporal(TemporalType.DATE)
	@Column(name = "release_time", columnDefinition="DATETIME")
	private Date releaseTime;

	@Column(name = "epitome" , nullable = false, columnDefinition="varchar(500)")
	private String epitome;

	@Column(name = "document_path", unique = true)
	private String docPath;

	@Column(name = "processId", nullable = false, length = 60)
	private String processInstanceId;
	
	@Column(name = "completed")
	private boolean completed = false;

	@Column(name = "incoming")
	private boolean incoming = false;
	
	@Column(name = "comment", columnDefinition="varchar(1500)")
	private String comment;
	
	@Column(name = "note")
	private String note;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "doc_type_id", nullable = false)
	private DocumentType docType;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "tenure_id", nullable = false)
	private Tenure tenure;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "organ_id")
	private Organ organ;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "privacy_id")
	private PrivacyLevel privacyLevel;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "emergency_id")
	private EmergencyLevel emergencyLevel;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "document_recipient_organ", joinColumns = @JoinColumn(name = "document_id"), inverseJoinColumns = @JoinColumn(name = "organ_id"))
	private Set<Organ> recipients = new HashSet<Organ>(0);

	public Document() {
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNumberSign() {
		return numberSign;
	}

	public void setNumberSign(String numberSign) {
		this.numberSign = numberSign;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getEpitome() {
		return epitome;
	}

	public void setEpitome(String epitome) {
		this.epitome = epitome;
	}

	public String getDocPath() {
		return docPath;
	}

	public void setDocPath(String docPath) {
		this.docPath = docPath;
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

	public boolean isIncoming() {
		return incoming;
	}

	public void setIncoming(boolean incoming) {
		this.incoming = incoming;
	}

	public DocumentType getDocType() {
		return docType;
	}

	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}

	public PrivacyLevel getPrivacyLevel() {
		return privacyLevel;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setPrivacyLevel(PrivacyLevel privacyLevel) {
		this.privacyLevel = privacyLevel;
	}

	public EmergencyLevel getEmergencyLevel() {
		return emergencyLevel;
	}

	public void setEmergencyLevel(EmergencyLevel emergencyLevel) {
		this.emergencyLevel = emergencyLevel;
	}

	public Set<Organ> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<Organ> recipients) {
		this.recipients = recipients;
	}

	public Integer getDocId() {
		return docId;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public Tenure getTenure() {
		return tenure;
	}

	public void setTenure(Tenure tenure) {
		this.tenure = tenure;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
