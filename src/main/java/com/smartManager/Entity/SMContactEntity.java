package com.smartManager.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class SMContactEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int contactID;
	private String email;
	private String name;
	private String nickName;
	private String about;
	private String work;
	private Boolean enabledStatus;
	private String phone;
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] profilePicture;
	
	@ManyToOne
	private SMUserEntity user;
	public int getContactID() {
		return contactID;
	}
	public void setContactID(int contactID) {
		this.contactID = contactID;
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
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public Boolean getEnabledStatus() {
		return enabledStatus;
	}
	public void setEnabledStatus(Boolean enabledStatus) {
		this.enabledStatus = enabledStatus;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public byte[] getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}
	public SMUserEntity getUser() {
		return user;
	}
	public void setUser(SMUserEntity user) {
		this.user = user;
	}
	
	
	

}
