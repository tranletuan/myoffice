package com.myoffice.myapp.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.service.DataService;

@Controller
@RequestMapping(value = "/impl")
public class ImplementController {
	
	@Autowired
	private DataService dataService;

	@RequestMapping(value = "/create_form", method = RequestMethod.GET)
	public ModelAndView createNewDocForm(){
		ModelAndView model = new ModelAndView("create");
		
		List<DocumentType> typeList = dataService.findAllDocType();
		List<EmergencyLevel> emeList = dataService.findAllEmergencyLevel();
		List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
		
		model.addObject("typeList", typeList);
		model.addObject("emeList", emeList);
		model.addObject("privacyList", privacyList);
		
		return model;
	}
	
	@RequestMapping(value = "/create_new_doc", method=RequestMethod.POST)
	public ModelAndView submitNewDoc(
			@RequestParam("title") String title,
			@RequestParam("docName") String docName,
			@RequestParam("releaseTime") Date releaseTime,
			@RequestParam("epitome") String epitome,
			@RequestParam("docPath") String docPath,
			@RequestParam("docType") String docTypeId,
			@RequestParam("unit") String unitId,
			@RequestParam("privacyLevel") String privacyLevelId,
			@RequestParam("emergencyLevel") String emergencyLevelId){
		ModelAndView model = new ModelAndView("implement");
		
		System.out.println(title);
		System.out.println(docName);
		System.out.println(releaseTime);
		System.out.println(epitome);
		System.out.println(docPath);
		System.out.println(docTypeId);
		System.out.println(unitId);
		System.out.println(privacyLevelId);
		System.out.println(emergencyLevelId);
		

		return model;
	}
}
