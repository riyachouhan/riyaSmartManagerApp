package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReferalSubWalletListModel implements Serializable
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

    public Integer getDiff() {
        return diff;
    }

    public void setDiff(Integer diff) {
        this.diff = diff;
    }

    public class Datum implements Serializable
    {

        @SerializedName("walletsAddress")
        @Expose
        private String walletsAddress;
        @SerializedName("payouts")
        @Expose
        private Double payouts;
        @SerializedName("totalLendUSD")
        @Expose
        private Double totalLendUSD;
        private final static long serialVersionUID = 317443793902323325L;

        public String getWalletsAddress() {
            return walletsAddress;
        }

        public void setWalletsAddress(String walletsAddress) {
            this.walletsAddress = walletsAddress;
        }

        public Double getPayouts() {
            return payouts;
        }

        public void setPayouts(Double payouts) {
            this.payouts = payouts;
        }

        public Double getTotalLendUSD() {
            return totalLendUSD;
        }

        public void setTotalLendUSD(Double totalLendUSD) {
            this.totalLendUSD = totalLendUSD;
        }
    }
}
