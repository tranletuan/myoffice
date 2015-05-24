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
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Unit;
import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.utils.FlowUtil;

@Controller
@RequestMapping(value = "/impl")
public class ImplementController {
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private FlowUtil flowUtil;
	
	private static final String pdId = "processDefinitionId";

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
			@RequestParam("docType") Integer docTypeId,
			@RequestParam("unit") Integer unitId,
			@RequestParam("privacyLevel") Integer privacyLevelId,
			@RequestParam("emergencyLevel") Integer emergencyLevelId){
		ModelAndView model = new ModelAndView("implement");
		
		//Set data to save
		Document doc = new Document();
		doc.setTitle(title);
		doc.setDocName(docName);
		doc.setReleaseTime(releaseTime);
		doc.setEpitome(epitome);
		doc.setDocPath(docPath);
		
		DocumentType docType = dataService.findDocTypeById(docTypeId);
		doc.setDocType(docType);
		
		Unit unit = dataService.findUnitById(unitId);
		doc.setUnit(unit);
		
		EmergencyLevel emergencyLevel = dataService.findEmergencyLevelById(emergencyLevelId);
		doc.setEmergencyLevel(emergencyLevel);
		
		PrivacyLevel privacyLevel = dataService.findPrivacyLevelById(privacyLevelId);
		doc.setPrivacyLevel(privacyLevel);
		
		//create new flow
		Parameter processDefinitionIdParam = dataService.findParameterByName(pdId);
		try{
			String processInstanceId = flowUtil.startProcess(processDefinitionIdParam.getValue());
			doc.setProcessInstanceId(processInstanceId);
			dataService.saveDocument(doc);
			
		}catch(Exception e){
			model.setViewName("error");
			model.addObject("errorMessage", "Lỗi!Không thể tạo văn bản");
		}
		
		return model;
	}
}
