package com.ddkcommunity.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TutorialResponse implements Parcelable {

    public final static Creator<TutorialResponse> CREATOR = new Creator<TutorialResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public TutorialResponse createFromParcel(Parcel in) {
            return new TutorialResponse(in);
        }

        public TutorialResponse[] newArray(int size) {
            return (new TutorialResponse[size]);
        }

    };
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<Tutorial> tutorial = null;

    protected TutorialResponse(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.tutorial, (Tutorial.class.getClassLoader()));
    }

    public TutorialResponse() {
    }

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

    public List<Tutorial> getTutorial() {
        return tutorial;
    }

    public void setTutorial(List<Tutorial> tutorial) {
        this.tutorial = tutorial;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeList(tutorial);
    }

    public int describeContents() {
        return 0;
    }

}
