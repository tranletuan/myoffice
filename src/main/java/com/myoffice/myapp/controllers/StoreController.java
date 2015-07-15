package com.myoffice.myapp.controllers;

import java.util.ArrayList;
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
import com.myoffice.myapp.models.dto.OrganType;
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
		List<OrganType> organTypeList = dataService.findAllOrganType();
		
		model.addObject("tenureItemOutList", tenureItemOutList);
		model.addObject("tenureItemInList", tenureItemInList);
		model.addObject("docTypeList", docTypeList);
		model.addObject("organTypeList", organTypeList);
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
	
	@RequestMapping(value = "/list/{type}/{tenureId}/{docTypeId}")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ModelAndView showList(
			@PathVariable("type") String type,
			@PathVariable("tenureId") Integer tenureId,
			@PathVariable("docTypeId") Integer docTypeId){
		ModelAndView model = new ModelAndView("fragment/store-element");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		List<Integer> elemList = new ArrayList<Integer>();
		List<Integer> rowList = new ArrayList<Integer>();	

		if (type.equals("out")) {
			List<Document> docList = dataService.findDocumentBy(organ.getOrganId(), tenureId, docTypeId, true, null,
					null, true);
			UtilMethod.preparePagination(rowList, elemList, docList, model, null);
			model.addObject("docList", docList);
			model.addObject("out", true);
		} else {
			List<DocumentRecipient> docList = dataService.findDocRecipient(organ.getOrganId(), tenureId, docTypeId,
					true, null, null);
			UtilMethod.preparePagination(rowList, elemList, docList, model, null);
			model.addObject("docList", docList);
			model.addObject("in", true);
		}
		
		return model;
	}
	
	//Search Document Out
	@RequestMapping(value = "/search_doc_out", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ModelAndView searchDocOut(
			@RequestParam(value = "docName", required = false) String docName,
			@RequestParam(value = "epitome", required = false) String epitome,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "docTypeId") int docTypeId,
			@RequestParam(value = "department" , required = false) String department,
			@RequestParam(value = "minDay", required = false) String minDay,
			@RequestParam(value = "maxDay", required = false) String maxDay){
		ModelAndView model = new ModelAndView("fragment/store-element");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		List<Integer> elemList = new ArrayList<Integer>();
		List<Integer> rowList = new ArrayList<Integer>();	
		
		Date minDaySet = null;
		Date maxDaySet = null;
		try {
			if(minDay != null) minDaySet = UtilMethod.toDate(minDay, "dd-MM-yyyy");
			if(maxDay != null) maxDaySet = UtilMethod.toDate(maxDay, "dd-MM-yyyy");
		} catch(Exception e){}
		
		List<Document> docList = dataService.findCompletedDocOut(organ.getOrganId(), docName, epitome, number, docTypeId,
				department, minDaySet, maxDaySet, null, null);
		UtilMethod.preparePagination(rowList, elemList, docList, model, null);
		model.addObject("docList", docList);
		model.addObject("out", true);

		return model;
	}
	
	
	//Search Document In
	@RequestMapping(value = "/search_doc_in", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ModelAndView searchDocIn(
			@RequestParam(value = "docName", required = false) String docName,
			@RequestParam(value = "epitome", required = false) String epitome,
			@RequestParam(value = "docTypeId", required = false) Integer docTypeId,
			@RequestParam(value = "organTypeId", required = false) Integer organTypeId,
			@RequestParam(value = "number", required = false) String number,
			@RequestParam(value = "department" , required = false) String department,
			@RequestParam(value = "minDay", required = false) String minDay,
			@RequestParam(value = "maxDay", required = false) String maxDay,
			@RequestParam(value = "numberRec", required = false) String numberRec,
			@RequestParam(value = "minDayRec", required = false) String minDayRec,
			@RequestParam(value = "maxDayRec", required = false) String maxDayRec){
		ModelAndView model = new ModelAndView("fragment/store-element");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		List<Integer> elemList = new ArrayList<Integer>();
		List<Integer> rowList = new ArrayList<Integer>();	
		
		Date minDaySet = null;
		Date maxDaySet = null;
		Date minDayRecSet = null;
		Date maxDayRecSet = null;
		
		try {
			if(minDay != null) minDaySet = UtilMethod.toDate(minDay, "dd-MM-yyyy");
			if(maxDay != null) maxDaySet = UtilMethod.toDate(maxDay, "dd-MM-yyyy");
			if(minDayRec != null) minDayRecSet = UtilMethod.toDate(minDayRec, "dd-MM-yyyy");
			if(maxDayRec != null) maxDayRecSet = UtilMethod.toDate(maxDayRec, "dd-MM-yyyy");
		} catch(Exception e){}
		
		List<DocumentRecipient> docList = dataService.findCompletedDocIn(organ.getOrganId(), docName, epitome,
				numberRec, docTypeId, organTypeId, department, minDaySet, maxDaySet, minDayRecSet, maxDayRecSet, null,
				null);

		UtilMethod.preparePagination(rowList, elemList, docList, model, null);
		model.addObject("docList", docList);
		model.addObject("in", true);

		return model;
	}
}
