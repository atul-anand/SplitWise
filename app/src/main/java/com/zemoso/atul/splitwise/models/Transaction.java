package com.zemoso.atul.splitwise.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
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
    private String dot;
    //    @Ignore
    private RealmList<UserId> lender;
    //    @Ignore
    private RealmList<UserId> borrower;
    //    @Ignore
    private String imageFilePath;

    public Transaction() {
        this.transId = -1;
        this.groupId = -1;
        this.description = "testTransaction";
        this.mop = "Cash";
        this.dot = String.valueOf(new Date().getDate());
        this.lender = new RealmList<>();
        this.borrower = new RealmList<>();
        this.amount = 0.0;
        this.imageFilePath = "";
    }

    public Transaction(JSONObject jsonObject) {
        this.transId = jsonObject.optLong("transID");
        this.groupId = jsonObject.optLong("groupId");
        this.description = jsonObject.optString("description");
        this.amount = jsonObject.optDouble("amount");
        this.amount = Math.round(this.amount * 100.0) / 100.0;
        this.mop = jsonObject.optString("mop");
        this.dot = jsonObject.optString("dot");
        this.lender = new RealmList<>();
        this.borrower = new RealmList<>();

        try {
            JSONArray lenders = jsonObject.getJSONArray("lender");
            for (int i = 0; i < lenders.length(); i++) {
                lender.add(new UserId(Long.valueOf(String.valueOf(lenders.get(i)))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray borrowers = jsonObject.getJSONArray("borrower");
            for (int i = 0; i < borrowers.length(); i++) {
                borrower.add(new UserId(Long.valueOf(String.valueOf(borrowers.get(i)))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        this.lender = new RealmList( jsonObject.optJSONArray("lender"));
//        this.borrower = ((List) jsonObject.optJSONArray("borrower"));
        this.imageFilePath = jsonObject.optString("url");
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

    public String getDot() {
        return dot;
    }

    public void setDot(String dot) {
        this.dot = dot;
    }

    public RealmList<UserId> getLender() {
        return lender;
    }

    public void setLender(RealmList lender) {
        this.lender = lender;
    }

    public RealmList<UserId> getBorrower() {
        return borrower;
    }

    public void setBorrower(RealmList borrower) {
        this.borrower = borrower;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transId=" + transId +
                ", groupId=" + groupId +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", mop='" + mop + '\'' +
                ", dot='" + dot + '\'' +
                ", url='" + imageFilePath + '\'' +
                '}';
    }
}
