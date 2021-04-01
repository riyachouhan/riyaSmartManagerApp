package com.ddkcommunity.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private User user;

    @SerializedName("btc_details")
    @Expose
    private eth_details btc_details;

    @SerializedName("usdt_details")
    @Expose
    private eth_details usdt_details;

    @SerializedName("sam_koin_details")
    @Expose
    private eth_details sam_koin_details;

    @SerializedName("eth_details")
    @Expose
    private eth_details eth_details;

    @SerializedName("profile_path")
    @Expose
    private String profilePath;

    public Integer getStatus() {
        return status;
    }

    public com.ddkcommunity.model.user.eth_details getSam_koin_details() {
        return sam_koin_details;
    }

    public void setSam_koin_details(com.ddkcommunity.model.user.eth_details sam_koin_details) {
        this.sam_koin_details = sam_koin_details;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public eth_details getEth_details() {
        return eth_details;
    }

    public void setEth_details(eth_details eth_details) {
        this.eth_details = eth_details;
    }

    public eth_details getUsdt_details() {
        return usdt_details;
    }

    public void setUsdt_details(eth_details usdt_details) {
        this.usdt_details = usdt_details;
    }

    public eth_details getBtc_details() {
        return btc_details;
    }

    public void setBtc_details(eth_details btc_details) {
        this.btc_details = btc_details;
    }
}
