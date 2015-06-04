package com.myoffice.myapp.models.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myoffice.myapp.models.dao.document.DocumentDao;
import com.myoffice.myapp.models.dao.parameter.ParameterDao;
import com.myoffice.myapp.models.dao.role.RoleDao;
import com.myoffice.myapp.models.dao.unit.UnitDao;
import com.myoffice.myapp.models.dao.user.UserDao;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.Unit;
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
	
	//EMERGENCY LEVEL
	public List<EmergencyLevel> findAllEmergencyLevel(){
		return docDao.findAllEmergencyLevel();
	}
	
	public EmergencyLevel findEmergencyLevelById(Integer emergencyLevelId){
		return docDao.findEmergencyLevelById(emergencyLevelId);
	}
	
	//PRIVACY LEVEL
	public List<PrivacyLevel> findAllPrivacyLevel(){
		return docDao.findAllPrivacyLevel();
	}
	
	public PrivacyLevel findPrivacyLevelById(Integer privacyLevelId){
		return docDao.findPrivacyLevelById(privacyLevelId);
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
