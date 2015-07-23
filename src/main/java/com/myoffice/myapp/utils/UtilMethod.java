package com.myoffice.myapp.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.controllers.FlowController;
import com.myoffice.myapp.models.dto.AssignContent;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentFile;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.dto.UserDetail;
import com.myoffice.myapp.models.service.DataConfig;
import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.support.ItemDocInWait;
import com.myoffice.myapp.support.ItemDocOutWait;
import com.myoffice.myapp.support.JSSeries;
import com.myoffice.myapp.support.JSonChart;
import com.myoffice.myapp.support.JSonRow;

public class UtilMethod {

	private static final Logger logger = LoggerFactory.getLogger(UtilMethod.class);

	// Get User Name
	public static String getFullName(User user) {
		String result = user.getUserName();
		UserDetail userDetail = user.getUserDetail();
		if (userDetail != null) {
			if (userDetail.getFullName() != null && userDetail.getFullName().trim().length() > 0) {
				result = userDetail.getFullName();
			}
		}
		return result;
	}

	// Between two day
	public static int betweenTwoDay(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return -1;
		if (d1.before(d2)) {
			return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
		} else {
			return (int) ((d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
		}

	}

	// Covert String to Date
	public static Date toDate(String dateString, String dateFormat) throws ParseException {
		try {
			DateFormat df = new SimpleDateFormat(dateFormat);
			Date rsDate = df.parse(dateString);
			return rsDate;
		} catch (Exception e) {
			return null;
		}
	}
	
	//Convert Date to Date
	public static Date toDate(Date date, String dateFormat) {
		try {
			DateFormat df = new SimpleDateFormat(dateFormat);
			String dateString = df.format(date);
			Date rsDate = df.parse(dateString);
			return rsDate;
		} catch (ParseException e) {
			return null;
		}
	}

	// Convert Date to String
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

	public static void preparePagination(List<Integer> rowList, String rowListName, List<Integer> elemList,
			String elemListName, List infoList, ModelAndView model, Integer maxElementPerRow) {
		int maxElem = 10;
		if (maxElementPerRow != null)
			maxElem = maxElementPerRow;
		for (int i = 0; i < maxElem; i++) {
			elemList.add(i);
		}

		int numRow = infoList.size() / 10;
		int maxRow = infoList.size() % 10 == 0 ? numRow : numRow + 1;

		for (int i = 0; i < maxRow; i++) {
			rowList.add(i);
		}

		model.addObject(rowListName, rowList);
		model.addObject(elemListName, elemList);
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
				taskTime = task.getCreateTime();

				if (task.getAssignee() == null) {
					HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTask(doc.getProcessInstanceId());
					if (preTask != null) {
						if (userName == null || (userName != null && userName.equals(preTask.getAssignee()))) {
							user = dataService.findUserByName(preTask.getAssignee());
							taskTime = preTask.getCreateTime();
							checkUser = true;
						}
					}
				} else if (userName == null || (userName != null && userName.equals(task.getAssignee()))) {
					user = dataService.findUserByName(task.getAssignee());
					checkUser = true;
				}
			} else {
				HistoricProcessInstance completedProc = flowUtil.getProcessCompled(doc.getProcessInstanceId());
				if (completedProc != null) {
					taskTime = completedProc.getEndTime();
				}
			}

			if (userName == null || checkUser == true) {
				ItemDocOutWait item = new ItemDocOutWait();
				item.setDoc(doc);
				item.setAssignee(user);
				item.setTaskTime(taskTime);
				docOutList.add(item);
			}
		}

		return docOutList;
	}

	public static List<ItemDocInWait> getListDocInWait(DataService dataService, FlowUtil flowUtil,
			List<DocumentRecipient> docList, String userName) {
		List<ItemDocInWait> docInList = new ArrayList<ItemDocInWait>();

		for (DocumentRecipient docRec : docList) {
			User user = null;
			Date taskTime = null;
			boolean checkUser = false;
			if (!flowUtil.isEnded(docRec.getProcessInstanceId())) {
				Task task = flowUtil.getCurrentTask(docRec.getProcessInstanceId());
				taskTime = task.getCreateTime();

				if (task.getAssignee() == null) {
					HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTask(docRec.getProcessInstanceId());
					if (preTask != null) {
						if (userName == null || (userName != null && userName.equals(preTask.getAssignee()))) {
							user = dataService.findUserByName(preTask.getAssignee());
							taskTime = preTask.getCreateTime();
							checkUser = true;
						}
					}
				} else if (userName == null || (userName != null && userName.equals(task.getAssignee()))) {
					user = dataService.findUserByName(task.getAssignee());
					checkUser = true;
				}
			}

			if (userName == null || checkUser == true) {
				User owner = null;
				User candidate = null;

				if (docRec.getAssignContent() != null) {
					AssignContent assContent = docRec.getAssignContent();
					owner = dataService.findUserByName(assContent.getOwnerName());
					if (assContent.getCandidateName() != null) {
						candidate = dataService.findUserByName(assContent.getCandidateName());
					}
				}

				ItemDocInWait item = new ItemDocInWait();
				item.setDocRec(docRec);
				item.setAssignee(user);
				item.setOwner(owner);
				item.setCandidate(candidate);
				item.setTaskTime(taskTime);
				docInList.add(item);
			}
		}

		return docInList;
	}

	public static List<ItemDocInWait> getListOwnerWait(DataService dataService, FlowUtil flowUtil,
			List<DocumentRecipient> docList, String userName) {
		List<ItemDocInWait> docInList = new ArrayList<ItemDocInWait>();
		if (userName == null) {
			logger.error("ERROR : User name of Owner Wait List can not null");
			return docInList;
		}

		for (DocumentRecipient docRec : docList) {
			User assignee = null;
			User owner = null;
			User candidate = null;
			Date taskTime = null;
			AssignContent assContent = docRec.getAssignContent();

			if (!flowUtil.isEnded(docRec.getProcessInstanceId())) {
				Task task = flowUtil.getCurrentTask(docRec.getProcessInstanceId());
				taskTime = task.getCreateTime();

				if (assContent == null && task.getAssignee() != null && task.getAssignee().equals(userName)) {
					assignee = dataService.findUserByName(task.getAssignee());
					owner = assignee;
				}

				if (assContent != null && assContent.getOwnerName().equals(userName)) {
					assignee = dataService.findUserByName(assContent.getOwnerName());
					owner = assignee;
					candidate = dataService.findUserByName(assContent.getCandidateName());
				}
			}

			if (owner != null) {
				ItemDocInWait item = new ItemDocInWait();
				item.setDocRec(docRec);
				item.setAssignee(assignee);
				item.setOwner(owner);
				item.setCandidate(candidate);
				item.setTaskTime(taskTime);
				docInList.add(item);
			}
		}

		return docInList;
	}

	public static List<ItemDocInWait> getListInputerWait(DataService dataService, FlowUtil flowUtil,
			List<DocumentRecipient> docList, String userName) {
		List<ItemDocInWait> docInList = new ArrayList<ItemDocInWait>();
		if (userName == null) {
			logger.error("ERROR : User name of Inputer Wait List can not null");
			return docInList;
		}

		for (DocumentRecipient docRec : docList) {
			User assignee = null;
			User owner = null;
			User candidate = null;
			Date taskTime = null;
			AssignContent assContent = docRec.getAssignContent();

			if (!flowUtil.isEnded(docRec.getProcessInstanceId())) {
				Task task = flowUtil.getCurrentTask(docRec.getProcessInstanceId());
				taskTime = task.getCreateTime();
				if (docRec.getReceiveTime() != null && docRec.getNumber() != null && assContent == null) {
					if (task.getAssignee() == null) {
						HistoricTaskInstance preTask = flowUtil.getPreviousCompletedTask(docRec.getProcessInstanceId());
						if (preTask != null && preTask.getAssignee().equals(userName)) {
							assignee = dataService.findUserByName(preTask.getAssignee());
							taskTime = preTask.getCreateTime();
						}
					}
				}
			}

			if (docRec.getReceiveTime() == null || docRec.getNumber() == null || assignee != null) {

				ItemDocInWait item = new ItemDocInWait();
				item.setDocRec(docRec);
				item.setAssignee(assignee);
				item.setOwner(owner);
				item.setCandidate(candidate);
				item.setTaskTime(taskTime);
				docInList.add(item);
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
				String fileName = doc.getDocId() + "-" + number + "-" + docType.getShortName() + "-" + organType + "-"
						+ docName;

				String[] parts = file.getOriginalFilename().split("\\.");
				String tail = parts[parts.length - 1];
				if (tail.trim().length() <= 0)
					return;

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

	public static void prepareJSChartHistoryDocument(FlowUtil flowUtil, DataService dataService, Organ organ,
			Map<String, Object> map, Date startDay, Date endDay) {
		// Văn bản đi
		List<String> finishDocOutProcIdList = flowUtil.getListProcessInstanceIdByDate(DataConfig.RSC_NAME_FLOW_OUT,
				DataConfig.PROC_DEF_KEY_FLOW_OUT, startDay, endDay, true, organ.getOrganId().toString());
		List<String> unfinishDocOutProcIdList = flowUtil.getListProcessInstanceIdByDate(DataConfig.RSC_NAME_FLOW_OUT,
				DataConfig.PROC_DEF_KEY_FLOW_OUT, startDay, endDay, false, organ.getOrganId().toString());
		Integer countFinishDocOut = dataService.countDocByProcessIdList(organ.getOrganId(), finishDocOutProcIdList);
		Integer countUnfinishDocOut = dataService.countDocByProcessIdList(organ.getOrganId(), unfinishDocOutProcIdList);
		
		// Văn bản đến
		List<String> finishDocInProcIdList = flowUtil.getListProcessInstanceIdByDate(DataConfig.RSC_NAME_FLOW_IN,
				DataConfig.PROC_DEF_KEY_FLOW_IN, startDay, endDay, true, organ.getOrganId().toString());
		List<String> unfinishDocInProcIdList = flowUtil.getListProcessInstanceIdByDate(DataConfig.RSC_NAME_FLOW_IN,
				DataConfig.PROC_DEF_KEY_FLOW_IN, startDay, endDay, false, organ.getOrganId().toString());
		Integer countFinishDocIn = dataService.countDocRecByProcessIdList(organ.getOrganId(), finishDocInProcIdList);
		Integer countUnfinishDocIn = dataService.countDocRecByProcessIdList(organ.getOrganId(),
				unfinishDocInProcIdList);
		
		
		JSonChart jsChart = new JSonChart();
		jsChart.setType("bar");
		jsChart.setTitle("Thống kê văn bản từ " + UtilMethod.dateToString(startDay, "dd-MM-yyyy") + " đến "
				+ UtilMethod.dateToString(endDay, "dd-MM-yyyy"));
		jsChart.setxAxisCategories(new ArrayList<String>() {
			{
				add("Văn bản đi");
				add("Văn bản đến");
			}
		});
		
		int countOut = countFinishDocOut + countUnfinishDocOut;
		int countIn = countFinishDocIn + countUnfinishDocIn;
	
		
		jsChart.setyAxisTitle("Số lượng");
		jsChart.setValueSuffix(" (văn bản)");

		List<JSSeries> series = new ArrayList<JSSeries>();
		
		JSSeries serieTotal = new JSSeries();
		serieTotal.setName("Tổng số");
		List<Integer> dataTotal = new ArrayList<Integer>();
		dataTotal.add(countOut);
		dataTotal.add(countIn);
		serieTotal.setData(dataTotal);

		JSSeries serieFinish = new JSSeries();
		serieFinish.setName("Đã hoàn thành");
		List<Integer> dataFinish = new ArrayList<Integer>();
		dataFinish.add(countFinishDocOut);
		dataFinish.add(countFinishDocIn);
		serieFinish.setData(dataFinish);

		JSSeries serieUnfinish = new JSSeries();
		serieUnfinish.setName("Chưa hoàn thành");
		List<Integer> dataUnfinish = new ArrayList<Integer>();
		dataUnfinish.add(countUnfinishDocOut);
		dataUnfinish.add(countUnfinishDocIn);
		serieUnfinish.setData(dataUnfinish);

		series.add(serieTotal);
		series.add(serieFinish);
		series.add(serieUnfinish);
		jsChart.setSeries(series);

		map.put("jsChart", jsChart);
	}

	public static Map<Integer, JSonRow> userListToMap(List<User> userList) {
		Map<Integer, JSonRow> map = new HashMap<Integer, JSonRow>();
		
		for(User u : userList) {
			JSonRow row = new JSonRow();
			row.setId(u.getUserId());
			row.setFullName(getFullName(u));
			row.setLevel(u.getLevel().getLevelName());
			map.put(u.getUserId(), row);
		}
		
		return map;
	}

	public static void prepareJSTableHistory(FlowUtil flowUtil, DataService dataService, Organ organ,
			Map<String, Object> map, Date startDay, Date endDay) {

		List<String> processIdList = flowUtil.getListProcessInstanceIdByDate(DataConfig.RSC_NAME_FLOW_IN,
				DataConfig.PROC_DEF_KEY_FLOW_IN, startDay, endDay, null, organ.getOrganId().toString());
		List<DocumentRecipient> docRecList = dataService.findDocRecByProcessIdList(organ.getOrganId(), processIdList);
		List<User> userList = dataService.findAllUserByOrgan(organ.getOrganId());
		Map<Integer, JSonRow> mapUser = UtilMethod.userListToMap(userList);

		for (DocumentRecipient docRec : docRecList) {
			AssignContent assContent = docRec.getAssignContent();
			List<HistoricTaskInstance> preTasks = flowUtil.getHistoryTask(docRec.getProcessInstanceId());
			for (HistoricTaskInstance preTask : preTasks) {
				String[] taskName = preTask.getName().split(" ");
				if (taskName[taskName.length - 1].equals("report")) {
					User cand = dataService.findUserByName(preTask.getAssignee());
					Integer id = cand.getUserId();
					mapUser.get(id).increaseCountTask();

					// Kiểm tra hoàn thành
					if (preTask.getEndTime() != null) {
						mapUser.get(cand.getUserId()).increaseCountCompleted();

						// Kiểm tra trễ hạn
						Date endTime = preTask.getEndTime();
						Date dueTime = preTask.getDueDate();
						Date today = UtilMethod.toDate(new Date(), "dd-MM-yyyy");
						if (dueTime != null && (endTime.after(dueTime) || today.after(dueTime))) {
							mapUser.get(id).increaseCountLate();
						}
					} else {
						// Kiểm tra trễ hạn
						Date dueTime = preTask.getDueDate();
						Date today = UtilMethod.toDate(new Date(), "dd-MM-yyyy");
						if (dueTime != null && today.after(dueTime)) {
							mapUser.get(id).increaseCountLate();
						}
					}
				}
			}
		}

		map.put("jsTable", mapUser.values().toArray());
	}
}
