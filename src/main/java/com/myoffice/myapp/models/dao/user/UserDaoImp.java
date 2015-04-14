package com.myoffice.myapp.models.dao.user;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.AbstractDao;
import com.myoffice.myapp.models.dto.User;

@Repository("userDao")
public class UserDaoImp extends AbstractDao implements UserDao {

	@Override
	@SuppressWarnings("unchecked")
	public User findUserByName(String username) {
		Query query = (Query)getSession().createQuery("from User where username=?");
		query.setParameter(0, username);
		List<User> result = query.getResultList();
		
		if(result.size() > 0) {
			return result.get(0);
		}
		
		return null;
	}

	@Override
	public void saveUser(User user) {
		persit(user);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> findAllUsers() {
		Criteria criteria = getSession().createCriteria(User.class);
		return (List<User>)criteria.list();
	}

	@Override
	public void deleteUserByName(String username) {
		Query query = (Query) getSession().createSQLQuery("delete from User where username=?");
		query.setParameter(0, username);
		query.executeUpdate();
	}

}
