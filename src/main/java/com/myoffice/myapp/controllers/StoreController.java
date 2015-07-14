package com.myoffice.myapp.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.support.ListDoc;
import com.myoffice.myapp.support.TenureMenuItem;
import com.myoffice.myapp.utils.UtilMethod;

@Controller
@RequestMapping(value = "/store")
public class StoreController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(StoreController.class);

	@RequestMapping(value = "/list/{type}", method = RequestMethod.GET)
	public ModelAndView storeHome(
			@PathVariable("type") String type) {
		ModelAndView model = new ModelAndView("store");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		List<TenureMenuItem> tenureItemOutList = dataService.findMenuTenureOut(organ.getOrganId());
		List<TenureMenuItem> tenureItemInList = dataService.findMenuTenureIn(organ.getOrganId());
		List<DocumentType> docTypeList = dataService.findAllDocType();
		
		model.addObject("tenureItemOutList", tenureItemOutList);
		model.addObject("tenureItemInList", tenureItemInList);
		model.addObject("docTypeList", docTypeList);
		model.addObject("organ", organ);
		
		if(type.equals("out")) {
			List<Document> docList = dataService.findDocumentBy(organ.getOrganId(), null, null, true, 0, 9, true);
			model.addObject("docList", docList);
			model.addObject("out", true);
		} else {
			List<DocumentRecipient> docList = dataService.findDocRecipient(organ.getOrganId(), null, null, true, 0, 9);
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
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		List<TenureMenuItem> tenureItemOutList = dataService.findMenuTenureOut(organ.getOrganId());
		List<TenureMenuItem> tenureItemInList = dataService.findMenuTenureIn(organ.getOrganId());
		
		model.addObject("tenureItemOutList", tenureItemOutList);
		model.addObject("tenureItemInList", tenureItemInList);
		
		if(type.equals("out")) {
			List<Document> docList = dataService.findDocumentBy(organ.getOrganId(), tenureId, docTypeId, true,
					firstNumber, firstNumber + 9, true);
			model.addObject("docList", docList);
			model.addObject("out", true);
		} else {
			List<DocumentRecipient> docList = dataService.findDocRecipient(organ.getOrganId(), tenureId, docTypeId, true,
					firstNumber, firstNumber + 9);
			model.addObject("docList", docList);
			model.addObject("in", true);
		}
		return model;
	}
	
	
	@RequestMapping(value = "/search_doc_out/{firstResult}")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ModelAndView searchByEpitome(
			@RequestParam(value = "docName", required = false) String docName,
			@RequestParam(value = "epitome", required = false) String epitome,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "docTypeId") int docTypeId,
			@RequestParam(value = "department" , required = false) String department,
			@RequestParam(value = "minDay", required = false) String minDay,
			@RequestParam(value = "maxDay", required = false) String maxDay,
			@PathVariable("firstResult") int firstResult,
			HttpServletRequest request){
		ModelAndView model = new ModelAndView("fragment/store-list");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		
		Date minDaySet = null;
		Date maxDaySet = null;
		try {
			if(minDay != null) minDaySet = UtilMethod.toDate(minDay, "dd-MM-yyyy");
			if(maxDay != null) maxDaySet = UtilMethod.toDate(maxDay, "dd-MM-yyyy");
		} catch(Exception e){}
		
		ListDoc list = dataService.findCompletedDocOut(organ.getOrganId(), docName, epitome, number, docTypeId,
				department, minDaySet, maxDaySet, firstResult, firstResult + 9);
		
		String contextPath = request.getContextPath();
		list.setUrl(contextPath + "/search_doc_out");
		model.addObject("listDoc", list);
		model.addObject("out", true);
		logger.info(String.valueOf(list.getCount()));
		return model;
	}
}
