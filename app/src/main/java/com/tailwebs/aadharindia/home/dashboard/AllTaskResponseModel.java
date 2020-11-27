package com.tailwebs.aadharindia.home.dashboard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.home.models.TaskModel;

import java.util.ArrayList;

public class AllTaskResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("errors")
    @Expose
    private Object errors;

    @SerializedName("notice")
    @Expose
    private String notice;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("tasks")
    @Expose
    private ArrayList<TaskModel> taskModel;


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<TaskModel> getTaskModel() {
        return taskModel;
    }

    public void setTaskModel(ArrayList<TaskModel> taskModel) {
        this.taskModel = taskModel;
    }
}
