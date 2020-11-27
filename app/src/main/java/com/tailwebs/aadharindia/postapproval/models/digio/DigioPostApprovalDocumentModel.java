package com.tailwebs.aadharindia.postapproval.models.digio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DigioPostApprovalDocumentModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("digio_loan_agreement_id")
    @Expose
    private String digio_loan_agreement_id;

    @SerializedName("loan_agreement")
    @Expose
    private String loan_agreement;

    @SerializedName("applicant_num")
    @Expose
    private String applicant_num;

    @SerializedName("co_applicant_num")
    @Expose
    private String co_applicant_num;

    @SerializedName("loan_agreement_generated")
    @Expose
    private boolean loan_agreement_generated;

    @SerializedName("applicant_esigned")
    @Expose
    private boolean applicant_esigned;

    @SerializedName("co_applicant_esigned")
    @Expose
    private boolean co_applicant_esigned;

    @SerializedName("applicant_signed")
    @Expose
    private boolean applicant_signed;

    @SerializedName("co_applicant_signed")
    @Expose
    private boolean co_applicant_signed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDigio_loan_agreement_id() {
        return digio_loan_agreement_id;
    }

    public void setDigio_loan_agreement_id(String digio_loan_agreement_id) {
        this.digio_loan_agreement_id = digio_loan_agreement_id;
    }

    public String getLoan_agreement() {
        return loan_agreement;
    }

    public void setLoan_agreement(String loan_agreement) {
        this.loan_agreement = loan_agreement;
    }

    public boolean isApplicant_esigned() {
        return applicant_esigned;
    }

    public void setApplicant_esigned(boolean applicant_esigned) {
        this.applicant_esigned = applicant_esigned;
    }

    public boolean isCo_applicant_esigned() {
        return co_applicant_esigned;
    }

    public void setCo_applicant_esigned(boolean co_applicant_esigned) {
        this.co_applicant_esigned = co_applicant_esigned;
    }

    public String getApplicant_num() {
        return applicant_num;
    }

    public void setApplicant_num(String applicant_num) {
        this.applicant_num = applicant_num;
    }

    public String getCo_applicant_num() {
        return co_applicant_num;
    }

    public void setCo_applicant_num(String co_applicant_num) {
        this.co_applicant_num = co_applicant_num;
    }

    public boolean isLoan_agreement_generated() {
        return loan_agreement_generated;
    }

    public void setLoan_agreement_generated(boolean loan_agreement_generated) {
        this.loan_agreement_generated = loan_agreement_generated;
    }

    public boolean isApplicant_signed() {
        return applicant_signed;
    }

    public void setApplicant_signed(boolean applicant_signed) {
        this.applicant_signed = applicant_signed;
    }

    public boolean isCo_applicant_signed() {
        return co_applicant_signed;
    }

    public void setCo_applicant_signed(boolean co_applicant_signed) {
        this.co_applicant_signed = co_applicant_signed;
    }
}
