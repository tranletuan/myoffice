package com.myoffice.myapp.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataService;

@Controller
@SessionAttributes("userObj")
public class MainController {

	private static final Logger logger = LoggerFactory
			.getLogger(MainController.class);

	@Autowired
	private DataService dataService;

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public ModelAndView createNewAccount() {
		ModelAndView model = new ModelAndView("home");
		try {
			User user1 = new User();
			user1.setUsername("tuantl");
			user1.setPassword("$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y");
			user1.setEnabled(true);
			dataService.saveUser(user1);

			User user2 = new User();
			user2.setUsername("tuanta");
			user2.setPassword("$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y");
			user2.setEnabled(true);
			dataService.saveUser(user2);

			Role role1 = new Role();
			role1.setRoleName("ROLE_ADMIN");
			dataService.saveRole(role1);

			Role role2 = new Role();
			role2.setRoleName("ROLE_USER");
			dataService.saveRole(role2);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		model.addObject("msg", "Init has completed!");
		return model;
	}

	@RequestMapping(value = { "/", "/welcome**" }, method = RequestMethod.GET)
	public String defaultPage() {

//		ModelAndView model = new ModelAndView("login");
//		Authentication auth = SecurityContextHolder.getContext()
//				.getAuthentication();
//		if (!(auth instanceof AnonymousAuthenticationToken)) {
//			UserDetails userDetail = (UserDetails) auth.getPrincipal();
//			User user = dataService.findUserByName(userDetail.getUsername());
//			model.addObject("userObj", user);
//			logger.info(user.getUsername());
//		}
		
		return "login";

	}

	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security + Hibernate Example");
		model.addObject("message", "This page is for ROLE_ADMIN only!");
		model.setViewName("admin");

		return model;

	}

//	@RequestMapping(value = "/login", method = RequestMethod.GET)
//	public ModelAndView login(
//			@RequestParam(value = "error", required = false) String error,
//			@RequestParam(value = "logout", required = false) String logout,
//			HttpServletRequest request) {
//
//		ModelAndView model = new ModelAndView();
//		if (error != null) {
//			model.addObject("error",
//					getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
//		}
//
//		if (logout != null) {
//			model.addObject("msg", "You've been logged out successfully.");
//		}
//
//		model.setViewName("login");
//
//		return model;
//
//	}

	// customize the error message
	private String getErrorMessage(HttpServletRequest request, String key) {

		Exception exception = (Exception) request.getSession()
				.getAttribute(key);

		String error = "";
		if (exception instanceof BadCredentialsException) {
			error = "Invalid username and password!";
		} else if (exception instanceof LockedException) {
			error = exception.getMessage();
		} else {
			error = "Invalid username and password!";
		}

		logger.info(error);

		return error;
	}

	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied() {

		ModelAndView model = new ModelAndView();

		// check if user is login
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();

			model.addObject("username", userDetail.getUsername());

		}

		model.setViewName("403");

		return model;

	}

}