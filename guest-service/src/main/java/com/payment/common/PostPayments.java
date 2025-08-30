package com.payment.common;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.UniqueElements;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PostPayments {
	
	

	//private double dueAmount;
	private double amountPaid;
	private int buildingId ;
	@UniqueElements
	private String transactionId;
	 @JsonFormat(pattern="yyyy-MM-dd")
	    private java.util.Date transactionDate = new java.util.Date(System.currentTimeMillis());
	//private Date checkinDate;
	private String paymentPurpose;
	// private String occupancyType[] = { "Daily" , "Monthly" , "Regular" };
	private String occupancyType;
	private double refundAmount;
	
	 @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
		@Temporal(TemporalType.TIMESTAMP)
		private Date  createdOn  = new Date(System.currentTimeMillis());

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	// Fields taking reference from guest-Master Data .
	private String guestId; // (f k) from guestId

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public java.util.Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(java.util.Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getPaymentPurpose() {
		return paymentPurpose;
	}

	public void setPaymentPurpose(String paymentPurpose) {
		this.paymentPurpose = paymentPurpose;
	}

	public String getOccupancyType() {
		return occupancyType;
	}

	public void setOccupancyType(String occupancyType) {
		this.occupancyType = occupancyType;
	}

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public PostPayments(double amountPaid, int buildingId, String transactionId, Date transactionDate,
			String paymentPurpose, String occupancyType, double refundAmount, String guestId) {
		super();
		this.amountPaid = amountPaid;
		this.buildingId = buildingId;
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		this.paymentPurpose = paymentPurpose;
		this.occupancyType = occupancyType;
		this.refundAmount = refundAmount;
		this.guestId = guestId;
	}

	public PostPayments() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
