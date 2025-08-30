package com.arshaa.model;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class PasswordReset {
	private String email;
	private String newPassword;
	private String confirmPassword;
	@Temporal(TemporalType.DATE)
	private Date lastPasswordChangedDate = new Date(System.currentTimeMillis());
	public void setLastPasswordChangedDate(Date lastPasswordChangedDate) {
	    this.lastPasswordChangedDate = lastPasswordChangedDate;
	}

	public Date getLastPasswordChangedDate() {
	    return lastPasswordChangedDate;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public String getNewPassword() {
	    return newPassword;
	}

	public void setNewPassword(String newPassword) {
	    this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
	    return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
	    this.confirmPassword = confirmPassword;
	}
}
