package com.tailwebs.aadharindia.postapproval.models.digio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.postapproval.models.GroupPostApprovalDocumentModel;
import com.tailwebs.aadharindia.member.models.CalculatedEMIModel;
import com.tailwebs.aadharindia.member.models.CustomerModel;
import com.tailwebs.aadharindia.models.ProfileImages;

public class DigioLoanTakerModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("loan_taker_id")
    @Expose
    private String loan_taker_id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("primary_phone_number")
    @Expose
    private String primary_phone_number;

    @SerializedName("applicant_esigned")
    @Expose
    private boolean applicant_esigned;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoan_taker_id() {
        return loan_taker_id;
    }

    public void setLoan_taker_id(String loan_taker_id) {
        this.loan_taker_id = loan_taker_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimary_phone_number() {
        return primary_phone_number;
    }

    public void setPrimary_phone_number(String primary_phone_number) {
        this.primary_phone_number = primary_phone_number;
    }

    public boolean isApplicant_esigned() {
        return applicant_esigned;
    }

    public void setApplicant_esigned(boolean applicant_esigned) {
        this.applicant_esigned = applicant_esigned;
    }
}
