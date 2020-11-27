package com.tailwebs.aadharindia.postapproval.models.digio;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DigioCoApplicantModel {



    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("primary_phone_number")
    @Expose
    private String primary_phone_number;

    @SerializedName("co_applicant_esigned")
    @Expose
    private boolean co_applicant_esigned;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimary_phone_number() {
        return primary_phone_number;
    }

    public void setPrimary_phone_number(String primary_phone_number) {
        this.primary_phone_number = primary_phone_number;
    }

    public boolean isCo_applicant_esigned() {
        return co_applicant_esigned;
    }

    public void setCo_applicant_esigned(boolean co_applicant_esigned) {
        this.co_applicant_esigned = co_applicant_esigned;
    }
}
