package com.myoffice.myapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StoreController {

	@RequestMapping(value="/store", method = RequestMethod.GET)
	public ModelAndView test(){
		ModelAndView model = new ModelAndView("store");
		
		return model;
	}
}