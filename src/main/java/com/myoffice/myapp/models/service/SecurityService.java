package com.myoffice.myapp.models.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.myoffice.myapp.models.dao.user.UserDao;
import com.myoffice.myapp.models.dto.Role;

@Service
@Transactional(readOnly = true)
public class SecurityService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	private com.myoffice.myapp.models.dto.User user;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		user = userDao.findUserByName(username);
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

		// check if user is login
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			if(userDetail.getUsername() != user.getUserName()){
				user = userDao.findUserByName(userDetail.getUsername());
			}
			
			return user;
		}

		return null;
	}
}
