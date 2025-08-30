package com.arshaa.common;

public class MailDto {

	private String email ;
	private String guestId ;
	private double dueAmount ;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public MailDto(String email, String guestId, double dueAmount) {
		super();
		this.email = email;
		this.guestId = guestId;
		this.dueAmount = dueAmount;
	}
	public MailDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getGuestId() {
		return guestId;
	}
	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}
	public double getDueAmount() {
		return dueAmount;
	}
	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}
	
	
}
