package com.arshaa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.arshaa.entity.Defaults;
import com.arshaa.repository.SecurityDepositRepo;

@Service
public class SecurityDepositServiceImpl implements SecurityDepositService {
	
	@Autowired
	private SecurityDepositRepo securityDepositRepo;
	

	@Override
	public ResponseEntity<List<Defaults>> getData() {
		try {
			List<Defaults> securityadd = securityDepositRepo.findAll();
			return new ResponseEntity<>(securityadd,HttpStatus.OK);
			
		}
		catch (Exception e){
			return new ResponseEntity(e.getMessage(),HttpStatus.OK);
			
		}
	}

	@Override
	public ResponseEntity updateDataById(int id, Defaults sdepo) {
		try {
			Defaults s = securityDepositRepo.findById(sdepo.getId()).orElse(null);
			s.setId(id);
			s.setMaintainanceCharge(sdepo.getMaintainanceCharge());
			s.setNoticeDays(sdepo.getNoticeDays());
			 securityDepositRepo.save(s);
			 return new ResponseEntity(s,HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity("Something went Wrong! Try again!",HttpStatus.OK);
		}
		 
	
	}

	@Override
	public ResponseEntity deleteDataById(int id) {
		try {
			Defaults depo=new Defaults();
			Defaults sdepo = securityDepositRepo.findById(depo.getId()).orElse(null);
			securityDepositRepo.delete(sdepo);
			return new ResponseEntity(sdepo,HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.OK);
		}
		
	}

	@Override
	public ResponseEntity<Defaults> addData(Defaults sdepo) {
		
		try {
			if(securityDepositRepo.existsByOccupancyType(sdepo.getOccupancyType())==false)
			{
				Defaults securityadd = securityDepositRepo.save(sdepo);
				return new ResponseEntity<>(securityadd,HttpStatus.OK);

			}
			else {
				return new ResponseEntity("Data already Exists",HttpStatus.OK);

			}
		}
		catch (Exception e){
			return new ResponseEntity("Something went Wrong! Try again",HttpStatus.OK);
			
		}
	}

	@Override
	public ResponseEntity getSecurityDepositByOccupancyType(String occupancyType) {
		try {
			Defaults otype = securityDepositRepo.findByOccupancyType(occupancyType);
			return new ResponseEntity<>(otype,HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity("Something Wrong",HttpStatus.OK);
		}
	}
		

}
