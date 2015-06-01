package com.myoffice.myapp.models.dao.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.User;

@Repository
public class UserDaoImp extends AbstractDao implements UserDao {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImp.class);

	public UserDaoImp(){
		logger.info("USER DAO HAS CONSTRUCTED");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public User findUserByName(String username) {
		
		Query query = (Query)getSession().createQuery("from User where user_name=?");
		query.setParameter(0, username);
		List<User> result =  query.list();
	
		if(result.size() > 0) {
			return result.get(0);
		}
		
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<User> findAllUsers() {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return (List<User>)criteria.list();
	}

	@Override
	public void saveUser(User user) {
		persist(user);
	}

	@Override
	public void deleteUser(User user) {
		delete(user);
		
	}
	
	
}
