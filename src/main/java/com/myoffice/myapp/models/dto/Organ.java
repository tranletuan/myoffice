package com.myoffice.myapp.models.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

	@Column(name = "phone_number", length = 20)
	private String phoneNumber;
	
	@Column(name = "email", length = 50)
	private String email;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "unit_id", nullable = false)
	private Unit unit;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "organ_type_id", nullable = false)
	private OrganType organType;

	public Organ() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getOrganId() {
		return organId;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public OrganType getOrganType() {
		return organType;
	}

	public void setOrganType(OrganType organType) {
		this.organType = organType;
	}

}
