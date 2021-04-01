package com.ddkcommunity.model.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RewardHistory {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("SponsorList")
    @Expose
    private List<SponsorList> sponsorList = null;
    @SerializedName("count")
    @Expose
    private String count;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<SponsorList> getSponsorList() {
        return sponsorList;
    }

    public void setSponsorList(List<SponsorList> sponsorList) {
        this.sponsorList = sponsorList;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
