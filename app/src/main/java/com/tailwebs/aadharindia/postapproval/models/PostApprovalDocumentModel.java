package com.tailwebs.aadharindia.postapproval.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.UploadImages;
import com.tailwebs.aadharindia.models.PDFFiles;
import com.tailwebs.aadharindia.models.ProfileImages;

import java.util.ArrayList;

public class PostApprovalDocumentModel {

    @SerializedName("id")
    @Expose
    private String id;

//    @SerializedName("guarantors_joint_liability_agreement_not_available")
//    @Expose
//    private String guarantors_joint_liability_agreement_not_available;

//    @SerializedName("sanction_letter_not_available")
//    @Expose
//    private String sanction_letter_not_available;

    @SerializedName("loan_agreement_not_available")
    @Expose
    private String loan_agreement_not_available;

    @SerializedName("applicant_photo_insurance_form_not_available")
    @Expose
    private String applicant_photo_insurance_form_not_available;

    @SerializedName("applicant_health_declaration_form_not_available")
    @Expose
    private String applicant_health_declaration_form_not_available;

    @SerializedName("coapplicant_photo_insurance_form_not_available")
    @Expose
    private String coapplicant_photo_insurance_form_not_available;

    @SerializedName("coapplicant_health_declaration_form_not_available")
    @Expose
    private String coapplicant_health_declaration_form_not_available;

    @SerializedName("post_approval_other_document_not_available")
    @Expose
    private String post_approval_other_document_not_available;

//    @SerializedName("sanction_letter")
//    @Expose
//    private PDFFiles sanction_letter;

    @SerializedName("loan_agreement")
    @Expose
    private String loan_agreement;

//    @SerializedName("guarantors_joint_liability_agreement")
//    @Expose
//    private PDFFiles guarantors_joint_liability_agreement;

    @SerializedName("signature")
    @Expose
    private UploadImages signature;

    @SerializedName("applicant_photo_insurance_form")
    @Expose
    private UploadImages applicant_photo_insurance_form;

    @SerializedName("applicant_health_declaration_form")
    @Expose
    private UploadImages applicant_health_declaration_form;

    @SerializedName("coapplicant_photo_insurance_form")
    @Expose
    private UploadImages coapplicant_photo_insurance_form;

    @SerializedName("coapplicant_health_declaration_form")
    @Expose
    private UploadImages coapplicant_health_declaration_form;

    @SerializedName("post_approval_other_documents")
    @Expose
    private ArrayList<PostApprovalOtherDocumentsModel> postApprovalOtherDocumentsModelArrayList;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public String getGuarantors_joint_liability_agreement_not_available() {
//        return guarantors_joint_liability_agreement_not_available;
//    }
//
//    public void setGuarantors_joint_liability_agreement_not_available(String guarantors_joint_liability_agreement_not_available) {
//        this.guarantors_joint_liability_agreement_not_available = guarantors_joint_liability_agreement_not_available;
//    }
//
//    public String getSanction_letter_not_available() {
//        return sanction_letter_not_available;
//    }
//
//    public void setSanction_letter_not_available(String sanction_letter_not_available) {
//        this.sanction_letter_not_available = sanction_letter_not_available;
//    }

    public String getLoan_agreement_not_available() {
        return loan_agreement_not_available;
    }

    public void setLoan_agreement_not_available(String loan_agreement_not_available) {
        this.loan_agreement_not_available = loan_agreement_not_available;
    }

    public String getApplicant_photo_insurance_form_not_available() {
        return applicant_photo_insurance_form_not_available;
    }

    public void setApplicant_photo_insurance_form_not_available(String applicant_photo_insurance_form_not_available) {
        this.applicant_photo_insurance_form_not_available = applicant_photo_insurance_form_not_available;
    }

    public String getApplicant_health_declaration_form_not_available() {
        return applicant_health_declaration_form_not_available;
    }

    public void setApplicant_health_declaration_form_not_available(String applicant_health_declaration_form_not_available) {
        this.applicant_health_declaration_form_not_available = applicant_health_declaration_form_not_available;
    }

    public String getCoapplicant_photo_insurance_form_not_available() {
        return coapplicant_photo_insurance_form_not_available;
    }

    public void setCoapplicant_photo_insurance_form_not_available(String coapplicant_photo_insurance_form_not_available) {
        this.coapplicant_photo_insurance_form_not_available = coapplicant_photo_insurance_form_not_available;
    }

    public String getCoapplicant_health_declaration_form_not_available() {
        return coapplicant_health_declaration_form_not_available;
    }

    public void setCoapplicant_health_declaration_form_not_available(String coapplicant_health_declaration_form_not_available) {
        this.coapplicant_health_declaration_form_not_available = coapplicant_health_declaration_form_not_available;
    }

    public String getPost_approval_other_document_not_available() {
        return post_approval_other_document_not_available;
    }

    public void setPost_approval_other_document_not_available(String post_approval_other_document_not_available) {
        this.post_approval_other_document_not_available = post_approval_other_document_not_available;
    }


    public String getLoan_agreement() {
        return loan_agreement;
    }

    public void setLoan_agreement(String loan_agreement) {
        this.loan_agreement = loan_agreement;
    }


    public UploadImages getApplicant_photo_insurance_form() {
        return applicant_photo_insurance_form;
    }

    public void setApplicant_photo_insurance_form(UploadImages applicant_photo_insurance_form) {
        this.applicant_photo_insurance_form = applicant_photo_insurance_form;
    }

    public UploadImages getApplicant_health_declaration_form() {
        return applicant_health_declaration_form;
    }

    public void setApplicant_health_declaration_form(UploadImages applicant_health_declaration_form) {
        this.applicant_health_declaration_form = applicant_health_declaration_form;
    }

    public UploadImages getCoapplicant_photo_insurance_form() {
        return coapplicant_photo_insurance_form;
    }

    public void setCoapplicant_photo_insurance_form(UploadImages coapplicant_photo_insurance_form) {
        this.coapplicant_photo_insurance_form = coapplicant_photo_insurance_form;
    }

    public UploadImages getCoapplicant_health_declaration_form() {
        return coapplicant_health_declaration_form;
    }

    public void setCoapplicant_health_declaration_form(UploadImages coapplicant_health_declaration_form) {
        this.coapplicant_health_declaration_form = coapplicant_health_declaration_form;
    }

    public ArrayList<PostApprovalOtherDocumentsModel> getPostApprovalOtherDocumentsModelArrayList() {
        return postApprovalOtherDocumentsModelArrayList;
    }

    public void setPostApprovalOtherDocumentsModelArrayList(ArrayList<PostApprovalOtherDocumentsModel> postApprovalOtherDocumentsModelArrayList) {
        this.postApprovalOtherDocumentsModelArrayList = postApprovalOtherDocumentsModelArrayList;
    }

    public UploadImages getSignature() {
        return signature;
    }

    public void setSignature(UploadImages signature) {
        this.signature = signature;
    }
}
