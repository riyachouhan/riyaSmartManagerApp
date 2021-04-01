package com.ddkcommunity.model.reward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RewardResponse {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("reward_history")
    @Expose
    private RewardHistory rewardHistory;
    @SerializedName("status")
    @Expose
    private Integer status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RewardHistory getRewardHistory() {
        return rewardHistory;
    }

    public void setRewardHistory(RewardHistory rewardHistory) {
        this.rewardHistory = rewardHistory;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
