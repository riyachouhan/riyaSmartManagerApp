package com.ddkcommunity.model.withdrawal;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Withdrawal {
    @SerializedName("msg")
    @Expose
    public String msg = "";
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("data")
    @Expose
    public List<WithdrawalData> data;
}

