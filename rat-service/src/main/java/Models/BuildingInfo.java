package Models;

import java.util.List;

public class BuildingInfo {

	private String buildingName;
	private List<FloorsInfo> floors;
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public List<FloorsInfo> getFloors() {
		return floors;
	}
	public void setFloors(List<FloorsInfo> floors) {
		this.floors = floors;
	}


}
