package com.tailwebs.aadharindia.housevisit.models.housevisitstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.coapplicant.models.CoAStatusModel;

public class HVStatusResponseModel {

    @SerializedName("status")
    @Expose
    private HVStatusModel statusModel;

    @SerializedName("next_action")
    @Expose
    private String next_action;

    public HVStatusModel getStatusModel() {
        return statusModel;
    }

    public void setStatusModel(HVStatusModel statusModel) {
        this.statusModel = statusModel;
    }

    public String getNext_action() {
        return next_action;
    }

    public void setNext_action(String next_action) {
        this.next_action = next_action;
    }
}
