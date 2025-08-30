package net.arshaa.rat.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import Models.AvailableBeds;
import Models.BedInfoForBedChange;
import Models.BedsCount;
import Models.BuildingInfo;
import Models.RoomDto;
import net.arshaa.rat.entity.Bed;
import net.arshaa.rat.entity.Buildings;
import net.arshaa.rat.entity.Rooms;

public interface RatInterfaces {

	public List<BuildingInfo> getBedByBuildingId(Integer id);
	public List<AvailableBeds> getAllAvailableBed();
	public ResponseEntity<BuildingInfo> getDataforSquareIconsByBuildingId(Integer id, String type,String occupencyType);
	public BedInfoForBedChange getBedInformation(String bedId,int buildingId);
		public BedsCount getAvailableBedsCount(int buildingId);

	Map<Integer, String> getAllBuildingsNameByBuildingIds(List<Integer> buildingId);
	
	
	public List<Buildings> getAllBuildings();

}