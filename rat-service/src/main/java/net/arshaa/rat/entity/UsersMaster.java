package net.arshaa.rat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class UsersMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column
	private String buildingName;
	@Column
	private String createdBy;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column
	@Temporal(TemporalType.DATE)
	private java.util.Date createdOn = new java.util.Date(System.currentTimeMillis());
	@Column
	private String email;
	@Column
	private String password;
	@Column
	private String userType;
	@Column
	private String userName;
	@Column
	private Long userPhoneNumber;

	public UsersMaster(int id, String buildingName, String manager_name, long phone_number, String createdBy,
			Date createdOn, String email, String password, String userType, String userName, Long userPhoneNumber) {
		super();
		this.id = id;
		this.buildingName = buildingName;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.userName = userName;
		this.userPhoneNumber = userPhoneNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public java.util.Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(java.util.Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUserPhoneNumber() {
		return userPhoneNumber;
	}

	public void setUserPhoneNumber(Long userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	public UsersMaster() {
// TODO Auto-generated constructor stub
	}

}