package com.morandror.scclient.models;

import java.util.Date;

public class Item extends BaseModel {
    private String foundAt;
    private String image;
    private Date recentlyUsed;
    private int usage;
    private String brand;
    private String color;
    private int size;
    private String type;

    public Item(String foundAt, String image, Date recentlyUsed, int usage, String brand, String color, int size, String type) {
        this.foundAt = foundAt;
        this.image = image;
        this.recentlyUsed = recentlyUsed;
        this.usage = usage;
        this.brand = brand;
        this.color = color;
        this.size = size;
        this.type = type;
    }

    public Item() {
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

    public Date getRecentlyUsed() {
        return recentlyUsed;
    }

    public void setRecentlyUsed(Date recentlyUsed) {
        this.recentlyUsed = recentlyUsed;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.brand;
    }
}
