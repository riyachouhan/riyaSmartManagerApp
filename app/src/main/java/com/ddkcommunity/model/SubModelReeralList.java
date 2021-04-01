package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubModelReeralList  implements Serializable
    {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("data")
        @Expose
        private List<Datum> data = null;
        @SerializedName("diff")
        @Expose
        private Integer diff;
        private final static long serialVersionUID = -1314872361795609563L;

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<Datum> getData() {
            return data;
        }

        public void setData(List<Datum> data) {
            this.data = data;
        }

        public Integer getDiff() {
            return diff;
        }

        public void setDiff(Integer diff) {
            this.diff = diff;
        }

public class Datum implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Daily")
    @Expose
    private Double daily;
    @SerializedName("total_payout")
    @Expose
    private Double totalPayout;
    @SerializedName("Reward_per")
    @Expose
    private Double rewardPer;
    @SerializedName("Reward")
    @Expose
    private Double reward;
    @SerializedName("level")
    @Expose
    private String level;
    private final static long serialVersionUID = -4640799553386413438L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDaily() {
        return daily;
    }

    public void setDaily(Double daily) {
        this.daily = daily;
    }

    public Double getTotalPayout() {
        return totalPayout;
    }

    public void setTotalPayout(Double totalPayout) {
        this.totalPayout = totalPayout;
    }

    public Double getRewardPer() {
        return rewardPer;
    }

    public void setRewardPer(Double rewardPer) {
        this.rewardPer = rewardPer;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(Double reward) {
        this.reward = reward;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}

}