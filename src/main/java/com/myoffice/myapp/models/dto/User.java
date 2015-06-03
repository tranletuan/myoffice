package com.myoffice.myapp.models.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "user_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(name = "user_name", unique = true, nullable = false, length = 45)
	private String userName;

	@Column(name = "password", nullable = false, length = 60)
	private String password;

	@Column(name = "enabled", nullable = false)
	private boolean enabled = true;

	@Column(name = "description", length = 255)
	private String description;

	// mapping
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>(0);

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "detail_id", nullable = true, insertable = true, updatable = true)
	private UserDetail userDetail;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "unit_id", nullable = false)
	private Unit unit;

	public User() {

	}

	public Integer getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		this.password = hashedPassword;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	public String getRoleNames(){
		String rs = "";
		int size = roles.size();
		int i = 0;
		for(Role r : roles){
			rs += r.getFullName();
			if(i >= 0 && i < size - 1){
				rs += ", ";
			}
			i++;
		}
		
		return rs;
	}
}
