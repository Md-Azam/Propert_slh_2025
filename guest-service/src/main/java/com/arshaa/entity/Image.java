package com.arshaa.entity;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="images")

public class Image {
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getGuestId() {
        return guestId;
    }
    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }
    public String getImageName() {
        return imageName;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id ;
    private String guestId ;
    private String imageName ;
   

    
    
}
