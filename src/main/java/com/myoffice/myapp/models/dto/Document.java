package com.myoffice.myapp.models.dto;

import java.util.Date;
import java.util.HashSet;
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
	
	@Column(name = "document_name", nullable = false, length = 100)
	private String docName;

	@Temporal(TemporalType.DATE)
	@Column(name = "release_time")
	private Date releaseTime;

	@Column(name = "epitome")
	private String epitome;

	@Column(name = "document_path", nullable = false)
	private String docPath;

	@Column(name = "processId", nullable = false, length = 60)
	private String processInstanceId;
	
	@Column(name = "completed")
	private boolean completed = false;

	@Column(name = "incomming")
	private boolean incoming = false;

	@Column(name = "description")
	private String description;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "type_id", nullable = false, insertable = true, updatable = true)
	private DocumentType docType;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "unit_id", nullable = false, insertable = true, updatable = true)
	private Unit unit;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "privacy_id")
	private PrivacyLevel privacyLevel;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "emergency_id")
	private EmergencyLevel emergencyLevel;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "document_receivers", joinColumns = @JoinColumn(name = "document_id"), inverseJoinColumns = @JoinColumn(name = "receiver_id"))
	private Set<Receiver> receivers = new HashSet<Receiver>(0);

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "document_recipient_units", joinColumns = @JoinColumn(name = "document_id"), inverseJoinColumns = @JoinColumn(name = "unit_id"))
	private Set<Unit> recipientUnits = new HashSet<Unit>(0);

	public Document() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public DocumentType getDocType() {
		return docType;
	}

	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public PrivacyLevel getPrivacyLevel() {
		return privacyLevel;
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

	public Set<Receiver> getReceivers() {
		return receivers;
	}

	public void setReceivers(Set<Receiver> receivers) {
		this.receivers = receivers;
	}

	public Set<Unit> getRecipientUnits() {
		return recipientUnits;
	}

	public void setRecipientUnits(Set<Unit> recipientUnits) {
		this.recipientUnits = recipientUnits;
	}

	public Integer getDocId() {
		return docId;
	}
}
