package com.zemoso.atul.splitwise.models;

import io.realm.RealmObject;

/**
 * Created by zemoso on 24/8/17.
 */

public class UserId extends RealmObject {

    private Long userId;

    public UserId() {
    }

    public UserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.valueOf(userId);
    }
}
