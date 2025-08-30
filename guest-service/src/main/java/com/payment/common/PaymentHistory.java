package com.payment.common;

import java.util.Date;

public class PaymentHistory {

	private double amountPaid;
	private String transactionId;
	private String transactionDate;
	
	private int id ;
	
	private String guestId;
	private int buildingId ;
	public double getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getGuestId() {
		return guestId;
	}
	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}
	public int getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}
	public PaymentHistory(double amountPaid, String transactionId,int id , String transactionDate, String guestId,
			int buildingId) {
		super();
		this.amountPaid = amountPaid;
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.guestId = guestId;
		this.id= id ;
		this.buildingId = buildingId;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public PaymentHistory () {
		super();
	}
}
