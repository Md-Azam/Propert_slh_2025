package com.arshaa.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VacatedGuests {

	private String firstName;
    private String id;
    private String BuildingName;
    private String bedId;
    private String personalNumber;
    private String guestStatus ;
	private String email;
    
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
    private java.util.Date checkOutDate;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
    private java.util.Date checkInDate;
	
    
    
}
