package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.models.City;

public class PostApprovalDocumentsTaskModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("task_number")
    @Expose
    private String task_number;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("city")
    @Expose
    private City city;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("by")
    @Expose
    private String by;

    @SerializedName("scheduled_on")
    @Expose
    private String scheduled_on;

    @SerializedName("created_on")
    @Expose
    private String created_on;

    @SerializedName("task_type_name")
    @Expose
    private String task_type_name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_number() {
        return task_number;
    }

    public void setTask_number(String task_number) {
        this.task_number = task_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getScheduled_on() {
        return scheduled_on;
    }

    public void setScheduled_on(String scheduled_on) {
        this.scheduled_on = scheduled_on;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getTask_type_name() {
        return task_type_name;
    }

    public void setTask_type_name(String task_type_name) {
        this.task_type_name = task_type_name;
    }
}
