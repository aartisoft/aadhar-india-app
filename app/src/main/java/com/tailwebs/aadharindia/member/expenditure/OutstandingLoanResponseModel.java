package com.tailwebs.aadharindia.member.expenditure;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.cashincome.models.FamilyModel;

import java.util.ArrayList;

public class OutstandingLoanResponseModel {

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

    @SerializedName("outside_borrowings")
    @Expose
    private ArrayList<OutstandingLoanModel> outstandingLoanModels;

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

    public ArrayList<OutstandingLoanModel> getOutstandingLoanModels() {
        return outstandingLoanModels;
    }

    public void setOutstandingLoanModels(ArrayList<OutstandingLoanModel> outstandingLoanModels) {
        this.outstandingLoanModels = outstandingLoanModels;
    }
}
