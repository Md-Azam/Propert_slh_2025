package common;

import javax.persistence.Column;

public class User {

	private String email;
	private String password;
	private String userType;
	private String userName;
	private Long userPhoneNumber;
	private int buildingId;

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public User(String email, String password, String userType, String userName, Long userPhoneNumber, int buildingId) {
		super();
		this.email = email;
		this.password = password;
		this.userType = userType;
		this.userName = userName;
		this.userPhoneNumber = userPhoneNumber;
		this.buildingId = buildingId;
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

	public User() {
// TODO Auto-generated constructor stub
	}

}