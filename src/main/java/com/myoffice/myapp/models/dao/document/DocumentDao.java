package com.myoffice.myapp.models.dao.document;

import java.util.List;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Tenure;

public interface DocumentDao {
	
	Document findDocumentByName(String name);
	
	List<Document> findAllDocument();
	
	void saveDocument(Document doc);
	
	void deleteDocument(Document doc);
	
	List<Document> findDocumentBy(DocumentType docType, boolean completed, boolean incomming);
	
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
	
	void saveTenure(Tenure tenure);
	
	void deleteTenure(Tenure tenure);

}
