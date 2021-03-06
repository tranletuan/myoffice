package com.myoffice.myapp.controllers;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myoffice.myapp.models.dto.AssignContent;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.User;
import com.myoffice.myapp.models.dto.UserDetail;
import com.myoffice.myapp.models.service.DataService;
import com.myoffice.myapp.models.service.SecurityService;

@Controller
public class MainController extends AbstractController {

	private static final Logger logger = LoggerFactory
			.getLogger(MainController.class);
	
	@RequestMapping(value = { "/", "/home**", "/signin" })
	public ModelAndView defaultPage() throws ParseException {
		ModelAndView model = new ModelAndView("signin");
		
		User user = securityService.getCurrentUser();
		if(user != null) {
			model.setViewName("home");
		}
		return model;
	}

	@RequestMapping(value = "/signin-{msg}", method = RequestMethod.GET)
	public ModelAndView loginMessage(@PathVariable("msg") String message) {
		ModelAndView model = new ModelAndView("home");
		
		if (message.equals("error")) {
			model.addObject("error", true);
			model.setViewName("signin");
		}else if(message.equals("signout")){
			model.addObject("signout", true);
			model.setViewName("signin");
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
	
	@RequestMapping(value = "/user_detail/{userId}", method = RequestMethod.GET)
	public ModelAndView userDetail(
			@PathVariable("userId") Integer userId){
		ModelAndView model = new ModelAndView("user-detail");
		User user = null;
		User curUser = securityService.getCurrentUser();
		
		if(userId != null && userId > 0 && curUser.getUserId() != userId) {
			user = dataService.findUserById(userId);
			model.addObject("user", user);
		} 
		
		if(user == null) {
			model.addObject("user", curUser);
			model.addObject("canChange", true);
		}
		
		return model;
	}
	
	@RequestMapping(value = "/save_user_detail", method = RequestMethod.POST)
	public ModelAndView saveDetail(
			@RequestParam("fullName") String fullName,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "phoneNumber", required = false) String phoneNumber,
			@RequestParam(value = "email", required = false) String email){
		ModelAndView model = new ModelAndView("redirect:/home");
		User curUser = securityService.getCurrentUser();
		UserDetail userDetail = curUser.getUserDetail();
		
		if(userDetail == null) {
			userDetail = new UserDetail();
		}
		
		userDetail.setFullName(fullName);
		if(address != null) userDetail.setAddress(address);
		if(phoneNumber != null) userDetail.setPhoneNumber(phoneNumber);
		if(email != null) userDetail.setEmail(email);
		
		curUser.setUserDetail(userDetail);
		dataService.saveUser(curUser);
		model.setViewName("redirect:user_detail/" + curUser.getUserId());
		return model;
	}

	@RequestMapping(value = "/change_password", method = RequestMethod.POST)
	public ModelAndView changePassword(
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confPassword,
			RedirectAttributes reAttr){
		ModelAndView model = new ModelAndView("redirect:user_detail/0");
		User curUser = securityService.getCurrentUser();
		
		if(curUser.checkOldPassword(oldPassword) &&
				curUser.checkNewPassword(newPassword) &&
				curUser.checkConfPassword(newPassword, confPassword)) {
			curUser.setPassword(newPassword);
			dataService.saveUser(curUser);
			reAttr.addFlashAttribute("success", true);
			reAttr.addFlashAttribute("successMessage", "Đổi mật khẩu thành công");
		} else {
			reAttr.addFlashAttribute("error", true);
			reAttr.addFlashAttribute("errorMessage", "Lỗi, Đổi mật khẩu thất bại");
		}
		
		return model;
	}
	
	@RequestMapping(value = "/check_old_password", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public boolean checkOldPassword(
			@RequestParam("oldPassword") String oldPassword){
		User curUser = securityService.getCurrentUser();
		return curUser.checkOldPassword(oldPassword);
	}
	
	@RequestMapping(value = "/check_new_password", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public boolean checkNewPassword(
			@RequestParam("newPassword") String newPassword){
		User curUser = securityService.getCurrentUser();
		return curUser.checkNewPassword(newPassword);
	}
	
	@RequestMapping(value = "/check_confirm_password", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public boolean checkConfPassword(
			@RequestParam("newPassword") String newPassword,
			@RequestParam("confirmPassword") String confirmPassword){
		User curUser = securityService.getCurrentUser();
		return curUser.checkConfPassword(newPassword, confirmPassword);
	}

}