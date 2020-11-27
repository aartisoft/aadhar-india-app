package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.center.searchinmap.models.CenterModel;
import com.tailwebs.aadharindia.models.City;

public class GroupModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("group_id")
    @Expose
    private String group_id;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("group_leader_id")
    @Expose
    private String group_leader_id;

    @SerializedName("group_leader_name")
    @Expose
    private String group_leader_name;

    @SerializedName("city_center_id")
    @Expose
    private String city_center_id;

    @SerializedName("city_center")
    @Expose
    private CenterModel centerModel;

    @SerializedName("city")
    @Expose
    private City city;

    @SerializedName("applicant_count")
    @Expose
    private ApplicantCountModel applicantCountModel;

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity_center_id() {
        return city_center_id;
    }

    public void setCity_center_id(String city_center_id) {
        this.city_center_id = city_center_id;
    }

    public CenterModel getCenterModel() {
        return centerModel;
    }

    public void setCenterModel(CenterModel centerModel) {
        this.centerModel = centerModel;
    }

    public ApplicantCountModel getApplicantCountModel() {
        return applicantCountModel;
    }

    public void setApplicantCountModel(ApplicantCountModel applicantCountModel) {
        this.applicantCountModel = applicantCountModel;
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

    public String getGroup_leader_id() {
        return group_leader_id;
    }

    public void setGroup_leader_id(String group_leader_id) {
        this.group_leader_id = group_leader_id;
    }

    public String getGroup_leader_name() {
        return group_leader_name;
    }

    public void setGroup_leader_name(String group_leader_name) {
        this.group_leader_name = group_leader_name;
    }


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
