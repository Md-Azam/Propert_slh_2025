package net.arshaa.rat.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@NamedStoredProcedureQueries({
		@NamedStoredProcedureQuery(name = "firstProcedure", procedureName = " GUEST_COUNT", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "BUILDING_ID", type = Integer.class) }),
		@NamedStoredProcedureQuery(name = "Procedure2", procedureName = " PENDING_PAYMENTS_BY_BUILDING_ID", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "BUILDING__ID", type = Integer.class) }),
		@NamedStoredProcedureQuery(name = "Procedure3", procedureName = " BUILDING_WISE_TOTAL_DUE", parameters = {
				@StoredProcedureParameter(mode = ParameterMode.IN, name = "BUILDING__ID", type = Integer.class) }) })

public class Buildings {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int buildingId;
	@Column
	private String buildingName;
	@Column
	private String createdBy;
	@JsonFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date createdOn = new java.util.Date(System.currentTimeMillis());

	
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

	public Buildings(int building_id, String building_name, String manager_name, long phone_number, String createdBy,
			Date createdOn) {
		super();
		this.buildingId = building_id;
		this.buildingName = building_name;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
	}

	public Buildings() {
		super();
// TODO Auto-generated constructor stub
	}

}