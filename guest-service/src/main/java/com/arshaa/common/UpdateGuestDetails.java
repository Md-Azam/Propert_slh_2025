package com.arshaa.common;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGuestDetails {

	
	private String id ;
	private String firstName ;
	private String lastName;
	private String email ;
	private String personalNumber;
	private String aadharNumber;
	private String addressLine1 ;
	private Date dateOfBirth ;
	private String gender ;
	private String pincode;
	private String city;
	private String state;
	private String vehicleNo;

}
