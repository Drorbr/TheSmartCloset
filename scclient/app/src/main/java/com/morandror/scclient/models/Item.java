package com.morandror.scclient.models;

import java.util.Date;

public class Item extends BaseModel {
    private int id;
    private String foundAt;
    private String image;
    private Date recently_used;
    private Date insert_date;
    private int usage;
    private String brand;
    private String color;
    private String size;
    private String type;

    public Item(String foundAt, String image, Date recently_used, int usage, String brand, String color, String size, String type) {
        this.foundAt = foundAt;
        this.image = image;
        this.recently_used = recently_used;
        this.usage = usage;
        this.brand = brand;
        this.color = color;
        this.size = size;
        this.type = type;
    }

    public Item() {
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public Date getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(Date insert_date) {
        this.insert_date = insert_date;
    }

    @Override
    public String toString() {
        return String.format("%s %s from %s", this.color, this.type, this.brand);
    }
}
