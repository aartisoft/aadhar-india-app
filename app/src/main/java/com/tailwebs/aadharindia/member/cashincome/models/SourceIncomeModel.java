package com.tailwebs.aadharindia.member.cashincome.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SourceIncomeModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("income")
    @Expose
    private String income;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }
}
