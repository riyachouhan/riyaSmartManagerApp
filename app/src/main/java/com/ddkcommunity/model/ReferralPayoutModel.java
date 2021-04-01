package com.ddkcommunity.model;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReferralPayoutModel implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("diff")
    @Expose
    private Integer diff;

    public Integer getDiff() {
        return diff;
    }

    public void setDiff(Integer diff) {
        this.diff = diff;
    }

    public Integer getStatus() {
        return status;
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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }


 public class Datum implements Serializable
{
    @SerializedName("id")
    @Expose
    private Double id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("unique_code")
    @Expose
    private String uniqueCode;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("country_id")
    @Expose
    private Double countryId;
    @SerializedName("phone_code")
    @Expose
    private String phoneCode;
    @SerializedName("mpin")
    @Expose
    private Object mpin;
    @SerializedName("total_ddk_coin")
    @Expose
    private Double totalDdkCoin;
    @SerializedName("avl_ddk_coin")
    @Expose
    private Double avlDdkCoin;
    @SerializedName("referal_code")
    @Expose
    private String referalCode;
    @SerializedName("otp")
    @Expose
    private Double otp;
    @SerializedName("otp_expire_date")
    @Expose
    private String otpExpireDate;
    @SerializedName("signup_otp_verify")
    @Expose
    private String signupOtpVerify;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("fcm_id")
    @Expose
    private String fcmId;
    @SerializedName("designation")
    @Expose
    private Object designation;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("ddkcode")
    @Expose
    private String ddkcode;
    @SerializedName("id_proof_status")
    @Expose
    private String idProofStatus;
    @SerializedName("social_type")
    @Expose
    private String socialType;
    @SerializedName("test_data")
    @Expose
    private String testData;
    @SerializedName("refferal_level_amount")
    @Expose
    private Double refferalLevelAmount;
    @SerializedName("refferal_level_amount_save")
    @Expose
    private Double refferalLevelAmountSave;
    @SerializedName("ddkAddress")
    @Expose
    private List<String> ddkAddress = null;
    @SerializedName("ddkAddressAsc")
    @Expose
    private String ddkAddressAsc;
    @SerializedName("poolingbgColor")
    @Expose
    private String poolingbgColor;
    @SerializedName("poolingColor")
    @Expose
    private String poolingColor;
    @SerializedName("refferallist")
    @Expose
    private List<Object> refferallist = null;
    @SerializedName("total_reward")
    @Expose
    private Double totalReward;
    @SerializedName("total_ddk")
    @Expose
    private Double totalDdk;
    @SerializedName("sam_points")
    @Expose
    private Double samPoints;
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
    private final static long serialVersionUID = 3796729166663690089L;

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getCountryId() {
        return countryId;
    }

    public void setCountryId(Double countryId) {
        this.countryId = countryId;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Object getMpin() {
        return mpin;
    }

    public void setMpin(Object mpin) {
        this.mpin = mpin;
    }

    public Double getTotalDdkCoin() {
        return totalDdkCoin;
    }

    public void setTotalDdkCoin(Double totalDdkCoin) {
        this.totalDdkCoin = totalDdkCoin;
    }

    public Double getAvlDdkCoin() {
        return avlDdkCoin;
    }

    public void setAvlDdkCoin(Double avlDdkCoin) {
        this.avlDdkCoin = avlDdkCoin;
    }

    public String getReferalCode() {
        return referalCode;
    }

    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }

    public Double getOtp() {
        return otp;
    }

    public void setOtp(Double otp) {
        this.otp = otp;
    }

    public String getOtpExpireDate() {
        return otpExpireDate;
    }

    public void setOtpExpireDate(String otpExpireDate) {
        this.otpExpireDate = otpExpireDate;
    }

    public String getSignupOtpVerify() {
        return signupOtpVerify;
    }

    public void setSignupOtpVerify(String signupOtpVerify) {
        this.signupOtpVerify = signupOtpVerify;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getFcmId() {
        return fcmId;
    }

    public void setFcmId(String fcmId) {
        this.fcmId = fcmId;
    }

    public Object getDesignation() {
        return designation;
    }

    public void setDesignation(Object designation) {
        this.designation = designation;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDdkcode() {
        return ddkcode;
    }

    public void setDdkcode(String ddkcode) {
        this.ddkcode = ddkcode;
    }

    public String getIdProofStatus() {
        return idProofStatus;
    }

    public void setIdProofStatus(String idProofStatus) {
        this.idProofStatus = idProofStatus;
    }

    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    public String getTestData() {
        return testData;
    }

    public void setTestData(String testData) {
        this.testData = testData;
    }

    public Double getRefferalLevelAmount() {
        return refferalLevelAmount;
    }

    public void setRefferalLevelAmount(Double refferalLevelAmount) {
        this.refferalLevelAmount = refferalLevelAmount;
    }

    public Double getRefferalLevelAmountSave() {
        return refferalLevelAmountSave;
    }

    public void setRefferalLevelAmountSave(Double refferalLevelAmountSave) {
        this.refferalLevelAmountSave = refferalLevelAmountSave;
    }

    public List<String> getDdkAddress() {
        return ddkAddress;
    }

    public void setDdkAddress(List<String> ddkAddress) {
        this.ddkAddress = ddkAddress;
    }

    public String getDdkAddressAsc() {
        return ddkAddressAsc;
    }

    public void setDdkAddressAsc(String ddkAddressAsc) {
        this.ddkAddressAsc = ddkAddressAsc;
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

    public List<Object> getRefferallist() {
        return refferallist;
    }

    public void setRefferallist(List<Object> refferallist) {
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

    public Double getSamPoints() {
        return samPoints;
    }

    public void setSamPoints(Double samPoints) {
        this.samPoints = samPoints;
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

}