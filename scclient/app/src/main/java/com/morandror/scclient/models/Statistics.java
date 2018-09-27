package com.morandror.scclient.models;

import java.util.List;

public class Statistics extends BaseModel {
    private Item mostUsed;
    private String favoriteColor;
    private List<Item> lastDays;
    private List<Item> newestItems;

    public Statistics(Item mostUsed, String favoriteColor, List<Item> lastDays, List<Item> newestItems) {
        this.mostUsed = mostUsed;
        this.favoriteColor = favoriteColor;
        this.lastDays = lastDays;
        this.newestItems = newestItems;
    }

    public Item getMostUsed() {
        return mostUsed;
    }

    public void setMostUsed(Item mostUsed) {
        this.mostUsed = mostUsed;
    }

    public String getFavoriteColor() {
        return favoriteColor;
    }

    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }

    public List<Item> getLastDays() {
        return lastDays;
    }

    public void setLastDays(List<Item> lastDays) {
        this.lastDays = lastDays;
    }

    public List<Item> getNewestItems() {
        return newestItems;
    }

    public void setNewestItems(List<Item> newestItems) {
        this.newestItems = newestItems;
    }
}
