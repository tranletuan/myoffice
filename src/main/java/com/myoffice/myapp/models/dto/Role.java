package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

	@Id
	@Column(name = "role_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roleId;

	@Column(name = "role_name", unique = true, nullable = false)
	private String roleName;
	
	@Column(name= "full_name", unique = true, nullable = false, length = 100)
	private String fullName;
	
	@Column(name= "short_name", unique = true, nullable = false)
	private String shortName;

	public Role() {
		
	}

	public Role(String roleName) {
		this.roleName = roleName;
	}

	public Role(Integer roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
