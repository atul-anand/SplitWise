package com.zemoso.atul.splitwise.javaBeans;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zemoso on 14/8/17.
 */

public class Category {
    private String text;
    private String imageUrl;
    private List<Category> menu;

    public Category(String text, String imageUrl, List<Category> menu) {
        this.text = text;
        this.imageUrl = imageUrl;
        this.menu = menu;
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

    public List<Category> isMenu() {
        return menu;
    }

    public void setMenu(List menu) {
        menu = menu;
    }

}
