package com.myoffice.myapp.controllers;

import java.io.File;
import java.io.IOException;
import java.security.spec.MGF1ParameterSpec;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Utilities;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cmd.FindActiveActivityIdsCmd;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
		return dataService.findMaxDocNumber(tenureId, docTypeId, organId);
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
		else doc.setDepartments(null);
		
		if(comment != null && comment.trim().length() > 0) doc.setComment(comment);
		else doc.setComment(null);
		
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
			doc.setNumber(dataService.findMaxDocNumber(tenureId, docTypeId, organ.getOrganId()) - 1);
		}
		
		//LƯU VĂN BẢN VÀ TẠO LUỒNG XỬ LÝ
		if (docId == null || docId <= 0) {
			try {
				String procDefId = flowUtil.getProcessDefinitionId(
						DataConfig.RSC_NAME_FLOW_OUT,
						DataConfig.PROC_DEF_KEY_FLOW_OUT);
				String procInsId = flowUtil.startProcess(procDefId, organ.getOrganId().toString());

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
		
		// SAVE FILE
		UtilMethod.saveDocFile(file, tenure, docType, number, organ.getOrganType().getShortName(), docName, docId,
				dataService, doc);

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
				
				List<DocumentRecipient> recipientList = dataService.findRecipients(doc.getDocId());
				if (recipientList.size() > 0) {
					List<Integer> rowListRecipient = new ArrayList<Integer>();
					List<Integer> elemListRecipient = new ArrayList<Integer>();
					UtilMethod.preparePagination(rowListRecipient, "rowListRecipient", elemListRecipient, "elemListRecipient", recipientList, model, null);
					model.addObject("recipientList", recipientList);
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
								roles, curUser, null);
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
				docRec.setComingTime(new Date());
				
				procDefId = flowUtil.getProcessDefinitionId(DataConfig.RSC_NAME_FLOW_IN, DataConfig.PROC_DEF_KEY_FLOW_IN);
				procInsId = flowUtil.startProcess(procDefId, o.getOrganId().toString());
				
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
		return dataService.findMaxDocRecNumber(tenureId, organId).intValue();
	}
	
	@RequestMapping(value = "/receive_doc", method = RequestMethod.POST)
	public ModelAndView receiveDoc(
			@ModelAttribute("docId") Integer docId, 
			@RequestParam("numberRec") String numberRec,
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
		int num = UtilMethod.parseNumDoc(numberRec);
		if (num <= 0) {
			num = dataService.findMaxDocRecNumber(doc.getTenure().getTenureId(), organ.getOrganId()).intValue();
		}

		Date recTime = new Date();
		try {
			recTime = UtilMethod.toDate(receiveTime, "dd-MM-yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
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
		DocumentFile file = dataService.findNewestDocFile(doc.getDocId());
		AssignContent assContent = docRec.getAssignContent();
		
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
	
			//Tự động bổ sung tên người nhận nhiệm vụ và ngày hết hạn
			if(curTask != null && taskName[taskName.length - 1].equals("report")) {
				//Người nhận nhiệm vụ
				String candidateName = null;
				if(curTask.getAssignee() != null) candidateName = curTask.getAssignee();
				else {
					HistoricTaskInstance preCandidateTask = flowUtil
							.getPreviousCompletedTaskWithName(docRec.getProcessInstanceId(), curTask.getName());
					if(preCandidateTask != null)
						candidateName = preCandidateTask.getAssignee();
				}
				
				if(candidateName != null && !candidateName.equals(assContent.getCandidateName())) {
					assContent.setCandidateName(candidateName);
					dataService.saveAssignContent(assContent);
				}
				
				//Ngày hết hạn
				Date dueDate = curTask.getDueDate();
				Date today = UtilMethod.toDate(new Date(), "dd-MM-yyyy");
				if(dueDate == null || today.compareTo(dueDate) < 0) {
					flowUtil.getTaskService().setDueDate(curTask.getId(), assContent.getTimeEnd());
				}
				
				//Người giao nhiệm vụ
				String ownerName = curTask.getOwner();
				if(ownerName == null || !ownerName.equals(assContent.getOwner().getUserName())) {
					flowUtil.getTaskService().setOwner(curTask.getId(), assContent.getOwner().getUserName());
				}
			}
			
			//Tên người nhận nhiệm vụ
			if(assContent != null && assContent.getCandidateName() != null) {
				User candidate = dataService.findUserByName(assContent.getCandidateName());
				model.addObject("candidate", candidate);
			}
			
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
						String cmpValue = taskName[taskName.length - 1].equals("check") ? null : "lt";
						List<User> userList = dataService.findUserByArrRoleShortName(organ.getOrganId(), roles, curUser, cmpValue);			
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
					int numberRec = dataService.findMaxDocRecNumber(doc.getTenure().getTenureId(), organ.getOrganId()).intValue();
					model.addObject("numberRec", numberRec);
				}
			}
			else { //task đã được giao quyền
				User rUser = dataService.findUserByName(curTask.getAssignee());
				model.addObject("assignee", rUser);
				
				//User đang là người giữ quyền
				if(curUser.getUserName().equals(curTask.getAssignee())){
					model.addObject("isAccess", true);
					
					if(taskName[taskName.length - 1].equals("check")){
						model.addObject("isAssignee", true);
						model.addObject("isCheckTask", true);
					} 
					else if(taskName[taskName.length - 1].equals("report")){
						model.addObject("isReport", true);
					} 
				//User không giữ quyền nhưng là người phân công
				} else if(assContent != null && assContent.getOwner().getUserId() == curUser.getUserId()){
					model.addObject("isOwner", true);
					String[] ownerRole = {"mng"};
					List<Role> assignRole = dataService.findRolesByArrShortName(ownerRole);
					List<User> userList = dataService.findUserByArrRoleShortName(organ.getOrganId(), ownerRole, curUser, null);
					model.addObject("userList", userList);
					model.addObject("assignRole", assignRole);
				}
			}
		}
		
		if(curUser.checkRoleByShortName("inputer")) {
			List<DocumentType> docTypeList = dataService.findAllDocType();
			List<Tenure> tenureList = dataService.findAllTenure();
			List<PrivacyLevel> privacyList = dataService.findAllPrivacyLevel();
			List<EmergencyLevel> emeList = dataService.findAllEmergencyLevel();
			
			model.addObject("docTypeList", docTypeList);
			model.addObject("tenureList", tenureList);
			model.addObject("privacyList", privacyList);
			model.addObject("emeList", emeList);
 		}
		
		String[] numberSign = doc.getNumberSign().split("-");
		if(numberSign.length >= 2) {
			model.addObject("numberSign", numberSign[1]);
			model.addObject("number", numberSign[0]);
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
			@RequestParam(value = "timeStart", required = false) String timeStart,
			@RequestParam(value = "timeEnd", required = false) String timeEnd,
			@RequestParam(value = "content", required = false) String content,
			RedirectAttributes reAttr) throws ParseException{
		ModelAndView model = new ModelAndView("redirect:/store/list/out/1");
		if(docId < 0) return model;
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		DocumentRecipient docRec = dataService.findDocRecipient(docId, organ.getOrganId());
		AssignContent assContent = docRec.getAssignContent();
		
		Date startDate = UtilMethod.toDate(timeStart, "dd-MM-yyyy");
		Date endDate = UtilMethod.toDate(timeEnd, "dd-MM-yyyy");
		
		if(assContent == null) {
			if(startDate == null || endDate == null || (content == null && content.trim().length() > 0)) {
				reAttr.addFlashAttribute("error", true);
				reAttr.addFlashAttribute("errorMessage", "Lỗi, vui lòng điền đầy đủ thông tin nhiệm vụ");
				return model;
			}
			assContent = new AssignContent();
		}
		
		Date today = UtilMethod.toDate(new Date(), "dd-MM-yyyy");
	
		if(endDate.compareTo(today) < 0) {
			reAttr.addFlashAttribute("error", true);
			reAttr.addFlashAttribute("errorMessage", "Lỗi, ngày kết thúc phải lớn hơn hoặc bằng hôm nay");
		}
		else if(startDate.compareTo(endDate) > 0){
			reAttr.addFlashAttribute("error", true);
			reAttr.addFlashAttribute("errorMessage", "Lỗi, ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu");
		} else {
			// Lưu phân công
			assContent.setProgress(0);
			if (startDate != null)
				assContent.setTimeStart(startDate);
			if (endDate != null)
				assContent.setTimeEnd(endDate);
			if (content != null && content.trim().length() > 0)
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
			@RequestParam(value = "report", required = false) String report, 
			@RequestParam("progressValue") Integer progressValue){
		ModelAndView model = new ModelAndView("redirect:/store/list/out/1");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		DocumentRecipient docRec = dataService.findDocRecipient(docId, organ.getOrganId());
		AssignContent assContent = docRec.getAssignContent();
		if (assContent == null)
			return model;

		if (report != null && report.trim().length() > 0)
			assContent.setReport(report);

		if (progressValue >= 0 && progressValue <= 100)
			assContent.setProgress(progressValue);
		
		dataService.saveDocRecipient(docRec);
		
		model.setViewName("redirect:/flow/doc_in_info/" + docId);
		return model;
	}
	
	@RequestMapping(value = "/complete_report")
	public ModelAndView completeReportTask(
			@ModelAttribute("docId") Integer docId,
			@ModelAttribute("procId") String procId,
			RedirectAttributes reAttr) {
		ModelAndView model = new ModelAndView("redirect:/flow/doc_in_info/" + docId);
		if(docId != null && docId > 0){
			Task curTask = flowUtil.getCurrentTask(procId);
			User curUser = securityService.getCurrentUser();
			DocumentRecipient docRec = dataService.findDocRecipient(docId, curUser.getOrgan().getOrganId());
			AssignContent assContent = docRec.getAssignContent();
			User owner = assContent.getOwner();
			
			//Không thể hoàn thành task nếu chưa có file báo cáo
			if(docRec.getAssignContent().getReportDoc() == null){
				reAttr.addFlashAttribute("error", true);
				reAttr.addFlashAttribute("errorMessage", "Lỗi, không tồn tại file báo cáo");
				return model;
			}
			
			if (curTask == null || assContent == null)
				return model;

			String[] taskName = curTask.getName().split(" ");
			if (curUser.checkRoleByShortName(taskName[0]) && curTask.getAssignee().equals(curUser.getUserName())) {
				String taskId = curTask.getId();
				flowUtil.getTaskService().complete(taskId);
				curTask = flowUtil.getCurrentTask(procId);
				flowUtil.getTaskService().setAssignee(curTask.getId(), owner.getUserName());
				assContent.setProgress(100);
				dataService.saveAssignContent(assContent);
			}
		}
		
		return model;
	}
	
	@RequestMapping("/transfer_manage")
	public ModelAndView transferManage(
			@ModelAttribute("docId") Integer docId, 
			@ModelAttribute("acId") Integer acId,
			@RequestParam("userId") Integer userId,
			RedirectAttributes reAttr) {
		ModelAndView model = new ModelAndView("redirect:/flow/doc_in_info/" + docId);
		AssignContent assContent = dataService.findAssignContentById(acId);
		User curUser = securityService.getCurrentUser();
		User transferUser = dataService.findUserById(userId);
		DocumentRecipient docRec = dataService.findDocRecipient(docId, curUser.getOrgan().getOrganId());
		if(docRec.getAssignContent().getContentId() != assContent.getContentId()) return model;
		if(curUser.getUserId() != assContent.getOwner().getUserId()) return model;
		if(!transferUser.checkRoleByShortName("mng")) return model;
		
		assContent.setOwner(transferUser);
		dataService.saveAssignContent(assContent);
		reAttr.addFlashAttribute("success", true);
		reAttr.addFlashAttribute("successMessage", "Chuyển quyền theo dõi, kiểm tra tiến độ nhiệm vụ thành công!");
		
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
		} /*else {
			String reportProcId = docRec.getAssignContent().getReportDoc().getProcessInstanceId();
			if(!flowUtil.isEnded(reportProcId)){
				reAttr.addFlashAttribute("error", true);
				reAttr.addFlashAttribute("errorMessage", "Lỗi, file báo cáo chưa hoàn thành");
				return model;
			}
		}*/
		
		docRec.setCompleted(true);
		dataService.saveDocRecipient(docRec);
		flowUtil.getTaskService().complete(curTask.getId());
		model.addObject("success", true);
		model.addObject("successMessage", "Hoàn thành");
		
		return model;
	}
	
	@RequestMapping(value = "/create_doc_in")
	public ModelAndView createDocIn() {
		ModelAndView model = new ModelAndView("create-doc-in");
		
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
		model.addObject("docId", 0);
		
		return model;
	}
	
	@RequestMapping(value = "/save_doc_in")
	public ModelAndView submitNewDocIn(
			@ModelAttribute("docId") Integer docId, 
			@RequestParam("title") String title,
			@RequestParam("docName") String docName, 
			@RequestParam("epitome") String epitome, 
			@RequestParam("docTypeId") Integer docTypeId,
			@RequestParam("note") String note,
			@RequestParam("number") String number,
			@RequestParam("numberSign") String numberSign,
			@RequestParam(value = "departments", required = false) String departments,
			@RequestParam("tenureId") Integer tenureId,
			@RequestParam(value = "releaseTime", required = false) String releaseTime,
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam("privacyId") Integer privacyId, 
			@RequestParam("emeId") Integer emeId,
			RedirectAttributes reAttr) {
		ModelAndView model = new ModelAndView("redirect:error");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		
		DocumentRecipient docRec = new DocumentRecipient();
		Document doc = new Document();
		DocumentType docType = dataService.findDocTypeById(docTypeId);
		Tenure tenure = dataService.findTenureById(tenureId);
		PrivacyLevel privacyLevel = dataService.findPrivacyLevelById(privacyId);
		EmergencyLevel emeLevel = dataService.findEmergencyLevelById(emeId);
		numberSign = number + "-" + numberSign;
		if(departments != null && departments.trim().length() > 0) numberSign += "-" + departments;
		
		if(docId != null && docId > 0) {
			docRec = dataService.findDocRecipient(docId, organ.getOrganId());
			doc = docRec.getDocument();
		}
		
		doc.setTitle(title);
		doc.setDocName(docName);
		doc.setEpitome(epitome);
		doc.setDocType(docType);
		doc.setNote(note);
		doc.setNumberSign(numberSign);
		doc.setTenure(tenure);
		doc.setPrivacyLevel(privacyLevel);
		doc.setEmergencyLevel(emeLevel);
		doc.setIncoming(true);
		if(departments != null && departments.trim().length() > 0) doc.setDepartments(departments);
		else doc.setDepartments(null);
		
		Integer num = UtilMethod.parseNumDoc(number);
		if (num > 0) {
			doc.setNumber(num);
		}
	
		if(releaseTime != null && releaseTime.trim().length() > 0) {
			try {
				Date releaseDate = UtilMethod.toDate(releaseTime, "dd-MM-yyyy");
				int cmpStart = releaseDate.compareTo(tenure.getTimeStart());
				int cmpEnd = releaseDate.compareTo(tenure.getTimeEnd());
				int cmpReceive = 0;
				if(docRec.getReceiveTime() != null) {
					cmpReceive = releaseDate.compareTo(docRec.getReceiveTime());
				}
				if (cmpStart >= 0 && cmpEnd <= 0 && cmpReceive <=0) {
					doc.setReleaseTime(releaseDate);
				} else {
					reAttr.addFlashAttribute("error", true);
					if (cmpReceive < 0) {
						reAttr.addFlashAttribute("errorMessage",
								"Lỗi, ngày ban hành phải thuộc khoảng thời gian trong năm đã chọn, vui lòng cập nhật lại");
					} else {
						reAttr.addFlashAttribute("errorMessage",
								"Lỗi, ngày ban hành phải nhỏ hơn hoặc bằng tiếp nhận");
					}
				}
			} catch (Exception e) {}
		}
		
		docRec.setDocument(doc);

		//LƯU VĂN BẢN VÀ TẠO LUỒNG XỬ LÝ
		if (docId == null || docId <= 0) {
			try {
				String procDefId = flowUtil.getProcessDefinitionId(DataConfig.RSC_NAME_FLOW_IN, DataConfig.PROC_DEF_KEY_FLOW_IN);
				String procInsId = flowUtil.startProcess(procDefId, organ.getOrganId().toString());
				boolean checkSave = false;
				
				if (procDefId == " " || procInsId == null) {
					model.setViewName("error");
					model.addObject("errorMessage", "Luồng văn bản chưa được tạo, vui lòng liên hệ admin");
					return model;
				} else {
					dataService.saveDocument(doc);
					doc.setCompleted(true);
					doc.setSended(true);
					doc.setIncoming(true);
					
					docRec.setDocument(doc);
					docRec.setProcessInstanceId(procInsId);
					docRec.setOrgan(organ);
					docRec.setComingTime(new Date());
					checkSave = dataService.saveDocRecipient(docRec);
					doc = docRec.getDocument();
				}

				if (procInsId != null && !checkSave) {
					flowUtil.deleteProcessInstanceById(procInsId, "can not create new document");
					dataService.deleteDocumentIn(doc);
					model.setViewName("error");
					model.addObject("errorMessage", "Lỗi, không thể tạo văn bản");
					return model;
				}
			} catch (Exception e) {}
		} else {
			dataService.saveDocument(doc);
			dataService.saveDocRecipient(docRec);
		}
		
		//LƯU TẬP TIN
		String []sign = numberSign.split("/");
		UtilMethod.saveDocFile(file, tenure, docType, number, sign[sign.length -1], docName, docId, dataService, doc);
		
		model.setViewName("redirect:doc_in_info/" + doc.getDocId());
		return model;
	}
	
	//=============WAITING DOC=============
	@RequestMapping(value = "/wait_list/{type}")
	public ModelAndView waitingPage(@PathVariable("type") String type) {
		ModelAndView model = new ModelAndView("wait");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		List<Integer> elemList = new ArrayList<Integer>();
		List<Integer> rowList = new ArrayList<Integer>();
		
		if (type.equals("in")) {
			List<DocTypeMenuItem> typeInList = dataService.findMenuDocIn(organ.getOrganId(), false, null);
			List<DocumentRecipient> docList = dataService.findDocRecipient(organ.getOrganId(), null, null, false, 0, 9);
			List<ItemDocInWait> docInList = UtilMethod.getListDocInWait(dataService, flowUtil, docList, null);
			UtilMethod.preparePagination(rowList, "rowList", elemList, "elemList", docInList, model, null);
			
			model.addObject("typeInList", typeInList);
			model.addObject("docList", docInList);
			model.addObject("in", true);

		} else if (type.equals("out")) {
			List<DocTypeMenuItem> typeOutList = dataService.findMenuDocOut(organ.getOrganId(), false, null);
			List<Document> docList = dataService.findDocumentBy(organ.getOrganId(), null, null, false, false, 0, 9, true);
			List<ItemDocOutWait> docOutList = UtilMethod.getListDocOutWait(dataService, flowUtil, docList, null);
			UtilMethod.preparePagination(rowList, "rowList", elemList, "elemList", docOutList, model, null);
			
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
			UtilMethod.preparePagination(rowList, "rowList", elemList, "elemList", docList, model, null);
			
			model.addObject("docList", docInList);
			model.addObject("in", true);

		} else if (type.equals("out")) { // flow out
			List<Document> docList = dataService.findDocumentBy(organ.getOrganId(), null, docTypeId, false, false, null, null, true);
			List<ItemDocOutWait> docOutList = UtilMethod.getListDocOutWait(dataService, flowUtil, docList, null);
			UtilMethod.preparePagination(rowList, "rowList", elemList, "elemList", docOutList, model, null);
			
			model.addObject("docList", docOutList);
			model.addObject("out", true);
		}

		return model;
	}

	@RequestMapping(value = "/disable_list")
	public ModelAndView disableList() {
		ModelAndView model = new ModelAndView("disable-list");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		
		List<Document> disableList = dataService.findDocumentBy(organ.getOrganId(), null, null, null, null, null, null, false);
		List<Integer> elemList = new ArrayList<Integer>();
		List<Integer> rowList = new ArrayList<Integer>();
		UtilMethod.preparePagination(rowList, "rowList", elemList, "elemList", disableList, model, null);
		logger.info(String.valueOf(disableList.size()));
		model.addObject("docList", disableList);
		model.addObject("out", true);
		return model;
	}
	
	//============MY TASK==================
	
	@RequestMapping(value = "/my_task")
	public ModelAndView myTask() {
		ModelAndView model = new ModelAndView("my-task");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		
		//Văn bản chờ xử lý
		List<Document> docOutList = dataService.findDocumentBy(organ.getOrganId(), null, null, false, false, null, null, true);
		List<ItemDocOutWait> docOutWaitList = UtilMethod.getListDocOutWait(dataService, flowUtil, docOutList, curUser.getUserName());
		
		List<Integer> elemListOut = new ArrayList<Integer>();
		List<Integer> rowListOut = new ArrayList<Integer>();
		UtilMethod.preparePagination(rowListOut, "rowListOut", elemListOut, "elemListOut", docOutWaitList, model, null);
		model.addObject("docOutList", docOutWaitList);
		
		//Văn bản được phân công nhiệm vụ
		List<DocumentRecipient> docCandidateList = dataService.findDocRecByCandidate(organ.getOrganId(), curUser.getUserName());
		List<ItemDocInWait> docCandidateWaitList = UtilMethod.getListDocInWait(dataService, flowUtil, docCandidateList, curUser.getUserName());
		
		List<Integer> elemListCandidate = new ArrayList<Integer>();
		List<Integer> rowListCandidate = new ArrayList<Integer>();
		UtilMethod.preparePagination(rowListCandidate, "rowListCandidate", elemListCandidate, "elemListCandidate", docCandidateWaitList, model, null);
		model.addObject("docCandidateList", docCandidateWaitList);
		
		//Văn bản đã phân công nhiệm vụ
		if(curUser.checkRoleByShortName("mng")) {
			List<DocumentRecipient> docOwnerList = dataService.findDocRecByOwner(organ.getOrganId(), curUser.getUserId());
			List<ItemDocInWait> docOwnerWaitList = UtilMethod.getListOwnerWait(dataService, flowUtil, docOwnerList, curUser.getUserName());
			
			List<Integer> elemListOwner = new ArrayList<Integer>();
			List<Integer> rowListOwner = new ArrayList<Integer>();
			UtilMethod.preparePagination(rowListOwner, "rowListOwner", elemListOwner, "elemListOwner", docOwnerWaitList, model, null);
			model.addObject("docOwnerList", docOwnerWaitList);
		}
			
		//Văn bản chờ gửi
		if(curUser.checkRoleByShortName("outputer")) {
			List<Document> docSendList = dataService.findDocumentBy(organ.getOrganId(), null, null, true, false, null, null, true);
			List<ItemDocOutWait> docSendWaitList = UtilMethod.getListDocOutWait(dataService, flowUtil, docSendList, null);
			
			List<Integer> elemListSend = new ArrayList<Integer>();
			List<Integer> rowListSend = new ArrayList<Integer>();
			UtilMethod.preparePagination(rowListSend, "rowListSend", elemListSend, "elemListSend", docSendWaitList, model, null);
			model.addObject("docSendList", docSendWaitList);
		}
		
		//Văn bản chờ nhận
		if(curUser.checkRoleByShortName("inputer")) {
			List<DocumentRecipient> docReceiveList = dataService.findDocRecForInputer(organ.getOrganId());
			List<ItemDocInWait> docReceiveWaitList = UtilMethod.getListInputerWait(dataService, flowUtil, docReceiveList, curUser.getUserName());
			
			List<Integer> elemListReceive = new ArrayList<Integer>();
			List<Integer> rowListReceive = new ArrayList<Integer>();
			UtilMethod.preparePagination(rowListReceive, "rowListReceive", elemListReceive, "elemListReceive", docReceiveWaitList, model, null);
			model.addObject("docReceiveList", docReceiveWaitList);
		}
		
		
		
		
		return model;
	}
}
