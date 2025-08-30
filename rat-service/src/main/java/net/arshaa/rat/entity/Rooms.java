package net.arshaa.rat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Rooms {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roomId;
    @Column
    private String roomNumber;
    @Column
    private int floorId;
    @Column
    private int buildingId;
	@Column
    private String createdBy;   
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
    @Temporal(TemporalType.DATE)
    private java.util.Date createdOn= new java.util.Date(System.currentTimeMillis());
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public int getFloorId() {
		return floorId;
	}
	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}
	public int getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public java.util.Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(java.util.Date createdOn) {
		this.createdOn = createdOn;
	}
	public Rooms(int roomId, String roomNumber, int floorId,int buildingId, String createdBy, Date createdOn) {
		super();
		this.roomId = roomId;
		this.roomNumber = roomNumber;
		this.floorId = floorId;
		this.buildingId = buildingId;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}
	public Rooms() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	    }

