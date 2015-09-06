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
	
	//43200000 = 12g
	@Scheduled(fixedDelay = 43200000)
	public void schedulingDocInTask() {
		Date toDay = UtilMethod.toDate(new Date(), DataConfig.DATE_FORMAT_STRING);
		List<TimeReminder> timeReminderList = dataService.findActiveTimeReminder(toDay);

		for (TimeReminder t : timeReminderList) {
			try {
				officeMail.sendMail(t.getCurTaskMail(), t.getPreTaskMail(), null, t.getRemindSubject(), t.getRemindContent());
				
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
