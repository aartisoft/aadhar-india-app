package com.tailwebs.aadharindia.postapproval.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.CalculatedEMIModel;
import com.tailwebs.aadharindia.member.models.CustomerModel;
import com.tailwebs.aadharindia.models.ProfileImages;

public class LoanTakerPostApprovalDocumentModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("loan_taker_id")
    @Expose
    private String loan_taker_id;

    @SerializedName("aadhar_number")
    @Expose
    private String aadhar_number;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("group_id")
    @Expose
    private String group_id;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("aadhar_co")
    @Expose
    private String aadhar_co;

    @SerializedName("primary_phone_number")
    @Expose
    private String primary_phone_number;

    @SerializedName("secondary_phone_number")
    @Expose
    private String secondary_phone_number;

    @SerializedName("landline_phone_number")
    @Expose
    private String landline_phone_number;

    @SerializedName("pan_number")
    @Expose
    private String pan_number;

    @SerializedName("voter_id")
    @Expose
    private String voter_id;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("is_married")
    @Expose
    private Boolean is_married;

    @SerializedName("is_fresh_customer")
    @Expose
    private String is_fresh_customer;

    @SerializedName("incorrect_document_count")
    @Expose
    private int incorrect_document_count;

    @SerializedName("display_status")
    @Expose
    private String display_status;

    @SerializedName("loan_cycle")
    @Expose
    private String loan_cycle;

    @SerializedName("profile_images")
    @Expose
    private ProfileImages profileImages;

    @SerializedName("customer")
    @Expose
    private CustomerModel customerModel;

    @SerializedName("post_approval_document")
    @Expose
    private PostApprovalDocumentModel postApprovalDocumentModel;

    @SerializedName("calculated_emi")
    @Expose
    private CalculatedEMIModel calculatedEMIModel;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoan_taker_id() {
        return loan_taker_id;
    }

    public void setLoan_taker_id(String loan_taker_id) {
        this.loan_taker_id = loan_taker_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAadhar_co() {
        return aadhar_co;
    }

    public void setAadhar_co(String aadhar_co) {
        this.aadhar_co = aadhar_co;
    }

    public String getPrimary_phone_number() {
        return primary_phone_number;
    }

    public void setPrimary_phone_number(String primary_phone_number) {
        this.primary_phone_number = primary_phone_number;
    }

    public String getSecondary_phone_number() {
        return secondary_phone_number;
    }

    public void setSecondary_phone_number(String secondary_phone_number) {
        this.secondary_phone_number = secondary_phone_number;
    }

    public String getLandline_phone_number() {
        return landline_phone_number;
    }

    public void setLandline_phone_number(String landline_phone_number) {
        this.landline_phone_number = landline_phone_number;
    }

    public String getPan_number() {
        return pan_number;
    }

    public void setPan_number(String pan_number) {
        this.pan_number = pan_number;
    }

    public String getVoter_id() {
        return voter_id;
    }

    public void setVoter_id(String voter_id) {
        this.voter_id = voter_id;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public PostApprovalDocumentModel getPostApprovalDocumentModel() {
        return postApprovalDocumentModel;
    }

    public void setPostApprovalDocumentModel(PostApprovalDocumentModel postApprovalDocumentModel) {
        this.postApprovalDocumentModel = postApprovalDocumentModel;
    }

    public ProfileImages getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(ProfileImages profileImages) {
        this.profileImages = profileImages;
    }

    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    public CalculatedEMIModel getCalculatedEMIModel() {
        return calculatedEMIModel;
    }

    public void setCalculatedEMIModel(CalculatedEMIModel calculatedEMIModel) {
        this.calculatedEMIModel = calculatedEMIModel;
    }

    public Boolean getIs_married() {
        return is_married;
    }

    public void setIs_married(Boolean is_married) {
        this.is_married = is_married;
    }

    public String getIs_fresh_customer() {
        return is_fresh_customer;
    }

    public void setIs_fresh_customer(String is_fresh_customer) {
        this.is_fresh_customer = is_fresh_customer;
    }

    public String getLoan_cycle() {
        return loan_cycle;
    }

    public void setLoan_cycle(String loan_cycle) {
        this.loan_cycle = loan_cycle;
    }

    public int getIncorrect_document_count() {
        return incorrect_document_count;
    }

    public void setIncorrect_document_count(int incorrect_document_count) {
        this.incorrect_document_count = incorrect_document_count;
    }

    public String getDisplay_status() {
        return display_status;
    }

    public void setDisplay_status(String display_status) {
        this.display_status = display_status;
    }
}
