package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateRequestModel
{
    @SerializedName("sender_address")
    @Expose
    public String sender_address;

    @SerializedName("amount")
    @Expose
    public String amount;

    @SerializedName("Transaction_date")
    @Expose
    public String Transaction_date;

    @SerializedName("Selectedstatus")
    @Expose
    public String Selectedstatus;

    @SerializedName("id")
    @Expose
    public String id;

    public String getSender_address() {
        return sender_address;
    }

    public void setSender_address(String sender_address) {
        this.sender_address = sender_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_date() {
        return Transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        Transaction_date = transaction_date;
    }

    public String getSelectedstatus() {
        return Selectedstatus;
    }

    public void setSelectedstatus(String selectedstatus) {
        Selectedstatus = selectedstatus;
    }
}
