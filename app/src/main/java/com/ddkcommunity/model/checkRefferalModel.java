package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class checkRefferalModel implements Serializable
    {
        @SerializedName("html_content")
        @Expose
        private String html_content;

        @SerializedName("register_status")
        @Expose
        private String register_status;

        @SerializedName("subscription_status")
        @Expose
        private String subscription_status;

        @SerializedName("status")
        @Expose
        private String  status;

        @SerializedName("token")
        @Expose
        private String token;

        @SerializedName("msg")
        @Expose
        private String msg;

        public String getRegister_status() {
            return register_status;
        }

        public void setRegister_status(String register_status) {
            this.register_status = register_status;
        }

        public String getSubscription_status() {
            return subscription_status;
        }

        public void setSubscription_status(String subscription_status) {
            this.subscription_status = subscription_status;
        }

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

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getHtml_content() {
            return html_content;
        }

        public void setHtml_content(String html_content) {
            this.html_content = html_content;
        }
    }
