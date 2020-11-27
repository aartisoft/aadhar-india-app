package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApplicantCountModel {

    @SerializedName("completed")
    @Expose
    private String completed;

    @SerializedName("approved")
    @Expose
    private String approved;

    @SerializedName("pre_approved")
    @Expose
    private String pre_approved;

    @SerializedName("post_approved")
    @Expose
    private String post_approved;

    @SerializedName("rejected")
    @Expose
    private String rejected;

    @SerializedName("all_completed")
    @Expose
    private String all_completed;

    @SerializedName("inreview")
    @Expose
    private String inreview;

    @SerializedName("all")
    @Expose
    private String all;

    @SerializedName("reviewed")
    @Expose
    private String reviewed;

    @SerializedName("disbursed")
    @Expose
    private String disbursed;


    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getPre_approved() {
        return pre_approved;
    }

    public void setPre_approved(String pre_approved) {
        this.pre_approved = pre_approved;
    }

    public String getPost_approved() {
        return post_approved;
    }

    public void setPost_approved(String post_approved) {
        this.post_approved = post_approved;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public String getAll_completed() {
        return all_completed;
    }

    public void setAll_completed(String all_completed) {
        this.all_completed = all_completed;
    }

    public String getInreview() {
        return inreview;
    }

    public void setInreview(String inreview) {
        this.inreview = inreview;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getReviewed() {
        return reviewed;
    }

    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }

    public String getDisbursed() {
        return disbursed;
    }

    public void setDisbursed(String disbursed) {
        this.disbursed = disbursed;
    }
}
