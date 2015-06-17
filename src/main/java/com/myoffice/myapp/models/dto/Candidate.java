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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.DateTime;

@Entity
@Table(name = "candidate")
public class Candidate {

	@Id
	@Column(name = "candidate_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer candidateId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time_start", columnDefinition = "DATETIME", nullable = false)
	private Date timeStart;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time_end", columnDefinition = "DATETIME", nullable = false)
	private Date timeEnd;
	
	@OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
	@JoinTable(name = "candidate_user", joinColumns = @JoinColumn(name = "candidate_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> candidateGroup = new HashSet<User>();

	@Column(name = "content", columnDefinition="varchar(1500)", nullable = false)
	private String content;
	
	@Column(name = "completed")
	private boolean completed = false;
	
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

	public Set<User> getCandidateGroup() {
		return candidateGroup;
	}

	public void setCandidateGroup(Set<User> candidateGroup) {
		this.candidateGroup = candidateGroup;
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

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	
	
}
