package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "emergency_level")
public class EmergencyLevel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emergency_id", nullable = false, unique = true)
	private Integer emeId;
	
	@Column(name = "emergency_name", nullable = false, unique = true, length = 60)
	private String emeName;
	
	@Column(name = "description")
	private String description;

	public EmergencyLevel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmeName() {
		return emeName;
	}

	public void setEmeName(String emeName) {
		this.emeName = emeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getEmeId() {
		return emeId;
	}
	
	
}
