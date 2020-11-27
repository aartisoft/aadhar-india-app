package com.tailwebs.aadharindia.postapproval.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.postapproval.models.PostApprovalOtherDocumentsModel;
import com.tailwebs.aadharindia.member.models.UploadImages;
import com.tailwebs.aadharindia.models.ProfileImages;

import java.util.ArrayList;

public class ApprovedLoanDetailModel {

    @SerializedName("rate_of_interest")
    @Expose
    private String rate_of_interest;

    @SerializedName("tenure")
    @Expose
    private String tenure;

    @SerializedName("gst")
    @Expose
    private String gst;

    @SerializedName("approved_amount_in_format")
    @Expose
    private String approved_amount_in_format;

    @SerializedName("admin_charge_in_format")
    @Expose
    private String admin_charge_in_format;

    @SerializedName("admin_charge_gst_in_format")
    @Expose
    private String admin_charge_gst_in_format;

    @SerializedName("final_admin_charge_in_format")
    @Expose
    private String final_admin_charge_in_format;

    @SerializedName("processing_fee_in_format")
    @Expose
    private String processing_fee_in_format;

    @SerializedName("processing_fee_gst_in_format")
    @Expose
    private String processing_fee_gst_in_format;

    @SerializedName("processing_fee_charge")
    @Expose
    private String processing_fee_charge;

    @SerializedName("processing_fee_without_gst_in_format")
    @Expose
    private String processing_fee_without_gst_in_format;

    @SerializedName("final_processing_fee_in_format")
    @Expose
    private String final_processing_fee_in_format;

    @SerializedName("applicant_insurance_amount_in_format")
    @Expose
    private String applicant_insurance_amount_in_format;

    @SerializedName("co_aplicant_insurance_amount_in_format")
    @Expose
    private String co_aplicant_insurance_amount_in_format;

    @SerializedName("insurance_amount_in_format")
    @Expose
    private String insurance_amount_in_format;

    @SerializedName("emi_amount_in_format")
    @Expose
    private String emi_amount_in_format;

    @SerializedName("final_disbursal_amount_in_format")
    @Expose
    private String final_disbursal_amount_in_format;

    @SerializedName("emi_collection_date")
    @Expose
    private String emi_collection_date;

    @SerializedName("first_emi_amount")
    @Expose
    private String first_emi_amount;

    @SerializedName("total_loan_interest")
    @Expose
    private String total_loan_interest;


    public String getRate_of_interest() {
        return rate_of_interest;
    }

    public void setRate_of_interest(String rate_of_interest) {
        this.rate_of_interest = rate_of_interest;
    }

    public String getAdmin_charge_in_format() {
        return admin_charge_in_format;
    }

    public void setAdmin_charge_in_format(String admin_charge_in_format) {
        this.admin_charge_in_format = admin_charge_in_format;
    }

    public String getAdmin_charge_gst_in_format() {
        return admin_charge_gst_in_format;
    }

    public void setAdmin_charge_gst_in_format(String admin_charge_gst_in_format) {
        this.admin_charge_gst_in_format = admin_charge_gst_in_format;
    }

    public String getFinal_admin_charge_in_format() {
        return final_admin_charge_in_format;
    }

    public void setFinal_admin_charge_in_format(String final_admin_charge_in_format) {
        this.final_admin_charge_in_format = final_admin_charge_in_format;
    }

    public String getProcessing_fee_in_format() {
        return processing_fee_in_format;
    }

    public void setProcessing_fee_in_format(String processing_fee_in_format) {
        this.processing_fee_in_format = processing_fee_in_format;
    }

    public String getProcessing_fee_gst_in_format() {
        return processing_fee_gst_in_format;
    }

    public void setProcessing_fee_gst_in_format(String processing_fee_gst_in_format) {
        this.processing_fee_gst_in_format = processing_fee_gst_in_format;
    }

    public String getFinal_processing_fee_in_format() {
        return final_processing_fee_in_format;
    }

    public void setFinal_processing_fee_in_format(String final_processing_fee_in_format) {
        this.final_processing_fee_in_format = final_processing_fee_in_format;
    }

    public String getApplicant_insurance_amount_in_format() {
        return applicant_insurance_amount_in_format;
    }

    public void setApplicant_insurance_amount_in_format(String applicant_insurance_amount_in_format) {
        this.applicant_insurance_amount_in_format = applicant_insurance_amount_in_format;
    }

    public String getCo_aplicant_insurance_amount_in_format() {
        return co_aplicant_insurance_amount_in_format;
    }

    public void setCo_aplicant_insurance_amount_in_format(String co_aplicant_insurance_amount_in_format) {
        this.co_aplicant_insurance_amount_in_format = co_aplicant_insurance_amount_in_format;
    }

    public String getInsurance_amount_in_format() {
        return insurance_amount_in_format;
    }

    public void setInsurance_amount_in_format(String insurance_amount_in_format) {
        this.insurance_amount_in_format = insurance_amount_in_format;
    }

    public String getEmi_amount_in_format() {
        return emi_amount_in_format;
    }

    public void setEmi_amount_in_format(String emi_amount_in_format) {
        this.emi_amount_in_format = emi_amount_in_format;
    }

    public String getFinal_disbursal_amount_in_format() {
        return final_disbursal_amount_in_format;
    }

    public void setFinal_disbursal_amount_in_format(String final_disbursal_amount_in_format) {
        this.final_disbursal_amount_in_format = final_disbursal_amount_in_format;
    }


    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getApproved_amount_in_format() {
        return approved_amount_in_format;
    }

    public void setApproved_amount_in_format(String approved_amount_in_format) {
        this.approved_amount_in_format = approved_amount_in_format;
    }

    public String getProcessing_fee_charge() {
        return processing_fee_charge;
    }

    public void setProcessing_fee_charge(String processing_fee_charge) {
        this.processing_fee_charge = processing_fee_charge;
    }

    public String getProcessing_fee_without_gst_in_format() {
        return processing_fee_without_gst_in_format;
    }

    public void setProcessing_fee_without_gst_in_format(String processing_fee_without_gst_in_format) {
        this.processing_fee_without_gst_in_format = processing_fee_without_gst_in_format;
    }

    public String getEmi_collection_date() {
        return emi_collection_date;
    }

    public void setEmi_collection_date(String emi_collection_date) {
        this.emi_collection_date = emi_collection_date;
    }

    public String getFirst_emi_amount() {
        return first_emi_amount;
    }

    public void setFirst_emi_amount(String first_emi_amount) {
        this.first_emi_amount = first_emi_amount;
    }

    public String getTotal_loan_interest() {
        return total_loan_interest;
    }

    public void setTotal_loan_interest(String total_loan_interest) {
        this.total_loan_interest = total_loan_interest;
    }
}
