package com.ddkcommunity.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class eth_details
{
    @SerializedName("wallet_address")
    @Expose
    private String wallet_address;

    @SerializedName("wallet_id")
    @Expose
    private String wallet_id;

    @SerializedName("secret")
    @Expose
    private String secret;

    @SerializedName("public_key")
    @Expose
    private String public_key;

    public String getWallet_address() {
        return wallet_address;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public void setWallet_address(String wallet_address) {
        this.wallet_address = wallet_address;
    }

    public String getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(String wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
