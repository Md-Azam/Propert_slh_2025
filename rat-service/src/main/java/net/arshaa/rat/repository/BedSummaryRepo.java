package net.arshaa.rat.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;


import net.arshaa.rat.entity.Bed;


public interface BedSummaryRepo extends JpaRepository<Bed, Integer> {

    List<Bed> findByBuildingIdAndBedStatus(int building_id, boolean b);

   
}