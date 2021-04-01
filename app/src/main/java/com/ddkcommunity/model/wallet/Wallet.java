package com.ddkcommunity.model.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wallet {

    @SerializedName("wallet_id")
    @Expose
    private String walletId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("secret")
    @Expose
    private String secret;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("qr_code")
    @Expose
    private String qrCode;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("unconfirmed_balance")
    @Expose
    private String unconfirmedBalance;
    @SerializedName("unconfirmed_signature")
    @Expose
    private String unconfirmedSignature;
    @SerializedName("second_signature")
    @Expose
    private String secondSignature;
    @SerializedName("second_publickey")
    @Expose
    private String secondPublickey;
    @SerializedName("total_frozen_amt")
    @Expose
    private String totalFrozenAmt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("deleted")
    @Expose
    private String deleted;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUnconfirmedBalance() {
        return unconfirmedBalance;
    }

    public void setUnconfirmedBalance(String unconfirmedBalance) {
        this.unconfirmedBalance = unconfirmedBalance;
    }

    public String getUnconfirmedSignature() {
        return unconfirmedSignature;
    }

    public void setUnconfirmedSignature(String unconfirmedSignature) {
        this.unconfirmedSignature = unconfirmedSignature;
    }

    public String getSecondSignature() {
        return secondSignature;
    }

    public void setSecondSignature(String secondSignature) {
        this.secondSignature = secondSignature;
    }

    public String getSecondPublickey() {
        return secondPublickey;
    }

    public void setSecondPublickey(String secondPublickey) {
        this.secondPublickey = secondPublickey;
    }

    public String getTotalFrozenAmt() {
        return totalFrozenAmt;
    }

    public void setTotalFrozenAmt(String totalFrozenAmt) {
        this.totalFrozenAmt = totalFrozenAmt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
