package com.zemoso.atul.splitwise.modules;

import org.json.JSONObject;

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
    }

    public Group(JSONObject jsonObject) {
        this.groupId = jsonObject.optLong("groupId");
        this.groupName = jsonObject.optString("groupName");
        this.dateOfCreation = new Date(jsonObject.optString("dateOfCreation"));
        this.totalMembers = jsonObject.optInt("totalMembers");
        this.createdBy = jsonObject.optString("createdBy");
        this.imageFilePath = jsonObject.optString("imageFilePath");
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
}
