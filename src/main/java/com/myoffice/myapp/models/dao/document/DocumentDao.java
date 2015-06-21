package com.myoffice.myapp.models.dao.document;

import java.util.HashMap;
import java.util.List;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentFile;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.support.NoteDoctypeInt;

public interface DocumentDao {
	
	Document findDocumentById(Integer docId);
	
	Document findDocumentByName(String name);
	
	List<Document> findAllDocument();
	
	void saveDocument(Document doc);
	
	void deleteDocument(Document doc);
	
	//DocumentType
	DocumentType findDocTypeById(Integer typeId);
	
	List<DocumentType> findAllDocType();
	
	void saveDocType(DocumentType docType);
	
	List<NoteDoctypeInt> findWaitingMenu(boolean incoming);

	
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
	
	void saveTenure(Tenure tenure);
	
	void deleteTenure(Tenure tenure);
	
	//Number
	Integer findMaxNumber(Integer tenureId, Integer docTypeId, Integer organId, boolean incoming);
	
	boolean isDocumentExist(Integer organId, String docPath);
	
	//File
	Integer findNewestDocFile(Integer docId);
	
	void saveDocFile(DocumentFile docFile);
	
	List<DocumentFile> findAllFile(Integer docId);
	
	DocumentFile findDocFileById(Integer docFileId);
	
	//Document Recipient
	void saveDocRecipient(DocumentRecipient docRec);


}
