package com.myoffice.myapp.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

import com.myoffice.myapp.models.dto.AssignContent;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.support.JSonEvent;
import com.myoffice.myapp.utils.UtilMethod;

@Controller
@RequestMapping(value = "/calendar")
public class CalendarController extends AbstractController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(CalendarController.class);
	
	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView calendar() {
		ModelAndView model = new ModelAndView("my-calendar");
			
		return model;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public List<JSonEvent> updateCalendar(
			HttpServletRequest request,
			@RequestParam("start") String startDate,
			@RequestParam("end") String endDate){
		User curUser = securityService.getCurrentUser();
		Organ organ = curUser.getOrgan();
		
		Date start;
		Date end;
		String dateFormat = "yyyy-MM-dd";
		try{
			start = UtilMethod.toDate(startDate, dateFormat);
			end = UtilMethod.toDate(endDate, dateFormat);
		}catch(Exception e){
			return null;
		}

		List<DocumentRecipient> docRecList = dataService.findDocRecByAssignDate(organ.getOrganId(), false, start, end);
		List<JSonEvent> jsonEvents = new ArrayList<JSonEvent>();
		String contextPath = request.getContextPath();
	
		for(DocumentRecipient docRec : docRecList) {
			AssignContent assContent = docRec.getAssignContent();
			User owner = assContent.getOwner();
			JSonEvent event = new JSonEvent();
			String title = docRec.getNumber() + "-" + docRec.getDocument().getDocName();
			
			/*Task curTask = flowUtil.getCurrentTask(docRec.getProcessInstanceId());
			if(curTask != null && curTask.getAssignee() != null) {
				User user = dataService.findUserByName(curTask.getAssignee());
				if(user.getUserDetail() != null && user.getUserDetail().getFullName() != null) {
					title = user.getUserDetail().getFullName() + " - " + title;
				} else {
					title = user.getUserName() + " - " + title;
				}
			}*/
			
			if(assContent.getCandidateName() != null) {
				User candidate = dataService.findUserByName(assContent.getCandidateName());
				title = UtilMethod.getFullName(candidate) + " - " + title;
			} else {
				title = UtilMethod.getFullName(owner) + " - " + title;
			}
			
			event.setTitle(title);
			event.setStart(assContent.getTimeStart().toString());
			event.setEnd(assContent.getTimeEnd().toString());
			event.setColor(owner.getColor());
			event.setUrl(contextPath+"/flow/doc_in_info/" + docRec.getDocument().getDocId());
			jsonEvents.add(event);
		}
		
		
		return jsonEvents;
	}
	

}
