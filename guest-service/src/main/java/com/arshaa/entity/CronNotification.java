package com.arshaa.entity;


import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="cronNotification")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CronNotification {

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String cronType ;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date ;
}
