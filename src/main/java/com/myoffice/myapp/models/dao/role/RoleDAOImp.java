package com.myoffice.myapp.models.dao.role;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.Role;

@Repository
public class RoleDAOImp extends AbstractDao implements RoleDAO {

	@Override
	@SuppressWarnings("unchecked")
	public Role findRoleByName(String roleName) {
		
		Query query = (Query)getSession().createSQLQuery("from Role where role_name=?");
		query.setParameter(0, roleName);
		List<Role> roles = query.list();
		
		if(roles.size() > 0) {
			return roles.get(0);
		}
		
		return null;
	}

	@Override
	public void saveRole(Role role) {
		persit(role);
	}

	@Override
	public void deleteRoleByName(String roleName) {
		
		Query query = (Query)getSession().createQuery("delete from Role where role_name=?");
		query.setParameter(0, roleName);
		query.executeUpdate();

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Role> findAllRoles() {
		Criteria criteria = getSession().createCriteria(Role.class);
		return (List<Role>)criteria.list();
	}

}
