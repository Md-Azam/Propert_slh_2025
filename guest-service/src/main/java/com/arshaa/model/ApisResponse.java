package com.arshaa.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApisResponse<T> {

	private boolean status;
	private String message;
	private T data;
}
