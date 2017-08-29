package com.zemoso.atul.splitwise.javaBeans;

import com.zemoso.atul.splitwise.models.Transaction;

import org.json.JSONObject;

/**
 * Created by zemoso on 18/8/17.
 */

public class TransactionBalances {
    private Long userId;
    private String username;
    private Double debt;
    private Double amount;

    public TransactionBalances() {
        this.userId = -1L;
        this.username = "";
        this.debt = 0.0;
        this.amount = 0.0;
    }

    public TransactionBalances(Transaction transaction) {

    }

    public TransactionBalances(JSONObject jsonObject) {
        this.userId = jsonObject.optLong("userId");
        this.username = jsonObject.optString("name");
        this.debt = jsonObject.optDouble("debt");
        this.amount = jsonObject.optDouble("amount");
        this.amount = Math.round(this.amount * 100.0) / 100.0;
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

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
