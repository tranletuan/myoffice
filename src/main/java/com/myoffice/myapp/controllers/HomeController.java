package com.myoffice.myapp.controllers;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dao.user.UserDao;
import com.myoffice.myapp.models.dao.user.UserDaoImp;
import com.myoffice.myapp.models.dto.Level;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.service.DataService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired
	private DataService dataService;
	
	private static final Logger log = Logger.getLogger("Home Controller");

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		model.addAttribute("msg", formattedDate);

		return "home";
	}

	@RequestMapping(value = "/{username}/{password}/{role}", method = RequestMethod.GET)
	public ModelAndView newAccount(@PathVariable("username") String username,
			@PathVariable("password") String password,
			@PathVariable("role") String role) {

		ModelAndView model = new ModelAndView("home");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEnabled(true);
		Level level = new Level();
		level.setLevelName("CVP");
		user.setLevel(level);
		
		dataService.saveUser(user);
		user = null;
		user = (User) dataService.findUserByName(username);
		System.out.println(user.getUsername());
		
		dataService.deleteUser(user);
		
		
		
		model.addObject("msg", "Create account : " + username
				+ " has completed!");
		return model;
	}
}
