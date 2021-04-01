package com.ddkcommunity.model.withdrawal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WithdrawalData {
    @SerializedName("receiver_address")
    @Expose
    public String receiverAddress = "";
    @SerializedName("no")
    @Expose
    public String no = "";
    @SerializedName("Transaction_date")
    @Expose
    public String transactionDate = "";
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("amount")
    @Expose
    public double amount;
}