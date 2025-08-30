package com.payment.common;

import java.util.Date;

public class PaymentConfirmation {

	private String email;
	private String name;
	private double amountPaid;
	private String transactionId;
	private Date date;
	private int paymentId;
	private double refundAmount;
	private String  bedId;
	private Date checkInDate;
	private String buildingName;
	private String purpose;
	
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	private   String hostelName;
	
	

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getHostelName() {
		return hostelName;
	}



	public void setHostelName(String hostelName) {
		this.hostelName = hostelName;
	}

	public Date getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	public String getBedId() {
		return bedId;
	}

	public void setBedId(String bedId) {
		this.bedId = bedId;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public PaymentConfirmation(String email, String name, double amountPaid, String transactionId, Date date,
			int paymentId, double refundAmount, String bedId, Date checkInDate, String buildingName, String purpose,
			String hostelName) {
		super();
		this.email = email;
		this.name = name;
		this.amountPaid = amountPaid;
		this.transactionId = transactionId;
		this.date = date;
		this.paymentId = paymentId;
		this.refundAmount = refundAmount;
		this.bedId = bedId;
		this.checkInDate = checkInDate;
		this.buildingName = buildingName;
		this.purpose = purpose;
		this.hostelName = hostelName;
	}

	
	public PaymentConfirmation() {
		super();
		// TODO Auto-generated constructor stub
	}



    

    
}