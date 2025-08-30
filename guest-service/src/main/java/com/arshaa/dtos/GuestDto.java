package com.arshaa.dtos;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class GuestDto {
	
	private String id;
    private String guestName ;
    private String email;
    @JsonFormat(pattern = "dd-mm-yyyy")
    private Date dateOfBirth;
    private String personalNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
	@Temporal(TemporalType.TIMESTAMP)
    private java.util.Date checkInDate = new java.util.Date(System.currentTimeMillis());
    
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
    private java.util.Date checkOutDate;
    
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private java.util.Date plannedCheckOutDate;
    private LocalDate alertPlannedCheckOutDate;
	private String guestStatus;
    private double defaultRent;
    private double amountPaid;
    private String occupancyType;
    private String gender;
    private String vehicleNo;
    private String aadharNumber;
    private String buildingName ;
    private int buildingId;
    private String bedId;
    private int duration;
    private double dueAmount;
    private String addressLine1;
    private String addressLine2;
    private String pincode;
    private String city;
    private String state;
    private String imageUrl;
	
	
    

    

}
