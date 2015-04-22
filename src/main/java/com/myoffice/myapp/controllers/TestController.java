package com.myoffice.myapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.utils.FlowUtil;

@Controller
public class TestController {

	@Autowired
	private FlowUtil flowUtil;
	
	@RequestMapping(value="/{execution}")
	public ModelAndView testFlow(
			@PathVariable("execution") String execution){
		
		ModelAndView model = new ModelAndView("home");
		
		if(execution.equals("deploy")){
			flowUtil.deployProcess("release_document_process.bpmn20.xml", "D:/Research/XML Process/release_document_process.bpmn20.xml");
		}
		else if(execution.equals("start")){
			flowUtil.startProcess("release_document_process:5:22504");
		}
		else if(execution.equals("task")){
			flowUtil.getCurrentTask("25001");
		}
		
		return model;
	}
}
