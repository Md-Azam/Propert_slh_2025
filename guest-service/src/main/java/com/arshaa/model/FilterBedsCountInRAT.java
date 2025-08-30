package com.arshaa.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FilterBedsCountInRAT {

	private int id;
	private String title;
	private String category;
	private String type;
	private int count;
}
