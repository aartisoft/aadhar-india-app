package com.tailwebs.aadharindia.postapproval.models.groupstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GIndividualDocumentsModel {

    @SerializedName("completed")
    @Expose
    private String completed;

    @SerializedName("activated")
    @Expose
    private Boolean activated;


    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
}
