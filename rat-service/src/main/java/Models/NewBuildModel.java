package Models;

import java.util.List;

public class NewBuildModel {

    private int buildingId;
    private String buildingName;
    // private List<BedsInfo> beds;
    //private List<BedSummary> bedSummary;

    private int totalBeds;
    private int occupiedBeds;
    private int availableBeds;
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
	public int getTotalBeds() {
		return totalBeds;
	}
	public void setTotalBeds(int totalBeds) {
		this.totalBeds = totalBeds;
	}
	public int getOccupiedBeds() {
		return occupiedBeds;
	}
	public void setOccupiedBeds(int occupiedBeds) {
		this.occupiedBeds = occupiedBeds;
	}
	public int getAvailableBeds() {
		return availableBeds;
	}
	public void setAvailableBeds(int availableBeds) {
		this.availableBeds = availableBeds;
	}
	public NewBuildModel(int buildingId, String buildingName, int totalBeds, int occupiedBeds, int availableBeds) {
		super();
		this.buildingId = buildingId;
		this.buildingName = buildingName;
		this.totalBeds = totalBeds;
		this.occupiedBeds = occupiedBeds;
		this.availableBeds = availableBeds;
	}
	public NewBuildModel() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    
}
