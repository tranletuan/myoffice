package com.myoffice.myapp.models.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void persit(Object obj) {
		try {
			Session session = getSession();
			Transaction trans = session.beginTransaction();
			session.persist(obj);
			trans.commit();
			
		} catch (Exception e) {
			System.out.println("ERROR : can't persit object :  "
					+ obj.getClass() + " cause of session : " + e.getMessage());
		}
	}

	public void delete(Object obj) {
		try {
			Session session = getSession();
			Transaction trans = session.beginTransaction();
			session.delete(obj);
			trans.commit();
		} catch (Exception e) {
			System.out.println("ERROR : can't delete object : "
					+ obj.getClass() + " cause of session : " + e.getMessage());
		}

	}

}
