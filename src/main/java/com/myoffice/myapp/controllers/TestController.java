package com.myoffice.myapp.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.TimeReminder;
import com.myoffice.myapp.models.service.DataConfig;
import com.myoffice.myapp.support.OfficeMail;
import com.myoffice.myapp.utils.FlowUtil;
import com.myoffice.myapp.utils.UtilMethod;

@Controller
public class TestController extends AbstractController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(TestController.class);

	@Autowired
	private OfficeMail officeMail;
	
	@RequestMapping(value = "/test")
	public ModelAndView pagination(HttpServletRequest request) throws AddressException, MessagingException {
		ModelAndView model = new ModelAndView("test/testmail");

		TimeReminder t = dataService.findTimeReminderById(7);
		officeMail.sendMail(t.getPreTaskMail(), t.getCurTaskMail(), null, t.getRemindSubject(), t.getRemindContent());
		
		/*String host = request.getRemoteHost();
		logger.info(host);
		
		int port = request.getLocalPort();
		logger.info(String.valueOf(port));
		
		int portSv = request.getServerPort();
		logger.info(String.valueOf(portSv));
		
		String path = (String) request.getAttribute("javax.servlet.forward.request_uri");
		logger.info(path);
		
		String scheme = request.getProtocol();
		logger.info(scheme);
		
		StringBuffer url = request.getRequestURL();
		
		String urlStr = url.toString();
		logger.info(url.toString());
		
		String uri = request.getRequestURI();
		logger.info(uri);
		

		String contextPath = request.getContextPath();
		logger.info(contextPath);
		
		String docURL = urlStr.replace(uri, contextPath + "/doc_in_info/" + 4);
		logger.info(docURL);*/
		return model;  
	}
	
	
}
