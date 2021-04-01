package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionFeeData implements Serializable {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("DDK Address")
    @Expose
    private String ddk_address;

    @SerializedName("eth_address")
    @Expose
    private String eth_address;

    @SerializedName("btc_address")
    @Expose
    private String btc_address;

    @SerializedName("usdt_address")
    @Expose
    private String usdt_address;

    @SerializedName("sam_address")
    @Expose
    private String sam_address;

    //for fees
    @SerializedName("eth_trans_address")
    @Expose
    private String eth_trans_address;

    @SerializedName("sam_trans_address")
    @Expose
    private String sam_trans_address;

    @SerializedName("btc_trans_address")
    @Expose
    private String btc_trans_address;

    @SerializedName("usdt_trans_address")
    @Expose
    private String usdt_trans_address;

    public String getEth_trans_address() {
        return eth_trans_address;
    }

    public void setEth_trans_address(String eth_trans_address) {
        this.eth_trans_address = eth_trans_address;
    }

    public String getSam_trans_address() {
        return sam_trans_address;
    }

    public void setSam_trans_address(String sam_trans_address) {
        this.sam_trans_address = sam_trans_address;
    }

    public String getBtc_trans_address() {
        return btc_trans_address;
    }

    public void setBtc_trans_address(String btc_trans_address) {
        this.btc_trans_address = btc_trans_address;
    }

    public String getUsdt_trans_address() {
        return usdt_trans_address;
    }

    public void setUsdt_trans_address(String usdt_trans_address) {
        this.usdt_trans_address = usdt_trans_address;
    }

    public String getSam_address() {
        return sam_address;
    }

    public void setSam_address(String sam_address) {
        this.sam_address = sam_address;
    }

    public String getEth_address() {
        return eth_address;
    }

    public void setEth_address(String eth_address) {
        this.eth_address = eth_address;
    }

    public String getBtc_address() {
        return btc_address;
    }

    public void setBtc_address(String btc_address) {
        this.btc_address = btc_address;
    }

    public String getUsdt_address() {
        return usdt_address;
    }

    public void setUsdt_address(String usdt_address) {
        this.usdt_address = usdt_address;
    }

    @SerializedName("fees_amount")
    @Expose
    private double feesAmount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted")
    @Expose
    private String deleted;

    public String getDdk_address() {
        return ddk_address;
    }

    public void setDdk_address(String ddk_address) {
        this.ddk_address = ddk_address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getFeesAmount() {
        return feesAmount;
    }

    public void setFeesAmount(double feesAmount) {
        this.feesAmount = feesAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }


}
