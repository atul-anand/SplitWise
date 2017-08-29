package com.zemoso.atul.splitwise.javaBeans;

import org.json.JSONObject;

/**
 * Created by zemoso on 14/8/17.
 */

public class TransactionHolder {


    private Long userId;
    private String name;
    private Double amount;

    public TransactionHolder() {
        this.userId = -1L;
        this.name = "";
        this.amount = 0.0;
    }

    public TransactionHolder(Long userId, String name, Double amount) {
        this.userId = userId;
        this.name = name;
        this.amount = amount;
        this.amount = Math.round(this.amount * 100.0) / 100.0;
    }

    public TransactionHolder(JSONObject jsonObject) {
        this.userId = jsonObject.optLong("userId");
        this.name = jsonObject.optString("userName");
        this.amount = jsonObject.optDouble("debt");
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

