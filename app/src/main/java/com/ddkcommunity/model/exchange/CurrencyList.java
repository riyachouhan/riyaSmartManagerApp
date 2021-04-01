package com.ddkcommunity.model.exchange;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrencyList {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public List<CurrencyData> data = null;

    public class CurrencyData {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("currency_name")
        @Expose
        public String currency_name;
        @SerializedName("code")
        @Expose
        public String code;
    }
}