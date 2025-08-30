package Models;

public class BedSummary {

    private int totalBeds;
    private int occupiedBeds;
    private int availableBeds;
    

    private int bedNum;
    private String buildingName;

    public int getBedNum() {
        return bedNum;
    }

    public void setBedNum(int bedNum) {
        this.bedNum = bedNum;
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



    public BedSummary(int totalBeds, int occupiedBeds, int availableBeds, int bedNum, String buildingName) {
        super();
        this.totalBeds = totalBeds;
        this.occupiedBeds = occupiedBeds;
        this.availableBeds = availableBeds;
        this.bedNum = bedNum;
        this.buildingName = buildingName;
    }

    public BedSummary() {
        super();
    }

}
