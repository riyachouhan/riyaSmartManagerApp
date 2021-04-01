package com.ddkcommunity.model.exchange;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

public class ExchangeRate {
    @SerializedName("success")
    @Expose
    public boolean status;
    @SerializedName("rates")
    @Expose
    public JSONObject data = new JSONObject();
}