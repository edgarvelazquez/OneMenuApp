package com.aje.onemenu.classes;

public class Restaurant {

    private String name;
    private String address;
    private String description;
    private String website;
    private String phoneNumber;
    private String cuisine;

    public Restaurant(){}

    public Restaurant(String name, String address, String description, String website, String phoneNumber, String cuisine) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.website = website;
        this.phoneNumber = phoneNumber;
        this.cuisine = cuisine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
}
