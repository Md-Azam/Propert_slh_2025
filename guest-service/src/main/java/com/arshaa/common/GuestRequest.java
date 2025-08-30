package com.arshaa.common;

import com.arshaa.entity.Guest;

public class GuestRequest {

	private Guest guest;
	private Payment payment;

	public GuestRequest(Guest guest, Payment payment) {
		super();
		this.guest = guest;
		this.payment = payment;
	}

	public GuestRequest() {
		super();
	}

	public Guest getGuest() {
		return guest;
	}

	public void setGuest(Guest guest) {
		this.guest = guest;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

}
