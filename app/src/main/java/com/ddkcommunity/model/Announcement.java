package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Announcement {
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("data")
    @Expose
    public ArrayList<AnnouncementData> announcementData;
    public class AnnouncementData{
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("deleted")
        @Expose
        public String deleted;
        public boolean isChecked;
    }
}
