package com.tailwebs.aadharindia.member.cashincome.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PrimaryJobResponseModel {


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


    @SerializedName("primary_jobs")
    @Expose
    private ArrayList<PrimaryJobModel> primaryJobModelArrayList;


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

    public ArrayList<PrimaryJobModel> getPrimaryJobModelArrayList() {
        return primaryJobModelArrayList;
    }

    public void setPrimaryJobModelArrayList(ArrayList<PrimaryJobModel> primaryJobModelArrayList) {
        this.primaryJobModelArrayList = primaryJobModelArrayList;
    }
}
