package com.tailwebs.aadharindia.housevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSocialParametersModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("is_there")
    @Expose
    private boolean is_there;

    @SerializedName("social_parameter_id")
    @Expose
    private String social_parameter_id;

    @SerializedName("social_parameter")
    @Expose
    private SocialParametersModel socialParametersModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIs_there() {
        return is_there;
    }

    public void setIs_there(boolean is_there) {
        this.is_there = is_there;
    }

    public String getSocial_parameter_id() {
        return social_parameter_id;
    }

    public void setSocial_parameter_id(String social_parameter_id) {
        this.social_parameter_id = social_parameter_id;
    }

    public SocialParametersModel getSocialParametersModel() {
        return socialParametersModel;
    }

    public void setSocialParametersModel(SocialParametersModel socialParametersModel) {
        this.socialParametersModel = socialParametersModel;
    }
}
