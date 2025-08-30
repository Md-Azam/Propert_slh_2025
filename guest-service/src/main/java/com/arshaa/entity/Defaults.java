package com.arshaa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Defaults {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String occupancyType;

	// @UniqueElements
	private int noticeDays;
	private int maintainanceCharge;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMaintainanceCharge() {
		return maintainanceCharge;
	}

	public void setMaintainanceCharge(int maintainanceCharge) {
		this.maintainanceCharge = maintainanceCharge;
	}

	public String getOccupancyType() {
		return occupancyType;
	}

	public void setOccupancyType(String occupancyType) {
		this.occupancyType = occupancyType;
	}

	public int getNoticeDays() {
		return noticeDays;
	}

	public void setNoticeDays(int noticeDays) {
		this.noticeDays = noticeDays;
	}

	public Defaults(int id, String occupancyType, int noticeDays, int maintainanceCharge) {
		super();
		this.id = id;
		this.maintainanceCharge = maintainanceCharge;
		this.occupancyType = occupancyType;
		this.noticeDays = noticeDays;
	}

	public Defaults() {
		super();
		// TODO Auto-generated constructor stub
	}

}
