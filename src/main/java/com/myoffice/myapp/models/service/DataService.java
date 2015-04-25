package com.myoffice.myapp.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myoffice.myapp.models.dao.role.RoleDAO;
import com.myoffice.myapp.models.dao.user.UserDao;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.User;

@Service
@Transactional
public class DataService {

	@Autowired
	private UserDao userDao;

	public User findUserByName(String username) {
		return userDao.findUserByName(username);
	}

	public void saveUser(User user) {
		userDao.saveUser(user);
	}

	public List<User> findAllUsers() {
		return userDao.findAllUsers();
	}

	public void deleteUser(User user) {
		userDao.deleteUser(user);
	}
	
	//=================
	@Autowired
	private RoleDAO roleDao;
	
	public Role findRoleByName(String roleName){
		return roleDao.findRoleByName(roleName);
	}
	
	public List<Role> findAllRoles(){
		return roleDao.findAllRoles();
	}
	
	public void saveRole(Role role){
		roleDao.saveRole(role);
	}
	
	public void deleteRole(Role role) {
		roleDao.deleteRole(role);
	}
	
	//===================

	
	//=====================
	
	
}
