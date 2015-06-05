package com.myoffice.myapp.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.Unit;
import com.myoffice.myapp.models.dto.User;

@Controller
@RequestMapping(value = "/admin")
public class AdminController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminController.class);

	@RequestMapping(value = "/user_list", method = RequestMethod.GET)
	public ModelAndView userList() {
		ModelAndView model = new ModelAndView("admin/user-list");

		List<User> userList = dataService.findAllUsers();
		List<Role> roleList = dataService.findAllRoles();
		List<Unit> unitList = dataService.findAllUnit();

		model.addObject("userList", userList);
		model.addObject("roleList", roleList);
		model.addObject("unitList", unitList);

		return model;
	}

	@RequestMapping(value = "/save_user", method = RequestMethod.POST)
	public ModelAndView saveUser(
			@RequestParam("userId") Integer userId,
			@RequestParam("userName") String userName,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam("unitId") Integer unitId,
			@RequestParam(value = "roles", required = false) Integer[] rolesId,
			@RequestParam(value = "enabled", required = false) boolean enabled) {
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

		// unit
		user.setUnit(dataService.findUnitById(unitId));

		// roles
		if (rolesId != null) {
			Set<Role> roles = new HashSet<Role>(
					dataService.findRolesByArrId(rolesId));
			user.setRoles(roles);
		}

		// enabled
		user.setEnabled(enabled);
		dataService.saveUser(user);

		return model;
	}

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

	@RequestMapping(value = "/organ_list", method = RequestMethod.GET)
	public ModelAndView organList(){
		ModelAndView model = new ModelAndView("admin/organ-list");
		List<Organ> organList = dataService.findAllOrgan();
		model.addObject("organList", organList);
		return model;
	}
	
	@RequestMapping(value = "/save_organ", method = RequestMethod.POST)
	public ModelAndView saveOrgan(
			@RequestParam("organId") Integer organId,
			@RequestParam("organName") String organName,
			@RequestParam("shortName") String shortName,
			@RequestParam("address") String address){
		ModelAndView model = new ModelAndView("redirect:organ_list");
		Organ organ = new Organ();
		
		if(organId > 0) {
			organ = dataService.findOrganById(organId);
		}
		
		organ.setOrganName(organName);
		organ.setShortName(shortName);
		organ.setAddress(address);
		
		dataService.saveOrgan(organ);
		return model;
	}

	@RequestMapping(value = "/unit_list", method = RequestMethod.GET)
	public ModelAndView unitList(){
		ModelAndView model = new ModelAndView("admin/unit-list");
		List<Unit> unitList = dataService.findAllUnit();
		List<Organ> organList = dataService.findAllOrgan();
		
		model.addObject("unitList", unitList);
		model.addObject("organList", organList);
		
		return model;
	}
	
	@RequestMapping(value = "/save_unit", method = RequestMethod.POST)
	public ModelAndView saveUnit(
			@RequestParam("unitId") Integer unitId,
			@RequestParam("unitName") String unitName, 
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("email") String email, 
			@RequestParam("organId") Integer organId,
			@RequestParam("shortName") String shortName){
		ModelAndView model = new ModelAndView("redirect:unit_list");
		Unit unit = new Unit();
		if(unitId > 0){
			unit = dataService.findUnitById(unitId);
		}
		
		unit.setUnitName(unitName);
		unit.setPhoneNumber(phoneNumber);
		unit.setEmail(email);
		unit.setOrgan(dataService.findOrganById(organId));
		unit.setShortName(shortName);
		
		dataService.saveUnit(unit);
		return model;
	}

	@RequestMapping(value ="/doctype_list", method = RequestMethod.GET)
	public ModelAndView docTypeList(){
		ModelAndView model = new ModelAndView("admin/doctype-list");
		List<DocumentType> docTypeList = dataService.findAllDocType();
		model.addObject("docTypeList", docTypeList);
		return model;
	}
	
	@RequestMapping(value = "/save_doctype", method = RequestMethod.POST)
	public ModelAndView saveDocType(
			@RequestParam("typeId") Integer typeId,
			@RequestParam("typeName") String typeName,
			@RequestParam("shortName") String shortName,
			@RequestParam(value = "description", required = false) String description){
		ModelAndView model = new ModelAndView("redirect:doctype_list");
		DocumentType docType = new DocumentType();
		if(typeId > 0) {
			docType = dataService.findDocTypeById(typeId);
		}
		
		docType.setTypeName(typeName);
		docType.setShortName(shortName);
		docType.setDescription(description);
	
		dataService.saveDocType(docType);
		
		return model;
	}
}
