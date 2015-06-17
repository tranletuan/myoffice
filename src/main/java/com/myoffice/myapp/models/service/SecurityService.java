package com.myoffice.myapp.models.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myoffice.myapp.controllers.FlowController;
import com.myoffice.myapp.models.dao.user.UserDao;
import com.myoffice.myapp.models.dto.Role;

@Service
@Transactional(readOnly = true)
public class SecurityService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(SecurityService.class);
	
	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		com.myoffice.myapp.models.dto.User user = userDao.findUserByName(username);
		List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
		return buildUserForAuthentication(user, authorities);
	}

	private User buildUserForAuthentication(
			com.myoffice.myapp.models.dto.User user,
			List<GrantedAuthority> authorities) {
		return new User(user.getUserName(), user.getPassword(),
				user.isEnabled(), true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(Set<Role> set) {
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

		for (Role role : set) {
			setAuths.add(new SimpleGrantedAuthority(role.getRoleName()));
		}

		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(
				setAuths);

		return result;
	}

	public com.myoffice.myapp.models.dto.User getCurrentUser() {
		
		com.myoffice.myapp.models.dto.User user = null;
		// check if user is login
		try {
			Authentication auth = SecurityContextHolder.getContext()
					.getAuthentication();
			if (auth != null) {
				UserDetails userDetail = (UserDetails) auth.getPrincipal();
				user = userDao.findUserByName(userDetail.getUsername());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return user;
	}
}
