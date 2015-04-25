package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "privacy_level")
public class PrivacyLevel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "privacy_id", nullable = false, unique = true)
	private Integer privacyId;

	@Column(name = "privacy_name", nullable = false, unique = true, length = 60)
	private String privacyName;

	@Column(name = "description")
	private String description;

	public PrivacyLevel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPrivacyName() {
		return privacyName;
	}

	public void setPrivacyName(String privacyName) {
		this.privacyName = privacyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrivacyId() {
		return privacyId;
	}
}
