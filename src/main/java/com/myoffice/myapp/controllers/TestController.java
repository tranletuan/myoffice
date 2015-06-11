package com.myoffice.myapp.controllers;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.service.DataConfig;

@Controller
@RequestMapping(value = "/test")
public class TestController extends AbstractController {

	@RequestMapping(value = "1")
	public ModelAndView test1(){
		ModelAndView model = new ModelAndView("home");
		
		flowUtil.deployProcess(DataConfig.RSC_NAME_FLOW_OUT, "D:/process.bpmn20.xml");
		flowUtil.startProcess(flowUtil.getProcessDefinitionId(DataConfig.RSC_NAME_FLOW_OUT, DataConfig.PROC_DEF_KEY_FLOW_OUT));
		
		model.addObject("message", "HELLO");
		
		return model;
	}
	
	@RequestMapping(value = "2")
	public ModelAndView test2(){
		ModelAndView model = new ModelAndView("home");
		
		Map<String, Integer> variables = new HashMap<String, Integer>();
		variables.put("choose", 1);
		//flowUtil.getTaskService().setVariables(flowUtil.getCurrentTask("95005").getId(), variables);
		flowUtil.getTaskService().complete(flowUtil.getCurrentTask("95005").getId());
		
		
		model.addObject("message", "OK");
		
		return model;
	}
	
	
}
