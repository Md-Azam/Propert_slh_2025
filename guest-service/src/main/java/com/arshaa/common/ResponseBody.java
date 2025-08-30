package com.arshaa.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBody<T> {

	private String message;
	private boolean status;
	private T data;
	
	
}
