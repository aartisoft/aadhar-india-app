package com.tailwebs.aadharindia.housevisit.models.housepersonalinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.housevisit.models.UpcomingEventsModel;

public class UpcomingResponseModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("is_there")
    @Expose
    private Boolean is_there;

    @SerializedName("upcoming_event_id")
    @Expose
    private int upcoming_event_id;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("upcoming_event")
    @Expose
    private UpcomingEventsModel upcomingEventsModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIs_there() {
        return is_there;
    }

    public void setIs_there(Boolean is_there) {
        this.is_there = is_there;
    }

    public int getUpcoming_event_id() {
        return upcoming_event_id;
    }

    public void setUpcoming_event_id(int upcoming_event_id) {
        this.upcoming_event_id = upcoming_event_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UpcomingEventsModel getUpcomingEventsModel() {
        return upcomingEventsModel;
    }

    public void setUpcomingEventsModel(UpcomingEventsModel upcomingEventsModel) {
        this.upcomingEventsModel = upcomingEventsModel;
    }
}
