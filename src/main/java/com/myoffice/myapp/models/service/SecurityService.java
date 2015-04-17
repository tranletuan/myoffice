package com.myoffice.myapp.models.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.myoffice.myapp.models.dao.user.UserDao;
import com.myoffice.myapp.models.dto.Role;

public class SecurityService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {

		com.myoffice.myapp.models.dto.User user = userDao
				.findUserByName(username);
		List<GrantedAuthority> authorities = buildUserAuthority(user.getRoles());
		return buildUserForAuthentication(user, authorities);
	}

	private User buildUserForAuthentication(
			com.myoffice.myapp.models.dto.User user,
			List<GrantedAuthority> authorities) {
		return new User(user.getPassword(), user.getPassword(),
				user.isEnabled(), true, true, true, authorities);
	}

	private List<GrantedAuthority> buildUserAuthority(Set<Role> roles) {
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
		
		for(Role role : roles){
			setAuths.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		
		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);
		
		return result;
	}
}
