package com.myoffice.myapp.models.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	
	@OneToMany(mappedBy = "docType")
	private Set<Number> numbers = new HashSet<Number>();
	
	public DocumentType() {
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

	public Set<Number> getNumbers() {
		return numbers;
	}

	public void setNumbers(Set<Number> numbers) {
		this.numbers = numbers;
	}
}
