package com.ddkcommunity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SliderImgResponse implements Serializable
{
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("data")
    @Expose
    public ArrayList<SliderImg> lendData;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<SliderImg> getLendData() {
        return lendData;
    }

    public void setLendData(ArrayList<SliderImg> lendData) {
        this.lendData = lendData;
    }
}
