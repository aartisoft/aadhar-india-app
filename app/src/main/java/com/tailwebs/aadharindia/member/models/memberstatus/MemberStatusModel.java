package com.tailwebs.aadharindia.member.models.memberstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASStatusModel;

public class MemberStatusModel {

    @SerializedName("status")
    @Expose
    private MSStatusModel statusModel;

    @SerializedName("next_action")
    @Expose
    private String next_action;

    public MSStatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(MSStatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public String getNext_action() {
        return next_action;
    }

    public void setNext_action(String next_action) {
        this.next_action = next_action;
    }
}
