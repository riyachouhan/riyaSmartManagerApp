package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class samBalanceModel implements Serializable
{

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

        @SerializedName("user_id")
        @Expose
        private Double userId;
        @SerializedName("balance")
        @Expose
        private Double balance;
        @SerializedName("total_frozen_amt")
        @Expose
        private String totalFrozenAmt;

        public Double getUserId() {
            return userId;
        }

        public void setUserId(Double userId) {
            this.userId = userId;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }

        public String getTotalFrozenAmt() {
            return totalFrozenAmt;
        }

        public void setTotalFrozenAmt(String totalFrozenAmt) {
            this.totalFrozenAmt = totalFrozenAmt;
        }

    }
}