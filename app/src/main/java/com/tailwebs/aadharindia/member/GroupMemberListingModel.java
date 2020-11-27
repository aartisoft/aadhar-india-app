package com.tailwebs.aadharindia.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.center.searchinmap.models.CenterModel;
import com.tailwebs.aadharindia.center.searchinmap.models.CityCenterResponseModel;
import com.tailwebs.aadharindia.member.models.CalculatedEMIModel;
import com.tailwebs.aadharindia.member.models.CustomerModel;
import com.tailwebs.aadharindia.member.models.LoanDetailModel;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.models.ProfileImages;
import com.tailwebs.aadharindia.models.common.CACDCastesModel;
import com.tailwebs.aadharindia.models.common.CACDMaritalStatusModel;
import com.tailwebs.aadharindia.models.common.CACDRationCardTypesModel;
import com.tailwebs.aadharindia.models.common.CACDReligionsModel;

import java.util.ArrayList;

public class GroupMemberListingModel {

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

    @SerializedName("loan_takers")
    @Expose
    private ArrayList<LoanTakerCalculatedEMIModel> loanTakerModels;

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

    public ArrayList<LoanTakerCalculatedEMIModel> getLoanTakerModels() {
        return loanTakerModels;
    }

    public void setLoanTakerModels(ArrayList<LoanTakerCalculatedEMIModel> loanTakerModels) {
        this.loanTakerModels = loanTakerModels;
    }
}
