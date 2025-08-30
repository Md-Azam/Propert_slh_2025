package com.arshaa.common;

import java.util.List;

import com.arshaa.entity.Guest;

public class IntialCheckout {

	private List<Guest> dueAmount;
	
	public List<Guest> getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(List<Guest> guests) {
		this.dueAmount = guests;
	}

	public IntialCheckout() {
		// TODO Auto-generated constructor stub
	}

}
