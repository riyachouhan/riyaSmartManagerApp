package com.ddkcommunity.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event implements Parcelable {

    public final static Creator<Event> CREATOR = new Creator<Event>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        public Event[] newArray(int size) {
            return (new Event[size]);
        }

    };
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("event_address")
    @Expose
    private String eventAddress;
    @SerializedName("event_start_date")
    @Expose
    private String eventStartDate;
    @SerializedName("event_start_time")
    @Expose
    private String eventStartTime;
    @SerializedName("event_end_date")
    @Expose
    private String eventEndDate;
    @SerializedName("event_end_time")
    @Expose
    private String eventEndTime;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("event_status")
    @Expose
    private String eventStatus;
    @SerializedName("event_url")
    @Expose
    private String eventUrl;
    @SerializedName("event_image")
    @Expose
    private String eventImage;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("created_by")
    @Expose
    private String createdBy;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("updated_by")
    @Expose
    private String updatedBy;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("deleted_by")
    @Expose
    private String deletedBy;

    protected Event(Parcel in) {
        this.eventId = ((String) in.readValue((String.class.getClassLoader())));
        this.eventName = ((String) in.readValue((String.class.getClassLoader())));
        this.eventAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.eventStartDate = ((String) in.readValue((String.class.getClassLoader())));
        this.eventStartTime = ((String) in.readValue((String.class.getClassLoader())));
        this.eventEndDate = ((String) in.readValue((String.class.getClassLoader())));
        this.eventEndTime = ((String) in.readValue((String.class.getClassLoader())));
        this.eventType = ((String) in.readValue((String.class.getClassLoader())));
        this.eventStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.eventUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.eventImage = ((String) in.readValue((String.class.getClassLoader())));
        this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
        this.createdBy = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.updatedBy = ((String) in.readValue((String.class.getClassLoader())));
        this.deleted = ((String) in.readValue((String.class.getClassLoader())));
        this.deletedAt = ((String) in.readValue((String.class.getClassLoader())));
        this.deletedBy = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Event() {
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(String eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(String eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getEventImage() {
        return eventImage;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(eventId);
        dest.writeValue(eventName);
        dest.writeValue(eventAddress);
        dest.writeValue(eventStartDate);
        dest.writeValue(eventStartTime);
        dest.writeValue(eventEndDate);
        dest.writeValue(eventEndTime);
        dest.writeValue(eventType);
        dest.writeValue(eventStatus);
        dest.writeValue(eventUrl);
        dest.writeValue(eventImage);
        dest.writeValue(createdAt);
        dest.writeValue(createdBy);
        dest.writeValue(updatedAt);
        dest.writeValue(updatedBy);
        dest.writeValue(deleted);
        dest.writeValue(deletedAt);
        dest.writeValue(deletedBy);
    }

    public int describeContents() {
        return 0;
    }

}
