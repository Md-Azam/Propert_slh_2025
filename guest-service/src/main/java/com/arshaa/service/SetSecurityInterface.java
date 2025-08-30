package com.arshaa.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.arshaa.dtos.GuestDto;
import com.arshaa.entity.SecurityDeposit;
import com.arshaa.repository.GuestRepository;


public interface SetSecurityInterface {

	
	public SecurityDeposit saveSecurityDeposit(SecurityDeposit security);
	
	public List<SecurityDeposit> getallSecuritydeposit();
	
	
	 public SecurityDeposit  updateSecurityDeposit( Integer id, SecurityDeposit sd);  
	   
	 public List<GuestDto> findExceededGuestByBuildingId(Integer buildingId,Date plannedCheckOutDate);
		public List<GuestDto> findTodaysCheckOutByBuildingId(Integer buildingId,Date plannedCheckOutDate);

	     
	 
}
