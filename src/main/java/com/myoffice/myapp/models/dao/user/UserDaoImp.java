package com.myoffice.myapp.models.dao.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.jta.platform.internal.ResinJtaPlatform;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.Role;
import com.myoffice.myapp.models.dto.User;

@Repository
public class UserDaoImp extends AbstractDao implements UserDao {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImp.class);

	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserByArrRoleShortName(Integer organId, String[] arrRoleShortName, User nUser, boolean checkValue) {
		
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.createAlias("roles", "r");
		criteria.createAlias("organ", "o");
		criteria.createAlias("level", "l");
		criteria.add(Restrictions.eq("o.organId", organId));
		criteria.add(Restrictions.and(Restrictions.in("r.shortName", arrRoleShortName)));
		if(checkValue) criteria.add(Restrictions.and(Restrictions.ge("l.value", nUser.getLevel().getValue())));
		criteria.add(Restrictions.and(Restrictions.eq("enabled", true)));
		criteria.add(Restrictions.and(Restrictions.ne("userId", nUser.getUserId())));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAllUsers(Integer organId, Integer roleId) {
		Criteria criteria = getSession().createCriteria(User.class);
		criteria.createAlias("roles", "r");
		criteria.createAlias("organ", "o");
		criteria.add(Restrictions.eq("r.roleId", roleId));
		criteria.add(Restrictions.and(Restrictions.eq("o.organId", organId)));
		return criteria.list();
	}

	@Override
	public void saveUser(User user) {
		persist(user);
	}

	@Override
	public void deleteUser(User user) {
		delete(user);
		
	}

	@Override
	public User findUserById(Integer userId) {
		return (User)getSession().get(User.class, userId);
	}
	
	
}
