package com.myoffice.myapp.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.utils.UtilMethod;

@Controller
@RequestMapping(value = "/history")
public class HistoryController extends AbstractController {
	
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
		//ModelAndView model = new ModelAndView("fragment/history-show");
		Map<String, Object> map = new HashMap<String, Object>();
		Date startDay = null;
		Date endDay = null;
		try {
			startDay = UtilMethod.toDate(timeStart, "dd-MM-yyyy");
			endDay = UtilMethod.toDate(timeEnd, "dd-MM-yyyy");
		} catch (Exception e) {
			return null;
		}
		
		
		
		return map;
	}
}
