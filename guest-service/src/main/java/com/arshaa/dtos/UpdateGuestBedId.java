package com.arshaa.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGuestBedId {
	
	private String guestId;
	private String bedId;
	private double defaultRent ;
	private Integer packageId;

}
