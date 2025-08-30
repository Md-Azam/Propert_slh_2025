package com.arshaa.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "prebook")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PreBooking {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer preBookId;
	private LocalDate bookingDate;
	private Double advanceAmount;
	private LocalDate joiningDate;
	private String name;
	private Long phoneNumber;
	private String transactionId;
	private String bedId;
	private Integer buildingId;
	private boolean preBookedStatus;

	/*
	 * {"advanceAmount":3000, "name":"azam", "joiningDate":"2024-01-21",
	 * "phoneNumber":566645, "transactionId":"345345634009", "bedId":"SN-504-A-AC",
	 * "buildingId":1 }
	 * 
	 */

}
