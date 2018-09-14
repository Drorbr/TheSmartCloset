package com.morandror.scclient.models;

import java.util.Date;
import java.util.Set;

public class Closet extends BaseModel{
    private int id;
    private String name;
    private byte[] image;
    private String rfidScanner;
    private String description;
    private String location;
    private Date insertDate;
    private Set<User> users;

    public Closet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getRfidScanner() {
        return rfidScanner;
    }

    public void setRfidScanner(String rfidScanner) {
        this.rfidScanner = rfidScanner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
