package com.arshaa.common;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BedsForBuildingName {

	private int buildingId;
	@Column
	private String buildingName;
	@Column
	private String createdBy;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createdOn = new java.util.Date(System.currentTimeMillis());
	
	

}
