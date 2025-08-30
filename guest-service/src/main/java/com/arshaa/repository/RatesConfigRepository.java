package com.arshaa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arshaa.entity.RatesConfig;

@Repository
public interface RatesConfigRepository extends JpaRepository<RatesConfig, Integer>{
	public List<RatesConfig>  findByBuildingId(int buildingId);
	
	public RatesConfig findByPriceAndOccupancyTypeAndBuildingId(double price,String occupancyType,Integer buildingId);
	
	public List<RatesConfig> findByBuildingIdAndOccupancyType(int buildingId , String occupancyType);
	public List<RatesConfig> findByOccupancyType(String occupancyType);
	RatesConfig findByOccupancyTypeAndBuildingIdAndSharing(String occupancyType,int buildingId,int sharing);

//	@Query(value='select * from rates_config where building_id =?1 and occupancy_type=?2 and price=?3 and room_type not like '%Non%';',nativeQuery=true)
//	get packageIdBybuildingIdOccupancyType
	RatesConfig findByBuildingIdAndOccupancyTypeAndPriceAndRoomType(int buildingId,String occupancyType, double price,String roomType);
	
	RatesConfig findByBuildingIdAndOccupancyTypeAndRoomTypeAndSharingAndDuplicatePackage(int buildingId,String occupancyType,String roomType,int sharing,boolean duplicatePackage);

}
