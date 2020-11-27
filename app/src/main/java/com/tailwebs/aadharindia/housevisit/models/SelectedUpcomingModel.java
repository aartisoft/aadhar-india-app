package com.tailwebs.aadharindia.housevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedUpcomingModel {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("note")
    @Expose
    private String note;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
