package com.tailwebs.aadharindia.models.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.models.common.CACDCastesModel;
import com.tailwebs.aadharindia.models.common.CACDLoanAmountsModel;
import com.tailwebs.aadharindia.models.common.CACDLoanReasonsModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTenuresModel;
import com.tailwebs.aadharindia.models.common.CACDMaritalStatusModel;
import com.tailwebs.aadharindia.models.common.CACDRationCardTypesModel;
import com.tailwebs.aadharindia.models.common.CACDReligionsModel;
import com.tailwebs.aadharindia.models.common.CACDSecondaryIDModel;

import java.util.ArrayList;

public class CityCenterCommonDataResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("errors")
    @Expose
    private Object errors;

    @SerializedName("notice")
    @Expose
    private String notice;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("genders")
    @Expose
    private  ArrayList<String> genders;

    @SerializedName("city_center_image_count")
    @Expose
    private int city_center_image_count;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getGenders() {
        return genders;
    }

    public void setGenders(ArrayList<String> genders) {
        this.genders = genders;
    }

    public int getCity_center_image_count() {
        return city_center_image_count;
    }

    public void setCity_center_image_count(int city_center_image_count) {
        this.city_center_image_count = city_center_image_count;
    }
}
