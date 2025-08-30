package com.arshaa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="loginData")
public class LoginInfo {

    
    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String email;
    @Column
    private String userType;
    @Column
    private String userName; 
    @Column
    private Long userPhoneNumber;
    @Column
    private int buildingId;
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss", timezone="IST")
    @Column(name = "userLoggedinDate", nullable = false, unique = true, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date userLoggedinDate = new java.util.Date(System.currentTimeMillis());
    public LoginInfo() {
        super();
        // TODO Auto-generated constructor stub
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getUserPhoneNumber() {
        return userPhoneNumber;
    }
    public void setUserPhoneNumber(Long userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
    public int getBuildingId() {
        return buildingId;
    }
    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }
    public java.util.Date getUserLoggedinDate() {
        return userLoggedinDate;
    }
    public void setUserLoggedinDate(java.util.Date userLoggedinDate) {
        this.userLoggedinDate = userLoggedinDate;
    }
    
    
    
}
