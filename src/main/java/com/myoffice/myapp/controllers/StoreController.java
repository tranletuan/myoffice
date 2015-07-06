package com.myoffice.myapp.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataService;

@Controller
@RequestMapping(value = "/store")
public class StoreController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(StoreController.class);

	@RequestMapping(value = "/list/{type}/{firstNumber}", method = RequestMethod.GET)
	public ModelAndView storeHome(
			@PathVariable("type") String type,
			@PathVariable("firstNumber") Integer firstNumber) {
		ModelAndView model = new ModelAndView("store");
		
		List<DocumentType> typeList = dataService.findAllDocType();
		List<Tenure> tenureList = dataService.findAllTenure();
		model.addObject("typeList", typeList);
		model.addObject("tenureList", tenureList);
		
		User curUser = securityService.getCurrentUser();
		int enabled = curUser.checkRoleByShortName("mng")? -1 : 1;
		Organ organ = curUser.getOrgan();
		
		if(type.equals("out")){
			List<Document> docList = dataService.findDocumentBy(organ.getOrganId(), 1, firstNumber, 10, enabled);
			model.addObject("docList", docList);
			model.addObject("out", true);
		} else {
			List<DocumentRecipient> docList = dataService.findDocRecipient(organ.getOrganId(), 1, firstNumber, 10);
			model.addObject("docList", docList);
			model.addObject("in", true);
		}
		
		return model;
	}
	
	@RequestMapping(value = "/list/{type}/{tenureId}/{docTypeId}/{firstNumber}")
	public ModelAndView showList(
			@PathVariable("type") String type,
			@PathVariable("tenureId") Integer tenureId,
			@PathVariable("docTypeId") Integer docTypeId,
			@PathVariable("firstNumber") Integer firstNumber){
		ModelAndView model = new ModelAndView("store");
		List<DocumentType> typeList = dataService.findAllDocType();
		List<Tenure> tenureList = dataService.findAllTenure();
		model.addObject("typeList", typeList);
		model.addObject("tenureList", tenureList);
		
		User curUser = securityService.getCurrentUser();
		int enabled = curUser.checkRoleByShortName("mng")? -1 : 1;
		Organ organ = curUser.getOrgan();
		
		if(type.equals("in")){
			List<DocumentRecipient> docList = dataService.findDocRecipient(
					organ.getOrganId(), tenureId, docTypeId, 1, firstNumber, 10);
			model.addObject("docList", docList);
			model.addObject("in", true);
		} else if(type.equals("out")){
			List<Document> docList = dataService.findDocumentBy(
					organ.getOrganId(), tenureId, docTypeId, 1, firstNumber, 10, enabled);
			model.addObject("docList", docList);
			model.addObject("out", true);
		}
		return model;
	}
}
