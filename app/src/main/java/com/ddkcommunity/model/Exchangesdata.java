package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Exchangesdata {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("priceddk")
    @Expose
    private String priceddk;

    @SerializedName("amountsam")
    @Expose
    private String amountsam;

    @SerializedName("totalsamddk")
    @Expose
    private String totalsamddk;

    @SerializedName("amountstatus")
    @Expose
    private String amountstatus;

    public Exchangesdata() {
    }

    public String getId() {
        return id;
    }

    public String getAmountstatus() {
        return amountstatus;
    }

    public void setAmountstatus(String amountstatus) {
        this.amountstatus = amountstatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriceddk() {
        return priceddk;
    }

    public void setPriceddk(String priceddk) {
        this.priceddk = priceddk;
    }

    public String getAmountsam() {
        return amountsam;
    }

    public void setAmountsam(String amountsam) {
        this.amountsam = amountsam;
    }

    public String getTotalsamddk() {
        return totalsamddk;
    }

    public void setTotalsamddk(String totalsamddk) {
        this.totalsamddk = totalsamddk;
    }
}