package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.home.models.CollectionModel;
import com.tailwebs.aadharindia.models.City;
import com.tailwebs.aadharindia.models.PDFFiles;
import com.tailwebs.aadharindia.models.ProfileImages;

public class LoanTakerCalculatedEMIModel {

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

    @SerializedName("state")
    @Expose
    private String state;

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
    private Boolean is_fresh_customer;

    @SerializedName("is_group_leader")
    @Expose
    private Boolean is_group_leader;

    @SerializedName("loan_cycle")
    @Expose
    private String loan_cycle;

    @SerializedName("incorrect_document_count")
    @Expose
    private int incorrect_document_count;

    @SerializedName("display_status")
    @Expose
    private String display_status;

    @SerializedName("profile_images")
    @Expose
    private ProfileImages profileImages;

    @SerializedName("signature")
    @Expose
    private PDFFiles signature;

    @SerializedName("city")
    @Expose
    private City city;

    @SerializedName("customer")
    @Expose
    private CustomerModel customerModel;

    @SerializedName("calculated_emi")
    @Expose
    private CalculatedEMIModel calculatedEMIModel;

    @SerializedName("collection")
    @Expose
    private CollectionModel collectionModel;

    public boolean checked;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

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

    public Boolean getIs_fresh_customer() {
        return is_fresh_customer;
    }

    public void setIs_fresh_customer(Boolean is_fresh_customer) {
        this.is_fresh_customer = is_fresh_customer;
    }

    public String getLoan_cycle() {
        return loan_cycle;
    }

    public void setLoan_cycle(String loan_cycle) {
        this.loan_cycle = loan_cycle;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public Boolean getIs_group_leader() {
        return is_group_leader;
    }

    public void setIs_group_leader(Boolean is_group_leader) {
        this.is_group_leader = is_group_leader;
    }

    public CollectionModel getCollectionModel() {
        return collectionModel;
    }

    public void setCollectionModel(CollectionModel collectionModel) {
        this.collectionModel = collectionModel;
    }


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }


    public PDFFiles getSignature() {
        return signature;
    }

    public void setSignature(PDFFiles signature) {
        this.signature = signature;
    }
}
