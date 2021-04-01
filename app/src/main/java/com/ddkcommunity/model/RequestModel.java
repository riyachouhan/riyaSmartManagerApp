package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RequestModel
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
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("conversion")
        @Expose
        private Double conversion;
        @SerializedName("payment_method_type")
        @Expose
        private String paymentMethodType;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("ddk_address")
        @Expose
        private String ddkAddress;
        @SerializedName("lender_id")
        @Expose
        private Integer lenderId;
        @SerializedName("subscription_amount")
        @Expose
        private Double subscriptionAmount;
        @SerializedName("request_lender_id")
        @Expose
        private Integer requestLenderId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("charge")
        @Expose
        private String charge;
        @SerializedName("sub_total")
        @Expose
        private Double subTotal;
        @SerializedName("ddk_amount")
        @Expose
        private String ddkAmount;
        @SerializedName("subs")
        @Expose
        private List<Sub> subs = null;
        private final static long serialVersionUID = -6024665572365423835L;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Double getConversion() {
            return conversion;
        }

        public void setConversion(Double conversion) {
            this.conversion = conversion;
        }

        public String getPaymentMethodType() {
            return paymentMethodType;
        }

        public void setPaymentMethodType(String paymentMethodType) {
            this.paymentMethodType = paymentMethodType;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getDdkAddress() {
            return ddkAddress;
        }

        public void setDdkAddress(String ddkAddress) {
            this.ddkAddress = ddkAddress;
        }

        public Integer getLenderId() {
            return lenderId;
        }

        public void setLenderId(Integer lenderId) {
            this.lenderId = lenderId;
        }

        public Double getSubscriptionAmount() {
            return subscriptionAmount;
        }

        public void setSubscriptionAmount(Double subscriptionAmount) {
            this.subscriptionAmount = subscriptionAmount;
        }

        public Integer getRequestLenderId() {
            return requestLenderId;
        }

        public void setRequestLenderId(Integer requestLenderId) {
            this.requestLenderId = requestLenderId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCharge() {
            return charge;
        }

        public void setCharge(String charge) {
            this.charge = charge;
        }

        public Double getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(Double subTotal) {
            this.subTotal = subTotal;
        }

        public String getDdkAmount() {
            return ddkAmount;
        }

        public void setDdkAmount(String ddkAmount) {
            this.ddkAmount = ddkAmount;
        }

        public List<Sub> getSubs() {
            return subs;
        }

        public void setSubs(List<Sub> subs) {
            this.subs = subs;
        }

    }

    public class Sub implements Serializable
    {

        @SerializedName("request_lender_id")
        @Expose
        private Integer requestLenderId;
        @SerializedName("lender_id")
        @Expose
        private Integer lenderId;
        @SerializedName("ddk_address")
        @Expose
        private String ddkAddress;
        @SerializedName("subscription_amount")
        @Expose
        private Double subscriptionAmount;
        @SerializedName("request_id")
        @Expose
        private Integer requestId;
        @SerializedName("deleted")
        @Expose
        private String deleted;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private Object updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        private final static long serialVersionUID = -8666683822699059653L;

        public Integer getRequestLenderId() {
            return requestLenderId;
        }

        public void setRequestLenderId(Integer requestLenderId) {
            this.requestLenderId = requestLenderId;
        }

        public Integer getLenderId() {
            return lenderId;
        }

        public void setLenderId(Integer lenderId) {
            this.lenderId = lenderId;
        }

        public String getDdkAddress() {
            return ddkAddress;
        }

        public void setDdkAddress(String ddkAddress) {
            this.ddkAddress = ddkAddress;
        }

        public Double getSubscriptionAmount() {
            return subscriptionAmount;
        }

        public void setSubscriptionAmount(Double subscriptionAmount) {
            this.subscriptionAmount = subscriptionAmount;
        }

        public Integer getRequestId() {
            return requestId;
        }

        public void setRequestId(Integer requestId) {
            this.requestId = requestId;
        }

        public String getDeleted() {
            return deleted;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Object getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Object updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

    }

}
