package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CollectionModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("loan_taker_id")
    @Expose
    private String loan_taker_id;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("total_collection")
    @Expose
    private String total_collection;

    @SerializedName("total_collection_in_format")
    @Expose
    private String total_collection_in_format;

    @SerializedName("amount_collected")
    @Expose
    private String amount_collected;

    @SerializedName("amount_collected_in_format")
    @Expose
    private String amount_collected_in_format;

    @SerializedName("pending_amount")
    @Expose
    private String pending_amount;

    @SerializedName("pending_amount_in_format")
    @Expose
    private String pending_amount_in_format;

    @SerializedName("display_status")
    @Expose
    private String display_status;

    @SerializedName("emi_remaining")
    @Expose
    private String emi_remaining;

    @SerializedName("current_pending_penalties")
    @Expose
    private String current_pending_penalties;

    @SerializedName("current_pending_penalties_in_format")
    @Expose
    private String current_pending_penalties_in_format;


    @SerializedName("pending_installments")
    @Expose
    private ArrayList<PendingInstallmentsModel> pendingInstallmentsModelArrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoan_taker_id() {
        return loan_taker_id;
    }

    public void setLoan_taker_id(String loan_taker_id) {
        this.loan_taker_id = loan_taker_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTotal_collection() {
        return total_collection;
    }

    public void setTotal_collection(String total_collection) {
        this.total_collection = total_collection;
    }

    public String getTotal_collection_in_format() {
        return total_collection_in_format;
    }

    public void setTotal_collection_in_format(String total_collection_in_format) {
        this.total_collection_in_format = total_collection_in_format;
    }

    public String getAmount_collected() {
        return amount_collected;
    }

    public void setAmount_collected(String amount_collected) {
        this.amount_collected = amount_collected;
    }

    public String getAmount_collected_in_format() {
        return amount_collected_in_format;
    }

    public void setAmount_collected_in_format(String amount_collected_in_format) {
        this.amount_collected_in_format = amount_collected_in_format;
    }

    public String getPending_amount() {
        return pending_amount;
    }

    public void setPending_amount(String pending_amount) {
        this.pending_amount = pending_amount;
    }

    public String getPending_amount_in_format() {
        return pending_amount_in_format;
    }

    public void setPending_amount_in_format(String pending_amount_in_format) {
        this.pending_amount_in_format = pending_amount_in_format;
    }

    public String getDisplay_status() {
        return display_status;
    }

    public void setDisplay_status(String display_status) {
        this.display_status = display_status;
    }

    public String getEmi_remaining() {
        return emi_remaining;
    }

    public void setEmi_remaining(String emi_remaining) {
        this.emi_remaining = emi_remaining;
    }

    public String getCurrent_pending_penalties() {
        return current_pending_penalties;
    }

    public void setCurrent_pending_penalties(String current_pending_penalties) {
        this.current_pending_penalties = current_pending_penalties;
    }

    public String getCurrent_pending_penalties_in_format() {
        return current_pending_penalties_in_format;
    }

    public void setCurrent_pending_penalties_in_format(String current_pending_penalties_in_format) {
        this.current_pending_penalties_in_format = current_pending_penalties_in_format;
    }


    public ArrayList<PendingInstallmentsModel> getPendingInstallmentsModelArrayList() {
        return pendingInstallmentsModelArrayList;
    }

    public void setPendingInstallmentsModelArrayList(ArrayList<PendingInstallmentsModel> pendingInstallmentsModelArrayList) {
        this.pendingInstallmentsModelArrayList = pendingInstallmentsModelArrayList;
    }
}
