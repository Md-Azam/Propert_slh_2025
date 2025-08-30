package com.arshaa.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;
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
    private int buildingId;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
    @Column(name = "userLoggedinDate", nullable = false, unique = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date userLoggedinDate = new java.util.Date(System.currentTimeMillis());
    
    private java.util.Date lastPasswordChangedDate;
    private boolean flag;
    private String Pwd_1;
    private boolean loginStatus;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
    private java.util.Date logoutDate = new java.util.Date(System.currentTimeMillis());
    //getters and setters
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
	public int getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}
	public java.util.Date getUserLoggedinDate() {
		return userLoggedinDate;
	}
	public void setUserLoggedinDate(Timestamp timestamp) {
		this.userLoggedinDate = timestamp;
	}
	public java.util.Date getLastPasswordChangedDate() {
		return lastPasswordChangedDate;
	}
	public void setLastPasswordChangedDate(java.util.Date lpcDate) {
		this.lastPasswordChangedDate = lpcDate;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getPwd_1() {
		return Pwd_1;
	}
	public void setPwd_1(String pwd_1) {
		Pwd_1 = pwd_1;
	}
	public boolean isLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(boolean loginStatus) {
		this.loginStatus = loginStatus;
	}
	public java.util.Date getLogoutDate() {
		return logoutDate;
	}
	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = logoutDate;
	}
	
	public User(int userId, String email, String password, String userType, String userName, Long userPhoneNumber,
			int buildingId, Date userLoggedinDate, Date lastPasswordChangedDate, boolean flag, String pwd_1,
			boolean loginStatus, Date logoutDate) {
		super();
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.userName = userName;
		this.userPhoneNumber = userPhoneNumber;
		this.buildingId = buildingId;
		this.userLoggedinDate = userLoggedinDate;
		this.lastPasswordChangedDate = lastPasswordChangedDate;
		this.flag = flag;
		Pwd_1 = pwd_1;
		this.loginStatus = loginStatus;
		this.logoutDate = logoutDate;
	}
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}  
		}
