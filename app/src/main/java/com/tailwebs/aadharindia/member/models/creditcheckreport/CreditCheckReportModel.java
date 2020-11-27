package com.tailwebs.aadharindia.member.models.creditcheckreport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreditCheckReportModel {

    @SerializedName("report")
    @Expose
    private CCRReportModel ccrReportModel;

    @SerializedName("status")
    @Expose
    private CCRStatusModel ccrStatusModel;

    @SerializedName("is_report_generated")
    @Expose
    private boolean is_report_generated;

    @SerializedName("error_code")
    @Expose
    private String error_code;

    @SerializedName("error_message")
    @Expose
    private String error_message;


    public CCRReportModel getCcrReportModel() {
        return ccrReportModel;
    }

    public void setCcrReportModel(CCRReportModel ccrReportModel) {
        this.ccrReportModel = ccrReportModel;
    }

    public CCRStatusModel getCcrStatusModel() {
        return ccrStatusModel;
    }

    public void setCcrStatusModel(CCRStatusModel ccrStatusModel) {
        this.ccrStatusModel = ccrStatusModel;
    }

    public boolean isIs_report_generated() {
        return is_report_generated;
    }

    public void setIs_report_generated(boolean is_report_generated) {
        this.is_report_generated = is_report_generated;
    }


    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
