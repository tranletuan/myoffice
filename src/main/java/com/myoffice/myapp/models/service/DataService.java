package com.myoffice.myapp.models.service;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.myoffice.myapp.models.dao.document.DocumentDao;
import com.myoffice.myapp.models.dao.parameter.ParameterDao;
import com.myoffice.myapp.models.dao.role.RoleDao;
import com.myoffice.myapp.models.dao.unit.UnitDao;
import com.myoffice.myapp.models.dao.user.UserDao;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.Unit;
import com.myoffice.myapp.models.dto.User;

@Service
@Transactional
public class DataService {

	public void upLoadFile(String saveDirectory, MultipartFile file) throws IllegalStateException, IOException{

		File dirFile = new File(saveDirectory);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}

		String fileName = file.getOriginalFilename();
		if (!"".equalsIgnoreCase(fileName)) {
			file.transferTo(new File(saveDirectory + fileName));
		}
	}

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
	
	public User findUserById(Integer userId){
		return userDao.findUserById(userId);
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
	
	//DOCUMENT TYPE
	public List<DocumentType> findAllDocType(){
		return docDao.findAllDocType();
	}
	
	public DocumentType findDocTypeById(Integer typeId){
		return docDao.findDocTypeById(typeId);
	}
	
	public void saveDocType(DocumentType docType){
		docDao.saveDocType(docType);
	}
	
	//EMERGENCY LEVEL
	public List<EmergencyLevel> findAllEmergencyLevel(){
		return docDao.findAllEmergencyLevel();
	}
	
	public EmergencyLevel findEmergencyLevelById(Integer emergencyLevelId){
		return docDao.findEmergencyLevelById(emergencyLevelId);
	}
	
	public void saveEmergency(EmergencyLevel emergency){
		docDao.saveEmergency(emergency);
	}
	
	public void deleteEmergency(EmergencyLevel emergency){
		docDao.deleteEmergency(emergency);
	}
	
	//PRIVACY LEVEL
	public List<PrivacyLevel> findAllPrivacyLevel(){
		return docDao.findAllPrivacyLevel();
	}
	
	public PrivacyLevel findPrivacyLevelById(Integer privacyLevelId){
		return docDao.findPrivacyLevelById(privacyLevelId);
	}
	
	public void savePrivacyLevel(PrivacyLevel privacy){
		docDao.savePrivacyLevel(privacy);
	}
	
	public void deletePrivacyLevel(PrivacyLevel privacy){
		docDao.deletePrivacyLevel(privacy);
	}
	
	//=====================
	@Autowired
	private ParameterDao paramDao;
	
	public Parameter findParameterByName(String paramName){
		return paramDao.findParameterByName(paramName);
	}
	
	public void saveParameter(Parameter param){
		paramDao.saveParameter(param);
	}
	
	public void deleteParameter(Parameter param){
		paramDao.deleteParameter(param);
	}
	
	//=====================
	@Autowired
	private UnitDao unitDao;
	
	public List<Unit> findAllUnit(){
		return unitDao.findAllUnit();
	}
	
	public Unit findUnitById(Integer unitId){
		return unitDao.findUnitById(unitId);
	}
	
	public void saveUnit(Unit unit){
		unitDao.saveUnit(unit);
	}
	
	public void deleteUnit(Unit unit){
		unitDao.deleteUnit(unit);
	}
	
	public List<Unit> findUnitByArray(Integer[] arrId){
		return unitDao.findUnitByArray(arrId);
	}
	
	//Ogran
	
    public List<Organ> findAllOrgan(){
    	return unitDao.findAllOrgan();
    }
	
	public Organ findOrganById(Integer organId){
		return unitDao.findOrganById(organId);
	}
	
	public void saveOrgan(Organ organ){
		unitDao.saveOrgan(organ);
	}
	
	public void deleteOrgan(Organ organ){
		unitDao.deleteOrgan(organ);
	}
	
	//=====================
	@Autowired
	private RoleDao roleDao;
	
    public Role findRoleByName(String roleName){
    	return roleDao.findRoleByName(roleName);
    }
	
	public List<Role> findAllRoles(){
		return roleDao.findAllRoles();
	}
	
	public void saveRole(Role role){
		roleDao.saveRole(role);
	}
	
	public void deleteRole(Role role){
		roleDao.deleteRole(role);
	}
	
	public List<Role> findRolesByArrId(Integer[] rolesId){
		return roleDao.findRolesByArrId(rolesId);
	}
	
	public Role findRoleById(Integer roleId){
		return roleDao.findRoleById(roleId);
	}
	
	
}
