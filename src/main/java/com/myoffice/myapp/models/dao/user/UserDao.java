package com.myoffice.myapp.models.dao.user;

import java.util.List;

import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.User;

public interface UserDao {

	User findUserByName(String username);
	
	List<User> findAllUsers();
	
	List<User> findUserByArrRoleShortName(Integer organId, String[] arrRoleShortName);

	void saveUser(User user);
	
	void deleteUser(User user);
	
	User findUserById(Integer userId);
}
