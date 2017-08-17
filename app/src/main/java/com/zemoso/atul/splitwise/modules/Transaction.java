package com.zemoso.atul.splitwise.modules;

import org.json.JSONObject;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zemoso on 10/8/17.
 */

public class Transaction extends RealmObject {
    @PrimaryKey
    private long transId;
    private long groupId;
    private String description;
    private double amount;
    private String mop;
    @Ignore
    private RealmList lender;
    @Ignore
    private RealmList borrower;
    @Ignore
    private String imageFilePath;

    public Transaction() {
    }

    public Transaction(JSONObject jsonObject) {
        this.transId = jsonObject.optLong("transId");
        this.groupId = jsonObject.optLong("groupId");
        this.description = jsonObject.optString("description");
        this.amount = jsonObject.optDouble("amount");
        this.mop = jsonObject.optString("mop");
        this.lender = (RealmList) ((List) jsonObject.optJSONArray("lender"));
        this.borrower = (RealmList) ((List) jsonObject.optJSONArray("borrower"));
        this.imageFilePath = jsonObject.optString("imageFilePath");
    }

    public long getTransId() {
        return transId;
    }

    public void setTransId(long transId) {
        this.transId = transId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMop() {
        return mop;
    }

    public void setMop(String mop) {
        this.mop = mop;
    }

    public List getLender() {
        return lender;
    }

    public void setLender(List lender) {
        this.lender = (RealmList) lender;
    }

    public List getBorrower() {
        return borrower;
    }

    public void setBorrower(List borrower) {
        this.borrower = (RealmList) borrower;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }
}
