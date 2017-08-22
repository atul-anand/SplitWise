package com.zemoso.atul.splitwise.models;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zemoso on 8/8/17.
 */

public class Group extends RealmObject {
    @PrimaryKey
    private long groupId;
    private String groupName;
    private Date dateOfCreation;
    private int totalMembers;
    private String createdBy;
    @Ignore
    private String imageFilePath;

    public Group() {
        this.groupId = -1;
        this.groupName = "Non Group Expenses";
        this.createdBy = "master";
        this.totalMembers = 1;
        this.dateOfCreation = new Date();
        this.imageFilePath = "";
    }

    public Group(JSONObject jsonObject) {
        this.groupId = jsonObject.optLong("groupId");
        this.groupName = jsonObject.optString("groupName");

        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            this.dateOfCreation = df.parse(jsonObject.optString("dateOfCreation"));
        } catch (ParseException e) {
            this.dateOfCreation = new Date();
            e.printStackTrace();
        }
        this.totalMembers = jsonObject.optInt("totalMembers");
        this.createdBy = jsonObject.optString("createdBy");
        this.imageFilePath = jsonObject.optString("url");
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", dateOfCreation=" + dateOfCreation +
                ", totalMembers=" + totalMembers +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
