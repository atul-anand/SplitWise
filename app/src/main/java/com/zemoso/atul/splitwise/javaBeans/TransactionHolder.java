package com.zemoso.atul.splitwise.javaBeans;

/**
 * Created by zemoso on 14/8/17.
 */

public class TransactionHolder {


    private Long userId;
    private String name;
    private Double amount;

    public TransactionHolder(Long userId, String name, Double amount) {
        this.userId = userId;
        this.name = name;
        this.amount = amount;
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

