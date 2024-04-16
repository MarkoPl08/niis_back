package com.frontend.niis_back.entity;

public class Restaurant {

    private String id;
    private String name;
    private Number location;
    private String zipCode;

    public Restaurant(String name, Number location, String zipCode) {
        this.name = name;
        this.location = location;
        this.zipCode = zipCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getLocation() {
        return location;
    }

    public void setLocation(Number location) {
        this.location = location;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
