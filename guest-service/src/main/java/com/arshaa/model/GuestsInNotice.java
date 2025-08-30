package com.arshaa.model;

import java.sql.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestsInNotice {

    private String firstName;
    private String id;
    private String BuildingName;
    private String bedId;
    private String personalNumber;
    private String email;
    private double dueAmount  ;
    private String occupancyType ;
    private Date noticeDate;
    // private List<E> totalDueAmount;
    
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
    private java.util.Date plannedCheckOutDate;

    
    public java.util.Date getPlannedCheckOutDate() {
		return plannedCheckOutDate;
	}
	public void setPlannedCheckOutDate(java.util.Date plannedCheckOutDate) {
		this.plannedCheckOutDate = plannedCheckOutDate;
	}
	public String getOccupancyType() {
		return occupancyType;
	}
	public void setOccupancyType(String occupancyType) {
		this.occupancyType = occupancyType;
	}
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
	//@Temporal(TemporalType.TIMESTAMP)
    private java.util.Date checkInDate = new java.util.Date(System.currentTimeMillis());
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
    private java.util.Date checkOutDate;
	
    
    
    }