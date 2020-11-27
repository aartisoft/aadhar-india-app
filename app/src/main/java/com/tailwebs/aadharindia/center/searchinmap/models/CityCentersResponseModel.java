package com.tailwebs.aadharindia.center.searchinmap.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.center.searchinmap.models.CenterModel;

import java.util.ArrayList;

public class CityCentersResponseModel {

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

    @SerializedName("city_centers")
    @Expose
    private ArrayList<CenterModel> centerModels;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public ArrayList<CenterModel> getCenterModels() {
        return centerModels;
    }

    public void setCenterModels(ArrayList<CenterModel> centerModels) {
        this.centerModels = centerModels;
    }
}
