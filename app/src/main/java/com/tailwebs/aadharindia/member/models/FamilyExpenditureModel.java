package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FamilyExpenditureModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("rent")
    @Expose
    private String rent;

    @SerializedName("food")
    @Expose
    private String food;

    @SerializedName("education")
    @Expose
    private String education;

    @SerializedName("medical")
    @Expose
    private String medical;

    @SerializedName("travel")
    @Expose
    private String travel;

    @SerializedName("clothing")
    @Expose
    private String clothing;

    @SerializedName("other")
    @Expose
    private String other;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getTravel() {
        return travel;
    }

    public void setTravel(String travel) {
        this.travel = travel;
    }

    public String getClothing() {
        return clothing;
    }

    public void setClothing(String clothing) {
        this.clothing = clothing;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
