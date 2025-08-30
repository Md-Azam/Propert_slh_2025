package com.arshaa.common;

public class OnboardingConfirmation {

	private String email;
	private String name;
	private double amountPaid;
	private String bedId;
	private String buildingName;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getBedId() {
		return bedId;
	}

	public void setBedId(String bedId) {
		this.bedId = bedId;
	}

	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public OnboardingConfirmation(String email, String name, double amountPaid, String bedId, String buildingName) {
		super();
		this.email = email;
		this.name = name;
		this.amountPaid = amountPaid;
		this.bedId = bedId;
		this.buildingName = buildingName;
	}
	public OnboardingConfirmation() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
