package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherLoanCardImagesModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("other_loan_card_image")
    @Expose
    private OtherLoanCardImageModel otherLoanCardImageModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OtherLoanCardImageModel getOtherLoanCardImageModel() {
        return otherLoanCardImageModel;
    }

    public void setOtherLoanCardImageModel(OtherLoanCardImageModel otherLoanCardImageModel) {
        this.otherLoanCardImageModel = otherLoanCardImageModel;
    }
}
