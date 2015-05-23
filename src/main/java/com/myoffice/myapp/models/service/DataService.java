package com.myoffice.myapp.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myoffice.myapp.models.dao.document.DocumentDao;
import com.myoffice.myapp.models.dao.role.RoleDao;
import com.myoffice.myapp.models.dao.user.UserDao;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.User;

@Service
@Transactional
public class DataService {

	@Autowired
	private UserDao userDao;

	public User findUserByName(String username) {
		return userDao.findUserByName(username);
	}

	public void saveUser(User user) {
		userDao.saveUser(user);
	}

	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

	public void deleteUser(User user) {
		userDao.deleteUser(user);
	}
	
	//===================
	@Autowired
	private DocumentDao docDao;
	
	public Document findDocumentByName(String docName){
		return docDao.findDocumentByName(docName);
	}
	
	public List<Document> findAllDocument() {
		return docDao.findAllDocument();
	}
	
	public void saveDocument(Document doc){
		docDao.saveDocument(doc);
	}
	
	public void deleteDocument(Document doc){
		docDao.deleteDocument(doc);
	}
	
	public List<Document> findDocumentBy(DocumentType docType, boolean completed, boolean incomming){
		return docDao.findDocumentBy(docType, completed, incomming);
	}
	
	public List<DocumentType> findAllDocType(){
		return docDao.findAllDocType();
	}
	
	//=====================
	
	
}
