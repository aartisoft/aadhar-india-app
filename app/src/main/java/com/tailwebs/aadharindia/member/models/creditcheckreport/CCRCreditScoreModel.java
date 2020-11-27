package com.tailwebs.aadharindia.member.models.creditcheckreport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CCRCreditScoreModel {

    @SerializedName("score")
    @Expose
    private String score;

    @SerializedName("is_satisfied")
    @Expose
    private Boolean is_satisfied;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Boolean getIs_satisfied() {
        return is_satisfied;
    }

    public void setIs_satisfied(Boolean is_satisfied) {
        this.is_satisfied = is_satisfied;
    }
}
