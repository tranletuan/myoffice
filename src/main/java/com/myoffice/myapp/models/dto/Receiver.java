package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "receiver")
public class Receiver {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "receiver_id", nullable = false, unique = true)
	private Integer receiverId;
	
	@Column(name = "full_name", nullable = false, length = 60)
	private String fullName;
	
	@Column(name = "address", length = 60)
	private String address;
	
	@Column(name = "email", nullable = false, length = 60)
	private String email;
	
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	public Receiver() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getReceiverId() {
		return receiverId;
	}
	
}
