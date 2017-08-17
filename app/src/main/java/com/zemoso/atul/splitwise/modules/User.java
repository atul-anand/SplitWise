package com.zemoso.atul.splitwise.modules;

import org.json.JSONObject;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zemoso on 8/8/17.
 */

public class User extends RealmObject {

    @PrimaryKey
    private long userId;
    private String name;
    private String emailId;
    private String phoneNumber;
    private int age;
    private double debt;
    @Ignore
    private String imageFilePath;

    public User() {
    }

    public User(JSONObject jsonObject) {
        this.userId = jsonObject.optLong("userId");
        this.name = jsonObject.optString("name");
        this.emailId = jsonObject.optString("emailId");
        this.phoneNumber = jsonObject.optString("phoneNumber");
        this.age = jsonObject.optInt("age");
        this.debt = jsonObject.optDouble("debt");
        this.imageFilePath = jsonObject.optString("imageFilePath");
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
}
