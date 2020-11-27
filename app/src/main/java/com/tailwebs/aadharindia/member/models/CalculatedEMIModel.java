package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.models.ProfileImages;

public class CalculatedEMIModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("tenure")
    @Expose
    private String tenure;

    @SerializedName("emi_amount")
    @Expose
    private String emi_amount;

    @SerializedName("amount_with_format")
    @Expose
    private String amount_with_format;

    @SerializedName("emi_amount_with_format")
    @Expose
    private String emi_amount_with_format;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTenure() {
        return tenure;
    }

    public void setTenure(String tenure) {
        this.tenure = tenure;
    }

    public String getEmi_amount() {
        return emi_amount;
    }

    public void setEmi_amount(String emi_amount) {
        this.emi_amount = emi_amount;
    }

    public String getAmount_with_format() {
        return amount_with_format;
    }

    public void setAmount_with_format(String amount_with_format) {
        this.amount_with_format = amount_with_format;
    }

    public String getEmi_amount_with_format() {
        return emi_amount_with_format;
    }

    public void setEmi_amount_with_format(String emi_amount_with_format) {
        this.emi_amount_with_format = emi_amount_with_format;
    }
}
