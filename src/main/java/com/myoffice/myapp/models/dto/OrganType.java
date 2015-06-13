package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "organ_type")
public class OrganType {

	@Id
	@Column(name = "organ_type_id", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer organTypeId;
	
	@Column(name = "organ_type_name", nullable = false, unique = true)
	private String organTypeName;
	
	@Column(name = "short_name", nullable = false, unique = true)
	private String shortName;

	public OrganType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOrganTypeName() {
		return organTypeName;
	}

	public void setOrganTypeName(String organTypeName) {
		this.organTypeName = organTypeName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getOrganTypeId() {
		return organTypeId;
	}
	
	
	
	
}
