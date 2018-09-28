package com.morandror.scclient.models;

import java.util.Date;
import java.util.Set;

public class Closet extends BaseModel{
    private int id;
    private String name;
    private String image;
    private String rfidScanner;
    private String description;
    private String location;
    private Date insertDate;
    private Set<User> users;
    private Set<Item> items;

    public Closet(int id, String name, String description, String location, Date insertDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.insertDate = insertDate;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
