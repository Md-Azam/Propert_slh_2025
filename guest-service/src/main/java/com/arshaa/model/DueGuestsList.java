package com.arshaa.model;

import java.sql.Date;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DueGuestsList {

	private String guestName;
	private String buildingName;
	private String bedId;
	private String guestId;
	private String phoneNumber;
	private String email;
	private double dueAmount;
	private String billGeneratedTill;
	
}
