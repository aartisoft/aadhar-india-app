package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.center.searchinmap.models.CenterModel;

public class LoanStatusModel {

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("status_type")
    @Expose
    private String status_type;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("customer_status")
    @Expose
    private String customer_status;

   @SerializedName("rejection_reason")
    @Expose
    private String loan_reason;

    @SerializedName("rejection_description")
    @Expose
    private String loan_description;

    @SerializedName("loan_status")
    @Expose
    private String loan_status;

    @SerializedName("outstanding_amount")
    @Expose
    private String outstanding_amount;

    @SerializedName("overdue_amount")
    @Expose
    private String overdue_amount;




    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }

    public String getLoan_reason() {
        return loan_reason;
    }

    public void setLoan_reason(String loan_reason) {
        this.loan_reason = loan_reason;
    }

    public String getLoan_description() {
        return loan_description;
    }

    public void setLoan_description(String loan_description) {
        this.loan_description = loan_description;
    }

    public String getStatus_type() {
        return status_type;
    }

    public void setStatus_type(String status_type) {
        this.status_type = status_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(String loan_status) {
        this.loan_status = loan_status;
    }

    public String getOutstanding_amount() {
        return outstanding_amount;
    }

    public void setOutstanding_amount(String outstanding_amount) {
        this.outstanding_amount = outstanding_amount;
    }

    public String getOverdue_amount() {
        return overdue_amount;
    }

    public void setOverdue_amount(String overdue_amount) {
        this.overdue_amount = overdue_amount;
    }
}
