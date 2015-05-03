package com.myoffice.myapp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.utils.FlowUtil;

@Controller
@RequestMapping(value = "/session")
@SessionAttributes("userObj")
public class TestController {

	@Autowired
	private FlowUtil flowUtil;

	@RequestMapping(value = "/{execution}")
	public ModelAndView testFlow(@PathVariable("execution") String execution) {

		ModelAndView model = new ModelAndView("home");

		if (execution.equals("deploy")) {
			flowUtil.deployProcess("process.bpmn20.xml",
					"D:/Research/XML Process/process.bpmn20.xml");
		} else if (execution.equals("start")) {
			flowUtil.startProcess("release_document_process:5:22504");
		} else if (execution.equals("task")) {
			flowUtil.getCurrentTask("25001");
		}

		return model;
	}

	@RequestMapping(value = "/user")
	public ModelAndView userTest(@ModelAttribute("userObj") User user) {
		ModelAndView model = new ModelAndView("home");
		model.addObject("msg", user.getUsername());
		System.out.println(user.getRoles());

		return model;
	}

	@RequestMapping(value = "/test")
	public ModelAndView userSessionTest(@ModelAttribute("userObj") User user) {
		ModelAndView model = new ModelAndView("home");
		System.out.println(user.getUsername());
		model.addObject("msg", user.getUsername());
		return model;
	}
}
