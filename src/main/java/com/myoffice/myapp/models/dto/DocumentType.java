package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "document_type")
public class DocumentType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "type_id", nullable = false, unique = true)
	private Integer typeId;
	
	@Column(name = "type_name", nullable = false, unique = true)
	private String typeName;
	
	@Column(name = "short_name", nullable = false, unique = true, length = 20)
	private String shortName;
	
	@Column(name = "description", columnDefinition="varchar(400)")
	private String description;
	

	public DocumentType() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Integer getTypeId() {
		return typeId;
	}

	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
