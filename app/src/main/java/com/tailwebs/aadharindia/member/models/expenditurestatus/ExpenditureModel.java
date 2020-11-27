package com.tailwebs.aadharindia.member.models.expenditurestatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.cashincomestatus.CIStatusModel;

public class ExpenditureModel {

    @SerializedName("status")
    @Expose
    private EStatusModel statusModel;

    @SerializedName("next_action")
    @Expose
    private String next_action;

    public EStatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(EStatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public String getNext_action() {
        return next_action;
    }

    public void setNext_action(String next_action) {
        this.next_action = next_action;
    }
}
