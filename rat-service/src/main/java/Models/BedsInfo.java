package Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import common.Guest;
import common.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class BedsInfo<E> {
	private boolean bedStatus;
	private String guestId;
	private double defaultRent;
	private boolean ac;
	private int roomId;
	private int floorId;
	private int buildingId;
	private String bedName;
	private String bedId;
	private String buildingName;
	private int bedNum;
	private String name;
    private double dueAmount;
    private String occupancyType ;
  private String   typeOfFilter ;
  private boolean exceeded ;
	  private byte[] url;
	  private String type;
	  private long size;
		private String guestName;
	private Guest guest;
    private String guestStatus;
    private int sharing;
    private String imageUrl;
    private double guestDue;
//    private List<E> guestDue ;
    @JsonFormat(pattern = "dd-MM-yyyy", timezone = "IST")
	private java.util.Date plannedCheckOutDate;

    
    
    private Object data;
    
    
    
 
	
}
