package com.ddkcommunity.model.credential;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Credential {

    @SerializedName("credential_id")
    @Expose
    private String credentialId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ddkcode")
    @Expose
    private String ddkcode;
    @SerializedName("passphrase")
    @Expose
    private String passphrase;
    @SerializedName("second_passphrase")
    @Expose
    private String secondPassphrase;
    @SerializedName("referal_link")
    @Expose
    private String referalLink;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("wallet_id")
    @Expose
    private String walletId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("test_data")
    @Expose
    private String testData;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("wallet_type")
    @Expose
    public String wallet_type;

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDdkcode() {
        return ddkcode;
    }

    public void setDdkcode(String ddkcode) {
        this.ddkcode = ddkcode;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getSecondPassphrase() {
        return secondPassphrase;
    }

    public void setSecondPassphrase(String secondPassphrase) {
        this.secondPassphrase = secondPassphrase;
    }

    public String getReferalLink() {
        return referalLink;
    }

    public void setReferalLink(String referalLink) {
        this.referalLink = referalLink;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getTestData() {
        return testData;
    }

    public void setTestData(String testData) {
        this.testData = testData;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
