package com.myoffice.myapp.support;

import com.myoffice.myapp.models.dto.DocumentType;

public class DocTypeMenuItem {

	private  DocumentType docType;
	private Long count;
	
	
	
	public DocTypeMenuItem(DocumentType docType, Long count) {
		super();
		this.docType = docType;
		this.count = count;
	}
	public DocumentType getDocType() {
		return docType;
	}
	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
