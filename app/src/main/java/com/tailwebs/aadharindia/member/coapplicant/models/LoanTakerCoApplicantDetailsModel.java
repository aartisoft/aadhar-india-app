package com.tailwebs.aadharindia.member.coapplicant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.center.searchinmap.models.ResidentAddressModel;
import com.tailwebs.aadharindia.member.models.CalculatedEMIModel;
import com.tailwebs.aadharindia.member.models.CustomerModel;
import com.tailwebs.aadharindia.models.HomeAddress;
import com.tailwebs.aadharindia.models.ProfileImages;
import com.tailwebs.aadharindia.models.common.CCoARelationModel;

public class LoanTakerCoApplicantDetailsModel {

    @SerializedName("relation_id")
    @Expose
    private String relation_id;

    @SerializedName("relation_name")
    @Expose
    private String relation_name;

    @SerializedName("aadhar_number")
    @Expose
    private String aadhar_number;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("aadhar_co")
    @Expose
    private String aadhar_co;

    @SerializedName("aadhar_co_relation_id")
    @Expose
    private String aadhar_co_relation_id;

    @SerializedName("primary_phone_number")
    @Expose
    private String primary_phone_number;

    @SerializedName("secondary_phone_number")
    @Expose
    private String secondary_phone_number;

    @SerializedName("is_residing_address")
    @Expose
    private Boolean is_residing_address;

    @SerializedName("resident_address_id")
    @Expose
    private String resident_address_id;

    @SerializedName("aadhar_co_relation_name")
    @Expose
    private String aadhar_co_relation_name;

    @SerializedName("aadhar_address_id")
    @Expose
    private String aadhar_address_id;

    @SerializedName("profile_images")
    @Expose
    private ProfileImages profileImages;

    @SerializedName("aadhar_co_relation")
    @Expose
    private CCoARelationModel aadharCoRelation;

    @SerializedName("aadhar_address")
    @Expose
    private HomeAddress aadharAdrress;

    @SerializedName("resident_address")
    @Expose
    private ResidentAddressModel residentAddressModel;

    @SerializedName("relation")
    @Expose
    private CCoARelationModel relationModel;


    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
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

    public String getAadhar_co_relation_id() {
        return aadhar_co_relation_id;
    }

    public void setAadhar_co_relation_id(String aadhar_co_relation_id) {
        this.aadhar_co_relation_id = aadhar_co_relation_id;
    }

    public Boolean getIs_residing_address() {
        return is_residing_address;
    }

    public void setIs_residing_address(Boolean is_residing_address) {
        this.is_residing_address = is_residing_address;
    }

    public String getResident_address_id() {
        return resident_address_id;
    }

    public void setResident_address_id(String resident_address_id) {
        this.resident_address_id = resident_address_id;
    }

    public String getAadhar_co_relation_name() {
        return aadhar_co_relation_name;
    }

    public void setAadhar_co_relation_name(String aadhar_co_relation_name) {
        this.aadhar_co_relation_name = aadhar_co_relation_name;
    }

    public String getAadhar_address_id() {
        return aadhar_address_id;
    }

    public void setAadhar_address_id(String aadhar_address_id) {
        this.aadhar_address_id = aadhar_address_id;
    }

    public ProfileImages getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(ProfileImages profileImages) {
        this.profileImages = profileImages;
    }

    public CCoARelationModel getAadharCoRelation() {
        return aadharCoRelation;
    }

    public void setAadharCoRelation(CCoARelationModel aadharCoRelation) {
        this.aadharCoRelation = aadharCoRelation;
    }

    public HomeAddress getAadharAdrress() {
        return aadharAdrress;
    }

    public void setAadharAdrress(HomeAddress aadharAdrress) {
        this.aadharAdrress = aadharAdrress;
    }

    public ResidentAddressModel getResidentAddressModel() {
        return residentAddressModel;
    }

    public void setResidentAddressModel(ResidentAddressModel residentAddressModel) {
        this.residentAddressModel = residentAddressModel;
    }

    public CCoARelationModel getRelationModel() {
        return relationModel;
    }

    public void setRelationModel(CCoARelationModel relationModel) {
        this.relationModel = relationModel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
}
