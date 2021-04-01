package com.ddkcommunity.model.referral;

import com.ddkcommunity.R;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReferralLevelList
{
    @SerializedName("id")
    @Expose
    public String id = "";

    @SerializedName("email")
    @Expose
    public String email = "";
    @SerializedName("name")
    @Expose
    public String name = "";
    @SerializedName("registration_date")
    @Expose
    public String dateRegister = "";
    @SerializedName("mobile")
    @Expose
    public String mobile = "";
    @SerializedName("amount")
    @Expose
    public double amount = 0.0;
    @SerializedName("level")
    @Expose
    public String level = "";
    @SerializedName("wallets")
    @Expose
    public List<WalletList> wallets;

    @SerializedName("country")
    @Expose
    public CountryName country;

    public class CountryName {
        @SerializedName("sortname")
        @Expose
        public String sortname = "";
        @SerializedName("id")
        @Expose
        public Integer id = 0;
        @SerializedName("country")
        @Expose
        public String country = "";
        @SerializedName("phonecode")
        @Expose
        public String phonecode = "";
    }
}
