package com.tailwebs.aadharindia.center.searchinmap.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.models.ProfileImages;

public class ContactPersonModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("aadhar_number")
    @Expose
    private String aadhar_number;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone_number")
    @Expose
    private String phone_number;

    @SerializedName("alternate_phone_number")
    @Expose
    private String alternate_phone_number;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("aadhar_co")
    @Expose
    private String aadhar_co;

    @SerializedName("resident_address_id")
    @Expose
    private String resident_address_id;

    @SerializedName("resident_address")
    @Expose
    private ResidentAddressModel residentAddressModel;

    @SerializedName("profile_images")
    @Expose
    private ProfileImages profileImages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAlternate_phone_number() {
        return alternate_phone_number;
    }

    public void setAlternate_phone_number(String alternate_phone_number) {
        this.alternate_phone_number = alternate_phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getResident_address_id() {
        return resident_address_id;
    }

    public void setResident_address_id(String resident_address_id) {
        this.resident_address_id = resident_address_id;
    }

    public ResidentAddressModel getResidentAddressModel() {
        return residentAddressModel;
    }

    public void setResidentAddressModel(ResidentAddressModel residentAddressModel) {
        this.residentAddressModel = residentAddressModel;
    }

    public ProfileImages getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(ProfileImages profileImages) {
        this.profileImages = profileImages;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }
}
