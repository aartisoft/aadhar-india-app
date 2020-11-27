package com.tailwebs.aadharindia.member.coapplicant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FamilyModel {

    @SerializedName("father_name")
    @Expose
    private String father_name;

    @SerializedName("mother_name")
    @Expose
    private String mother_name;

    @SerializedName("is_inlaw")
    @Expose
    private Boolean is_inlaw;

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getMother_name() {
        return mother_name;
    }

    public void setMother_name(String mother_name) {
        this.mother_name = mother_name;
    }

    public Boolean getIs_inlaw() {
        return is_inlaw;
    }

    public void setIs_inlaw(Boolean is_inlaw) {
        this.is_inlaw = is_inlaw;
    }
}
