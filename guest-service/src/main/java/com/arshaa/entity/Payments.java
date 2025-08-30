package com.arshaa.entity;

import javax.persistence.*;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Payments {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private double amountPaid;
	private int buildingId;
	// @UniqueElements
	private String transactionId;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date transactionDate = new java.util.Date(System.currentTimeMillis());
	private String paymentPurpose;
	private String occupancyType;
	private double refundAmount;

	// Fields taking reference from guest-Master Data .
	private String guestId; // (f k) from guestId
	private String createdBy;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn = new Date(System.currentTimeMillis());

	// Getters and setters .

}
