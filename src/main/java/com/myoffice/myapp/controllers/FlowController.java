package com.myoffice.myapp.controllers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cmd.FindActiveActivityIdsCmd;
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
import com.myoffice.myapp.models.dto.Candidate;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentFile;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Role;
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

	//===============DOC_OUT=================
	@RequestMapping(value = "/create_doc_out", method = RequestMethod.GET)
	public ModelAndView createNewDocOut(){
		ModelAndView model = new ModelAndView("create-doc-out");
		
		User user = securityService.getCurrentUser();
		Organ organ = user.getOrgan();
		if(organ == null){
			model.setViewName("redirect:/store/list");
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
			@RequestParam("departments") String departments,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "comment", required = false) String comment,
			RedirectAttributes reAttr) throws ParseException, IllegalStateException, IOException{
		ModelAndView model = new ModelAndView("redirect:doc_info");
		Tenure tenure = dataService.findTenureById(tenureId);
		DocumentType docType = dataService.findDocTypeById(docTypeId);
		Organ organ = securityService.getCurrentUser().getOrgan();
		numberSign = number + numberSign + departments;
		
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
		doc.setDepartments(departments);
		if(comment != null) doc.setComment(comment);
		
		Integer num = UtilMethod.parseNumDoc(number);
		if (num > 0) {
			doc.setNumber(num);
		} else {
			doc.setNumber(dataService.findMaxNumber(tenureId, docTypeId, organ.getOrganId(), false) - 1);
		}
		
		//SAVE DOC AND FLOW
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
					User curUser = securityService.getCurrentUser();
					dataService.saveDocument(doc);
					Task task = flowUtil.getCurrentTask(doc
							.getProcessInstanceId());
					flowUtil.getTaskService().setAssignee(task.getId(), curUser.getUserName());
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
			}
		}
		else {
			dataService.saveDocument(doc);
			model.setViewName("redirect:doc_info?docId=" + docId);
		}
		
		//SAVE FILE
		if(file != null){
			try {
				DocumentFile docFile = new DocumentFile();
				
				//File Path
				String filePath = DataConfig.DIR_SERVER
						+ tenure.getTenureName() + File.separator
						+ docType.getDocTypeName() + File.separator;
				//File Name
				String fileName = number + "-" + docType.getShortName() + "-"
						+ organ.getOrganType().getShortName() + "-" + docName;
				
				String[] parts = file.getOriginalFilename().split("\\.");
				fileName += "." + parts[parts.length - 1];
				docFile.setFilePath(filePath);

				if (docId != null && docId > 0) {
					Integer version = dataService.findNewestDocFile(docId).getVersion() + 1;
					fileName = "Ver" + version + "-" + fileName;
					docFile.setFileName(fileName);
					docFile.setDocument(doc);
					docFile.setVersion(version);
				} else {
					fileName = "Ver1-" + fileName;
					docFile.setFileName(fileName);
					docFile.setDocument(doc);
				}

				dataService.saveDocFile(docFile);
				dataService.upLoadFile(filePath, file, fileName);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		
		reAttr.addFlashAttribute("doc", doc);
		return model;
	}
	
	@RequestMapping(value = "/doc_info", method = RequestMethod.GET)
	public ModelAndView documentInfo(@RequestParam(value = "docId", required = false) Integer docId){
		ModelAndView model = new ModelAndView("redirect:/store/list");
		Document doc = new Document();

		if (docId != null && docId > 0) {
			doc = dataService.findDocumentById(docId);
			User curUser = securityService.getCurrentUser();
			if(doc.getOrgan().getOrganId() != curUser.getOrgan().getOrganId()) return model;
			
			List<DocumentType> docTypeList = dataService.findAllDocType();
			List<EmergencyLevel> emeList = dataService.findAllEmergencyLevel();
			List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
			List<Tenure> tenureList = dataService.findAllTenure(); 
			List<DocumentFile> fileList = dataService.findAllFile(docId);
			
			model.addObject("docTypeList", docTypeList);
			model.addObject("emeList", emeList);
			model.addObject("privacyList", privacyList);
			model.addObject("tenureList", tenureList);
			model.addObject("fileList", fileList);
			
			if (flowUtil.isEnded(doc.getProcessInstanceId())) {
				if (doc.getReleaseTime() == null || !doc.isCompleted()) {
					doc.setCompleted(true);
					doc.setReleaseTime(new Date());
					dataService.saveDocument(doc);
				}
				
				List<Organ> organList = dataService.findAllOrgan();
				for (Organ o : organList) {
					if (o.getOrganId() == curUser.getOrgan().getOrganId()) {
						organList.remove(o);
						break;
					}
				}
				model.addObject("organList", organList);
				model.addObject("taskDescription",
						"Văn bản đã được xử lý hoàn tất");
			} else {
				Task curTask = flowUtil.getCurrentTask(doc.getProcessInstanceId());
				HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTask(doc.getProcessInstanceId());
		
				String []taskName = curTask.getName().split(" ");
				String []roles = taskName[0].split(",");
				
				model.addObject("taskDescription", curTask.getDescription());
				
				//Khi hoàn thành task trước và chưa ủy quyền
				if(curTask.getAssignee() == null){
					if(curUser.getUserName().equals(preTask.getAssignee())){	
						List<Role> candidateRole = dataService.findRolesByArrShortName(roles);
						List<User> userList = dataService.findUserByArrRoleShortName(curUser.getOrgan().getOrganId(), roles);
						model.addObject("isForward", true);
						model.addObject("userList", userList);
						model.addObject("candidateRole", candidateRole);
						HistoricTaskInstance preCompletedTask = flowUtil.getPreviousCompletedTaskWithName(
										doc.getProcessInstanceId(),
										curTask.getName());
						if(preCompletedTask != null){
							User preUser = dataService.findUserByName(preCompletedTask.getAssignee());
							model.addObject("preUser", preUser);
							model.addObject("canReturn", true);
						}
					} else {
						User rUser = dataService.findUserByName(preTask.getAssignee());
						model.addObject("userRole", rUser.getRoleNames());
						model.addObject("assignee", rUser.getUserName());
					}
				} else { //task đã được giao quyền
					User rUser = dataService.findUserByName(curTask.getAssignee());
					model.addObject("userRole", rUser.getRoleNames());
					model.addObject("assignee", rUser.getUserName());
					
					if(curUser.getUserName().equals(curTask.getAssignee())){
						model.addObject("isAccess", true);
						
						if(taskName[taskName.length - 1].equals("check")){
							model.addObject("isCheck", true);
						}
					}
				}
			}
			
			model.addObject("doc", doc);
		}
		
		model.setViewName("doc-info");
		return model;
	}
	
	//==============COMMON==================
	@RequestMapping(value = "/forward/{direct}/{docId}/{procId}", method = RequestMethod.POST)
	public ModelAndView forwardUser(
			@PathVariable("direct") String direct,
			@PathVariable("docId") Integer docId,
			@PathVariable("procId") String procId,
			@RequestParam(value = "userId", required = false) Integer userId){
		ModelAndView model = new ModelAndView("redirect:/flow/" + direct + "?docId=" + docId);
		Task curTask = flowUtil.getCurrentTask(procId);
		if(curTask == null) return model;
		
		if(userId != null){
		User user = dataService.findUserById(userId);
			String[] taskName = curTask.getName().split(" ");
			if(user.checkRoleByShortName(taskName[0])){
				flowUtil.getTaskService().setAssignee(curTask.getId(), user.getUserName());
			}
		} else {
			HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTaskWithName(procId, curTask.getName());
			if(preTask != null) {
				flowUtil.getTaskService().setAssignee(curTask.getId(), preTask.getAssignee());
			}
		}
		
		return model;
	}
	
	@RequestMapping(value = "/download_document/{docFileId}", method = RequestMethod.GET)
	public void downLoadDocument(HttpServletResponse response,
			@PathVariable("docFileId") Integer docFileId)throws IOException {
		DocumentFile docFile = dataService.findDocFileById(docFileId);
		dataService.downLoadFile(docFile.getFilePath() + docFile.getFileName(), response);
	}
	
	@RequestMapping(value = "/complete/{checkNum}/{docId}/{procId}/{direct}")
	public ModelAndView completedTask(
			@PathVariable("checkNum") Integer checkNum,
			@PathVariable("docId") Integer docId,
			@PathVariable("procId") String procId,
			@PathVariable("direct") String direct){
		ModelAndView model = new ModelAndView("redirect:/flow/" + direct + "?docId=" + docId);
		
		if(docId != null && docId > 0){
			Task curTask = flowUtil.getCurrentTask(procId);
			User user = securityService.getCurrentUser();
			
			if(curTask == null) return model;
			
			String[] taskName = curTask.getName().split(" ");
			if (user.checkRoleByShortName(taskName[0]) && curTask.getAssignee().equals(user.getUserName())) {
				
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put("check", checkNum);
				String taskId = curTask.getId();
				flowUtil.getTaskService().complete(taskId, variables);		
				curTask = flowUtil.getCurrentTask(procId);
			}
		}
	
		return model;
	}
	
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public ModelAndView sendDoc(
			@RequestParam("docId") Integer docId,
			@RequestParam("recipients") Integer[] recipients,
			RedirectAttributes reAttr){
		ModelAndView model = new ModelAndView("redirect:doc_info?docId=" + docId);
		Document doc = dataService.findDocumentById(docId);
		List<Organ> organList = dataService.findOrganByArray(recipients);
		
		String procDefId = null;
		String procInsId = null; 
		for(Organ o : organList){
			try{
				DocumentRecipient docRec = new DocumentRecipient();
				docRec.setDocument(doc);
				docRec.setOrgan(o);
				
				procDefId = flowUtil.getProcessDefinitionId(DataConfig.RSC_NAME_FLOW_IN, DataConfig.PROC_DEF_KEY_FLOW_IN);
				procInsId = flowUtil.startProcess(procDefId);
				
				if(procDefId != " " && procInsId != null){
					docRec.setProcessInstanceId(procInsId);
					dataService.saveDocRecipient(docRec);
				} else {
					reAttr.addFlashAttribute("error", true);
					reAttr.addFlashAttribute("errorMessage", "Gửi thất bại đến " + o.getOrganName());
					return model;
				}
			}
			catch(Exception e) {
				logger.error(e.getMessage());
				
			}
		}
		
		doc.setSended(true);
		dataService.saveDocument(doc);
		reAttr.addFlashAttribute("success", true);
		reAttr.addFlashAttribute("successMessage", "Gửi thành công");
		return model;
	}
	
	//================DOC_IN==================
	
	@RequestMapping(value = "/number_in")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Integer autoGeneratedNumberIn(
			@RequestParam("tenureId") Integer tenureId,
			@RequestParam("organId") Integer organId) {
		return dataService.findMaxDocRecNumber(tenureId, organId);
	}
	
	@RequestMapping(value = "/receive_doc/{docId}", method = RequestMethod.POST)
	public ModelAndView receiveDoc(
			@PathVariable("docId") Integer docId, 
			@RequestParam("number") String number,
			@RequestParam("receiveTime") String receiveTime,
			RedirectAttributes reAttr){
		ModelAndView model = new ModelAndView("redirect:/store/list");
		if(docId == null || docId < 0) return model;
		
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		DocumentRecipient docRec = dataService.findDocRecipient(docId, organ.getOrganId());
		if(docRec.getNumber() != null) return model;
		Document doc = docRec.getDocument();
		
		Task curTask = flowUtil.getCurrentTask(docRec.getProcessInstanceId());
		String[] taskName = curTask.getName().split(" ");
		
		try {
			if (curUser.checkRoleByShortName(taskName[0])) {
				flowUtil.getTaskService().setAssignee(curTask.getId(), curUser.getUserName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// number
		int num = UtilMethod.parseNumDoc(number);
		if (num <= 0) {
			num = dataService.findMaxDocRecNumber(doc.getTenure().getTenureId(), organ.getOrganId());
		}

		Date recTime = new Date();
		logger.info(recTime.toString());
		logger.info(receiveTime);
		try {
			recTime = UtilMethod.toDate(receiveTime, "dd-MM-yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		logger.info(recTime.toString());
		
		docRec.setNumber(num);
		docRec.setReceiveTime(recTime);
		dataService.saveDocRecipient(docRec);
		flowUtil.getTaskService().complete(curTask.getId());
		
		
		reAttr.addFlashAttribute("doc", doc);
	
		model.setViewName("redirect:/flow/doc_in_info?docId=" + docId);
		return model;
	}
	
	@RequestMapping(value = "/doc_in_info", method = RequestMethod.GET)
	public ModelAndView documentInInfo(
			@RequestParam(value = "docId", required = false) Integer docId){
		ModelAndView model = new ModelAndView("redirect:/store/list");
		if(docId == null || docId < 0) return model;
		
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		DocumentRecipient docRec = dataService.findDocRecipient(docId, organ.getOrganId());
		Document doc = docRec.getDocument();
		DocumentFile file = dataService.findDocFileById(doc.getDocId());

		Task curTask = flowUtil.getCurrentTask(docRec.getProcessInstanceId());
		HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTask(docRec.getProcessInstanceId());
		
		if (curTask == null) return model;
		String []taskName = curTask.getName().split(" ");
		String []roles = taskName[0].split(",");
		
		model.addObject("taskDescription", curTask.getDescription());
		
		//Khi hoàn thành task trước và chưa chuyển quyền
		if (curTask.getAssignee() == null) {
			//Đã tiếp nhận văn bản
			if (preTask != null) {
				//Người đăng nhập là người tiếp nhận văn bản
				if (curUser.getUserName().equals(preTask.getAssignee())) {
					List<Role> candidateRole = dataService
							.findRolesByArrShortName(roles);
					List<User> userList = dataService
							.findUserByArrRoleShortName(organ.getOrganId(),
									roles);
					model.addObject("isForward", true);
					model.addObject("userList", userList);
					model.addObject("candidateRole", candidateRole);
					HistoricTaskInstance preCompletedTask = flowUtil.getPreviousCompletedTaskWithName(
							docRec.getProcessInstanceId(), curTask.getName());
					
				} else { //người đăng nhập không phải là người hoàn thành task trước
					User rUser = dataService.findUserByName(preTask.getAssignee());
					model.addObject("userRole", rUser.getRoleNames());
					model.addObject("assignee", rUser.getUserName());
				}
			} else { //Chưa tiếp nhận văn bản, tiến hành lấy số
				int number = dataService.findMaxDocRecNumber(doc.getTenure().getTenureId(), organ.getOrganId());
				model.addObject("number", number);
			}
		}
		else { //task đã được giao quyền
			User rUser = dataService.findUserByName(curTask.getAssignee());
			model.addObject("userRole", rUser.getRoleNames());
			model.addObject("assignee", rUser.getUserName());
			
			if(curUser.getUserName().equals(curTask.getAssignee())){
				model.addObject("isAccess", true);
				
				if(taskName[taskName.length - 1].equals("check")){
					model.addObject("isCheck", true);
				} 
				
				if(taskName[taskName.length - 1].equals("report")){
					model.addObject("isReport", true);
				} 
			}
		}
		
		model.setViewName("doc-in-info");
		model.addObject("docRec", docRec);
		model.addObject("doc", doc);
		model.addObject("file", file);
		
		return model;
	}

	@RequestMapping(value = "/assign/{docId}/{procId}", method = RequestMethod.POST)
	public ModelAndView assignTask(
			@PathVariable("docId") Integer docId,
			@PathVariable("procId") String procId,
			@RequestParam("userId") Integer userId,
			@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime){
		ModelAndView model = new ModelAndView("redirect:/store/list");
		if(docId < 0) return model;
		
		
		
		
		model.setViewName("redirect:doc-in-info?docId=?" + docId);
		return model;
	}
	
	@RequestMapping(value = "/complete_doc_in", method = RequestMethod.POST)
	public ModelAndView completeFlowDocInTask(){
		ModelAndView model = new ModelAndView("redirect:/store/list");
		
		return model;
	}
	
	/*@RequestMapping(value = "/claim_imp_doc", method = RequestMethod.GET)
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
	}*/
	
	/*@RequestMapping(value ="/unclaim_imp_doc", method = RequestMethod.GET)
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
	}*/
	
	/*@RequestMapping(value = "/create_doc_in", method = RequestMethod.GET)
	public ModelAndView createNewDocIn(){
		ModelAndView model = new ModelAndView("create-doc-in");
		
		User user = securityService.getCurrentUser();
		Organ organ = user.getOrgan();
		if(organ == null){
			model.setViewName("redirect:/store/list");
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
	*/
	/*@RequestMapping(value = "/save_doc_in", method = RequestMethod.POST)
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
	}*/

}
