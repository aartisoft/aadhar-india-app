package com.tailwebs.aadharindia.member.models.cashincomestatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASStatusModel;

public class CashIncomeModel {

    @SerializedName("status")
    @Expose
    private CIStatusModel statusModel;

    @SerializedName("next_action")
    @Expose
    private String next_action;

    public CIStatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(CIStatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public String getNext_action() {
        return next_action;
    }

    public void setNext_action(String next_action) {
        this.next_action = next_action;
    }
}
