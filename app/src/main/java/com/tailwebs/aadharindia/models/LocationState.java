package com.tailwebs.aadharindia.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationState {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("long_name")
    @Expose
    private String long_name;

    @SerializedName("short_name")
    @Expose
    private String short_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }
}
