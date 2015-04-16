package com.myoffice.myapp.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myoffice.myapp.models.dao.user.UserDao;
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

	public void deleteUserByName(String username) {
		userDao.deleteUserByName(username);
	}
	
	//=================
	
	
}
