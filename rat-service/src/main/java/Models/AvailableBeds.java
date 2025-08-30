package Models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AvailableBeds {

	
	private String bedId ;
	private int bId ;
	private  boolean bedStatus ;
	private String bedName ;
	private int buildingId;
	private String buildingName ;
	private String floorNumber ;
	private String roomNumber ;
	
	
	 private double defaultRent;
	 private int floorId ;
	 private int roomId ;
	 
	 
	public AvailableBeds() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getBedId() {
		return bedId;
	}
	public void setBedId(String bedId) {
		this.bedId = bedId;
	}
	public int getbId() {
		return bId;
	}
	public void setbId(int bId) {
		this.bId = bId;
	}
	public boolean isBedStatus() {
		return bedStatus;
	}
	public void setBedStatus(boolean bedStatus) {
		this.bedStatus = bedStatus;
	}
	public String getBedName() {
		return bedName;
	}
	public void setBedName(String bedName) {
		this.bedName = bedName;
	}
	public int getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(int buildingId) {
		this.buildingId = buildingId;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getFloorNumber() {
		return floorNumber;
	}
	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public double getDefaultRent() {
		return defaultRent;
	}
	public void setDefaultRent(double defaultRent) {
		this.defaultRent = defaultRent;
	}
	
	public int getFloorId() {
		return floorId;
	}
	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}
	public int getRoomId() {
		return roomId;
	}
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	public AvailableBeds(String bedId, int bId, boolean bedStatus, String bedName, int buildingId, String buildingName,
			String floorNumber, String roomNumber, double defaultRent, int floorId,
			int roomId) {
		super();
		this.bedId = bedId;
		this.bId = bId;
		this.bedStatus = bedStatus;
		this.bedName = bedName;
		this.buildingId = buildingId;
		this.buildingName = buildingName;
		this.floorNumber = floorNumber;
		this.roomNumber = roomNumber;
		this.defaultRent = defaultRent;
		this.floorId = floorId;
		this.roomId = roomId;
	}
	 
	 
}
