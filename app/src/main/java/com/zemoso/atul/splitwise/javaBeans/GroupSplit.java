package com.zemoso.atul.splitwise.javaBeans;

import org.json.JSONObject;

/**
 * Created by zemoso on 22/8/17.
 */

public class GroupSplit {
    private Long creditorId;
    private String creditorName;
    private Long debtorId;
    private String debtorName;
    private Double amount;

    public GroupSplit(JSONObject jsonObject) {
        creditorId = jsonObject.optLong("creditorId");
        creditorName = jsonObject.optString("creditorName");
        debtorId = jsonObject.optLong("debtorId");
        debtorName = jsonObject.optString("debtorName");
        amount = jsonObject.optDouble("amount");
        amount = Math.round(amount * 100.0) / 100.0;
    }

    public Long getCreditorId() {
        return creditorId;
    }

    public void setCreditorId(Long creditorId) {
        this.creditorId = creditorId;
    }

    public String getCreditorName() {
        return creditorName;
    }

    public void setCreditorName(String creditorName) {
        this.creditorName = creditorName;
    }

    public Long getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(Long debtorId) {
        this.debtorId = debtorId;
    }

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
