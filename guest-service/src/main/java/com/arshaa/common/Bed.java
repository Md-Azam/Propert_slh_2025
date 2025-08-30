package com.arshaa.common;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Bed {

    private String bedId;
    private String guestId;
    private double defaultRent;
    private String guestName;

    public String getGuestName() {
		return guestName;
	}

	public void setGuestName(String guestName) {
		this.guestName = guestName;
	}

	public Bed() {
    }

    public void setDefaultRent(double defaultRent) {
        this.defaultRent = defaultRent;
    }

    public double getDefaultRent() {
        return defaultRent;
    }

    public String getBedId() {
        return bedId;
    }

    public void setBedId(String bedId) {
        this.bedId = bedId;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

//    public double getDueAmount() {
//        return dueAmount;
//    }
//
//    public void setDueAmount(double dueAmount) {
//        this.dueAmount = dueAmount;
//    }
}
