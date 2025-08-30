package net.arshaa.rat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import net.arshaa.rat.entity.Floors;

public interface FloorRepository extends JpaRepository<Floors, Integer>{

	public Optional<List<Floors>> findByBuildingId(Integer id);

	public Floors getFloorNumberByFloorId(int floorId);

	public Optional<List<Floors>> getFloorIdAndNameByBuildingId(int buildingId);

	public List<Floors> getByBuildingId(int buildingId);

	public List<Floors> getFloorByBuildingId(int buildingId);

	public List<Floors> getFloorsByBuildingId(int buildingId);

}
