package com.myoffice.myapp.models.dao.role;

import java.util.List;

import com.myoffice.myapp.models.dto.Role;

public interface RoleDAO {
	
	Role findRoleByName(String roleName);
	
	void saveRole(Role role);
	
	void deleteRoleByName(String roleName);
	
	List<Role> findAllRoles();

}
