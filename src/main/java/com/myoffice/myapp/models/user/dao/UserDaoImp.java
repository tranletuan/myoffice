package com.myoffice.myapp.models.user.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.myoffice.myapp.models.user.dto.User;

public class UserDaoImp implements UserDao {

	@Autowired
	private  SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings("unchecked")
	public User findUserByName(String username) {
		
		List<User> users = sessionFactory.getCurrentSession()
				.createQuery("from User where username=?")
				.setParameter(0, "username")
				.list();
		
		if(users.size() > 0){
			return users.get(0);
		}
		
		return null;
	}

	@Override
	public void addUser(String username, String password, String role) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEnabled(true);
	

	}

}
