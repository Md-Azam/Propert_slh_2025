package com.arshaa.common;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiateCheckoutByGuestId {
	private String id;
	//private String occupancyType;
	
    private Date noticeDate;
//    @JsonFormat(pattern="yyyy-MM-dd")
//	    private java.util.Date plannedCheckOutDate;
	  
	
	//    private Date checkOutDate;
	  
		
	

	

}
