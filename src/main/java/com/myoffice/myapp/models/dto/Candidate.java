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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

import com.myoffice.myapp.utils.UtilMethod;

@Entity
@Table(name = "candidate")
public class Candidate {

	@Id
	@Column(name = "candidate_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer candidateId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "time_start", columnDefinition = "DATETIME", nullable = false)
	private Date timeStart;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "time_end", columnDefinition = "DATETIME", nullable = false)
	private Date timeEnd;

	@Column(name = "content", columnDefinition="varchar(1500)", nullable = false)
	private String content;

	@Column(name = "report", columnDefinition="varchar(1500)")
	private String report;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "document_id")
	private Document reportDoc;
	
	public Candidate() {
		super();
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}

	public Integer getCandidateId() {
		return candidateId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Document getReportDoc() {
		return reportDoc;
	}

	public void setReportDoc(Document reportDoc) {
		this.reportDoc = reportDoc;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}
	
	public String getTimeStartString(){
		return UtilMethod.dateToString(timeStart, "dd-MM-yyyy");
	}
	
	public String getTimeEndString(){
		return UtilMethod.dateToString(timeEnd, "dd-MM-yyyy");
	}
	
	
}
