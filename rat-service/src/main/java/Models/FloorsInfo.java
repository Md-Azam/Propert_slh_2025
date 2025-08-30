package Models;

import java.util.List;

public class FloorsInfo {
     private String floorName;
     private List<RoomsInfo> rooms;
	public String getFloorName() {
		return floorName;
	}
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	public List<RoomsInfo> getRooms() {
		return rooms;
	}
	public void setRooms(List<RoomsInfo> rooms) {
		this.rooms = rooms;
	}
     
}
