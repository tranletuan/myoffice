package com.myoffice.myapp.models.dao.common;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public boolean persist(Object obj) {
		try {
			getSession().saveOrUpdate(obj);
			return true;
		} catch (HibernateException e) {
			System.out.println("ERROR : can't persit object :  "
					+ obj.getClass() + " cause of session : " + e.getMessage());
			return false;
		}
	}

	public void delete(Object obj) {
		try {
			getSession().delete(obj);
		} catch (HibernateException e) {

			System.out.println("ERROR : can't delete object : "
					+ obj.getClass() + " cause of session : " + e.getMessage());
		}
	}
	

}
