package com.myoffice.myapp.controllers;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.models.dto.Unit;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.models.service.SecurityService;
import com.myoffice.myapp.utils.FlowUtil;
import com.myoffice.myapp.utils.UtilMethod;

@Controller
@RequestMapping(value = "/impl")
public class ImplementController extends AbstractController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(ImplementController.class);

	@RequestMapping(value = "/create_form", method = RequestMethod.GET)
	public ModelAndView createNewDocForm(){
		ModelAndView model = new ModelAndView("create");
		
		List<DocumentType> typeList = dataService.findAllDocType();
		List<EmergencyLevel> emeList = dataService.findAllEmergencyLevel();
		List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
		List<Unit>	unitList = dataService.findAllUnit();
		List<Tenure> tenureList = dataService.findAllTenure(); 
		
		model.addObject("typeList", typeList);
		model.addObject("emeList", emeList);
		model.addObject("privacyList", privacyList);
		model.addObject("unitList", unitList);
		model.addObject("tenureList", tenureList);
		//model.addObject("unit", user.getUnit());
		
		return model;
	}
	
	@RequestMapping(value = "/create_new_doc", method=RequestMethod.POST)
	public ModelAndView submitNewDoc(
			@RequestParam("docName") String docName,
			@RequestParam("title") String title,
			@RequestParam("releaseTime") String releaseTime,
			@RequestParam("epitome") String epitome,
			@RequestParam("typeId") Integer typeId,
			@RequestParam("unitId") Integer unitId,
			@RequestParam("privacyId") Integer privacyId,
			@RequestParam("emeId") Integer emeId,
			@RequestParam("recipientUnits") Integer[] arrUnitId,
			@RequestParam("docPath") String docPath,
			@RequestParam("tenureId") String tenureId) throws ParseException{
		ModelAndView model = new ModelAndView("implement");
	
		//Set data to save
		Document doc = new Document();
		doc.setDocName(docName);
		doc.setTitle(title);
		doc.setReleaseTime(UtilMethod.toDate(releaseTime, "dd-MM-yyyy"));
		doc.setEpitome(epitome);
		doc.setDocPath(docPath);
		doc.setDocType(dataService.findDocTypeById(typeId));
		doc.setEmergencyLevel(dataService.findEmergencyLevelById(emeId));
		doc.setPrivacyLevel(dataService.findPrivacyLevelById(privacyId));
		doc.setUnit(dataService.findUnitById(unitId));
		
		Set<Unit> recipientUnits = new HashSet<Unit>(dataService.findUnitByArray(arrUnitId));
		doc.setRecipientUnits(recipientUnits);
		
		
		//create new flow
		/*Parameter processDefinitionIdParam = dataService.findParameterByName(pdId);
		try{
			String processInstanceId = flowUtil.startProcess(processDefinitionIdParam.getValue());
			doc.setProcessInstanceId(processInstanceId);
			dataService.saveDocument(doc);
			
		}catch(Exception e){
			model.setViewName("error");
			model.addObject("errorMessage", "Lỗi!Không thể tạo văn bản");
		}*/
		
		return model;
	}
}
