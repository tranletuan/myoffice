package com.myoffice.myapp.controllers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

import com.mchange.v2.c3p0.FullQueryConnectionTester;
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
@RequestMapping(value = "/flow")
public class FlowController extends AbstractController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(FlowController.class);

	@RequestMapping(value = "/create_doc_out", method = RequestMethod.GET)
	public ModelAndView createNewDocOut(){
		ModelAndView model = new ModelAndView("create-doc-out");
		
		User user = securityService.getCurrentUser();
		Organ organ = user.getOrgan();
		if(organ == null){
			model.setViewName("redirect:store/list");
		}
		
		List<DocumentType> docTypeList = dataService.findAllDocType();
		List<EmergencyLevel> emeList = dataService.findAllEmergencyLevel();
		List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
		List<Tenure> tenureList = dataService.findAllTenure(); 
	
		model.addObject("docTypeList", docTypeList);
		model.addObject("emeList", emeList);
		model.addObject("privacyList", privacyList);
		model.addObject("tenureList", tenureList);
		model.addObject("organ", organ);
		
		return model;
	}
	
	@RequestMapping(value = "/number")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Integer autoGeneratedNumber(
			@RequestParam("tenureId") Integer tenureId,
			@RequestParam("docTypeId") Integer docTypeId,
			@RequestParam("organId") Integer organId, 
			@RequestParam("incoming") boolean incoming) {
		logger.info("OKE");
		return dataService.findMaxNumber(tenureId, docTypeId, organId, incoming);
	}
	
	@RequestMapping(value = "/save_doc_out", method=RequestMethod.POST)
	public ModelAndView submitNewDocOut(
			@RequestParam(value = "docId", required = false) Integer docId,
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
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "comment", required = false) String comment,
			RedirectAttributes reAttr) throws ParseException, IllegalStateException, IOException{
		ModelAndView model = new ModelAndView("redirect:doc_info");
		Tenure tenure = dataService.findTenureById(tenureId);
		DocumentType docType = dataService.findDocTypeById(docTypeId);
		Organ organ = securityService.getCurrentUser().getOrgan();
		numberSign = number + numberSign + sign;
		
		if(organ == null){
			model.setViewName("error");
			model.addObject("errorMessage", "Tài khoản chưa xác định thuộc đơn vị nào chưa thể tạo văn bản mới!");
		}
		
		//Set data to save
		Document doc = new Document();
		if(docId != null && docId > 0){
			doc = dataService.findDocumentById(docId);
		}
		
		doc.setDocName(docName);
		doc.setTitle(title);
		doc.setEpitome(epitome);
		doc.setDocType(docType);
		doc.setPrivacyLevel(dataService.findPrivacyLevelById(privacyId));
		doc.setEmergencyLevel(dataService.findEmergencyLevelById(emeId));
		doc.setTenure(tenure);
		doc.setOrgan(organ);
		doc.setNumberSign(numberSign);
		if(comment != null) doc.setComment(comment);
		
		String dirServer = DataConfig.DIR_SERVER + tenure.getTenureName() + File.separator + docType.getDocTypeName() + File.separator;
		String fileName = number + "-" + docType.getShortName() + "-" + organ.getOrganType().getShortName() + "-" + docName;
		String[] parts = file.getOriginalFilename().split("\\.");
		fileName += "." + parts[parts.length - 1];
		String docPath = dirServer + fileName;
		doc.setDocPath(docPath);
	
		//Number and Sign
		Integer num = UtilMethod.parseNumDoc(number);
		if (num > 0) {
			doc.setNumber(num);
		} else {
			doc.setNumber(dataService.findMaxNumber(tenureId, docTypeId, organ.getOrganId(), false) - 1);
		}
		
		//create new flow
		if (docId == null) {
			try {
				String procDefId = flowUtil.getProcessDefinitionId(
						DataConfig.RSC_NAME_FLOW_OUT,
						DataConfig.PROC_DEF_KEY_FLOW_OUT);
				String procInsId = flowUtil.startProcess(procDefId);

				if (procDefId == " " || procInsId == null) {
					model.setViewName("error");
					model.addObject("errorMessage",
							"Luồng văn bản chưa được tạo, vui lòng liên hệ admin");
					return model;
				}

				doc.setProcessInstanceId(procInsId);
				try {
					dataService.saveDocument(doc);
					Task task = flowUtil.getCurrentTask(doc
							.getProcessInstanceId());
					flowUtil.getTaskService().complete(task.getId());
				} catch (Exception e) {
					logger.error(e.getMessage());
					if (procInsId != null) {
						flowUtil.deleteProcessInstanceById(procInsId,
								"can not create new out document");
					}
					model.setViewName("error");
					model.addObject("errorMessage", e.getMessage());
				}

			} catch (Exception e) {
				logger.error(e.getMessage());

				model.setViewName("error");
				model.addObject("errorMessage", "Lỗi!Không thể tạo văn bản");
			}
		}
		else {
			dataService.saveDocument(doc);
			model.setViewName("redirect:doc_info?docId=" + docId);
		}
		
		if(file != null){
			dataService.upLoadFile(dirServer, file, fileName);
		}
		
		reAttr.addFlashAttribute("doc", doc);
		
		return model;
	}
	
	@RequestMapping(value = "/doc_info", method = RequestMethod.GET)
	public ModelAndView documentInfo(@RequestParam(value = "docId", required = false) Integer docId){
		ModelAndView model = new ModelAndView("doc-info");
		Document doc = new Document();

		if (docId != null && docId > 0) {
			doc = dataService.findDocumentById(docId);
			
			List<DocumentType> docTypeList = dataService.findAllDocType();
			List<EmergencyLevel> emeList = dataService.findAllEmergencyLevel();
			List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
			List<Tenure> tenureList = dataService.findAllTenure(); 
		
			model.addObject("docTypeList", docTypeList);
			model.addObject("emeList", emeList);
			model.addObject("privacyList", privacyList);
			model.addObject("tenureList", tenureList);
			
			if (flowUtil.isEnded(doc.getProcessInstanceId())) {
				if (doc.getReleaseTime() == null || !doc.isCompleted()) {
					doc.setCompleted(true);
					doc.setReleaseTime(new Date());
					dataService.saveDocument(doc);
				}

				model.addObject("taskDescription",
						"Văn bản đã được xử lý hoàn tất");
			} else {
				Task curTask = flowUtil.getCurrentTask(doc.getProcessInstanceId());
				if (curTask == null)
					return model;

				User user = securityService.getCurrentUser();

				String[] taskName = curTask.getName().split(" ");
				model.addObject("taskDescription", curTask.getDescription());
				model.addObject("taskRole",
						dataService.findRoleByShortName(taskName[0])
								.getFullName());

				if (user.checkRoleByShortName(taskName[0])) {

					model.addObject("access", true);
					if (curTask.getAssignee() == null) {
						model.addObject("claim", true);
					} else if (curTask.getAssignee().equals(user.getUserName())) {
						if (taskName[taskName.length - 1].equals("check")) {
							model.addObject("check", true);
						}
					} else {
						model.addObject("access", false);
					}
				}

				model.addObject("assignee", curTask.getAssignee());
			}
			
			model.addObject("doc", doc);
		}
		
		return model;
	}
	
	@RequestMapping(value = "/download_document/{docId}", method = RequestMethod.GET)
	public void downLoadDocument(HttpServletResponse response,
			@PathVariable("docId") Integer docId)throws IOException {
		Document doc = dataService.findDocumentById(docId);
		dataService.downLoadFile(doc.getDocPath(), response);
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
	
	@RequestMapping(value = "/claim_imp_doc", method = RequestMethod.GET)
	public ModelAndView claimFlowOutTask(@RequestParam("docId") Integer docId){
		ModelAndView model = new ModelAndView("redirect:doc_info?docId=" + docId);

		Document doc = dataService.findDocumentById(docId);
		Task curTask = flowUtil.getCurrentTask(doc.getProcessInstanceId());
		User user = securityService.getCurrentUser();

		String[] taskName = curTask.getName().split(" ");
		if (user.checkRoleByShortName(taskName[0])) {
			flowUtil.getTaskService().claim(curTask.getId(), user.getUserName());
		} 
		
		return model;
	}
	
	@RequestMapping(value ="/unclaim_imp_doc", method = RequestMethod.GET)
	public ModelAndView unclaimFlowOutTask(@RequestParam("docId") Integer docId){
		ModelAndView model = new ModelAndView("redirect:doc_info?docId=" + docId);
		Document doc = dataService.findDocumentById(docId);
		Task curTask = flowUtil.getCurrentTask(doc.getProcessInstanceId());
		User user = securityService.getCurrentUser();
	
		String[] taskName = curTask.getName().split(" ");
		if (user.checkRoleByShortName(taskName[0])) {
			if(curTask.getAssignee().equals(user.getUserName())){
				flowUtil.getTaskService().unclaim(curTask.getId());
			}
		} 
		
		return model;
	}

	@RequestMapping(value = "/complete_{checkNum}")
	public ModelAndView completedFlowOutTask(
			@RequestParam("docId") Integer docId,
			@PathVariable("checkNum") Integer checkNum){
		ModelAndView model = new ModelAndView("redirect:doc_info?docId=" + docId);
		Document doc = new Document();
		
		if(docId != null && docId > 0){
			doc = dataService.findDocumentById(docId);
			model.addObject("doc", doc);
			
			Task curTask = flowUtil.getCurrentTask(doc.getProcessInstanceId());
			User user = securityService.getCurrentUser();
			
			if(curTask == null) return model;
			
			String[] taskName = curTask.getName().split(" ");
			if(user.checkRoleByShortName(taskName[0]) && 
					curTask.getAssignee().equals(user.getUserName())){
				if(!taskName[taskName.length - 1].equals("check")){
					flowUtil.getTaskService().complete(curTask.getId());
				}
				else {
					Map<String, Object> variables = new HashMap<String, Object>();
					variables.put("check", checkNum);
					String taskId = curTask.getId();
					flowUtil.getTaskService().complete(taskId, variables);
				}
			}
			
			if(flowUtil.isEnded(doc.getProcessInstanceId())){
				doc.setCompleted(true);
				doc.setReleaseTime(new Date());
				dataService.saveDocument(doc);
				//send mail
			}
		}
	
		return model;
	}

	@RequestMapping(value = "/create_doc_in", method = RequestMethod.GET)
	public ModelAndView createNewDocIn(){
		ModelAndView model = new ModelAndView("create-doc-in");
		
		User user = securityService.getCurrentUser();
		Organ organ = user.getOrgan();
		if(organ == null){
			model.setViewName("redirect:store/list");
		}
		
		List<DocumentType> docTypeList = dataService.findAllDocType();
		List<EmergencyLevel> emeList = dataService.findAllEmergencyLevel();
		List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
		List<Tenure> tenureList = dataService.findAllTenure(); 
		
		
		model.addObject("docTypeList", docTypeList);
		model.addObject("emeList", emeList);
		model.addObject("privacyList", privacyList);
		model.addObject("tenureList", tenureList);
		model.addObject("organ", organ);
		
		return model;
	}
	
	@RequestMapping(value = "/save_doc_in", method = RequestMethod.POST)
	public ModelAndView submitNewDocIn(
			@RequestParam("title") String title,
			@RequestParam("docName") String docName,
			@RequestParam("epitome") String epitome,
			@RequestParam("docTypeId") Integer docTypeId,
			@RequestParam("note") String note,
			@RequestParam("tenureId") Integer tenureId,
			@RequestParam("number") String number,
			@RequestParam("numberSign") String numberSign,
			@RequestParam("sign") String sign,
			@RequestParam("file") MultipartFile file,
			@RequestParam("privacyId") Integer privacyId,
			@RequestParam("emeId") Integer emeId,
			RedirectAttributes reAttr) throws IllegalStateException, IOException{
		ModelAndView model = new ModelAndView("doc_info");
		
		Tenure tenure = dataService.findTenureById(tenureId);
		DocumentType docType = dataService.findDocTypeById(docTypeId);
		Organ organ = securityService.getCurrentUser().getOrgan();
		numberSign = number + numberSign + sign;
		
		if(organ == null){
			model.setViewName("error");
			model.addObject("errorMessage", "Tài khoản chưa xác định thuộc đơn vị nào chưa thể tạo văn bản mới!");
		}
		
		//Save file to server 
		String dirServer = DataConfig.DIR_SERVER + tenure.getTenureName() + File.separator + docType.getDocTypeName() + File.separator;
		String fileName = number + "-" + docType.getShortName() + "-" + organ.getOrganType().getShortName() + "-" + docName;
		String[] parts = file.getOriginalFilename().split("\\.");
		fileName += "." + parts[parts.length - 1];
		String docPath = dirServer + fileName;
		
		//Set data to save
		Document doc = new Document();
		doc.setTitle(title);
		doc.setDocName(docName);
		doc.setEpitome(epitome);
		doc.setDocType(docType);
		doc.setTenure(tenure);
		doc.setNumberSign(numberSign);
		doc.setDocPath(docPath);
		doc.setOrgan(organ);
		doc.setPrivacyLevel(dataService.findPrivacyLevelById(privacyId));
		doc.setEmergencyLevel(dataService.findEmergencyLevelById(emeId));
		
		//Number and Sign
		Integer num = UtilMethod.parseNumDoc(number);
		if (num > 0) {
			doc.setNumber(num);
		} else {
			doc.setNumber(dataService.findMaxNumber(tenureId, docTypeId, organ.getOrganId(), true) - 1);
		}
		
		
		//create new flow
		try{
			String procDefId = flowUtil.getProcessDefinitionId(DataConfig.RSC_NAME_FLOW_IN, DataConfig.PROC_DEF_KEY_FLOW_IN);
			String procInsId = flowUtil.startProcess(procDefId);
			
			if(procDefId == " " ||  procInsId == null){
				model.setViewName("error");
				model.addObject("errorMessage","Luồng văn bản chưa được tạo, vui lòng liên hệ admin");
				return model;
			}
			
			doc.setProcessInstanceId(procInsId);
			try {
				dataService.saveDocument(doc);
				Task task = flowUtil.getCurrentTask(doc.getProcessInstanceId());
				flowUtil.getTaskService().complete(task.getId());
			} catch (Exception e) {
				logger.error(e.getMessage());
				if (procInsId != null) {
					flowUtil.deleteProcessInstanceById(procInsId,"can not create new out document");
				}
				model.setViewName("error");
				model.addObject("errorMessage", e.getMessage());
			}
			
		}catch(Exception e){
			logger.error(e.getMessage());
			
			model.setViewName("error");
			model.addObject("errorMessage", "Lỗi!Không thể tạo văn bản");
		}
		
		dataService.upLoadFile(dirServer, file, fileName);
		reAttr.addFlashAttribute("doc", doc);
		
		return model;
	}


}
