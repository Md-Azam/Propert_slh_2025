package common;

import java.sql.Date;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guest {

	private String id;
	private String firstName;
	private String lastName;
	private String email;
	@JsonFormat(pattern = "dd-mm-yyyy")
	private Date dateOfBirth;
	private String personalNumber;
	private String secondaryPhoneNumber;
	private String fatherName;
	private String fatherNumber;
	private String bloodGroup;
	private String occupation;
	private String occupancyType;
	private String gender;
	private String aadharNumber;
	private int buildingId;
	private String bedId;
	private int duration;
	private double dueAmount;
	private String addressLine1;
	private String addressLine2;
	private String pincode;
	private String city;
	private String state;
	private String workPhone;
	private String workAddressLine1;
	private String workAddressLine2;
	private String transactionId;
	private String paymentPurpose;
	private double amountToBePaid;
	private double securityDeposit;
	private String guestStatus;

	private Date noticeDate;
	private double amountPaid;
	private String checkinNotes;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "IST")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date transactionDate = new java.util.Date(System.currentTimeMillis());
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date checkInDate;

	@JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private java.util.Date plannedCheckOutDate;

	private Date checkOutDate;
	private double defaultRent;

}
