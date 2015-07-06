package com.myoffice.myapp.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.User;

@Controller
@RequestMapping(value = "/calendar")
public class CalendarController extends AbstractController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(CalendarController.class);
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String updateCalendar(
			@RequestParam("start") String startDate,
			@RequestParam("end") String endDate){
		
		
		return "";
	}
	

}
