package com.payment.common;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecentTransactions {
	private int id;
	private double amountPaid;
	private String transactionId;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
	private Date transactionDate;
	private String paymentPurpose;
	private String guestId;
	private String guestName;
	private int buildingId ;
	private String buildingName ;
	private String bedId ;

	
}
