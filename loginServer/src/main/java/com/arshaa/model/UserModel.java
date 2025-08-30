package com.arshaa.model;
import java.sql.Date;

public class UserModel {

    private int userId;
    private boolean flag;
    //private String userPassword;
    private String email;   
    private int buildingId;
    private Date lastPasswordChangedDate;
    private String userType;
 
	public UserModel(int userId, boolean flag, String userPassword, String email, int buildingId,
			Date lastPasswordChangedDate, String userType) {
		super();
		this.userId = userId;
		this.flag = flag;
		this.email = email;
		this.buildingId = buildingId;
		this.lastPasswordChangedDate = lastPasswordChangedDate;
		this.userType = userType;
	}


	public String getUserType() {
		return userType;
	}


	public void setUserType(String userType) {
		this.userType = userType;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}


	public boolean isFlag() {
		return flag;
	}


	public void setFlag(boolean flag) {
		this.flag = flag;
	}


	/*
	 * public String getUserPassword() { return userPassword; }
	 * 
	 * 
	 * public void setUserPassword(String userPassword) { this.userPassword =
	 * userPassword; }
	 */

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getBuildingId() {
		return buildingId;
	}


	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}


	public Date getLastPasswordChangedDate() {
		return lastPasswordChangedDate;
	}


	public void setLastPasswordChangedDate(Date lastPasswordChangedDate) {
		this.lastPasswordChangedDate = lastPasswordChangedDate;
	}


	public UserModel() {
		// TODO Auto-generated constructor stub
	}


	public void setLastPasswordChangedDate(java.util.Date lastPasswordChangedDate2) {
		// TODO Auto-generated method stub
		
	}

}
