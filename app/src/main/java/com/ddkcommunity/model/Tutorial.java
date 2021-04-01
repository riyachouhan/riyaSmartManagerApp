package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tutorial {


    @SerializedName("tutorial_id")
    @Expose
    private Integer tutorialId;
    @SerializedName("tutorial_title")
    @Expose
    private String tutorialTitle;
    @SerializedName("tutorial_video")
    @Expose
    private String tutorialVideo;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public Tutorial() {
    }

    public Integer getTutorialId() {
        return tutorialId;
    }

    public void setTutorialId(Integer tutorialId) {
        this.tutorialId = tutorialId;
    }

    public String getTutorialTitle() {
        return tutorialTitle;
    }

    public void setTutorialTitle(String tutorialTitle) {
        this.tutorialTitle = tutorialTitle;
    }

    public String getTutorialVideo() {
        return tutorialVideo;
    }

    public void setTutorialVideo(String tutorialVideo) {
        this.tutorialVideo = tutorialVideo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
