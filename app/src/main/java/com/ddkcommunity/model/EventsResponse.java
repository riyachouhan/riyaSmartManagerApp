package com.ddkcommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsResponse implements Parcelable {

    public final static Creator<EventsResponse> CREATOR = new Creator<EventsResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public EventsResponse createFromParcel(Parcel in) {
            return new EventsResponse(in);
        }

        public EventsResponse[] newArray(int size) {
            return (new EventsResponse[size]);
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
    private List<Event> events = null;

    protected EventsResponse(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.events, (Event.class.getClassLoader()));
    }

    public EventsResponse() {
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

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeList(events);
    }

    public int describeContents() {
        return 0;
    }
}
