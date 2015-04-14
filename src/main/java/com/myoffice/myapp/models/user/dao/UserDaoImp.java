package com.myoffice.myapp.models.user.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.user.dto.User;
import com.myoffice.myapp.models.user.dto.UserRole;

@Repository
public class UserDaoImp implements UserDao {

	@Autowired
	private  SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings("unchecked")
	public User findUserByName(String username) {
		
		List<User> users = sessionFactory.getCurrentSession()
				.createQuery("from User where username=?")
				.setParameter(0, username)
				.list();
		
		if(users.size() > 0){
			return users.get(0);
		}
		
		return null;
	}

	@Override
	public void addUser(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEnabled(true);
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(user);
		session.getTransaction().commit();
	}

	@Override
	public void addUserRole(User user, String role) {
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(userRole);
		session.getTransaction().commit();
		session.close();
		
	}



	
	

}
