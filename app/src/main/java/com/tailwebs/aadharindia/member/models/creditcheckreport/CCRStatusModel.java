package com.tailwebs.aadharindia.member.models.creditcheckreport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CCRStatusModel {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("is_satisfied")
    @Expose
    private Boolean is_satisfied;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIs_satisfied() {
        return is_satisfied;
    }

    public void setIs_satisfied(Boolean is_satisfied) {
        this.is_satisfied = is_satisfied;
    }
}
