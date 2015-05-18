package com.myoffice.myapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ImplementController {

	@RequestMapping(value = "/implement", method = RequestMethod.GET)
	public ModelAndView test(){
		ModelAndView model = new ModelAndView("implement");
		
		return  model;
	}
}
