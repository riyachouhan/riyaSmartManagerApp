package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class buyCryptoModel implements Serializable
    {

        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("currency_type_list")
        @Expose
        private List<String> currencyTypeList = null;
        private final static long serialVersionUID = -2777474628790208015L;

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

        public List<String> getCurrencyTypeList() {
            return currencyTypeList;
        }

        public void setCurrencyTypeList(List<String> currencyTypeList) {
            this.currencyTypeList = currencyTypeList;
        }

    }

