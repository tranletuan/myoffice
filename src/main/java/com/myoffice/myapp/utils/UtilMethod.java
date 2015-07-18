package com.myoffice.myapp.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.controllers.FlowController;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentFile;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataConfig;
import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.support.ItemDocInWait;
import com.myoffice.myapp.support.ItemDocOutWait;

public class UtilMethod {
	
	private static final Logger logger = LoggerFactory
			.getLogger(UtilMethod.class);

	//Between two day
	public static int betweenTwoDay(Date d1, Date d2) {
		if(d1 == null || d2 == null) return -1;
		if(d1.before(d2)) {
			return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
		} else {
			return (int) ((d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
		}
		
	}
	
	//Covert String to Date
	public static Date toDate(String dateString, String dateFormat) throws ParseException{
	    DateFormat df = new SimpleDateFormat(dateFormat); 
	    Date rsDate = df.parse(dateString);
	    return rsDate;
	}
	
	//Convert Date to String
	public static String dateToString(Date date, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
		String rsDate = df.format(date);
		return rsDate;
	}
	
	// Check String is Number
	public static Integer parseNumDoc(String number) {
		Integer integer = -1;
		try {
			integer = Integer.parseInt(number);
		} catch (NumberFormatException nfe) {

		}
		return integer;
	}
	
	public static void preparePagination(List<Integer> rowList, List<Integer> elemList, List infoList,
			ModelAndView model, Integer maxElementPerRow) {
		int maxElem = 10;
		if(maxElementPerRow != null) maxElem = maxElementPerRow;
		for(int i = 0; i < maxElem; i++){
			elemList.add(i);
		}
		
		int numRow = infoList.size() / 10;
		int maxRow = infoList.size() % 10 == 0 ? numRow : numRow + 1;
		
		for(int i = 0; i < maxRow; i++){
			rowList.add(i);
		}
		

		model.addObject("rowList", rowList);
		model.addObject("elemList", elemList);
	}
	
	public static List<ItemDocOutWait> getListDocOutWait(DataService dataService, FlowUtil flowUtil,
			List<Document> docList, String userName) {
		
		List<ItemDocOutWait> docOutList = new ArrayList<ItemDocOutWait>();

		for (Document doc : docList) {
			User user = null;
			Date taskTime = null;
			boolean checkUser = false;
			
			if (!flowUtil.isEnded(doc.getProcessInstanceId())) {
				Task task = flowUtil.getCurrentTask(doc.getProcessInstanceId());
				if (task.getAssignee() == null) {
					HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTask(doc.getProcessInstanceId());
					if (preTask != null) {
						if (userName == null || (userName != null && userName.equals(preTask.getAssignee()))) {
							user = dataService.findUserByName(preTask.getAssignee());
							taskTime = preTask.getCreateTime();
							checkUser = true;
						}
					}
				} else if(userName == null || (userName != null && userName.equals(task.getAssignee()))){
					user = dataService.findUserByName(task.getAssignee());
					taskTime = task.getCreateTime();
					checkUser = true;
				}
			} else {
				HistoricProcessInstance completedProc = flowUtil.getProcessCompled(doc.getProcessInstanceId());
				if(completedProc != null) {
					taskTime = completedProc.getEndTime();
				}
			}
			
			if (userName == null || checkUser == true) {
				ItemDocOutWait docInfo = new ItemDocOutWait();
				docInfo.setDoc(doc);
				docInfo.setUser(user);
				docInfo.setTaskTime(taskTime);
				docOutList.add(docInfo);
			}
		}
		
		return docOutList;
	}
	
	public static List<ItemDocInWait> getListDocInWait(DataService dataService, FlowUtil flowUtil,
			List<DocumentRecipient> docList, String userName) {
		List<ItemDocInWait> docInList = new ArrayList<ItemDocInWait>();
		
		for (DocumentRecipient docRec : docList) {
			User user = null;
			
			boolean checkUser = false;
			if (!flowUtil.isEnded(docRec.getProcessInstanceId())) {
				Task task = flowUtil.getCurrentTask(docRec.getProcessInstanceId());
				
				if (task.getAssignee() == null) {
					HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTask(docRec.getProcessInstanceId());
					if (preTask != null) {
						if (userName == null || (userName != null && userName.equals(preTask.getAssignee()))) {
							user = dataService.findUserByName(preTask.getAssignee());
							checkUser = true;
						}
					}
				} else if(userName == null || (userName != null && userName.equals(task.getAssignee()))){
					user = dataService.findUserByName(task.getAssignee());
					checkUser = true;
				}
			}
			
			if (userName == null || checkUser == true) {
				docInList.add(new ItemDocInWait(docRec, user));
			}
		}
		
		return docInList;
	}
	
	public static void saveDocFile(MultipartFile file, Tenure tenure, DocumentType docType, String number,
			String organType, String docName, Integer docId, DataService dataService, Document doc) {
		
		// SAVE FILE
		if (file != null && doc.getDocId() != null) {
			try {
				DocumentFile docFile = new DocumentFile();

				// File Path
				String filePath = DataConfig.DIR_SERVER + tenure.getTenureName() + File.separator
						+ docType.getDocTypeName() + File.separator;
				// File Name
				String fileName = doc.getDocId() + "-" + number + "-" + docType.getShortName() + "-" + organType
						+ "-" + docName;

				String[] parts = file.getOriginalFilename().split("\\.");
				String tail = parts[parts.length - 1];
				if(tail.trim().length() <= 0) return;
				
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
	}
}
