package com.ddkcommunity.model.referral;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReferralChain {
    @SerializedName("msg")
    @Expose
    public String msg = "";
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("data")
    @Expose
    public List<ChainData> data;
}

