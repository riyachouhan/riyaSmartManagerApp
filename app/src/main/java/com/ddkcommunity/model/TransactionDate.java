package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionDate {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public List<TransactionDateData> data;

    public class TransactionDateData {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("from_date")
        @Expose
        public String from_date;
        @SerializedName("to_date")
        @Expose
        public String to_date;
        @SerializedName("conversion_rate")
        @Expose
        public double conversion_rate;
        @SerializedName("created_at")
        @Expose
        public String created_at;
        @SerializedName("name")
        @Expose
        public String name;
    }
}