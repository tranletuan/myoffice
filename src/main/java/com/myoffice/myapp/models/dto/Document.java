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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.myoffice.myapp.utils.UtilMethod;

@Entity
@Table(name = "document")
public class Document {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "document_id", nullable = false, unique = true)
	private Integer docId;

	@Column(name = "title", length = 60)
	private String title;
	
	@Column(name = "number_sign", length = 60)
	private String numberSign;
	
	@Column(name = "number")
	private Integer number;
	
	@Column(name = "departments", length = 60)
	private String departments;
	
	@Column(name = "document_name", length = 100)
	private String docName;

	@Temporal(TemporalType.DATE)
	@Column(name = "release_time", columnDefinition="DATETIME")
	private Date releaseTime;

	@Column(name = "epitome" , nullable = false, columnDefinition="varchar(1000)")
	private String epitome;

	@Column(name = "processId")
	private String processInstanceId;
	
	@Column(name = "completed")
	private boolean completed = false;

	@Column(name = "incoming")
	private boolean incoming = false;
	
	@Column(name = "comment", columnDefinition="varchar(1500)")
	private String comment;
	
	@Column(name = "is_sended")
	private boolean sended = false;
	
	@Column(name = "is_enabled")
	private boolean enabled = true;
	
	@Column(name = "note", columnDefinition="varchar(500)")
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "document")
	private Set<DocumentRecipient> recipients = new HashSet<DocumentRecipient>(0);

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "document")
	private Set<DocumentFile> files = new HashSet<DocumentFile>(0);
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "time_reminder_id", insertable = true, updatable = true)
	private TimeReminder timeReminder;
	
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
	
	public String getReleaseTimeString() {
		return UtilMethod.dateToString(releaseTime, "dd-MM-yyyy");
	}

	public String getEpitome() {
		return epitome;
	}

	public void setEpitome(String epitome) {
		this.epitome = epitome;
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

	public Set<DocumentRecipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(Set<DocumentRecipient> recipients) {
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

	public Set<DocumentFile> getFiles() {
		return files;
	}

	public void setFiles(Set<DocumentFile> files) {
		this.files = files;
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public boolean isSended() {
		return sended;
	}

	public void setSended(boolean sended) {
		this.sended = sended;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getNote() {
		return note;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public TimeReminder getTimeReminder() {
		return timeReminder;
	}

	public void setTimeReminder(TimeReminder timeReminder) {
		this.timeReminder = timeReminder;
	}

	
	
	


}
