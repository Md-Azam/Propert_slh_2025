package com.arshaa.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arshaa.entity.PreBooking;

@Repository
public interface PreBookRepository extends JpaRepository<PreBooking, Integer> {
	


	List<PreBooking> findAllByBuildingId(Integer buildingId);

	PreBooking findByBedId(String bedId);
	
	List<PreBooking> findByJoiningDateBeforeAndPreBookedStatusTrue(LocalDate localDate);
	
	List<PreBooking> findByJoiningDateAndPreBookedStatusTrue(LocalDate localDate);
	
	
	
	
	

}
