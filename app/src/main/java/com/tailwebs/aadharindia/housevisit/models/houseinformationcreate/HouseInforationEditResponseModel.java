package com.tailwebs.aadharindia.housevisit.models.houseinformationcreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HouseInforationEditResponseModel {

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

    @SerializedName("house_visit")
    @Expose
    private HouseInformationEditModel houseInformationModel;

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

    public HouseInformationEditModel getHouseInformationModel() {
        return houseInformationModel;
    }

    public void setHouseInformationModel(HouseInformationEditModel houseInformationModel) {
        this.houseInformationModel = houseInformationModel;
    }
}
