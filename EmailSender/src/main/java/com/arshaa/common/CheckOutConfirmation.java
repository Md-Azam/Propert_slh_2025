package com.arshaa.common;


import java.util.Date;

public class CheckOutConfirmation {

	
	 private String email;
	    private String name;
	    private Date date;
	    private String  bedId;
	    private Date checkInDate;
	    private String buildingName;
	    private Date noticeDate;
	    private java.sql.Date checkOutDate ;
	    private java.util.Date plannedCheckOutDate;
		public java.util.Date getPlannedCheckOutDate() {
			return plannedCheckOutDate;
		}
		public void setPlannedCheckOutDate(java.util.Date plannedCheckOutDate) {
			this.plannedCheckOutDate = plannedCheckOutDate;
		}
		public String getEmail() {
			return email;
		}
		public String getName() {
			return name;
		}
		public Date getDate() {
			return date;
		}
		public String getBedId() {
			return bedId;
		}
		public Date getCheckInDate() {
			return checkInDate;
		}
		public String getBuildingName() {
			return buildingName;
		}
		public Date getNoticeDate() {
			return noticeDate;
		}
		public java.sql.Date getCheckOutDate() {
			return checkOutDate;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public void setBedId(String bedId) {
			this.bedId = bedId;
		}
		public void setCheckInDate(Date checkInDate) {
			this.checkInDate = checkInDate;
		}
		public void setBuildingName(String buildingName) {
			this.buildingName = buildingName;
		}
		public void setNoticeDate(Date noticeDate) {
			this.noticeDate = noticeDate;
		}
		public void setCheckOutDate(java.sql.Date checkOutDate) {
			this.checkOutDate = checkOutDate;
		}
	    
	    
}
