package com.myoffice.myapp.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.OrganType;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.models.dto.Unit;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataConfig;
import com.myoffice.myapp.utils.UtilMethod;

@Controller
@RequestMapping(value = "/admin")
public class AdminController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminController.class);

	//USER
	@RequestMapping(value = "/user_list", method = RequestMethod.GET)
	public ModelAndView userList() {
		ModelAndView model = new ModelAndView("admin/user-list");

		List<User> userList = dataService.findAllUsers();
		List<Role> roleList = dataService.findAllRoles();
		List<Unit> unitList = dataService.findAllUnit();
		List<Organ> organList = dataService.findAllOrgan();
		
		model.addObject("userList", userList);
		model.addObject("roleList", roleList);
		model.addObject("unitList", unitList);
		model.addObject("organList", organList);
		
		return model;
	}

	@RequestMapping(value = "/save_user", method = RequestMethod.POST)
	public ModelAndView saveUser(
			@RequestParam("userId") Integer userId,
			@RequestParam("userName") String userName,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "roles", required = false) Integer[] rolesId,
			@RequestParam(value = "enabled", required = false) boolean enabled,
			@RequestParam(value = "organId", required = false) Integer organId) {
		ModelAndView model = new ModelAndView("redirect:user_list");
		User user = new User();

		if (userId > 0) {
			user = dataService.findUserById(userId);
		}

		// userName
		user.setUsername(userName);

		// password
		if (password != "") {
			user.setPassword(password);
		}

		// roles
		if (rolesId != null) {
			Set<Role> roles = new HashSet<Role>(
					dataService.findRolesByArrId(rolesId));
			user.setRoles(roles);
		}

		// enabled
		user.setEnabled(enabled);
		
		if(organId > 0){
			user.setOrgan(dataService.findOrganById(organId));
		}
		
		dataService.saveUser(user);
		return model;
	}

	//ROLE
	@RequestMapping(value = "/role_list", method = RequestMethod.GET)
	public ModelAndView roleList() {
		ModelAndView model = new ModelAndView("admin/role-list");

		List<Role> roleList = dataService.findAllRoles();

		model.addObject("roleList", roleList);

		return model;
	}

	@RequestMapping(value = "/save_role", method = RequestMethod.POST)
	public ModelAndView saveRole(
			@RequestParam("roleId") Integer roleId,
			@RequestParam("roleName") String roleName,
			@RequestParam("fullName") String fullName,
			@RequestParam("shortName") String shortName) {
		ModelAndView model = new ModelAndView("redirect:role_list");
		Role role = new Role();
		if (roleId > 0) {
			role = dataService.findRoleById(roleId);
		}

		role.setRoleName(roleName);
		role.setFullName(fullName);
		role.setShortName(shortName);

		dataService.saveRole(role);

		return model;
	}

	//ORGAN
	@RequestMapping(value = "/organ_list", method = RequestMethod.GET)
	public ModelAndView organList(){
		ModelAndView model = new ModelAndView("admin/organ-list");
		List<Organ> organList = dataService.findAllOrgan();
		List<OrganType> organTypeList = dataService.findAllOrganType();
		List<Unit> unitList = dataService.findAllUnit();
		
		model.addObject("organList", organList);
		model.addObject("organTypeList", organTypeList);
		model.addObject("unitList", unitList);
		return model;
	}
	
	@RequestMapping(value = "/save_organ", method = RequestMethod.POST)
	public ModelAndView saveOrgan(
			@RequestParam("organId") Integer organId,
			@RequestParam("organName") String organName,
			@RequestParam("shortName") String shortName,
			@RequestParam("email") String email,
			@RequestParam(value = "phoneNumber", required = false) String phoneNumber,
			@RequestParam("unitId") Integer unitId,
			@RequestParam("organTypeId") Integer organTypeId){
		ModelAndView model = new ModelAndView("redirect:organ_list");
		Organ organ = new Organ();
		
		if(organId > 0) {
			organ = dataService.findOrganById(organId);
		}
		
		organ.setOrganName(organName);
		organ.setShortName(shortName);
		organ.setEmail(email);
		organ.setPhoneNumber(phoneNumber);
		organ.setOrganType(dataService.findOrganTypeById(organTypeId));
		organ.setUnit(dataService.findUnitById(unitId));
		
		dataService.saveOrgan(organ);
		return model;
	}
	
	//ORGAN TYPE
	@RequestMapping(value = "/organ_type_list", method = RequestMethod.GET)
	public ModelAndView organTypeList(){
		ModelAndView model = new ModelAndView("admin/organ-type-list");
		List<OrganType> organTypeList = dataService.findAllOrganType();
		model.addObject("organTypeList", organTypeList);
		return model;
	}
	
	@RequestMapping(value = "/save_organ_type", method = RequestMethod.POST)
	public ModelAndView saveOrganType(
			@RequestParam("organTypeId") Integer organTypeId,
			@RequestParam("organTypeName") String organTypeName,
			@RequestParam("shortName") String shortName){
		ModelAndView model = new ModelAndView("redirect:organ_type_list");
		OrganType organType = new OrganType();
		if(organTypeId > 0){
			organType = dataService.findOrganTypeById(organTypeId);
		}
		
		organType.setOrganTypeName(organTypeName);
		organType.setShortName(shortName);
		
		dataService.saveOrganType(organType);
		return model;
	}

	//UNIT
	@RequestMapping(value = "/unit_list", method = RequestMethod.GET)
	public ModelAndView unitList(){
		ModelAndView model = new ModelAndView("admin/unit-list");
		List<Unit> unitList = dataService.findAllUnit();
		model.addObject("unitList", unitList);
		return model;
	}
	
	@RequestMapping(value = "/save_unit", method = RequestMethod.POST)
	public ModelAndView saveUnit(
			@RequestParam("unitId") Integer unitId,
			@RequestParam("unitName") String unitName,
			@RequestParam("address") String address,
			@RequestParam("shortName") String shortName){
		ModelAndView model = new ModelAndView("redirect:unit_list");
		Unit unit = new Unit();
		if(unitId > 0){
			unit = dataService.findUnitById(unitId);
		}
		
		unit.setUnitName(unitName);
		unit.setAddress(address);
		unit.setShortName(shortName);
		
		dataService.saveUnit(unit);
		return model;
	}
	
	//DOCTYPE
	@RequestMapping(value ="/doctype_list", method = RequestMethod.GET)
	public ModelAndView docTypeList(){
		ModelAndView model = new ModelAndView("admin/doctype-list");
		List<DocumentType> docTypeList = dataService.findAllDocType();
		model.addObject("docTypeList", docTypeList);
		return model;
	}
	
	@RequestMapping(value = "/save_doctype", method = RequestMethod.POST)
	public ModelAndView saveDocType(
			@RequestParam("docTypeId") Integer docTypeId,
			@RequestParam("docTypeName") String docTypeName,
			@RequestParam("shortName") String shortName,
			@RequestParam(value = "description", required = false) String description){
		ModelAndView model = new ModelAndView("redirect:doctype_list");
		DocumentType docType = new DocumentType();
		if(docTypeId > 0) {
			docType = dataService.findDocTypeById(docTypeId);
		}
		
		docType.setDocTypeName(docTypeName);
		docType.setShortName(shortName);
		docType.setDescription(description);
		
		dataService.saveDocType(docType);
		
		return model;
	}
	
	//PRIVACY
	@RequestMapping(value = "/privacy_list", method = RequestMethod.GET)
	public ModelAndView privacyList(){
		ModelAndView model = new ModelAndView("admin/privacy-list");
		List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
		model.addObject("privacyList", privacyList);
		return model;
	}
	
	@RequestMapping(value = "/save_privacy", method = RequestMethod.POST)
	public ModelAndView savePrivacy(
			@RequestParam("privacyId") Integer privacyId,
			@RequestParam("privacyName") String privacyName,
			@RequestParam("description") String description){
		ModelAndView model = new ModelAndView("redirect:privacy_list");
		PrivacyLevel privacy = new PrivacyLevel();
		if(privacyId > 0){
			privacy = dataService.findPrivacyLevelById(privacyId);
		}
		
		privacy.setPrivacyName(privacyName);
		privacy.setDescription(description);
		
		dataService.savePrivacyLevel(privacy);
		
		return model;
	}
	
	//EMERGENCY
	@RequestMapping(value = "/emergency_list", method = RequestMethod.GET)
	public ModelAndView emergencyList(){
		ModelAndView model = new ModelAndView("admin/emergency-list");
		List<EmergencyLevel> emergencyList = dataService.findAllEmergencyLevel();
		model.addObject("emergencyList", emergencyList);
		return model;
	}
	
	@RequestMapping(value = "/save_emergency", method = RequestMethod.POST)
	public ModelAndView saveEmergency(
			@RequestParam("emeId") Integer emeId,
			@RequestParam("emeName") String emeName,
			@RequestParam("description") String description){
		ModelAndView model = new ModelAndView("redirect:emergency_list");
		EmergencyLevel emergency = new EmergencyLevel();
		if(emeId > 0){
			emergency = dataService.findEmergencyLevelById(emeId);
		}
		
		emergency.setEmeName(emeName);
		emergency.setDescription(description);
		
		dataService.saveEmergency(emergency);
		return model;
	}
	
	//FLOW
	@RequestMapping(value = "/flow", method = RequestMethod.GET)
	public ModelAndView flowView(){
		ModelAndView model = new ModelAndView("admin/flow");
		model.addObject("flow_out", flowUtil.getProcessDefinitionId(DataConfig.RSC_NAME_FLOW_OUT, DataConfig.PROC_DEF_KEY_FLOW_OUT));
		model.addObject("flow_in", flowUtil.getProcessDefinitionId(DataConfig.RSC_NAME_FLOW_IN, DataConfig.PROC_DEF_KEY_FLOW_IN));
		return model;
	}
	
	@RequestMapping(value = "/upload_flow_out", method = RequestMethod.POST)
	public ModelAndView uploadFlowOut(@RequestParam("file") MultipartFile file)
			throws IllegalStateException, IOException {
		ModelAndView model = new ModelAndView("redirect:flow");
		if (!file.isEmpty()) {
			dataService.upLoadFile(DataConfig.DIR_FILE_FLOW, file, DataConfig.RSC_NAME_FLOW_OUT);
			flowUtil.deployProcess(DataConfig.RSC_NAME_FLOW_OUT,
					DataConfig.DIR_FILE_FLOW + DataConfig.RSC_NAME_FLOW_OUT);
		}
		return model;
	}
	
	@RequestMapping(value = "/upload_flow_in", method = RequestMethod.POST)
	public ModelAndView uploadFlowIn(@RequestParam("file") MultipartFile file)
			throws IllegalStateException, IOException {
		ModelAndView model = new ModelAndView("redirect:flow");
		if (!file.isEmpty()) {
			dataService.upLoadFile(DataConfig.DIR_FILE_FLOW, file, DataConfig.RSC_NAME_FLOW_IN);
			flowUtil.deployProcess(DataConfig.RSC_NAME_FLOW_IN,
					DataConfig.DIR_FILE_FLOW + DataConfig.RSC_NAME_FLOW_IN);
		}
		return model;
	}
	
	@RequestMapping(value = "/download_flow_out", method = RequestMethod.GET)
	public void getFile(HttpServletResponse response) throws IOException {
		dataService.downLoadFile(DataConfig.DIR_SERVER_NAME_FLOW_OUT, response);
	}
	
	@RequestMapping (value = "/download_flow_in", method = RequestMethod.GET)
	public void downloadFlowIn(HttpServletResponse response) throws IOException{
		dataService.downLoadFile(DataConfig.DIR_SERVER_NAME_FLOW_IN, response);
	}
	
	//TENURE
	@RequestMapping(value = "/tenure_list")
	public ModelAndView tenureList(){
		ModelAndView model = new ModelAndView("admin/tenure-list");
		List<Tenure> tenureList = dataService.findAllTenure();
		model.addObject("tenureList", tenureList);
		return model;
	}
	
	@RequestMapping(value = "/save_tenure", method = RequestMethod.POST)
	public ModelAndView saveTenure(
			@RequestParam("tenureId") Integer tenureId,
			@RequestParam("tenureName") String tenureName,
			@RequestParam("timeStart") String timeStart, 
			@RequestParam("timeEnd") String timeEnd, 
			RedirectAttributes reAttr){
		ModelAndView model = new ModelAndView("redirect:tenure_list");
		
		Tenure tenure = new Tenure();
		if(tenureId > 0){
			tenure = dataService.findTenureById(tenureId);
		}
		
		Date dateStart;
		Date dateEnd;
		String dateFormat = "dd-MM-yyyy";
		
		try {
			 dateStart = UtilMethod.toDate(timeStart, dateFormat);
			 dateEnd = UtilMethod.toDate(timeEnd, dateFormat);
			 
			 logger.info(timeStart);
			 logger.info(dateStart.toString());
			 
			 logger.info(timeEnd);
			 logger.info(dateEnd.toString());
			 
			 if(dateStart.compareTo(dateEnd) > -1){
				 reAttr.addFlashAttribute("error", true);
				 reAttr.addFlashAttribute("errorMessage", "Thêm thất bại, ngày kết thúc phải lớn hơn ngày bắt đầu!");
				 return model;
			 }
			 
		} catch (ParseException e) {
			reAttr.addFlashAttribute("error", true);
			reAttr.addFlashAttribute("errorMessage", "Lỗi hệ thống");
			e.printStackTrace();
			return model;
		}
		
		tenure.setTenureName(tenureName);
		tenure.setTimeStart(dateStart);
		tenure.setTimeEnd(dateEnd);
		
		dataService.saveTenure(tenure);
		return model;
	}
}
