package com.myoffice.myapp.models.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;
import org.hibernate.engine.spi.CascadeStyle;
import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "user_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	@Column(name = "user_name", unique = true, nullable = false, length = 45)
	private String username;

	@Column(name = "password", nullable = false, length = 60)
	private String password;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@Column(name = "description", length = 255)
	private String description;

	// mapping
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>(0);

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "level_id", nullable = true, insertable = true, updatable = true)
	private Level level;

	public User() {

	}

	public User(Integer userId, String username, String password,
			boolean enabled) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public User(Integer userId, String username, String password,
			boolean enabled, Set<Role> roles) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.roles = roles;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

}
