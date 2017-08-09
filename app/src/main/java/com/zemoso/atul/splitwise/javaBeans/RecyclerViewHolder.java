package com.zemoso.atul.splitwise.javaBeans;

/**
 * Created by zemoso on 8/8/17.
 */

public class RecyclerViewHolder {

    private String mAvatarUrl;
    private String mAvatarFilePath;
    private String mHeading;
    private String mStatus;
    private Boolean mImageDownloaded;

    public RecyclerViewHolder(String mAvatarUrl, String mAvatarFilePath, String mHeading, String mStatus) {
        this.mAvatarUrl = mAvatarUrl;
        this.mAvatarFilePath = mAvatarFilePath;
        this.mHeading = mHeading;
        this.mStatus = mStatus;
        this.mImageDownloaded = false;
    }

    public RecyclerViewHolder(String mAvatarUrl, String mAvatarFilePath, String mHeading, String mStatus, Boolean mImageDownloaded) {
        this.mAvatarUrl = mAvatarUrl;
        this.mAvatarFilePath = mAvatarFilePath;
        this.mHeading = mHeading;
        this.mStatus = mStatus;
        this.mImageDownloaded = mImageDownloaded;
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
