package com.myoffice.myapp.controllers;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.ibatis.session.Configuration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	
	@Autowired 
	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		model.addAttribute("msg", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/{username}/{password}/{role}")
	@Transactional
	public ModelAndView newAccount(@PathVariable("username") String username,
			@PathVariable("password") String password,
			@PathVariable("role") String role) {

		ModelAndView model = new ModelAndView("home");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEnabled(true);
		
		System.out.println(sessionFactory.getCurrentSession());
		
		model.addObject("msg", "Create account : " + username + " has completed!");
		return model;

	}
}
