package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class usdtreddeemModel implements Serializable
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

    public class Data implements Serializable
    {

        @SerializedName("transaction_fees")
        @Expose
        private Double transactionFees;
        @SerializedName("usdt_amount")
        @Expose
        private Double usdtAmount;
        private final static long serialVersionUID = 3762806030968619532L;

        public Double getTransactionFees() {
            return transactionFees;
        }

        public void setTransactionFees(Double transactionFees) {
            this.transactionFees = transactionFees;
        }

        public Double getUsdtAmount() {
            return usdtAmount;
        }

        public void setUsdtAmount(Double usdtAmount) {
            this.usdtAmount = usdtAmount;
        }

    }
}