package com.tailwebs.aadharindia.housevisit.models.housevisitstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.coapplicant.models.CoApplicantStatusModel;

public class HouseVisitStatusResponseModel {

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

    @SerializedName("loan_taker")
    @Expose
    private HouseVisitStatusModel houseVisitStatusModel;

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

    public HouseVisitStatusModel getHouseVisitStatusModel() {
        return houseVisitStatusModel;
    }

    public void setHouseVisitStatusModel(HouseVisitStatusModel houseVisitStatusModel) {
        this.houseVisitStatusModel = houseVisitStatusModel;
    }
}
