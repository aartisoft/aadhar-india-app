package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;

import java.util.ArrayList;

public class MemberCollectionResponseModel {

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
    private LoanTakerCalculatedEMIModel loanTakerModels;


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

    public LoanTakerCalculatedEMIModel getLoanTakerModels() {
        return loanTakerModels;
    }

    public void setLoanTakerModels(LoanTakerCalculatedEMIModel loanTakerModels) {
        this.loanTakerModels = loanTakerModels;
    }
}
