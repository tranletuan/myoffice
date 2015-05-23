package com.myoffice.myapp.models.dao.document;

import java.util.List;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentType;

public interface DocumentDao {
	
	Document findDocumentByName(String name);
	
	List<Document> findAllDocument();
	
	void saveDocument(Document doc);
	
	void deleteDocument(Document doc);
	
	List<Document> findDocumentBy(DocumentType docType, boolean completed, boolean incomming);
	
	List<DocumentType> findAllDocType();
	

}
