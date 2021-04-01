package com.ddkcommunity.model.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SponsorList {
    @SerializedName("sponsor_address")
    @Expose
    private String sponsorAddress;
    @SerializedName("introducer_address")
    @Expose
    private String introducerAddress;
    @SerializedName("reward")
    @Expose
    private String reward;
    @SerializedName("sponsor_level")
    @Expose
    private String sponsorLevel;
    @SerializedName("transaction_type")
    @Expose
    private String transactionType;
    @SerializedName("reward_time")
    @Expose
    private String rewardTime;

    public String getSponsorAddress() {
        return sponsorAddress;
    }

    public void setSponsorAddress(String sponsorAddress) {
        this.sponsorAddress = sponsorAddress;
    }

    public String getIntroducerAddress() {
        return introducerAddress;
    }

    public void setIntroducerAddress(String introducerAddress) {
        this.introducerAddress = introducerAddress;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getSponsorLevel() {
        return sponsorLevel;
    }

    public void setSponsorLevel(String sponsorLevel) {
        this.sponsorLevel = sponsorLevel;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getRewardTime() {
        return rewardTime;
    }

    public void setRewardTime(String rewardTime) {
        this.rewardTime = rewardTime;
    }
}
