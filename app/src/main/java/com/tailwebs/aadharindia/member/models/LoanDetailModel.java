package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.models.common.CACDLoanReasonsModel;

public class LoanDetailModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("loan_id")
    @Expose
    private String loan_id;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("loan_reason_id")
    @Expose
    private String loan_reason_id;

    @SerializedName("descrption")
    @Expose
    private String descrption;

    @SerializedName("loan_reason_title")
    @Expose
    private String loan_reason_title;

    @SerializedName("loan_reason")
    @Expose
    private CACDLoanReasonsModel loanReasonsModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(String loan_id) {
        this.loan_id = loan_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLoan_reason_id() {
        return loan_reason_id;
    }

    public void setLoan_reason_id(String loan_reason_id) {
        this.loan_reason_id = loan_reason_id;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getLoan_reason_title() {
        return loan_reason_title;
    }

    public void setLoan_reason_title(String loan_reason_title) {
        this.loan_reason_title = loan_reason_title;
    }

    public CACDLoanReasonsModel getLoanReasonsModel() {
        return loanReasonsModel;
    }

    public void setLoanReasonsModel(CACDLoanReasonsModel loanReasonsModel) {
        this.loanReasonsModel = loanReasonsModel;
    }
}
