package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoggedUser {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public ArrayList<LoggedUserData> user;
    @SerializedName("image_url")
    @Expose
    public String profilePath;
    public class LoggedUserData{
        @SerializedName("user_id")
        @Expose
        public Integer user_id;
        @SerializedName("device_id")
        @Expose
        public String device_id;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("user_image")
        @Expose
        public String user_image;
    }
}
