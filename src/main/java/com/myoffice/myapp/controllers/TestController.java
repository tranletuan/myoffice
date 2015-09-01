package com.myoffice.myapp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView pagination() throws AddressException, MessagingException {
		ModelAndView model = new ModelAndView("test/testmail");

		String[] toMail = {"tranletuan1405@gmail.com"};
		String subject = "Test Mail";
		String text = "text email";
	
		officeMail.sendMail(toMail, null, null, subject, text);
		
		model.addObject("to", toMail);
		model.addObject("subject", subject);
		model.addObject("text", text);
		
		return model;  
	}
	
	
}
