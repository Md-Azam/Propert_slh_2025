package com.arshaa.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MonthlyAndDailyPlanExtension {

	    @JsonFormat(pattern="yyyy-MM-dd")
	    private java.util.Date plannedCheckOutDate;
	    private String guestId;
}