package com.arshaa.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BedInfoForBedChange {

	private String guestId;
	private int buildingId;
	private String buildingName;
	private int sharing;
	private String roomType;
	private String bedId;
	private String occupancyType;
	private double defaultRent;
	private double dueAmount;
	private Date lastBillGenerationDate;
	private Date billGeneratedTill;
	
}
