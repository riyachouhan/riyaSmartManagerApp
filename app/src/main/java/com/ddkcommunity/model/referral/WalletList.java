package com.ddkcommunity.model.referral;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletList
{
    @SerializedName("walletsAddress")
    @Expose
    public String walletsAddress = "";

    @SerializedName("amount")
    @Expose
    public double amount = 0.0;

    @SerializedName("type")
    @Expose
    public String type ="";
}