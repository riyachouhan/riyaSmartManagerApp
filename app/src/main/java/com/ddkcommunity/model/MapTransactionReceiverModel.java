package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MapTransactionReceiverModel implements Serializable {

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

    public class Data implements Serializable
    {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sam_receiver_address")
        @Expose
        private String samReceiverAddress;
        @SerializedName("eth_receiver_address")
        @Expose
        private String ethReceiverAddress;
        @SerializedName("btc_receiver_address")
        @Expose
        private String btcReceiverAddress;
        @SerializedName("usdt_receiver_address")
        @Expose
        private String usdtReceiverAddress;
        @SerializedName("sam_receiver_trans_address")
        @Expose
        private String samReceiverTransAddress;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("eth_receiver_trans_address")
        @Expose
        private String ethReceiverTransAddress;
        @SerializedName("btc_receiver_trans_address")
        @Expose
        private String btcReceiverTransAddress;
        @SerializedName("usdt_receiver_trans_address")
        @Expose
        private String usdtReceiverTransAddress;
        private final static long serialVersionUID = 824130638594028823L;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSamReceiverAddress() {
            return samReceiverAddress;
        }

        public void setSamReceiverAddress(String samReceiverAddress) {
            this.samReceiverAddress = samReceiverAddress;
        }

        public String getEthReceiverAddress() {
            return ethReceiverAddress;
        }

        public void setEthReceiverAddress(String ethReceiverAddress) {
            this.ethReceiverAddress = ethReceiverAddress;
        }

        public String getBtcReceiverAddress() {
            return btcReceiverAddress;
        }

        public void setBtcReceiverAddress(String btcReceiverAddress) {
            this.btcReceiverAddress = btcReceiverAddress;
        }

        public String getUsdtReceiverAddress() {
            return usdtReceiverAddress;
        }

        public void setUsdtReceiverAddress(String usdtReceiverAddress) {
            this.usdtReceiverAddress = usdtReceiverAddress;
        }

        public String getSamReceiverTransAddress() {
            return samReceiverTransAddress;
        }

        public void setSamReceiverTransAddress(String samReceiverTransAddress) {
            this.samReceiverTransAddress = samReceiverTransAddress;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getEthReceiverTransAddress() {
            return ethReceiverTransAddress;
        }

        public void setEthReceiverTransAddress(String ethReceiverTransAddress) {
            this.ethReceiverTransAddress = ethReceiverTransAddress;
        }

        public String getBtcReceiverTransAddress() {
            return btcReceiverTransAddress;
        }

        public void setBtcReceiverTransAddress(String btcReceiverTransAddress) {
            this.btcReceiverTransAddress = btcReceiverTransAddress;
        }

        public String getUsdtReceiverTransAddress() {
            return usdtReceiverTransAddress;
        }

        public void setUsdtReceiverTransAddress(String usdtReceiverTransAddress) {
            this.usdtReceiverTransAddress = usdtReceiverTransAddress;
        }

    }
}