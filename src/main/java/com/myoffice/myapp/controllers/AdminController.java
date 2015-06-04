package com.myoffice.myapp.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.Unit;
import com.myoffice.myapp.models.dto.User;

@Controller
@RequestMapping(value = "/admin")
public class AdminController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(AdminController.class);

	@RequestMapping(value = "/user_list", method = RequestMethod.GET)
	public ModelAndView userList() {
		ModelAndView model = new ModelAndView("admin/user-list");

		List<User> userList = dataService.findAllUsers();
		List<Role> roleList = dataService.findAllRoles();
		List<Unit> unitList = dataService.findAllUnit();

		model.addObject("userList", userList);
		model.addObject("roleList", roleList);
		model.addObject("unitList", unitList);

		return model;
	}

	@RequestMapping(value = "/save_user", method = RequestMethod.POST)
	public ModelAndView saveUser(
			@RequestParam("userId") Integer userId,
			@RequestParam("userName") String userName,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam("unitId") Integer unitId,
			@RequestParam(value = "roles", required = false) Integer[] rolesId,
			@RequestParam(value = "enabled", required = false) boolean enabled) {
		ModelAndView model = new ModelAndView("redirect:user_list");
		User user = new User();

		if (userId > 0) {
			user = dataService.findUserById(userId);
		}

		//userName
		user.setUsername(userName);
		
		//password
		if (password != "") {
			user.setPassword(password);
		}

		//unit
		user.setUnit(dataService.findUnitById(unitId));

		//roles
		if (rolesId != null) {
			Set<Role> roles = new HashSet<Role>(
					dataService.findRolesByArrId(rolesId));
			user.setRoles(roles);
		}

		//enabled
		user.setEnabled(enabled);
		dataService.saveUser(user);

		return model;
	}
}
