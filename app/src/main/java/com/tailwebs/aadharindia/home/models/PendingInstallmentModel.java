package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingInstallmentModel {

    @SerializedName("payment")
    @Expose
    private String payment;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("installment_date")
    @Expose
    private String installment_date;

    @SerializedName("payment_in_format")
    @Expose
    private String payment_in_format;

    @SerializedName("current_pending_payment")
    @Expose
    private String current_pending_payment;

    @SerializedName("current_pending_payment_in_format")
    @Expose
    private String current_pending_payment_in_format;

    @SerializedName("emi_for")
    @Expose
    private String emi_for;

    @SerializedName("emi_detail")
    @Expose
    private String emi_detail;


    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getInstallment_date() {
        return installment_date;
    }

    public void setInstallment_date(String installment_date) {
        this.installment_date = installment_date;
    }

    public String getPayment_in_format() {
        return payment_in_format;
    }

    public void setPayment_in_format(String payment_in_format) {
        this.payment_in_format = payment_in_format;
    }

    public String getCurrent_pending_payment() {
        return current_pending_payment;
    }

    public void setCurrent_pending_payment(String current_pending_payment) {
        this.current_pending_payment = current_pending_payment;
    }

    public String getCurrent_pending_payment_in_format() {
        return current_pending_payment_in_format;
    }

    public void setCurrent_pending_payment_in_format(String current_pending_payment_in_format) {
        this.current_pending_payment_in_format = current_pending_payment_in_format;
    }

    public String getEmi_for() {
        return emi_for;
    }

    public void setEmi_for(String emi_for) {
        this.emi_for = emi_for;
    }

    public String getEmi_detail() {
        return emi_detail;
    }

    public void setEmi_detail(String emi_detail) {
        this.emi_detail = emi_detail;
    }
}
