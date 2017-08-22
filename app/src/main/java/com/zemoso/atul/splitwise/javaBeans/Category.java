package com.zemoso.atul.splitwise.javaBeans;

/**
 * Created by zemoso on 14/8/17.
 */

public class Category {
    private String text;
    private String imageUrl;

    public Category() {
        this.text = "";
        this.imageUrl = "";
    }

    public Category(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
