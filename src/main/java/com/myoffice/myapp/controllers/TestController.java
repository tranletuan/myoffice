package com.myoffice.myapp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.service.DataConfig;
import com.myoffice.myapp.utils.FlowUtil;
import com.myoffice.myapp.utils.UtilMethod;

@Controller
public class TestController extends AbstractController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(TestController.class);
	
	@RequestMapping(value = "/test")
	public ModelAndView pagination() {
		ModelAndView model = new ModelAndView("test");
		List<Integer> elemList = new ArrayList<Integer>();
		List<Integer> rowList = new ArrayList<Integer>();	
		List<String> testList = new ArrayList<String>();
		for(int i = 0; i <= 35; i++){
			testList.add("Content : " + i);
		}

		UtilMethod.preparePagination(rowList, elemList, testList, model, null);
		
		model.addObject("testList", testList);
		return model;  
	}
	
	
}
