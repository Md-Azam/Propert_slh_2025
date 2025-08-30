package com.arshaa.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AvailableBedsResponse {

	private boolean status;
	private String message;
	private BedsCount data;
}
