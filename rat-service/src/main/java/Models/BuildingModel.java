package Models;
import java.util.List;

public class BuildingModel {
    private int buildingId;
    private String buildingName;
    private List<BedsInfo> beds;
    private List<BedSummary>bedSum;
    public BuildingModel(int buildingId, String buildingName, List<BedsInfo> beds, List<BedSummary> bedSum) {
        super();
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.beds = beds;
        this.bedSum = bedSum;
    }
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
    public List<BedsInfo> getBeds() {
        return beds;
    }
    public void setBeds(List<BedsInfo> beds) {
        this.beds = beds;
    }
    public List<BedSummary> getBedSum() {
        return bedSum;
    }
    public void setBedSum(List<BedSummary> bedSum) {
        this.bedSum = bedSum;
    }
    public BuildingModel() {
        super();
        // TODO Auto-generated constructor stub
    }




}