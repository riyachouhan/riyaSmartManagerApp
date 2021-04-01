package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReferallayoutNew implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("unique_code")
    @Expose
    private String uniqueCode;
    @SerializedName("poolingbgColor")
    @Expose
    private String poolingbgColor;
    @SerializedName("poolingColor")
    @Expose
    private String poolingColor;
    @SerializedName("refferallist")
    @Expose
    private List<ReferralPayout> refferallist = null;
    @SerializedName("total_reward")
    @Expose
    private Double totalReward;
    @SerializedName("total_ddk")
    @Expose
    private Double totalDdk;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("conversion")
    @Expose
    private Double conversion;
    @SerializedName("transaction")
    @Expose
    private String transaction;
    private final static long serialVersionUID = -525272876860167681L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getPoolingbgColor() {
        return poolingbgColor;
    }

    public void setPoolingbgColor(String poolingbgColor) {
        this.poolingbgColor = poolingbgColor;
    }

    public String getPoolingColor() {
        return poolingColor;
    }

    public void setPoolingColor(String poolingColor) {
        this.poolingColor = poolingColor;
    }

    public List<ReferralPayout> getRefferallist() {
        return refferallist;
    }

    public void setRefferallist(List<ReferralPayout> refferallist) {
        this.refferallist = refferallist;
    }

    public Double getTotalReward() {
        return totalReward;
    }

    public void setTotalReward(Double totalReward) {
        this.totalReward = totalReward;
    }

    public Double getTotalDdk() {
        return totalDdk;
    }

    public void setTotalDdk(Double totalDdk) {
        this.totalDdk = totalDdk;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getConversion() {
        return conversion;
    }

    public void setConversion(Double conversion) {
        this.conversion = conversion;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

}

