package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class summaryHistoryModel implements Serializable {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Data data;
    private final static long serialVersionUID = -4078616553802259260L;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {

            @SerializedName("subscription")
            @Expose
            private Double subscription;
            @SerializedName("sam_reward")
            @Expose
            private Double samReward;
            @SerializedName("redeem")
            @Expose
            private Double redeem;
            @SerializedName("cashout")
            @Expose
            private Double cashout;
            @SerializedName("ddk_reward")
            @Expose
            private Double ddkReward;
            @SerializedName("sam_buy")
            @Expose
            private Double sam_buy;
            @SerializedName("sam_send")
            @Expose
            private Double sam_send;
            @SerializedName("btc_send")
            @Expose
            private Double btcSend;
            @SerializedName("eth_send")
            @Expose
            private Double ethSend;
            @SerializedName("ddk_send")
            @Expose
            private Double ddkSend;
            @SerializedName("usdt_send")
            @Expose
            private Double usdtSend;

            @SerializedName("ddk_buy")
            @Expose
            private Double ddk_buy;

            @SerializedName("usdt_buy")
            @Expose
            private Double usdt_buy;

            @SerializedName("eth_buy")
            @Expose
            private Double eth_buy;

            @SerializedName("btc_buy")
            @Expose
            private Double btc_buy;

        public Double getSam_buy() {
            return sam_buy;
        }

        public void setSam_buy(Double sam_buy) {
            this.sam_buy = sam_buy;
        }

        public Double getSam_send() {
            return sam_send;
        }

        public void setSam_send(Double sam_send) {
            this.sam_send = sam_send;
        }

        public Double getDdk_buy() {
            return ddk_buy;
        }

        public void setDdk_buy(Double ddk_buy) {
            this.ddk_buy = ddk_buy;
        }

        public Double getUsdt_buy() {
            return usdt_buy;
        }

        public void setUsdt_buy(Double usdt_buy) {
            this.usdt_buy = usdt_buy;
        }

        public Double getEth_buy() {
            return eth_buy;
        }

        public void setEth_buy(Double eth_buy) {
            this.eth_buy = eth_buy;
        }

        public Double getBtc_buy() {
            return btc_buy;
        }

        public void setBtc_buy(Double btc_buy) {
            this.btc_buy = btc_buy;
        }

        public Double getSubscription() {
            return subscription;
        }

            public void setSubscription(Double subscription) {
            this.subscription = subscription;
        }

            public Double getSamReward() {
            return samReward;
        }

            public void setSamReward(Double samReward) {
            this.samReward = samReward;
        }

            public Double getRedeem() {
            return redeem;
        }

            public void setRedeem(Double redeem) {
            this.redeem = redeem;
        }

            public Double getCashout() {
            return cashout;
        }

            public void setCashout(Double cashout) {
            this.cashout = cashout;
        }

            public Double getDdkReward() {
            return ddkReward;
        }

            public void setDdkReward(Double ddkReward) {
            this.ddkReward = ddkReward;
        }

            public Double getBtcSend() {
            return btcSend;
        }

            public void setBtcSend(Double btcSend) {
            this.btcSend = btcSend;
        }

            public Double getEthSend() {
            return ethSend;
        }

            public void setEthSend(Double ethSend) {
            this.ethSend = ethSend;
        }

            public Double getDdkSend() {
            return ddkSend;
        }

            public void setDdkSend(Double ddkSend) {
            this.ddkSend = ddkSend;
        }

            public Double getUsdtSend() {
            return usdtSend;
        }

            public void setUsdtSend(Double usdtSend) {
            this.usdtSend = usdtSend;
        }

        }
}