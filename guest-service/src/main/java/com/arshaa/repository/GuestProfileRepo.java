package com.arshaa.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arshaa.entity.GuestProfile;

@Repository
public interface GuestProfileRepo extends JpaRepository<GuestProfile, String>{

	//GuestProfile findByEmployeeId(String id);

	GuestProfile findByGuestId(String id);
	boolean existsByGuestId(String id);
	
	@Transactional
     public void deleteByGuestId(String guestId);
	
	@Modifying
	@Query(nativeQuery=true , value="delete from guest_profile where guest_id=?1")
	public void deleteGuestProfileByGuestId(@Param("guest_id")  String guestId);
	
}
