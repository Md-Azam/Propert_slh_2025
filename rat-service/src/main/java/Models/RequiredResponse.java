package Models;

import java.util.List;


import common.Guest;
import net.arshaa.rat.entity.Bed;

public class RequiredResponse {

	 
	private Bed bed;
	private Guest guest;
	public Bed getBed() {
		return bed;
	}
	public void setBed(Bed bed) {
		this.bed = bed;
	}
	public Guest getGuest() {
		return guest;
	}
	public void setGuest(Guest guest) {
		this.guest = guest;
	}
	


}
