package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "organ")
public class Organ {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "organ_id", nullable = false, unique = true)
	private Integer organId;

	@Column(name = "organ_name", nullable = false, unique = true)
	private String organName;

	@Column(name = "short_name", nullable = false, unique = true, length = 60)
	private String shortName;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "description")
	private String description;

	public Organ() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getOrganId() {
		return organId;
	}

	public void setOrganId(Integer organId) {
		this.organId = organId;
	}

	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
