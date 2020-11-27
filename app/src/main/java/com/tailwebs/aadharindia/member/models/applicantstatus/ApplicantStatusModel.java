package com.tailwebs.aadharindia.member.models.applicantstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicantStatusModel {

    @SerializedName("status")
    @Expose
    private ASStatusModel statusModel;

    @SerializedName("next_action")
    @Expose
    private String next_action;

    public ASStatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(ASStatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public String getNext_action() {
        return next_action;
    }

    public void setNext_action(String next_action) {
        this.next_action = next_action;
    }
}
