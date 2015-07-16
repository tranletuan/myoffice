package com.myoffice.myapp.controllers;

import java.io.File;
import java.io.IOException;
import java.security.spec.MGF1ParameterSpec;
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
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.junit.runner.Request;
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
import com.myoffice.myapp.models.dto.AssignContent;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentFile;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Level;
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
import com.myoffice.myapp.support.ItemDocInWait;
import com.myoffice.myapp.support.ItemDocOutWait;
import com.myoffice.myapp.support.DocTypeMenuItem;
import com.myoffice.myapp.utils.FlowUtil;
import com.myoffice.myapp.utils.UtilMethod;

@Controller
@RequestMapping(value = "/flow")
@SessionAttributes({"docId", "procId", "acId"})
public class FlowController extends AbstractController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(FlowController.class);

	//===============DOC_OUT=================
	//type 0 : tạo mới văn bản theo luồng văn bản đi
	//type 1 : tạo văn bản báo cáo theo luồng đến
	@RequestMapping(value = "/create_{type}", method = RequestMethod.GET)
	public ModelAndView createNewDocOut(
			@PathVariable("type") String type){
		ModelAndView model = new ModelAndView("create-doc-out");
		
		User user = securityService.getCurrentUser();
		Organ organ = user.getOrgan();
		Level level = user.getLevel();
		
		if(organ == null || level == null){
			model.addObject("error", true);
			model.addObject("errorMessage", "Lỗi, tài khoản chưa thuộc cơ quan nào hoặc chưa có chức vụ");
			return model;
		}
		
		List<DocumentType> docTypeList = dataService.findAllDocType();
		List<EmergencyLevel> emeList = dataService.findAllEmergencyLevel();
		List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
		List<Tenure> tenureList = dataService.findAllTenure(); 
		
		if(docTypeList.size() == 0) {
			model.addObject("error", true);
			model.addObject("errorMessage", "Lỗi, chưa cập nhật danh sách loại văn bản");
			return model;
		}
		
		if(emeList.size() == 0) {
			model.addObject("error", true);
			model.addObject("errorMessage", "Lỗi, chưa cập nhật danh sách mức độ khẩn");
			return model;
		}
		

		if(privacyList.size() == 0) {
			model.addObject("error", true);
			model.addObject("errorMessage", "Lỗi, chưa cập nhật danh sách mức độ mật");
			return model;
		}
		
		if(tenureList.size() == 0) {
			model.addObject("error", true);
			model.addObject("errorMessage", "Lỗi, chưa cập nhật danh sách năm/nhiệm kỳ");
			return model;
		}
		
		model.addObject("docTypeList", docTypeList);
		model.addObject("emeList", emeList);
		model.addObject("privacyList", privacyList);
		model.addObject("tenureList", tenureList);
		model.addObject("organ", organ);
		model.addObject("docId", 0);
		model.addObject("procId", "");
		
		if(type.equals("doc_out")){
			model.addObject("acId", 0);
		}
		return model;
	}
	
	@RequestMapping(value = "/number", method = RequestMethod.GET)
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
			@ModelAttribute("docId") Integer docId,
			@ModelAttribute("acId") Integer acId,
			@RequestParam("docName") String docName,
			@RequestParam("title") String title,
			@RequestParam("epitome") String epitome,
			@RequestParam("docTypeId") Integer docTypeId,
			@RequestParam("privacyId") Integer privacyId,
			@RequestParam("emeId") Integer emeId,
			@RequestParam("tenureId") Integer tenureId,
			@RequestParam(value = "releaseTime", required = false) String releaseTime,
			@RequestParam("number") String number,
			@RequestParam("numberSign") String numberSign,
			@RequestParam(value = "departments", required = false) String departments,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "comment", required = false) String comment,
			RedirectAttributes reAttr) throws ParseException, IllegalStateException, IOException{
		ModelAndView model = new ModelAndView("redirect:error");
		Tenure tenure = dataService.findTenureById(tenureId);
		DocumentType docType = dataService.findDocTypeById(docTypeId);
		Organ organ = securityService.getCurrentUser().getOrgan();
		numberSign = number + "-" + numberSign;
		if(departments != null && departments.trim().length() > 0) numberSign += "-" + departments;
		
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
		if(departments != null && departments.trim().length() > 0) doc.setDepartments(departments);
		if(comment != null && comment.trim().length() > 0) doc.setComment(comment);
		if(releaseTime != null && releaseTime.trim().length() > 0) {
			try {
				Date releaseDate = UtilMethod.toDate(releaseTime, "dd-MM-yyyy");
				int cmpStart = releaseDate.compareTo(tenure.getTimeStart());
				int cmpEnd = releaseDate.compareTo(tenure.getTimeEnd());

				if (cmpStart >= 0 && cmpEnd <= 0) {
					doc.setReleaseTime(releaseDate);
				} else {
					reAttr.addFlashAttribute("error", true);
					reAttr.addFlashAttribute("errorMessage",
							"Ngày ban hành phải thuộc khoảng thời gian trong năm đã chọn, vui lòng cập nhật lại");
				}
			} catch (Exception e) {}
		}
		
		Integer num = UtilMethod.parseNumDoc(number);
		if (num > 0) {
			doc.setNumber(num);
		} else {
			doc.setNumber(dataService.findMaxNumber(tenureId, docTypeId, organ.getOrganId(), false) - 1);
		}
		
		//LƯU VĂN BẢN VÀ TẠO LUỒNG XỬ LÝ
		if (docId == null || docId <= 0) {
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
					model.addObject("docId", doc.getDocId());
					
					//Văn bản báo cáo luồng văn bản đến
					AssignContent assContent = dataService.findAssignContentById(acId);
					if(assContent != null){
						assContent.setReportDoc(doc);
						dataService.saveAssignContent(assContent);
					}
					
					Task task = flowUtil.getCurrentTask(doc.getProcessInstanceId());
					flowUtil.getTaskService().setAssignee(task.getId(), curUser.getUserName());
					flowUtil.getTaskService().complete(task.getId());
					
					//Kiểm tra người thêm văn bản có phải là người nhận task tiếp theo
					//Nếu đúng bỏ qua task tiếp theo
					task = flowUtil.getCurrentTask(doc.getProcessInstanceId());
					String []taskName = task.getName().split(" ");
					if(curUser.checkRoleByShortName(taskName[0])) {
						flowUtil.getTaskService().setAssignee(task.getId(), curUser.getUserName());
						flowUtil.getTaskService().complete(task.getId());
					}
					
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
		
		model.setViewName("redirect:doc_info/" + doc.getDocId());
		return model;
	}
	
	@RequestMapping(value = "/doc_info/{docId}", method = RequestMethod.GET)
	public ModelAndView documentInfo(
			@PathVariable("docId") Integer docId){
		ModelAndView model = new ModelAndView("redirect:/store/list/out/1");
		Document doc = new Document();

		if (docId != null && docId > 0) {
			doc = dataService.findDocumentById(docId);
			User curUser = securityService.getCurrentUser();
			if(doc.getOrgan().getOrganId() != curUser.getOrgan().getOrganId() 
					|| (!doc.isEnabled() && !curUser.checkRoleByShortName("mng"))) return model;
			
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
			model.addObject("docId", docId);
			model.addObject("procId", doc.getProcessInstanceId());
			
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
						List<Role> assignRole = dataService.findRolesByArrShortName(roles);
						List<User> userList = dataService.findUserByArrRoleShortName(curUser.getOrgan().getOrganId(),
								roles, curUser, false);
						model.addObject("isForward", true);
						model.addObject("userList", userList);
						model.addObject("assignRole", assignRole);
						HistoricTaskInstance preCompletedTask = flowUtil.getPreviousCompletedTaskWithName(
										doc.getProcessInstanceId(),
										curTask.getName());
						if(preCompletedTask != null){
							User preUser = dataService.findUserByName(preCompletedTask.getAssignee());
							model.addObject("preUser", preUser);
							model.addObject("canReturn", true);
						}
					}
					
					//Thông tin người vừa hoàn thành task trước nhưng chưa trao quyền tiếp theo
					if(preTask != null) {
						User rUser = dataService.findUserByName(preTask.getAssignee());
						model.addObject("assignee", rUser);
						model.addObject("taskDescription", preTask.getDescription());
					}
				} else { //task đã được giao quyền
					User rUser = dataService.findUserByName(curTask.getAssignee());
					model.addObject("assignee", rUser);
					
					if(curUser.getUserName().equals(curTask.getAssignee())){
						model.addObject("isAccess", true);
						
						if(taskName[taskName.length - 1].equals("check")){
							model.addObject("isCheck", true);
						}
					}
				}
			}
			
			model.addObject("doc", doc);
			model.addObject("acId", 0);
			model.setViewName("doc-info");
		}
	
		return model;
	}
	
	//==============COMMON===================
	@RequestMapping(value = "/download_document/{docFileId}", method = RequestMethod.GET)
	public void downLoadDocument(
			HttpServletResponse response,
			@PathVariable("docFileId") Integer docFileId)throws IOException {
		DocumentFile docFile = dataService.findDocFileById(docFileId);
		dataService.downLoadFile(docFile.getFilePath() + docFile.getFileName(), response);
	}
	
	@RequestMapping(value = "/forward/{direct}")
	public ModelAndView forwardUser(
			@PathVariable("direct") String direct,
			@ModelAttribute("docId") Integer docId,
			@ModelAttribute("procId") String procId,
			@RequestParam(value = "userId", required = false) Integer userId){
		ModelAndView model = new ModelAndView("redirect:/flow/" + direct + "/" + docId);
		
		User curUser = securityService.getCurrentUser();
		Task curTask = flowUtil.getCurrentTask(procId);
		HistoricTaskInstance preCompletedTask = flowUtil.getPreviousCompletedTask(procId);
		
		if(curTask == null) return model;		
		String[] taskName = curTask.getName().split(" ");
		
		if (preCompletedTask.getAssignee().equals(curUser.getUserName())) {
			if (userId != null) {
				User user = dataService.findUserById(userId);
				if (user.checkRoleByShortName(taskName[0])) {
					flowUtil.getTaskService().setAssignee(curTask.getId(), user.getUserName());
				}
			} else {
				HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTaskWithName(procId, curTask.getName());
				if (preTask != null) {
					flowUtil.getTaskService().setAssignee(curTask.getId(), preTask.getAssignee());
				}
			}
		}
		
		return model;
	}
	
	@RequestMapping(value = "/complete/{checkNum}/{direct}")
	public ModelAndView completedTask(
			@PathVariable("checkNum") Integer checkNum,
			@ModelAttribute("docId") Integer docId,
			@ModelAttribute("procId") String procId,
			@PathVariable("direct") String direct){
		ModelAndView model = new ModelAndView("redirect:/flow/" + direct + "/" + docId);
		
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
			}
		}
	
		return model;
	}
	
	@RequestMapping(value = "/complete_assign/{checkNum}/{direct}")
	public ModelAndView completedAndAssign(
			@PathVariable("checkNum") Integer checkNum,
			@ModelAttribute("docId") Integer docId,
			@ModelAttribute("procId") String procId,
			@PathVariable("direct") String direct){
		ModelAndView model = new ModelAndView("redirect:/flow/" + direct + "/" + docId);
		
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
				HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTaskWithName(procId, curTask.getName());
				
				if (preTask != null) {
					flowUtil.getTaskService().setAssignee(curTask.getId(), preTask.getAssignee());
				}
			}
			
			
		}
		
		return model;
	}
	
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public ModelAndView sendDoc(
			@ModelAttribute("docId") Integer docId,
			@RequestParam("recipients") Integer[] recipients,
			RedirectAttributes reAttr){
		ModelAndView model = new ModelAndView("redirect:doc_info/" + docId);
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
	
	@RequestMapping(value = "/status/{value}")
	public ModelAndView changeStatusDoc(
			@ModelAttribute("docId") Integer docId,
			@ModelAttribute("procId") String procId,
			@PathVariable("value") boolean value){
		ModelAndView model = new ModelAndView("redirect:/store/list/out/1");
		if(docId == null || docId < 0) return model;
		Task curTask = flowUtil.getCurrentTask(procId);
		Document doc = dataService.findDocumentById(docId);
		doc.setEnabled(value);
		dataService.saveDocument(doc);
		
		if(curTask != null) {
			if (!value) {
				flowUtil.getRuntimeService().suspendProcessInstanceById(procId);
			} else {
				flowUtil.getRuntimeService().activateProcessInstanceById(procId);
			}
		}
		
		model.setViewName("redirect:/flow/doc_info/" + docId);
		return model;
	}
	
	//=============DOC_IN==================
	@RequestMapping(value = "/number_in", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Integer autoGeneratedNumberIn(
			@RequestParam("tenureId") Integer tenureId,
			@RequestParam("organId") Integer organId) {
		return dataService.findMaxDocRecNumber(tenureId, organId);
	}
	
	@RequestMapping(value = "/user_list", produces = "text/html;charset=UTF-8")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public String getUserList(
			@RequestParam("roleId") Integer roleId){
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		List<User> userList = dataService.findAllUsers(organ.getOrganId(), roleId);
		String result = "";
		
		if (userList.size() > 0) {
			result = "<select class=\"form-control\" name=\"userId\">";
			for (User u : userList) {
				result += "<option value=\"" + u.getUserId() + "\">";
				if (u.getUserDetail() != null) {
					result += u.getUserName();
				} else {
					result += u.getUserDetail().getFullName();
				}

				result += "</option>";
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/receive_doc", method = RequestMethod.POST)
	public ModelAndView receiveDoc(
			@ModelAttribute("docId") Integer docId, 
			@RequestParam("number") String number,
			@RequestParam("receiveTime") String receiveTime,
			RedirectAttributes reAttr){
		ModelAndView model = new ModelAndView("redirect:/store/list/out/1");
		if(docId == null || docId < 0) return model;
		
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		DocumentRecipient docRec = dataService.findDocRecipient(docId, organ.getOrganId());
		if(docRec.getNumber() != null) return model;
		Document doc = docRec.getDocument();
		
		Task curTask = flowUtil.getCurrentTask(docRec.getProcessInstanceId());
		String[] taskName = curTask.getName().split(" ");
		
		//Check user and role
		if (!curUser.checkRoleByShortName(taskName[0])){
			return model;
		}
		
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
	
		model.setViewName("redirect:/flow/doc_in_info/" + docId);
		return model;
	}
	
	@RequestMapping(value = "/doc_in_info/{docId}", method = RequestMethod.GET)
	public ModelAndView documentInInfo(
			@PathVariable("docId") Integer docId){
		ModelAndView model = new ModelAndView("redirect:/store/list/out/1");
		if(docId == null || docId < 0) return model;
		
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		DocumentRecipient docRec = dataService.findDocRecipient(docId, organ.getOrganId());
		Document doc = docRec.getDocument();
		DocumentFile file = dataService.findDocFileById(doc.getDocId());
		if(docRec.getAssignContent() != null) model.addObject("acId", docRec.getAssignContent().getContentId());
		
		if(flowUtil.isEnded(docRec.getProcessInstanceId())){
			if(docRec.isCompleted() == false) {
				docRec.setCompleted(true);
				dataService.saveDocRecipient(docRec);
			}
			model.addObject("taskDescription",
					"Văn bản đã được xử lý hoàn tất");
		} else {
			Task curTask = flowUtil.getCurrentTask(docRec.getProcessInstanceId());
			HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTask(docRec.getProcessInstanceId());
			
			if (!doc.isEnabled()) return model;
			String []taskName = curTask.getName().split(" ");
			String []roles = taskName[0].split(",");
			
			model.addObject("taskDescription", curTask.getDescription());
			
			//Khi hoàn thành task trước và chưa chuyển quyền
			if (curTask.getAssignee() == null) {
				//Đã tiếp nhận văn bản
				if (preTask != null) {
					User rUser = dataService.findUserByName(preTask.getAssignee());
					model.addObject("assignee", rUser);
					model.addObject("taskDescription", preTask.getDescription());
					//Người đăng nhập là người tiếp nhận văn bản
					if (curUser.getUserName().equals(preTask.getAssignee())) {
						List<Role> assignRole = dataService.findRolesByArrShortName(roles);
						List<User> userList = dataService.findUserByArrRoleShortName(organ.getOrganId(), roles, curUser, true);
					
						model.addObject("isForward", true);
						model.addObject("userList", userList);
						model.addObject("assignRole", assignRole);
						HistoricTaskInstance preCompletedTask = flowUtil.getPreviousCompletedTaskWithName(
								docRec.getProcessInstanceId(), curTask.getName());
						
						if(preCompletedTask != null){
							User preUser = dataService.findUserByName(preCompletedTask.getAssignee());
							model.addObject("canReturn", true);
							model.addObject("preUser", preUser);
						}
					}
				} else { //Chưa tiếp nhận văn bản, tiến hành lấy số
					int number = dataService.findMaxDocRecNumber(doc.getTenure().getTenureId(), organ.getOrganId());
					model.addObject("number", number);
				}
			}
			else { //task đã được giao quyền
				User rUser = dataService.findUserByName(curTask.getAssignee());
				model.addObject("assignee", rUser);
						
				if(curUser.getUserName().equals(curTask.getAssignee())){
					model.addObject("isAccess", true);
					
					if(taskName[taskName.length - 1].equals("check")){
						model.addObject("isAssign", true);
						model.addObject("isCheckTask", true);
					} 
					else if(taskName[taskName.length - 1].equals("report")){
						model.addObject("isReport", true);
					} 
				}
			}
		}
		
		model.setViewName("doc-in-info");
		model.addObject("docRec", docRec);
		model.addObject("doc", doc);
		model.addObject("file", file);
		model.addObject("docId", docId);
		model.addObject("procId", docRec.getProcessInstanceId());
		
		return model;
	}

	@RequestMapping(value = "/assign_content", method = RequestMethod.POST)
	public ModelAndView assignTask(
			@ModelAttribute("docId") Integer docId,
			@ModelAttribute("procId") String procId,
			@RequestParam("timeStart") String timeStart,
			@RequestParam("timeEnd") String timeEnd,
			@RequestParam("content") String content,
			RedirectAttributes reAttr) throws ParseException{
		ModelAndView model = new ModelAndView("redirect:/store/list/out/1");
		if(docId < 0) return model;
		AssignContent assContent = new AssignContent();
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		DocumentRecipient docRec = dataService.findDocRecipient(docId, organ.getOrganId());
		
		Date startDate = UtilMethod.toDate(timeStart, "dd-MM-yyyy");
		Date endDate = UtilMethod.toDate(timeEnd, "dd-MM-yyyy");
		
		if(startDate.after(endDate)){
			reAttr.addFlashAttribute("error", true);
			reAttr.addFlashAttribute("errorMessage", "Lỗi, ngày kết thúc phải lớn hơn ngày bắt đầu");
		} else {
			// Lưu phân công
			assContent.setTimeStart(startDate);
			assContent.setTimeEnd(endDate);
			assContent.setContent(content);
			assContent.setOwner(curUser);
			docRec.setAssignContent(assContent);
			dataService.saveDocRecipient(docRec);
		}
		
		model.setViewName("redirect:/flow/doc_in_info/" + docId);
		return model;
	}
	
	@RequestMapping(value = "/send_report", method = RequestMethod.POST)
	public ModelAndView reportTask(
			@ModelAttribute("docId") Integer docId,
			@ModelAttribute("procId") String procId,
			@RequestParam(value = "report", required = false) String report){
		ModelAndView model = new ModelAndView("redirect:/store/list/out/1");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		DocumentRecipient docRec = dataService.findDocRecipient(docId, organ.getOrganId());
		AssignContent assContent = docRec.getAssignContent();
		if(assContent == null) return model;
		
		assContent.setReport(report);
		dataService.saveDocRecipient(docRec);
		
		model.setViewName("redirect:/flow/doc_in_info/" + docId);
		return model;
	}
	
	@RequestMapping(value = "/complete_save")
	public ModelAndView completeDocIn(
			@ModelAttribute("docId") Integer docId,
			@ModelAttribute("procId") String procId,
			RedirectAttributes reAttr){
		ModelAndView model = new ModelAndView("redirect:doc_in_info/" + docId);
		User user = securityService.getCurrentUser();
		Task curTask = flowUtil.getCurrentTask(procId);
		Organ organ = user.getOrgan();
		
		DocumentRecipient docRec = dataService.findDocRecipient(docId, organ.getOrganId());
		if(docRec.getAssignContent().getReportDoc() == null){
			reAttr.addFlashAttribute("error", true);
			reAttr.addFlashAttribute("errorMessage", "Lỗi, không tồn tại file báo cáo");
			return model;
		} else {
			String reportProcId = docRec.getAssignContent().getReportDoc().getProcessInstanceId();
			if(!flowUtil.isEnded(reportProcId)){
				reAttr.addFlashAttribute("error", true);
				reAttr.addFlashAttribute("errorMessage", "Lỗi, file báo cáo chưa hoàn thành");
				return model;
			}
		}
		
		docRec.setCompleted(true);
		dataService.saveDocRecipient(docRec);
		flowUtil.getTaskService().complete(curTask.getId());
		model.addObject("success", true);
		model.addObject("successMessage", "Hoàn thành");
		
		return model;
	}
	
	//=============WAITING DOC=============
	@RequestMapping(value = "/wait_list/{type}")
	public ModelAndView waitingPage(@PathVariable("type") String type) {
		ModelAndView model = new ModelAndView("wait");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();

		if (type.equals("in")) {
			List<DocTypeMenuItem> typeInList = dataService.findMenuDocIn(organ.getOrganId(), false, null);
			List<DocumentRecipient> docList = dataService.findDocRecipient(organ.getOrganId(), null, null, false, 0, 9);
			List<ItemDocInWait> docInList = UtilMethod.getListDocInWait(dataService, flowUtil, docList, null);
			
			model.addObject("typeInList", typeInList);
			model.addObject("docList", docInList);
			model.addObject("in", true);

		} else if (type.equals("out")) {
			List<DocTypeMenuItem> typeOutList = dataService.findMenuDocOut(organ.getOrganId(), false, null);
			List<Document> docList = dataService.findDocumentBy(organ.getOrganId(), null, null, false, 0, 9, true);
			List<ItemDocOutWait> docOutList = UtilMethod.getListDocOutWait(dataService, flowUtil, docList, null);

			model.addObject("typeOutList", typeOutList);
			model.addObject("docList", docOutList);
			model.addObject("out", true);
		}
		return model;
	}

	@RequestMapping(value = "/wait_list/{type}/{docTypeId}")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ModelAndView waitListType(@PathVariable("type") String type, @PathVariable("docTypeId") Integer docTypeId) {
		ModelAndView model = new ModelAndView("fragment/wait-element");

		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		List<Integer> elemList = new ArrayList<Integer>();
		List<Integer> rowList = new ArrayList<Integer>();

		// flow in
		if (type.equals("in")) {
			List<DocumentRecipient> docList = dataService.findDocRecipient(organ.getOrganId(), null, docTypeId, false, null, null);
			List<ItemDocInWait> docInList = UtilMethod.getListDocInWait(dataService, flowUtil, docList, null);
			UtilMethod.preparePagination(rowList, elemList, docList, model, null);
			
			model.addObject("docList", docInList);
			model.addObject("in", true);

		} else if (type.equals("out")) { // flow out
			List<Document> docList = dataService.findDocumentBy(organ.getOrganId(), null, docTypeId, false, null, null, true);
			List<ItemDocOutWait> docOutList = UtilMethod.getListDocOutWait(dataService, flowUtil, docList, null);
			UtilMethod.preparePagination(rowList, elemList, docOutList, model, null);
			
			model.addObject("docList", docOutList);
			model.addObject("out", true);
		}

		return model;
	}

	//============MY TASK==================
	
	@RequestMapping(value = "/my_task")
	public ModelAndView myTask() {
		ModelAndView model = new ModelAndView("my-task");
		
		return model;
	}
}
