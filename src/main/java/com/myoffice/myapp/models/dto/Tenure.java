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

@Entity
@Table(name = "tenure")
public class Tenure {
	
	@Id
	@Column(name = "tenure_id", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tenureId;
	
	@Column(name = "tenure_name", nullable = false, unique = true)
	private String tenureName;
	
	@Column(name = "time_start", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date timeStart;
	
	@Column(name = "time_end", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date timeEnd;
	
	public Tenure() {
	}

	public String getTenureName() {
		return tenureName;
	}

	public void setTenureName(String tenureName) {
		this.tenureName = tenureName;
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

	
}
