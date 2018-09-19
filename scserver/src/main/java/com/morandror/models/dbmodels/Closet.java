package com.morandror.models.dbmodels;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.morandror.models.BaseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "closet")
public class Closet extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closet_id")
    private int id;

    @NotNull
    @Column(name = "name")
    private String name;

    //@NotNull
    @Column(name = "image")
    private byte[] image;

    @NotNull
    @Column(name = "rfid_scanner")
    private String rfidScanner;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "location")
    private String location;

    @NotNull
    @Column(name = "insert_date")
    private Date insertDate;

    @JsonBackReference
    @ManyToMany(mappedBy = "closets")
    private Set<User> users = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "closet")
    private Set<Item> items = new HashSet<>();

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

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
