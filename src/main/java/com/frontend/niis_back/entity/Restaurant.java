package com.frontend.niis_back.entity;

public class Restaurant {

    private String name;
    private String location;
    private Number zipCode;

    public Restaurant(String name, String location, Number zipCode) {
        this.name = name;
        this.location = location;
        this.zipCode = zipCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Number getZipCode() {
        return zipCode;
    }

    public void setZipCode(Number zipCode) {
        this.zipCode = zipCode;
    }
}
