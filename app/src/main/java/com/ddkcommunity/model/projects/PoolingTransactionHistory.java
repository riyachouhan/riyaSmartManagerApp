package com.ddkcommunity.model.projects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PoolingTransactionHistory {
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public ArrayList<PoolingHistoryData> lendData;
    @SerializedName("data1")
    @Expose
    public ArrayList<PoolingHistoryData> rewardData;
    @SerializedName("status")
    @Expose
    public Integer status;

    @SerializedName("Total_lend")
    @Expose
    public String totalLend;
    @SerializedName("Total_cancelled")
    @Expose
    public String totalCancelled;
    @SerializedName("Total_reward")
    @Expose
    public String totalReward;
    @SerializedName("Total_ddk_reward")
    @Expose
    public String totalDdkReward;
    @SerializedName("Total_start_date")
    @Expose
    public String totalStartDate;

    public class PoolingHistoryData {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("sender_address")
        @Expose
        public String sender_address;
        @SerializedName("amount")
        @Expose
        public Float amount;
        @SerializedName("conversion")
        @Expose
        public Float conversion;
        @SerializedName("cancel_status")
        @Expose
        public String cancel_status;
        @SerializedName("Transaction_date")
        @Expose
        public String transactionDate;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("ddk")
        @Expose
        public String ddk;
    }
}
