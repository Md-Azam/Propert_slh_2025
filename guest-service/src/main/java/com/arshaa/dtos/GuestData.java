package com.arshaa.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class GuestData<T> {
    
    private T content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages; 
    private boolean lastPage;
   
}

