package net.arshaa.rat.repository;



import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import net.arshaa.rat.entity.Buildings;

public interface BuildingRepository extends JpaRepository<Buildings, Integer> {

	Buildings getBuildingNameByBuildingId(int buildingId);

	List<Buildings> findAllByBuildingIdIn(List<Integer> buildingId);
}
