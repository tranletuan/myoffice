package com.myoffice.myapp.controllers;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.models.service.SecurityService;
import com.myoffice.myapp.support.OfficeMail;
import com.myoffice.myapp.utils.FlowUtil;

public class AbstractController {
	
	@Autowired
	protected DataService dataService;
	
	@Autowired
	protected FlowUtil flowUtil;
	
	@Autowired
	protected SecurityService securityService;
	
	@Autowired
	protected SessionFactory sessionFactory;

	@Autowired
	protected OfficeMail officeMail;
}
