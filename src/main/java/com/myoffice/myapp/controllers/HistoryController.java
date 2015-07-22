package com.myoffice.myapp.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataConfig;
import com.myoffice.myapp.support.JSSeries;
import com.myoffice.myapp.support.JSonChart;
import com.myoffice.myapp.utils.UtilMethod;

@Controller
@RequestMapping(value = "/history")
public class HistoryController extends AbstractController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(HistoryController.class);

	@RequestMapping("/show")
	public ModelAndView historyPage() {
		ModelAndView model = new ModelAndView("history-report");
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		
		model.addObject("organ", organ);
		return model;
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public Map<String, Object> searchHistory(
			@RequestParam("timeStart") String timeStart,
			@RequestParam("timeEnd") String timeEnd) {
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		Map<String, Object> map = new HashMap<String, Object>();
		Date startDay = null;
		Date endDay = null;
		
		try {
			startDay = UtilMethod.toDate(timeStart, "dd-MM-yyyy");
			endDay = UtilMethod.toDate(timeEnd, "dd-MM-yyyy");
		} catch (Exception e) {
			return null;
		}
		
		UtilMethod.prepageJSChartHistoryDocument(flowUtil, dataService, organ, map, startDay, endDay);
		List<String> processIdList = flowUtil.getListProcessInstanceIdByDate(DataConfig.RSC_NAME_FLOW_IN,
				DataConfig.PROC_DEF_KEY_FLOW_IN, startDay, endDay, null, organ.getOrganId().toString());
		List<DocumentRecipient> listDocRec = dataService.findDocRecByProcessIdList(organ.getOrganId(), processIdList);
		List<User> listUser = dataService.findAllUserByOrgan(organ.getOrganId());
		
		
		
		logger.info(String.valueOf(processIdList.size()));
		return map;
	}
}
