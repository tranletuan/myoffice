package com.myoffice.myapp.models.dao.user;

import java.util.List;





import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.AbstractDao;
import com.myoffice.myapp.models.dto.User;

@Repository
public class UserDaoImp extends AbstractDao implements UserDao {

	@Override
	@SuppressWarnings("unchecked")
	public User findUserByName(String username) {
		Transaction trans = getSession().beginTransaction();
		
		Query query = (Query)getSession().createQuery("from User where username=?");
		query.setParameter(0, username);
		List<User> result =  query.list();
	
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
		Transaction trans = getSession().beginTransaction();
	
		Query query = (Query) getSession().createQuery("delete from User where username=?");
		query.setParameter(0, username);
		query.executeUpdate();
		
		trans.commit();
	}

}
