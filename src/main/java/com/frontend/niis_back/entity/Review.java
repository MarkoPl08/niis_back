package com.frontend.niis_back.entity;

public class Review {
    private String id;
    private String title;
    private String text;
    private Number rating;

    public Review() {
    }

    public Review(String id, String title, String text, Number rating) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Number getRating() {
        return rating;
    }

    public void setRating(Number rating) {
        this.rating = rating;
    }
}
