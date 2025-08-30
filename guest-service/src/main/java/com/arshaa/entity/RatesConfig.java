package com.arshaa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="RatesConfig")
public class RatesConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id ;
	private int buildingId ;
	private int sharing ;
	private String roomType ;
	private String buildingName ;
	private String occupancyType ;
	private  double price ;
	private boolean duplicatePackage;

}
