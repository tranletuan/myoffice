package com.myoffice.myapp.controllers;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

}
