package com.tailwebs.aadharindia.member.models;

import android.provider.ContactsContract;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.center.searchinmap.models.CenterModel;
import com.tailwebs.aadharindia.models.ProfileImages;

public class CustomerModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("aadhar_number")
    @Expose
    private String aadhar_number;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("profile_images")
    @Expose
    private ProfileImages profileImages;

    @SerializedName("loan_status")
    @Expose
    private LoanStatusModel loanStatusModel;



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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public LoanStatusModel getLoanStatusModel() {
        return loanStatusModel;
    }

    public void setLoanStatusModel(LoanStatusModel loanStatusModel) {
        this.loanStatusModel = loanStatusModel;
    }
}
