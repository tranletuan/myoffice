package com.myoffice.myapp.models.dao.user;

import java.util.List;

import com.myoffice.myapp.models.dto.User;

public interface UserDao {

	User findUserByName(String username);
	
	void saveUser(User user);
	
	List<User> findAllUsers();
	
	void deleteUserByName(String username);
}
