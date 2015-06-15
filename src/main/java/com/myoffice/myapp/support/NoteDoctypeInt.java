package com.myoffice.myapp.support;

import com.myoffice.myapp.models.dto.DocumentType;

public class NoteDoctypeInt {
	
	DocumentType docType;
	Integer value;
	
	public NoteDoctypeInt(DocumentType docType, Integer value) {
		super();
		this.docType = docType;
		this.value = value;
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
