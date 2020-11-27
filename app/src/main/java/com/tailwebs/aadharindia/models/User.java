
package com.tailwebs.aadharindia.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("userid")
    @Expose
    private String userid;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;

    @SerializedName("alternate_phone_number")
    @Expose
    private String alternatePhoneNumber;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("is_password_changed")
    @Expose
    private Boolean isPasswordChanged;

    @SerializedName("home_address_id")
    @Expose
    private String homeAddressId;


    @SerializedName("home_address")
    @Expose
    private HomeAddress homeAddress;


    @SerializedName("profile_images")
    @Expose
    private PDFFiles pdfFiles;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAlternatePhoneNumber() {
        return alternatePhoneNumber;
    }

    public void setAlternatePhoneNumber(String alternatePhoneNumber) {
        this.alternatePhoneNumber = alternatePhoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getPasswordChanged() {
        return isPasswordChanged;
    }

    public void setPasswordChanged(Boolean passwordChanged) {
        isPasswordChanged = passwordChanged;
    }

    public String getHomeAddressId() {
        return homeAddressId;
    }

    public void setHomeAddressId(String homeAddressId) {
        this.homeAddressId = homeAddressId;
    }

    public HomeAddress getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(HomeAddress homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public PDFFiles getPdfFiles() {
        return pdfFiles;
    }

    public void setPdfFiles(PDFFiles pdfFiles) {
        this.pdfFiles = pdfFiles;
    }
}
