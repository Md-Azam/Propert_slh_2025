package com.arshaa.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.arshaa.entity.Defaults;

public interface SecurityDepositService {
	
	public ResponseEntity<Defaults> addData(Defaults sdepo);
	public ResponseEntity<List<Defaults>> getData();
	public ResponseEntity  updateDataById(int id,Defaults sdepo);
	public ResponseEntity  deleteDataById(int id);
	public ResponseEntity getSecurityDepositByOccupancyType(String occupancyType);
	
	

}
