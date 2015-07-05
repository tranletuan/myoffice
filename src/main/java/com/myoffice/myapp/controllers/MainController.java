package com.myoffice.myapp.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.models.service.SecurityService;

@Controller
public class MainController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(MainController.class);
	
	@RequestMapping(value = { "/", "/home**" }, method = RequestMethod.GET)
	public ModelAndView defaultPage() {

		ModelAndView model = new ModelAndView("home");
		
		return model;
	}

	@RequestMapping(value = "/signin")
	public String login() {
		
		User user = securityService.getCurrentUser();
		if(user != null) {
			return "home";
		}
		return "signin";
	}

	@RequestMapping(value = "/signin-{msg}", method = RequestMethod.GET)
	public ModelAndView loginMessage(@PathVariable("msg") String message) {
		ModelAndView model = new ModelAndView("signin");
		
		if (message.equals("error")) {
			model.addObject("error", true);
		}else if(message.equals("signout")){
			model.addObject("signout", true);
		}

		return model;
	}

	@RequestMapping("/error")
	public String error(HttpServletRequest request, Model model) {
		model.addAttribute("errorCode",
				request.getAttribute("javax.servlet.error.status_code"));
		Throwable throwable = (Throwable) request
				.getAttribute("javax.servlet.error.exception");
		String errorMessage = null;
		if (throwable != null) {
			errorMessage = throwable.getMessage();
		}
		model.addAttribute("errorMessage", errorMessage);
		return "error";
	}
	
	
}