package net.arshaa.rat.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Floors {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int floorId;
    @Column
    private String floorNumber;
    @Column
    private int buildingId;
    @Column
    private String createdBy; 
    
   
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private java.util.Date createdOn= new java.util.Date(System.currentTimeMillis());
	public int getFloorId() {
		return floorId;
	}
	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}
	public String getFloorNumber() {
		return floorNumber;
	}
	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
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
	public Floors(int floorId, String floorNumber ,int buildingId, String createdBy, Date createdOn) {
		super();
		this.floorId = floorId;
		this.floorNumber = floorNumber;
		this.buildingId = buildingId;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	
	}
	
	public Floors() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

    /*
     * @ManyToOne
     *
     * @JoinColumn(name = "building_id") private Building building;
     *
     * @OneToMany(mappedBy = "floor") private List<Room> room = new ArrayList<>();
     */


    }
