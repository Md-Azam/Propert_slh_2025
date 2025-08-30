package com.arshaa.common;

import javax.persistence.*;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Payment {

	
	
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	//private double dueAmount;
	private double amountPaid;
	private int buildingId ;
	//@UniqueElements
	private String transactionId;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDate = new Date(System.currentTimeMillis());
	//private Date checkinDate;
	private String paymentPurpose;
	// private String occupancyType[] = { "Daily" , "Monthly" , "Regular" };
	private String occupancyType;
	private double refundAmount;

	// Fields taking reference from guest-Master Data .
	private String guestId; // (f k) from guestId
	private String createdBy ;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
	@Temporal(TemporalType.TIMESTAMP)
	private Date  createdOn  = new Date(System.currentTimeMillis());

	// CONSTRUCTORS USING FIELDS
	public Payment(int id, double dueAmount, double amountPaid, String transactionId, Date transactionDate,
			Date checkinDate, String paymentPurpose,String createdBy ,int buildingId , Date createdOn , String occupancyType, double refundAmount, String guestId) {
		super();
		this.id = id;
		//this.dueAmount = dueAmount;
		this.amountPaid = amountPaid;
		this.transactionId = transactionId;
		this.transactionDate = transactionDate;
		//this.checkinDate = checkinDate;
		
		this.paymentPurpose = paymentPurpose;
		this.occupancyType = occupancyType;
		this.refundAmount = refundAmount;
		this.guestId = guestId;
		this.createdBy= createdBy ;
		this.createdOn= createdOn ;
		this.buildingId= buildingId ;
	}

	// Getters and setters .

	public double getRefundAmount() {
		return refundAmount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public int getPaymentId() {
		return id;
	}

	public void setPaymentId(int id) {
		this.id = id;
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

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public int getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
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

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	// DEFAULT CONSTRUCTOR
	public Payment() {
	}

	// TO STRING METHODS .
//	@Override
//	public String toString() {
//		return "Payment{" + "id=" + id + ", dueAmount=" + dueAmount + ", amountPaid=" + amountPaid + ", transactionId='"
//				+ transactionId + '\'' + ", transactionDate=" + transactionDate + ", checkinDate=" + checkinDate
//				+ ", paymentPurpose='" + paymentPurpose + '\'' + ", occupancyType='" + occupancyType + '\''
//				+ ", guestId='" + guestId + '\'' + '}';
//	}

	/*
	 * public double getDueAmount() { LocalDate checkInDate = LocalDate.now() ; int
	 * remainder = checkInDate.getDayOfMonth();
	 *
	 * double count=securityDeposit+defaultRent; dueAmount = count;
	 *
	 *
	 * if(remainder <30 && remainder > 1) {
	 *
	 * dueAmount = (defaultRent/30)*remainder ; } if (onBoard == true ) {
	 *
	 * newDuesAmount = dueAmount - amountPaid ; }
	 *
	 */

	/*
	 * public double getAmountPaid() { if(occupancyType.toString().equals("Daily"))
	 * {
	 *
	 * amountPaid = (defaultRent/30)* remainder ;
	 *
	 *
	 * }else if (occupancyType.toString().equals("Monthly")) {
	 *
	 * amountPaid = defaultRent ;
	 *
	 * }else if (occupancyType.toString().equals("Regular")){
	 *
	 * amountPaid = defaultRent ; System.out.println("Exception"); } return
	 * amountPaid; }
	 */

	/*
	 * public double getDefaultRent() { if(occupancyType.toString().equals("Daily"))
	 * { defaultRent = defaultRent/30;
	 *
	 *
	 * }else if (occupancyType.toString().equals("Monthly")) { defaultRent =
	 * amountPaid;
	 *
	 * }else if (occupancyType.toString().equals("Regular")){ defaultRent =
	 * amountPaid; } return defaultRent ; }
	 */
}
