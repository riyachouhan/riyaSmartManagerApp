package com.ddkcommunity.model.referral;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChainData {
    @SerializedName("unique_code")
    @Expose
    public String unique_code = "";
    @SerializedName("refferallist")
    @Expose
    public List<ReferralLevelList> referralLevelList;
}

