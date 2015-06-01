package com.myoffice.myapp.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.service.DataService;

@Controller
@RequestMapping(value = "/store")
public class StoreController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(StoreController.class);

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView test() {
		ModelAndView model = new ModelAndView("store");

		List<DocumentType> typeList = dataService.findAllDocType();
		model.addObject("typeList", typeList);

		return model;
	}
}
