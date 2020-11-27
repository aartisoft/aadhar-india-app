package com.tailwebs.aadharindia.housevisit.models.housepersonalinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.housevisit.models.houseinformationcreate.HouseInformationModel;

public class PersonalInforationCreateResponseModel {

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
    private PersonalInformationModel personalInformationModel;

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

    public PersonalInformationModel getPersonalInformationModel() {
        return personalInformationModel;
    }

    public void setPersonalInformationModel(PersonalInformationModel personalInformationModel) {
        this.personalInformationModel = personalInformationModel;
    }
}
