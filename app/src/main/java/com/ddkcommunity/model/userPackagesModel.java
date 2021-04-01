package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class userPackagesModel implements Serializable
{

    @SerializedName("status")
    @Expose
    private String  status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("extra_data")
    @Expose
    private List<ExtraDatum> extraData = null;
    private final static long serialVersionUID = 7573741107853880128L;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ExtraDatum> getExtraData() {
        return extraData;
    }

    public void setExtraData(List<ExtraDatum> extraData) {
        this.extraData = extraData;
    }

    public class ExtraDatum implements Serializable
    {

        @SerializedName("id")
        @Expose
        private String  id;
        @SerializedName("user_id")
        @Expose
        private String  userId;
        @SerializedName("plan_id")
        @Expose
        private String  planId;
        @SerializedName("pack_amt")
        @Expose
        private String  packAmt;
        @SerializedName("package_status")
        @Expose
        private String packageStatus;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("is_active")
        @Expose
        private String isActive;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        private final static long serialVersionUID = 2361385156606653143L;

        public String getPackageStatus() {
            return packageStatus;
        }

        public void setPackageStatus(String packageStatus) {
            this.packageStatus = packageStatus;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getPackAmt() {
            return packAmt;
        }

        public void setPackAmt(String packAmt) {
            this.packAmt = packAmt;
        }

        public String getIsActive() {
            return isActive;
        }

        public void setIsActive(String isActive) {
            this.isActive = isActive;
        }
    }
}
