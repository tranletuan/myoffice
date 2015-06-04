package com.myoffice.myapp.models.dao.role;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.Role;

@Repository
public class RoleDaoImp extends AbstractDao implements RoleDao {

	private static final Logger logger = LoggerFactory.getLogger(RoleDaoImp.class);
	
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
	@SuppressWarnings("unchecked")
	public List<Role> findAllRoles() {
		Criteria criteria = getSession().createCriteria(Role.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return (List<Role>)criteria.list();
	}
	
	@Override
	public void saveRole(Role role) {
		persist(role);
	}

	@Override
	public void deleteRole(Role role) {
		delete(role);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> findRolesByArrId(Integer[] rolesId) {
		
		if(rolesId == null) return null;
		
		String queryString = "from Role where ";
		
		for(int i = 0; i < rolesId.length; i++){
			queryString += "role_id=" + rolesId[i];
			if(i >= 0 && i < rolesId.length - 1){
				queryString += " or ";
			}
		}
		
		Query query = (Query)getSession().createQuery(queryString);
		return query.list();
	}
	
	

}
