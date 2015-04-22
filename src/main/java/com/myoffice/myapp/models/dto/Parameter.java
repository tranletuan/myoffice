package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parameter")
public class Parameter {

	@Id
	@Column(name = "param_name", unique = true, nullable = false, length = 60)
	private String paramName;

	@Column(name = "value", unique = true, nullable = false, length = 60)
	private String value;
	
		
	public Parameter() {
	}

	public Parameter(String paramName, String value) {
		this.paramName = paramName;
		this.value = value;
	}


	public String getParamName() {
		return paramName;
	}
	

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
