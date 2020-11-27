package com.tailwebs.aadharindia.postapproval.models.digio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.postapproval.models.postapprovalstatus.IndividualPostApprovalModel;

public class IndividualPostApprovalDigioResponseModel {

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
    private DigioLoanTakerModel loanTakerModel;

    @SerializedName("co_applicant")
    @Expose
    private DigioCoApplicantModel coApplicantModel;

    @SerializedName("post_approval_document")
    @Expose
    private DigioPostApprovalDocumentModel postApprovalDocumentModel;


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

    public DigioLoanTakerModel getLoanTakerModel() {
        return loanTakerModel;
    }

    public void setLoanTakerModel(DigioLoanTakerModel loanTakerModel) {
        this.loanTakerModel = loanTakerModel;
    }

    public DigioCoApplicantModel getCoApplicantModel() {
        return coApplicantModel;
    }

    public void setCoApplicantModel(DigioCoApplicantModel coApplicantModel) {
        this.coApplicantModel = coApplicantModel;
    }

    public DigioPostApprovalDocumentModel getPostApprovalDocumentModel() {
        return postApprovalDocumentModel;
    }

    public void setPostApprovalDocumentModel(DigioPostApprovalDocumentModel postApprovalDocumentModel) {
        this.postApprovalDocumentModel = postApprovalDocumentModel;
    }
}
