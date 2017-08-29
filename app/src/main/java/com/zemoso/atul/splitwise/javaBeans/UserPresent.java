package com.zemoso.atul.splitwise.javaBeans;

/**
 * Created by zemoso on 22/8/17.
 */

public class UserPresent {
    Long userId;
    String username;
    Boolean verified;

    public UserPresent(Long userId, String username, Boolean verified) {
        this.userId = userId;
        this.username = username;
        this.verified = verified;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean isVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }
}
