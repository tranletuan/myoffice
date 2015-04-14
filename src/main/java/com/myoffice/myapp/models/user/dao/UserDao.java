package com.myoffice.myapp.models.user.dao;

import java.util.Set;

import com.myoffice.myapp.models.user.dto.User;
import com.myoffice.myapp.models.user.dto.UserRole;

public interface UserDao {

	//user 
	User findUserByName(String username);
	void addUser(String username, String password);
	void addUserRole(User user, String role);

}
