package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReferralPayout implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("Daily")
    @Expose
    private String daily;
    @SerializedName("total_payout")
    @Expose
    private Double totalPayout;
    @SerializedName("Reward_per")
    @Expose
    private String rewardPer;
    @SerializedName("Reward")
    @Expose
    private Double reward;
    @SerializedName("level")
    @Expose
    private String level;
    @SerializedName("wallets")
    @Expose
    private List<Wallet> wallets = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }

    public Double getTotalPayout() {
        return totalPayout;
    }

    public void setTotalPayout(Double totalPayout) {
        this.totalPayout = totalPayout;
    }

    public String getRewardPer() {
        return rewardPer;
    }

    public void setRewardPer(String rewardPer) {
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

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }


    public static class Wallet implements Serializable {

        @SerializedName("walletsAddress")
        @Expose
        private String walletsAddress;
        @SerializedName("payouts")
        @Expose
        private Double payouts;
        @SerializedName("totalLendUSD")
        @Expose
        private Integer totalLendUSD;
        private final static long serialVersionUID = 4030398904659378069L;

        public String getWalletsAddress() {
            return walletsAddress;
        }

        public void setWalletsAddress(String walletsAddress) {
            this.walletsAddress = walletsAddress;
        }

        public Double getPayouts() {
            return payouts;
        }

        public void setPayouts(Double payouts) {
            this.payouts = payouts;
        }

        public Integer getTotalLendUSD() {
            return totalLendUSD;
        }

        public void setTotalLendUSD(Integer totalLendUSD) {
            this.totalLendUSD = totalLendUSD;
        }

    }
}
/*
public class ReferralPayout {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("diff")
    @Expose
    public Integer diff;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public List<PayoutUserData> data;


    public class PayoutUserData {
        @SerializedName("id")
        @Expose
        public Integer id = 0;
        @SerializedName("name")
        @Expose
        public String name = "";
        @SerializedName("total_reward")
        @Expose
        public double total_reward = 0.0;
        @SerializedName("total_ddk")
        @Expose
        public double total_ddk = 0.0;
        @SerializedName("from")
        @Expose
        public String from = "";
        @SerializedName("to")
        @Expose
        public String to = "";
        @SerializedName("transaction")
        @Expose
        public String transaction = "";
        @SerializedName("conversion")
        @Expose
        public double conversion = 0.0;
        @SerializedName("refferallist")
        @Expose
        public List<ReferralListPayout> referralListPayouts;
    }

    public class ReferralListPayout {
        @SerializedName("Daily")
        @Expose
        public String daily = "";
        @SerializedName("name")
        @Expose
        public String name = "";
        @SerializedName("Reward_per")
        @Expose
        public String reward_per = "";
        @SerializedName("level")
        @Expose
        public String level = "";
        @SerializedName("total_payout")
        @Expose
        public double total_payout = 0.0;
        @SerializedName("Reward")
        @Expose
        public double reward = 0.0;
        @SerializedName("wallets")
        @Expose
        public List<WalletPayout> wallets;

        public class WalletPayout {
            @SerializedName("payouts")
            @Expose
            public double payouts = 0.0;
            @SerializedName("walletsAddress")
            @Expose
            public String walletsAddress;
        }
    }

}*/
