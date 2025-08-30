package net.arshaa.rat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Models.BuildingInfo;
import net.arshaa.rat.service.RatService;

@RestController
@RequestMapping("/building/")
public class BuildingController {

	@Autowired
	private RatService ratService;
	
	
	public ResponseEntity<String> testApi(){
		return  new ResponseEntity<String>("Test Api Is Working",HttpStatus.OK);
	}
	
	//localhost:8989/building/data/3

	@GetMapping("data/{id}")
	public List<BuildingInfo> getBedWithGuestByBuildingId(@PathVariable Integer id) {

		return ratService.getBedByBuildingId(id);
	}

}
