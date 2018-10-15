package com.morandror.models.dbmodels;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.morandror.models.BaseModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "item")
public class Item extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int id;

    @NotNull
    @Column(name = "found_at")
    private String foundAt;

    @Column(name = "friend_email")
    private String friendEmail;

    @Column(name = "image")
    private byte[] image;

    //@NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "recently_used")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date recently_used;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "insert_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date insert_date;

    @NotNull
    @Column(name = "usage_number")
    private int usage;

    @NotNull
    @Column(name = "brand")
    private String brand;

    @NotNull
    @Column(name = "color")
    private String color;

    @NotNull
    @Column(name = "size")
    private String size;

    @NotNull
    @Column(name = "type")
    //@Enumerated(EnumType.STRING)
    private String type;

    //@NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="closet_id")
    private Closet closet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoundAt() {
        return foundAt;
    }

    public void setFoundAt(String foundAt) {
        this.foundAt = foundAt;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getRecently_used() {
        return recently_used;
    }

    public void setRecently_used(Date recently_used) {
        this.recently_used = recently_used;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Closet getCloset() {
        return closet;
    }

    public void setCloset(Closet closet) {
        this.closet = closet;
    }

    public Date getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }
}