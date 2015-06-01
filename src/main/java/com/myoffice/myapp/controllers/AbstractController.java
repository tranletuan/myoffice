package com.myoffice.myapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.models.service.SecurityService;
import com.myoffice.myapp.utils.FlowUtil;

public class AbstractController {
	
	@Autowired
	protected DataService dataService;
	
	@Autowired
	protected FlowUtil flowUtil;
	
	@Autowired
	protected SecurityService securityService;
	
}
