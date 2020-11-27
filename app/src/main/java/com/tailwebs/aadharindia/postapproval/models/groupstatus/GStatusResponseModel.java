package com.tailwebs.aadharindia.postapproval.models.groupstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HVStatusModel;

public class GStatusResponseModel {

    @SerializedName("status")
    @Expose
    private GStatusModel statusModel;

    @SerializedName("next_action")
    @Expose
    private String next_action;

    public GStatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(GStatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public String getNext_action() {
        return next_action;
    }

    public void setNext_action(String next_action) {
        this.next_action = next_action;
    }
}
