package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class samModel implements Serializable
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

        @SerializedName("user_id")
        @Expose
        private Double userId;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("total_frozen_amt")
        @Expose
        private String totalFrozenAmt;
        private final static long serialVersionUID = -9155203532262581596L;

        public Double getUserId() {
            return userId;
        }

        public void setUserId(Double userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getTotalFrozenAmt() {
            return totalFrozenAmt;
        }

        public void setTotalFrozenAmt(String totalFrozenAmt) {
            this.totalFrozenAmt = totalFrozenAmt;
        }

    }
}
