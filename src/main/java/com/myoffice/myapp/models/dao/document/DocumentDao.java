package com.myoffice.myapp.models.dao.document;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentFile;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.support.CalendarDoc;
import com.myoffice.myapp.support.DocTypeMenuItem;
import com.myoffice.myapp.support.TenureMenuItem;

public interface DocumentDao {
	
	Document findDocumentById(Integer docId);
	
	Document findDocumentByName(String name);
	
	List<Document> findAllDocument();
	
	void saveDocument(Document doc);
	
	void deleteDocument(Document doc);
	
	List<Document> findDocumentBy(Integer organId, Integer tenureId, 
			Integer docTypeId, Boolean completed, int firstResult, int maxResult, Boolean enabled);
	
	//DocumentType
	DocumentType findDocTypeById(Integer typeId);
	
	List<DocumentType> findAllDocType();
	
	void saveDocType(DocumentType docType);
	
	
	//EmergencyLevel
	List<EmergencyLevel> findAllEmergencyLevel();
	
	EmergencyLevel findEmergencyLevelById(Integer emergencyLevelId);
	
	void saveEmergency(EmergencyLevel emergency);
	
	void deleteEmergency(EmergencyLevel emergency);
	
	//PrivacyLevel
	List<PrivacyLevel> findAllPrivacyLevel();
	
	PrivacyLevel findPrivacyLevelById(Integer privacyLevelId);
	
	void savePrivacyLevel(PrivacyLevel privacy);
	
	void deletePrivacyLevel(PrivacyLevel privacy);
	
	//Tenure
	List<Tenure> findAllTenure();
	
	Tenure findTenureById(Integer tenureId);
	
	Tenure findLastTenure();
	
	void saveTenure(Tenure tenure);
	
	void deleteTenure(Tenure tenure);
	
	//Number
	Integer findMaxNumber(Integer tenureId, Integer docTypeId, Integer organId, boolean incoming);
	
	boolean isDocumentExist(Integer organId, String docPath);
	
	//File
	DocumentFile findNewestDocFile(Integer docId);
	
	void saveDocFile(DocumentFile docFile);
	
	List<DocumentFile> findAllFile(Integer docId);
	
	DocumentFile findDocFileById(Integer docFileId);
	
	//Document Recipient
	void saveDocRecipient(DocumentRecipient docRec);

	DocumentRecipient findDocRecipient(Integer docId, Integer organId);
	
	Integer findMaxDocRecNumber(Integer tenureId, Integer organId);
	
	List<DocumentRecipient> findDocRecipient(Integer organId, Integer tenureId, Integer docTypeId, Boolean completed, int firstResult, int maxResult);
	
	List<DocumentRecipient> findDocRecByAssignDate(Integer organId, Boolean completed, Date start, Date end);
	
	//DOCTYPE MENU
	List<DocTypeMenuItem> findMenuDocOut(Integer organId, boolean completed, Integer tenureId);
	
	List<DocTypeMenuItem> findMenuDocIn(Integer organId, boolean completed, Integer tenureId);
	
	//TENURE MENU
	List<TenureMenuItem> findMenuTenureOut(Integer organId);
	
	List<TenureMenuItem> findMenuTenureIn(Integer organId);
	
} 
