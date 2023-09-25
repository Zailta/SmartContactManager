package com.smartManager.Entity;

import java.util.*;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


@Entity
public class SMUserEntity {
	
	@Id
	@UuidGenerator
	private String userID;
	@NotEmpty(message = "Email must not be Empty")
	private String email;
	@NotEmpty(message = "Name must not be Empty")
	private String name;
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "The password should be in following format: .Qwerty1 ")
	private String password;
	@NotEmpty(message = "About must not be Empty")
	private String about;
	private String role;
	private Boolean enabledStatus;
	private String profilePicture;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
	private List <SMContactEntity> contacts = new ArrayList<>();
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Boolean getEnabledStatus() {
		return enabledStatus;
	}
	public void setEnabledStatus(Boolean enabledStatus) {
		this.enabledStatus = enabledStatus;
	}
	public String getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	public List<SMContactEntity> getContacts() {
		return contacts;
	}
	public void setContacts(List<SMContactEntity> contacts) {
		this.contacts = contacts;
	}
	
	
	
	
	

}
