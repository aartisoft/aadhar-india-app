package com.tailwebs.aadharindia.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.center.searchinmap.models.CenterModel;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;

import java.util.ArrayList;

public class GroupMemberModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("group_id")
    @Expose
    private String group_id;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("city_center_id")
    @Expose
    private String city_center_id;

    @SerializedName("city_center")
    @Expose
    private CenterModel centerModel;


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

}
