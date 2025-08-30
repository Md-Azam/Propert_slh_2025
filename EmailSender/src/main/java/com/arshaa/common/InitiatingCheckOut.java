package com.arshaa.common;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InitiatingCheckOut {
	
	 private String email;
	    private String name;
	    private Date date;
	    private String  bedId;
	    private Date checkInDate;
	    private String buildingName;
	    private Date noticeDate;
	    @JsonFormat(pattern="yyyy-MM-dd")
	    private java.util.Date plannedCheckOutDate;
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public String getBedId() {
			return bedId;
		}
		public void setBedId(String bedId) {
			this.bedId = bedId;
		}
		public Date getCheckInDate() {
			return checkInDate;
		}
		public void setCheckInDate(Date checkInDate) {
			this.checkInDate = checkInDate;
		}
		public String getBuildingName() {
			return buildingName;
		}
		public void setBuildingName(String buildingName) {
			this.buildingName = buildingName;
		}
		public Date getNoticeDate() {
			return noticeDate;
		}
		public void setNoticeDate(Date noticeDate) {
			this.noticeDate = noticeDate;
		}
		public java.util.Date getPlannedCheckOutDate() {
			return plannedCheckOutDate;
		}
		public void setPlannedCheckOutDate(java.util.Date plannedCheckOutDate) {
			this.plannedCheckOutDate = plannedCheckOutDate;
		}
		public InitiatingCheckOut(String email, String name, Date date, String bedId, Date checkInDate,
				String buildingName, Date noticeDate, Date plannedCheckOutDate) {
			super();
			this.email = email;
			this.name = name;
			this.date = date;
			this.bedId = bedId;
			this.checkInDate = checkInDate;
			this.buildingName = buildingName;
			this.noticeDate = noticeDate;
			this.plannedCheckOutDate = plannedCheckOutDate;
		}
		public InitiatingCheckOut() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    

}
