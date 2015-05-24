package com.myoffice.myapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.service.DataService;

@Controller
public class StoreController {
	
	@Autowired
	private DataService dataService;

	@RequestMapping(value="/store", method = RequestMethod.GET)
	public ModelAndView test(){
		ModelAndView model = new ModelAndView("store");
		
		List<DocumentType> typeList = dataService.findAllDocType();
		model.addObject("typeList", typeList);

		return model;
	}
}
