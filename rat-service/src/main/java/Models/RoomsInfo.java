package Models;

import java.util.List;

public class RoomsInfo {
    private String roomNumber;
    private List<BedsInfo>  beds;
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public List<BedsInfo> getBeds() {
		return beds;
	}
	public void setBeds(List<BedsInfo> beds) {
		this.beds = beds;
	}

    
}
