package com.tailwebs.aadharindia.models.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CashIncomeCommonDataResponseModel {

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

    @SerializedName("healths")
    @Expose
    private ArrayList<CIHealthStatusModel> healthStatusModelArrayList;

    @SerializedName("educations")
    @Expose
    private ArrayList<CIEducationStatusModel> educationStatusModelArrayList;

    @SerializedName("education_levels")
    @Expose
    private ArrayList<CIEducationLevelsStatusModel> educationLevelsStatusModelArrayList;

    @SerializedName("management_types")
    @Expose
    private ArrayList<CIManagementTypesStatusModel> managementTypesStatusModelArrayList;


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

    public ArrayList<CIHealthStatusModel> getHealthStatusModelArrayList() {
        return healthStatusModelArrayList;
    }

    public void setHealthStatusModelArrayList(ArrayList<CIHealthStatusModel> healthStatusModelArrayList) {
        this.healthStatusModelArrayList = healthStatusModelArrayList;
    }

    public ArrayList<CIEducationStatusModel> getEducationStatusModelArrayList() {
        return educationStatusModelArrayList;
    }

    public void setEducationStatusModelArrayList(ArrayList<CIEducationStatusModel> educationStatusModelArrayList) {
        this.educationStatusModelArrayList = educationStatusModelArrayList;
    }

    public ArrayList<CIEducationLevelsStatusModel> getEducationLevelsStatusModelArrayList() {
        return educationLevelsStatusModelArrayList;
    }

    public void setEducationLevelsStatusModelArrayList(ArrayList<CIEducationLevelsStatusModel> educationLevelsStatusModelArrayList) {
        this.educationLevelsStatusModelArrayList = educationLevelsStatusModelArrayList;
    }

    public ArrayList<CIManagementTypesStatusModel> getManagementTypesStatusModelArrayList() {
        return managementTypesStatusModelArrayList;
    }

    public void setManagementTypesStatusModelArrayList(ArrayList<CIManagementTypesStatusModel> managementTypesStatusModelArrayList) {
        this.managementTypesStatusModelArrayList = managementTypesStatusModelArrayList;
    }
}
