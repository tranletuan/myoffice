package com.myoffice.myapp.models.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void persit(Object obj) {
		try {
			getSession().persist(obj);
		} catch (Exception e) {
			System.out.println("ERROR : can't persit objetc :  " + obj.getClass()
					+ " cause of : " + e.getMessage());
		}
	}

	public void delete(Object obj) {
		try {
			getSession().delete(obj);
		} catch (Exception e) {
			System.out.println("ERROR : can't delete object : " + obj.getClass()
					+ " cause of : " + e.getMessage());
		}

	}

}
