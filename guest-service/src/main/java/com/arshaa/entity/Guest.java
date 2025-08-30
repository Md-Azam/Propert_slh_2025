package com.arshaa.entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Guest")
public class Guest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "guestId")
	@GenericGenerator(name = "guestId", strategy = "com.arshaa.entity.StringSequenceGenerator", parameters = {
			@Parameter(name = StringSequenceGenerator.INCREMENT_PARAM, value = "1"),
			@Parameter(name = StringSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "SLH"),
			@Parameter(name = StringSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%06d") })
	private String id;
	private String firstName;
	private String email;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dateOfBirth;
	private int maintainanceCharge;
	private String personalNumber;
	private String secondaryPhoneNumber;
	private String fatherName;
	private String fatherNumber;
	private String bloodGroup;
	private String occupation;
	private String occupancyType;
	private String gender;
	private int sharing;
	private String aadharNumber;
	private int buildingId;
	private String bedId;
	private String previousBedId;
	private int duration;
	private double dueAmount;
	private String addressLine1;
	private String addressLine2;
	private java.util.Date cancelCheckOutDate;
	private String pincode;
	private String city;
	private String state;
	private String vehicleNo;
	private Date lastBillGenerationDate;
	private Date paidtill;
	private Date nextDuesGeneration;
	private double checkOutExceptionalDue;
	private String checkOutExceptionalMessage;
	private int packageId;
	private String transactionId;
	private String paymentPurpose;
	private double amountToBePaid;
	private double securityDeposit;
	private String guestStatus;
	private String createdBy;
	private double overAllDue;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createdOn = new java.util.Date(System.currentTimeMillis());

	private double amountPaid;
	private String checkinNotes;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date transactionDate = new java.util.Date(System.currentTimeMillis());
	private Double previousDues;
	private Date checkInDate;
	private double previousDefaultRent;
	private Date noticeDate;
	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private java.util.Date plannedCheckOutDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date previousPlannedCheckOutDate;
	private Date checkOutDate;
	private double defaultRent;
	private Date billGeneratedTill;
	private double inNoticeDue;

}
