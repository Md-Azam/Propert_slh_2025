package net.arshaa.rat.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "BEDS")
@NamedStoredProcedureQueries({
	@NamedStoredProcedureQuery(name= "onlyDues" , procedureName= "guest_due" , parameters = {
			@StoredProcedureParameter(mode = ParameterMode.IN , name = "GUEST__ID" , type= String.class )
			}),
})
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String bedId;
    @Column
    private int roomId;
    @Column
    private boolean bedStatus;
    @Column
    private String guestId;
    @Column
    private String bedName;
    @Column
    private double defaultRent;
    @Column
    private boolean ac;
    @Column
    private String createdBy;
    @Column
    private int floorId;    
    private int buildingId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private java.util.Date createdOn= new java.util.Date(System.currentTimeMillis());
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public boolean isBedStatus() {
		return bedStatus;
	}
	public void setBedStatus(boolean bedStatus) {
		this.bedStatus = bedStatus;
	}
	public String getGuestId() {
		return guestId;
	}
	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public double getDefaultRent() {
		return defaultRent;
	}
	public void setDefaultRent(double defaultRent) {
		this.defaultRent = defaultRent;
	}
	public boolean isAc() {
		return ac;
	}
	public void setAc(boolean ac) {
		this.ac = ac;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
	public java.util.Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(java.util.Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public Bed(int id, String bedId, int roomId, boolean bedStatus, String guestId, String bedName, double defaultRent,
			boolean ac,String createdBy, int floorId, int buildingId, java.util.Date createdOn) {
		super();
		this.id = id;
		this.bedId = bedId;
		this.roomId = roomId;
		this.bedStatus = bedStatus;
		this.guestId = guestId;
		this.bedName = bedName;
		this.defaultRent = defaultRent;
		this.ac = ac;
		this.createdBy = createdBy;
		this.floorId = floorId;
		this.buildingId = buildingId;
		this.createdOn = createdOn;
	}
	public Bed() {
		super();
		// TODO Auto-generated constructor stub
	}	   
}