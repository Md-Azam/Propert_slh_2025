package com.arshaa.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class InnoticeToRegular {

    
    private String guestStatus ;
    private String id ;
    private Date noticeDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private java.util.Date plannedCheckOutDate;
}
