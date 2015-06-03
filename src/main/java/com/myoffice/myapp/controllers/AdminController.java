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

import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.Unit;
import com.myoffice.myapp.models.dto.User;

@Controller
@RequestMapping(value = "/admin")
public class AdminController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminController.class);
	
	@RequestMapping(value = "/user_list", method = RequestMethod.GET)
	public ModelAndView userList(){
		ModelAndView model = new ModelAndView("admin/user-list");
		
		List<User> userList = dataService.findAllUsers();
		List<Role> roleList = dataService.findAllRoles();
		List<Unit> unitList = dataService.findAllUnit();
		
 		model.addObject("userList", userList);
 		model.addObject("roleList", roleList);
 		model.addObject("unitList", unitList);

		return model;
	}
	
	@RequestMapping(value = "/save_user", method = RequestMethod.GET)
	public ModelAndView saveUser(
			@RequestParam("userId") Integer userId,
			@RequestParam("userName") String userName,
			@RequestParam("password") String password,
			@ModelAttribute("unit") Unit unit,
			@RequestParam("roles") Integer[] rolesId,
			@RequestParam("enabled") boolean enabled){
		ModelAndView model = new ModelAndView("admin/user-list");
		User user = new User();
		Set<Role> roles = new HashSet<Role>(dataService.findRolesByArrId(rolesId));
		
		if(userId != null){
			user = dataService.findUserById(userId);
		}
		
		if(password != null){
			user.setPassword(password);
		}
		
		user.setUsername(userName);
		user.setUnit(unit);
		user.setEnabled(enabled);
		user.setRoles(roles);
		
		logger.info(userId.toString());
		logger.info(userName);
		logger.info(password);
		logger.info(unit.getUnitName());
		logger.info(String.valueOf(enabled));
		
		return model;
	}
	
	
	

}
