package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShowRequestApiModel implements Serializable
{
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<ShowRequestMidel> data = null;
    private final static long serialVersionUID = -4474516099102627109L;

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

    public List<ShowRequestMidel> getData() {
        return data;
    }

    public void setData(List<ShowRequestMidel> data) {
        this.data = data;
    }

    public class ShowRequestMidel implements Serializable
    {

        @SerializedName("DDKSender")
        @Expose
        private String dDKSender;
        @SerializedName("TotalLendUSD")
        @Expose
        private Double totalLendUSD;
        @SerializedName("DateTimeLend")
        @Expose
        private String dateTimeLend;
        @SerializedName("user_id")
        @Expose
        private Integer userId;

        @SerializedName("lender_id")
        @Expose
        private String lender_id;

        @SerializedName("request_status")
        @Expose
        private String request_status;

        public String getRequest_status() {
            return request_status;
        }

        public void setRequest_status(String request_status) {
            this.request_status = request_status;
        }

        public String getLender_id() {
            return lender_id;
        }

        public void setLender_id(String lender_id) {
            this.lender_id = lender_id;
        }

        public String getDDKSender() {
            return dDKSender;
        }

        public void setDDKSender(String dDKSender) {
            this.dDKSender = dDKSender;
        }

        public Double getTotalLendUSD() {
            return totalLendUSD;
        }

        public void setTotalLendUSD(Double totalLendUSD) {
            this.totalLendUSD = totalLendUSD;
        }

        public String getDateTimeLend() {
            return dateTimeLend;
        }

        public void setDateTimeLend(String dateTimeLend) {
            this.dateTimeLend = dateTimeLend;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

    }

}
