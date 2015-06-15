package com.myoffice.myapp.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.models.dto.Unit;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataConfig;
import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.models.service.SecurityService;
import com.myoffice.myapp.support.NoteDoctypeInt;
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
		
		User user = securityService.getCurrentUser();
		Organ organ = user.getOrgan();
		if(organ == null){
			model.setViewName("redirect:store/list");
		}
		
		List<DocumentType> docTypeList = dataService.findAllDocType();
		List<EmergencyLevel> emeList = dataService.findAllEmergencyLevel();
		List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
		List<Organ> organList = dataService.findAllOrgan();
		List<Tenure> tenureList = dataService.findAllTenure(); 
		
		
		model.addObject("docTypeList", docTypeList);
		model.addObject("emeList", emeList);
		model.addObject("privacyList", privacyList);
		model.addObject("organList", organList);
		model.addObject("tenureList", tenureList);
		model.addObject("organ", organ);
		
		return model;
	}
	
	@RequestMapping(value = "/number")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Integer autoGeneratedNumber(
			@RequestParam("tenureId") Integer tenureId,
			@RequestParam("docTypeId") Integer docTypeId) {
		return dataService.findMaxNumber(tenureId, docTypeId);
	}
	
	@RequestMapping(value = "/save_doc", method=RequestMethod.POST)
	public ModelAndView submitNewDoc(
			@RequestParam("docName") String docName,
			@RequestParam("title") String title,
			@RequestParam("epitome") String epitome,
			@RequestParam("docTypeId") Integer docTypeId,
			@RequestParam("privacyId") Integer privacyId,
			@RequestParam("emeId") Integer emeId,
			@RequestParam("tenureId") Integer tenureId,
			@RequestParam("number") String number,
			@RequestParam("numberSign") String numberSign,
			@RequestParam("sign") String sign,
			@RequestParam("file") MultipartFile file,
			@RequestParam("recipient") Integer[] arrOrganId,
			RedirectAttributes reAttr) throws ParseException, IllegalStateException, IOException{
		ModelAndView model = new ModelAndView("redirect:doc_info");

		Tenure tenure = dataService.findTenureById(tenureId);
		DocumentType docType = dataService.findDocTypeById(docTypeId);
		Organ organ = securityService.getCurrentUser().getOrgan();
		
		if(organ == null){
			model.setViewName("error");
			model.addObject("errorMessage", "Tài khoản chưa xác định thuộc đơn vị nào chưa thể tạo văn bản mới!");
		}
		
		//Save file to server 
		String dirServer = DataConfig.DIR_SERVER + tenure.getTenureName() + "/" + docType.getDocTypeName() + "/";
		String docPath = dirServer + file.getOriginalFilename();
		
		//Set data to save
		Document doc = new Document();
		doc.setDocName(docName);
		doc.setTitle(title);
		doc.setEpitome(epitome);
		doc.setDocType(docType);
		doc.setPrivacyLevel(dataService.findPrivacyLevelById(privacyId));
		doc.setEmergencyLevel(dataService.findEmergencyLevelById(emeId));
		doc.setTenure(tenure);
		doc.setDocPath(docPath);
		doc.setOrgan(organ);
		doc.setNumberSign(number + numberSign + sign);
		
		List<Organ> recipients = dataService.findOrganByArray(arrOrganId);
		if(recipients != null){
			doc.setRecipients(new HashSet<Organ>(recipients));
		}
		
		//Number and Sign
		Integer num = UtilMethod.parseNumDoc(number);
		if (num > 0) {
			doc.setNumber(num);
		} else {
			doc.setNumber(dataService.findMaxNumber(tenureId, docTypeId) - 1);
		}
		
		
		//create new flow
		try{
			String procDefId = flowUtil.getProcessDefinitionId(DataConfig.RSC_NAME_FLOW_OUT, DataConfig.PROC_DEF_KEY_FLOW_OUT);
			String procInsId = flowUtil.startProcess(procDefId);

			doc.setProcessInstanceId(procInsId);
			try {
				dataService.saveDocument(doc);
				dataService.upLoadFile(dirServer, file, file.getOriginalFilename());
			} catch (Exception e) {
				logger.error(e.getMessage());
				if (procInsId != null) {
					flowUtil.deleteProcessInstanceById(procInsId,"can not create new out document");
				}
			}

			reAttr.addFlashAttribute("doc", doc);
			
		}catch(Exception e){
			logger.error(e.getMessage());
			
			model.setViewName("error");
			model.addObject("errorMessage", "Lỗi!Không thể tạo văn bản");
		}
		
		return model;
	}
	
	@RequestMapping(value = "/doc_info", method = RequestMethod.GET)
	public ModelAndView documentInfo(@RequestParam(value = "docId", required = false) Integer docId){
		ModelAndView model = new ModelAndView("doc-info");
		Document doc = new Document();
		if(docId != null && docId > 0){
			doc = dataService.findDocumentById(docId);
			model.addObject("doc", doc);
		}
		
		return model;
	}
	
	@RequestMapping(value = "/waiting_list_{type}", method = RequestMethod.GET)
	public ModelAndView waitingDocumentList(
			@PathVariable("type") String type,
			@RequestParam(value = "docTypeId", required = false) Integer docTypeId){
		ModelAndView model =  new ModelAndView("waiting-list");
		boolean incoming = type.equals("in") ? true : false;
		
		List<NoteDoctypeInt> docTypeIntList = dataService.findWaitingMenu(incoming);
		List<Document> docList = dataService.findWaitingDocByType(incoming, false, docTypeId);
		model.addObject("docTypeIntList", docTypeIntList);
		model.addObject("docList", docList);
		model.addObject("type", type);
	
		return model;
	}
}
