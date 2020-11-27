package com.tailwebs.aadharindia.member.cashincome.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.models.common.CCoARelationModel;
import com.tailwebs.aadharindia.models.common.CIEducationLevelsStatusModel;
import com.tailwebs.aadharindia.models.common.CIEducationStatusModel;
import com.tailwebs.aadharindia.models.common.CIHealthStatusModel;
import com.tailwebs.aadharindia.models.common.CIManagementTypesStatusModel;

public class JobModel {

    @SerializedName("primary_job_id")
    @Expose
    private String primary_job_id;

    @SerializedName("income")
    @Expose
    private String income;

    @SerializedName("cash_flow")
    @Expose
    private String cash_flow;

    @SerializedName("primary_job")
    @Expose
    private PrimaryJobModel primaryJobModel;

    public String getPrimary_job_id() {
        return primary_job_id;
    }

    public void setPrimary_job_id(String primary_job_id) {
        this.primary_job_id = primary_job_id;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getCash_flow() {
        return cash_flow;
    }

    public void setCash_flow(String cash_flow) {
        this.cash_flow = cash_flow;
    }

    public PrimaryJobModel getPrimaryJobModel() {
        return primaryJobModel;
    }

    public void setPrimaryJobModel(PrimaryJobModel primaryJobModel) {
        this.primaryJobModel = primaryJobModel;
    }
}
