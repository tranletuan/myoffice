package com.myoffice.myapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/admin")
public class AdminController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminController.class);
	
	@RequestMapping(value = "/user_list", method = RequestMethod.GET)
	public ModelAndView userList(){
		ModelAndView model = new ModelAndView("admin/user-list");
		
		
		return model;
	}

}
