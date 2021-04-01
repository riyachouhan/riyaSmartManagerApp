package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PollingHistoryTransction
{
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public ArrayList<PoolingHistoryData> lendData;

    @SerializedName("redeem_data")
    @Expose
    public ArrayList<PoolingHistoryData> redeem_data;

    @SerializedName("currency_wallet")
    @Expose
    public ArrayList<PoolingHistoryData> currency_wallet;

    @SerializedName("data1")
    @Expose
    public ArrayList<PoolingHistoryData> rewardData;

    @SerializedName("data2")
    @Expose
    public ArrayList<PoolingHistoryData> Data2;

    @SerializedName("data3")
    @Expose
    public ArrayList<PoolingHistoryData> Data3;

    @SerializedName("data4")
    @Expose
    public ArrayList<PoolingHistoryData> Data4;

    @SerializedName("data5")
    @Expose
    public ArrayList<PoolingHistoryData> Data5;

    @SerializedName("data6")
    @Expose
    public ArrayList<PoolingHistoryData> Data6;

    @SerializedName("data7")
    @Expose
    public ArrayList<PoolingHistoryData> Data7;

    @SerializedName("data8")
    @Expose
    public ArrayList<PoolingHistoryData> Data8;

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

        @SerializedName("sender_email")
        @Expose
        public String sender_email;

        @SerializedName("receiver_email")
        @Expose
        public String receiver_email;

        public String getSender_email() {
            return sender_email;
        }

        public void setSender_email(String sender_email) {
            this.sender_email = sender_email;
        }

        public String getReceiver_email() {
            return receiver_email;
        }

        public void setReceiver_email(String receiver_email) {
            this.receiver_email = receiver_email;
        }

        @SerializedName("outgoing_asset_type")
        @Expose
        public String outgoing_asset_type;

        @SerializedName("transaction_fees")
        @Expose
        public String transaction_fees;

        @SerializedName("receiver_address")
        @Expose
        public String receiver_address;

        //.....
        @SerializedName("outgoing_asset_sender")
        @Expose
        public String outgoing_asset_sender;

        @SerializedName("outgoing_asset_receiver")
        @Expose
        public String outgoing_asset_receiver;

        @SerializedName("outgoing_amount")
        @Expose
        public String outgoing_amount;

        @SerializedName("outgoing_admin_status")
        @Expose
        public String outgoing_admin_status;

        //......
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

        @SerializedName("created_at")
        @Expose
        public String created_at;

        @SerializedName("by_admin")
        @Expose
        public String by_admin;

        public String getBy_admin() {
            return by_admin;
        }

        public void setBy_admin(String by_admin) {
            this.by_admin = by_admin;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        @SerializedName("payment_type")
        @Expose
        public String payment_type;

        @SerializedName("payment_status")
        @Expose
        public String payment_status;

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        @SerializedName("payment_through_currency")
        @Expose
        public String payment_through_currency;

        @SerializedName("transaction_for")
        @Expose
        public String transaction_for;

        public String getPayment_through_currency() {
            return payment_through_currency;
        }

        public void setPayment_through_currency(String payment_through_currency) {
            this.payment_through_currency = payment_through_currency;
        }

        public String getOutgoing_asset_type() {
            return outgoing_asset_type;
        }

        public void setOutgoing_asset_type(String outgoing_asset_type) {
            this.outgoing_asset_type = outgoing_asset_type;
        }

        @SerializedName("quantity")
        @Expose
        public String quantity;

        @SerializedName("type")
        @Expose
        public String type;

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        @SerializedName("status")
        @Expose
        public String status;

        @SerializedName("mode")
        @Expose
        public String mode;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        @SerializedName("ddk")
        @Expose
        public String ddk;

        @SerializedName("TotalUSDWithCharge")
        @Expose
        public String TotalUSDWithCharge;

        @SerializedName("php_amount")
        @Expose
        public String php_amount;

        @SerializedName("transaction_type")
        @Expose
        public String transaction_type;

        public String getTransaction_fees() {
            return transaction_fees;
        }

        public void setTransaction_fees(String transaction_fees) {
            this.transaction_fees = transaction_fees;
        }
    }
}
