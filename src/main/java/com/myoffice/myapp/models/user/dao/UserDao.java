package com.myoffice.myapp.models.user.dao;

import com.myoffice.myapp.models.user.dto.User;

public interface UserDao {

	User findUserByName(String username);
	void addUser(String username, String password, String role);
}
