package com.myoffice.myapp.models.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Scheduled;

import com.myoffice.myapp.models.dto.TimeReminder;
import com.myoffice.myapp.support.OfficeMail;
import com.myoffice.myapp.utils.UtilMethod;

public class ScheduleService {

	private static final Logger logger = LoggerFactory
			.getLogger(ScheduleService.class);
	
	@Autowired
	private DataService dataService;
	
	@Autowired
	private OfficeMail officeMail;
	
	@Scheduled(fixedDelay = 15000)
	public void schedulingDocInTask() {
		Date toDay = UtilMethod.toDate(new Date(), DataConfig.DATE_FORMAT_STRING);
		List<TimeReminder> timeReminderList = dataService.findActiveTimeReminder(toDay);

		for (TimeReminder t : timeReminderList) {
			try {
				List<String> toList = new ArrayList<String>();
				toList.add(t.getCurTaskMail());
				
				List<String> ccList = new ArrayList<String>();
				ccList.add(t.getPreTaskMail());
				
				String[] toMail = new String[toList.size()];
				String[] ccMail = new String[ccList.size()];
				toList.toArray(toMail);
				ccList.toArray(ccMail);
				
				officeMail.sendMail(toMail, ccMail, null, t.getRemindSubject(), t.getRemindContent());
				
			} catch (MailSendException e) {
				e.printStackTrace();
				
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
