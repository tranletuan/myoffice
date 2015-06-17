package com.myoffice.myapp.models.dao.role;

import java.util.List;

import com.myoffice.myapp.models.dto.Role;

public interface RoleDao {
	
	Role findRoleByName(String roleName);
	
	List<Role> findAllRoles();
	
	void saveRole(Role role);
	
	void deleteRole(Role role);

	List<Role> findRolesByArrId(Integer[] rolesId);
	
	Role findRoleById(Integer roleId);
	
	Role findRoleByShortName(String shortName);

}
