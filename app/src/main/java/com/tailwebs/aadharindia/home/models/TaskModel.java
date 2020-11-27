package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.LoanStatusModel;
import com.tailwebs.aadharindia.models.City;
import com.tailwebs.aadharindia.models.ProfileImages;

public class TaskModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("task_number")
    @Expose
    private String task_number;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("reason")
    @Expose
    private String reason;

    @SerializedName("scheduled_on")
    @Expose
    private String scheduled_on;

    @SerializedName("city_id")
    @Expose
    private String city_id;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("task_type_id")
    @Expose
    private String task_type_id;

    @SerializedName("task_type")
    @Expose
    private TaskTypeModel task_type;

    @SerializedName("city")
    @Expose
    private City city;

    @SerializedName("created_at_in_format")
    @Expose
    private String created_at_in_format;

    @SerializedName("scheduled_on_in_format")
    @Expose
    private String scheduled_on_in_format;


    @SerializedName("group")
    @Expose
    private GroupModel groupModel;




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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getScheduled_on() {
        return scheduled_on;
    }

    public void setScheduled_on(String scheduled_on) {
        this.scheduled_on = scheduled_on;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTask_type_id() {
        return task_type_id;
    }

    public void setTask_type_id(String task_type_id) {
        this.task_type_id = task_type_id;
    }

    public TaskTypeModel getTask_type() {
        return task_type;
    }

    public void setTask_type(TaskTypeModel task_type) {
        this.task_type = task_type;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCreated_at_in_format() {
        return created_at_in_format;
    }

    public void setCreated_at_in_format(String created_at_in_format) {
        this.created_at_in_format = created_at_in_format;
    }

    public String getScheduled_on_in_format() {
        return scheduled_on_in_format;
    }

    public void setScheduled_on_in_format(String scheduled_on_in_format) {
        this.scheduled_on_in_format = scheduled_on_in_format;
    }

    public GroupModel getGroupModel() {
        return groupModel;
    }

    public void setGroupModel(GroupModel groupModel) {
        this.groupModel = groupModel;
    }
}
