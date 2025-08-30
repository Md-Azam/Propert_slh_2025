package com.payment.common;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class THistory {

	private int id;
	private double amountPaid;
	private int buildingId;
	private String transactionId;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDate = new Date(System.currentTimeMillis());
	// private Date checkinDate;
	private String paymentPurpose;
	private String occupancyType;
	private double refundAmount;
	private String email;
	private String GuestName;
	// private String lastName ;
	private String buildingName;
	private String personalNumber;
	private String bedId;
	private String GuestId;

}
