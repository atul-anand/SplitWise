package com.zemoso.atul.splitwise.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private Date dot;
    @Ignore
    private ArrayList lender;
    @Ignore
    private ArrayList borrower;
    @Ignore
    private String imageFilePath;

    public Transaction() {
        this.transId = -1;
        this.groupId = -1;
        this.description = "testTransaction";
        this.mop = "Cash";
        this.dot = new Date();
        this.lender = new ArrayList();
        this.borrower = new ArrayList<>();
        this.amount = 0.0;
        this.imageFilePath = "";
    }

    public Transaction(JSONObject jsonObject) {
        this.transId = jsonObject.optLong("transId");
        this.groupId = jsonObject.optLong("groupId");
        this.description = jsonObject.optString("description");
        this.amount = jsonObject.optDouble("amount");
        this.mop = jsonObject.optString("mop");
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            this.dot = df.parse(jsonObject.optString("dot"));
        } catch (ParseException e) {
            this.dot = new Date();
            e.printStackTrace();
        }
        this.lender = new ArrayList();
        this.borrower = new ArrayList<>();

        try {
            JSONArray lenders = null;
            lenders = jsonObject.getJSONArray("lender");
            for (int i = 0; i < lenders.length(); i++) {
                lender.add(lenders.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONArray borrowers = null;
            borrowers = jsonObject.getJSONArray("borrower");
            for (int i = 0; i < borrowers.length(); i++) {
                borrower.add(borrowers.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        this.lender = new RealmList( jsonObject.optJSONArray("lender"));
//        this.borrower = ((List) jsonObject.optJSONArray("borrower"));
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

    public Date getDot() {
        return dot;
    }

    public void setDot(Date dot) {
        this.dot = dot;
    }

    public ArrayList getLender() {
        return lender;
    }

    public void setLender(ArrayList lender) {
        this.lender = lender;
    }

    public ArrayList getBorrower() {
        return borrower;
    }

    public void setBorrower(ArrayList borrower) {
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
                '}';
    }
}
