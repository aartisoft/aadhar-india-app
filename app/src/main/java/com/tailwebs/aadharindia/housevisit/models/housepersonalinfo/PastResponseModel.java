package com.tailwebs.aadharindia.housevisit.models.housepersonalinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.housevisit.models.PastEventsModel;
import com.tailwebs.aadharindia.housevisit.models.UpcomingEventsModel;

public class PastResponseModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("is_there")
    @Expose
    private Boolean is_there;

    @SerializedName("past_event_id")
    @Expose
    private int past_event_id;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("past_event")
    @Expose
    private PastEventsModel pastEventsModel;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPast_event_id() {
        return past_event_id;
    }

    public void setPast_event_id(int past_event_id) {
        this.past_event_id = past_event_id;
    }

    public PastEventsModel getPastEventsModel() {
        return pastEventsModel;
    }

    public void setPastEventsModel(PastEventsModel pastEventsModel) {
        this.pastEventsModel = pastEventsModel;
    }
}
