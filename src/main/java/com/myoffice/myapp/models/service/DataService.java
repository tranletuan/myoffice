package com.myoffice.myapp.models.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.myoffice.myapp.controllers.FlowController;
import com.myoffice.myapp.models.dao.assign.AssignContentDao;
import com.myoffice.myapp.models.dao.document.DocumentDao;
import com.myoffice.myapp.models.dao.level.LevelDao;
import com.myoffice.myapp.models.dao.parameter.ParameterDao;
import com.myoffice.myapp.models.dao.role.RoleDao;
import com.myoffice.myapp.models.dao.unit.UnitDao;
import com.myoffice.myapp.models.dao.user.UserDao;
import com.myoffice.myapp.models.dto.AssignContent;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentFile;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Level;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.OrganType;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.models.dto.Unit;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.support.DocTypeMenuItem;
import com.myoffice.myapp.support.TenureMenuItem;

@Service
@Transactional
public class DataService {

	private static final Logger logger = LoggerFactory
			.getLogger(DataService.class);
	
	public void upLoadFile(String saveDirectory, MultipartFile file, String fileName) throws IllegalStateException, IOException{
		try {
			File dirFile = new File(saveDirectory);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}

			if (fileName == null) {
				fileName = file.getOriginalFilename();
			}

			if (!"".equalsIgnoreCase(fileName)) {
				file.transferTo(new File(saveDirectory + File.separator
						+ fileName));
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	
	}
	
	public void downLoadFile(String filePath, HttpServletResponse response){
		try {
			File f = new File(filePath);
			InputStream is = new FileInputStream(filePath);
			response.addHeader("Content-Disposition", "attachment; filename=" + f.getName());
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
			
		} catch (IOException e) {
			e.getMessage();
		}
	}

	@Autowired
	private UserDao userDao;
	
	public List<User> findUserByArrRoleShortName(Integer organId, String[] arrRoleShortName){
		return userDao.findUserByArrRoleShortName(organId, arrRoleShortName);
	}
	
	public List<User> findUserByArrRoleShortName(Integer organId, String[] arrRoleShortName, Integer levelValue){
		return userDao.findUserByArrRoleShortName(organId, arrRoleShortName, levelValue);
	}

	public List<User> findAllUsers(Integer organId, Integer roleId){
		return userDao.findAllUsers(organId, roleId);
	}
	
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
	
	/*public Integer countDocument(boolean incoming, boolean completed, Integer docTypeId){
		return docDao.countDocument(incoming, completed, docTypeId);
	}
	*/
	/*public List<Document> findWaitingDocByType(boolean incoming, boolean completed, Integer docTypeId){
		return docDao.findWaitingDocByType(incoming, completed, docTypeId);
	}*/
	
	public Document findDocumentById(Integer docId){
		return docDao.findDocumentById(docId);
	}
	
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
	
	public List<Document> findDocumentBy(Integer organId, Integer tenureId, Integer docTypeId, 
			Boolean completed, int firstResult, int maxResult, Boolean enabled){
		return docDao.findDocumentBy(organId, tenureId, docTypeId, completed, firstResult, maxResult, enabled);
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
	
	public EmergencyLevel findEmergencyLevelById(Integer emeId){
		return docDao.findEmergencyLevelById(emeId);
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
	
	public PrivacyLevel findPrivacyLevelById(Integer privacyId){
		return docDao.findPrivacyLevelById(privacyId);
	}
	
	public void savePrivacyLevel(PrivacyLevel privacy){
		docDao.savePrivacyLevel(privacy);
	}
	
	public void deletePrivacyLevel(PrivacyLevel privacy){
		docDao.deletePrivacyLevel(privacy);
	}
	
	//TENURE
	public List<Tenure> findAllTenure(){
		return docDao.findAllTenure();
	}
		
	public Tenure findTenureById(Integer tenureId){
		return docDao.findTenureById(tenureId);
	}
		
	public void saveTenure(Tenure tenure){
		docDao.saveTenure(tenure);
	}
		
	public void deleteTenure(Tenure tenure){
		docDao.deleteTenure(tenure);
	}
	
	public Tenure findLastTenure(){
		return docDao.findLastTenure();
	}
	
	//NUMBER
	public Integer findMaxNumber(Integer tenureId, Integer docTypeId, Integer organId, boolean incoming){
		return docDao.findMaxNumber(tenureId, docTypeId, organId, incoming);
	}
	
	public boolean isDocumentExist(Integer organId, String docPath){
		return docDao.isDocumentExist(organId, docPath);
	}
	
	//Document Recipient
	public void saveDocRecipient(DocumentRecipient docRec){
		docDao.saveDocRecipient(docRec);
	}
	
	public Integer findMaxDocRecNumber(Integer tenureId, Integer organId){
		return docDao.findMaxDocRecNumber(tenureId, organId);
	}
	
	public DocumentRecipient findDocRecipient(Integer docId, Integer organId){
		return docDao.findDocRecipient(docId, organId);
	}
	
	public List<DocumentRecipient> findDocRecipient(Integer organId,
			Integer tenureId, Integer docTypeId, Boolean completed, int firstResult, int maxResult){
		return docDao.findDocRecipient(organId, tenureId, docTypeId, completed, firstResult, maxResult);
	}

	
	public List<DocumentRecipient> findDocRecByAssignDate(Integer organId, Boolean completed, Date start, Date end){
		return docDao.findDocRecByAssignDate(organId, completed, start, end);
	}
	
	//FILE
	public DocumentFile findNewestDocFile(Integer docId){
		return docDao.findNewestDocFile(docId);
	}
	
	public void saveDocFile(DocumentFile docFile){
		docDao.saveDocFile(docFile);
	}
	
	public List<DocumentFile> findAllFile(Integer docId){
		return docDao.findAllFile(docId);
	}
	
	public DocumentFile findDocFileById(Integer docFileId){
		return docDao.findDocFileById(docFileId);
	}
	
	//DOCTYPE MENU
	public List<DocTypeMenuItem> findMenuDocOut(Integer organId, boolean completed, Integer tenureId) {
		return docDao.findMenuDocOut(organId, completed, tenureId);
	}
	
	public List<DocTypeMenuItem> findMenuDocIn(Integer organId, boolean completed, Integer tenureId) {
		return docDao.findMenuDocIn(organId, completed, tenureId);
	}
	
	//TENURE MENU
	public List<TenureMenuItem> findMenuTenureOut(Integer organId) {
		return docDao.findMenuTenureOut(organId);
	}
	
	public List<TenureMenuItem> findMenuTenureIn(Integer organId) {
		return docDao.findMenuTenureIn(organId);
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
	
	public List<Organ> findOrganByArray(Integer[] arrId){
		return unitDao.findOrganByArray(arrId);
	}
	
	//ORGAN TYPE
	public List<OrganType> findAllOrganType(){
		return unitDao.findAllOrganType();
	}
	
	public OrganType findOrganTypeById(Integer organTypeId){
		return unitDao.findOrganTypeById(organTypeId);
	}
	
	public void saveOrganType(OrganType organType){
		unitDao.saveOrganType(organType);
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
	
	public Role findRoleByShortName(String shortName){
		return roleDao.findRoleByShortName(shortName);
	}
	
	public List<Role> findRolesByArrShortName(String [] arrShortName){
		return roleDao.findRolesByArrShortName(arrShortName);
	}
	//=====================
	@Autowired
	private AssignContentDao assContentDao;

	public List<AssignContent> findAllAssignContent(){
		return assContentDao.findAllAssignContent();
	}
	
	public void saveAssignContent(AssignContent AssignContent){
		assContentDao.saveAssignContent(AssignContent);
	}
	
	public void deleteAssignContent(AssignContent AssignContent){
		assContentDao.deleteAssignContent(AssignContent);
	}
	
	public AssignContent findAssignContentById(Integer assContentId){
		return assContentDao.findAssignContentById(assContentId);
	}
	
	//======================
	@Autowired LevelDao levelDao;
	
	public List<Level> findAllLevel() {
		return levelDao.findAllLevel(); 
	}
	
	public Level findLevelById(Integer levelId) {
		return levelDao.findLevelById(levelId);
	}
	
	public void saveLevel(Level level){
		levelDao.saveLevel(level);
	}
	
	
}
