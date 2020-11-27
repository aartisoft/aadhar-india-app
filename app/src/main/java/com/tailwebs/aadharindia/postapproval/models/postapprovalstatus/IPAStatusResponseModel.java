package com.tailwebs.aadharindia.postapproval.models.postapprovalstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.postapproval.models.groupstatus.GStatusModel;

public class IPAStatusResponseModel {

    @SerializedName("status")
    @Expose
    private IPAStatusModel statusModel;

    @SerializedName("next_action")
    @Expose
    private String next_action;

    public IPAStatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(IPAStatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public String getNext_action() {
        return next_action;
    }

    public void setNext_action(String next_action) {
        this.next_action = next_action;
    }
}
