package com.myoffice.myapp.models.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "number")
public class Number {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "number_id", nullable = false, unique = true)
	private Integer numberId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "tenure_id", nullable = false)
	private Tenure tenure;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "type_id", nullable = false)
	private DocumentType docType;
	
	@JoinColumn(name = "value", nullable = false)
	private Integer value;

	public Number() {
	}
	
	public Integer getNumberId() {
		return numberId;
	}

	public Tenure getTenure() {
		return tenure;
	}

	public void setTenure(Tenure tenure) {
		this.tenure = tenure;
	}

	public DocumentType getDocType() {
		return docType;
	}

	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
