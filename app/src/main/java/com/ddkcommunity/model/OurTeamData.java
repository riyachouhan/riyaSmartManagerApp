package com.ddkcommunity.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class OurTeamData {
    public String description = "";
    public String  drawable;
    public String name = "";
    public String phone = "";
    public String email;
    public String designation = "";
    public String whatsapp_no;
    public String fb_url;
    public String instagram_url;
    public String twitter_url;
    public String youtube_url;

    public OurTeamData(String description, String drawable, String name, String phone, String email, String designation, String whatsapp_no, String fb_url, String instagram_url, String twitter_url, String youtube_url) {
        this.description = description;
        this.drawable = drawable;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.designation = designation;
        this.whatsapp_no = whatsapp_no;
        this.fb_url = fb_url;
        this.instagram_url = instagram_url;
        this.twitter_url = twitter_url;
        this.youtube_url = youtube_url;
    }

    protected OurTeamData(Parcel in) {
        description = in.readString();
        drawable = in.readString();
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        designation = in.readString();
    }

}
