package com.zemoso.atul.splitwise.javaBeans;

/**
 * Created by zemoso on 8/8/17.
 */

public class RecyclerViewHolder {

    private long id;
    private String mAvatarUrl;
    private String mAvatarFilePath;
    private String mHeading;
    private String mStatus;
    private Boolean mImageDownloaded;

    public RecyclerViewHolder(long id, String mAvatarUrl, String mAvatarFilePath, String mHeading, String mStatus) {
        this.id = id;
        this.mAvatarUrl = mAvatarUrl;
        this.mAvatarFilePath = mAvatarFilePath;
        this.mHeading = mHeading;
        this.mStatus = mStatus;
        this.mImageDownloaded = false;
    }

    public RecyclerViewHolder(int id, String mAvatarUrl, String mAvatarFilePath, String mHeading, String mStatus, Boolean mImageDownloaded) {
        this.id = id;
        this.mAvatarUrl = mAvatarUrl;
        this.mAvatarFilePath = mAvatarFilePath;
        this.mHeading = mHeading;
        this.mStatus = mStatus;
        this.mImageDownloaded = mImageDownloaded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getmAvatarUrl() {
        return mAvatarUrl;
    }

    public void setmAvatarUrl(String mAvatarUrl) {
        this.mAvatarUrl = mAvatarUrl;
    }

    public String getmAvatarFilePath() {
        return mAvatarFilePath;
    }

    public void setmAvatarFilePath(String mAvatarFilePath) {
        this.mAvatarFilePath = mAvatarFilePath;
    }

    public String getmHeading() {
        return mHeading;
    }

    public void setmHeading(String mHeading) {
        this.mHeading = mHeading;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public Boolean isImageDownloaded() {
        return mImageDownloaded;
    }

    public void setmDownloaded(Boolean mImageDownloaded) {
        this.mImageDownloaded = mImageDownloaded;
    }
}
