package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class versionModel implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private Data data;
    private final static long serialVersionUID = -4078616553802259260L;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable
    {

        @SerializedName("android_version")
        @Expose
        private String androidVersion;
        @SerializedName("android_status")
        @Expose
        private String androidStatus;
        @SerializedName("ios_version")
        @Expose
        private String iosVersion;
        @SerializedName("ios_status")
        @Expose
        private String iosStatus;
        private final static long serialVersionUID = 1947481242255423059L;

        public String getAndroidVersion() {
            return androidVersion;
        }

        public void setAndroidVersion(String androidVersion) {
            this.androidVersion = androidVersion;
        }

        public String getAndroidStatus() {
            return androidStatus;
        }

        public void setAndroidStatus(String androidStatus) {
            this.androidStatus = androidStatus;
        }

        public String getIosVersion() {
            return iosVersion;
        }

        public void setIosVersion(String iosVersion) {
            this.iosVersion = iosVersion;
        }

        public String getIosStatus() {
            return iosStatus;
        }

        public void setIosStatus(String iosStatus) {
            this.iosStatus = iosStatus;
        }

    }
}
