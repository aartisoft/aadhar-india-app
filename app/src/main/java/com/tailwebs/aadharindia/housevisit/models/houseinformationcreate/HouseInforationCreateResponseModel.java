package com.tailwebs.aadharindia.housevisit.models.houseinformationcreate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.housevisit.models.AgriculturalLandsModel;
import com.tailwebs.aadharindia.housevisit.models.FamilyTypesModel;
import com.tailwebs.aadharindia.housevisit.models.HouseTypesModel;
import com.tailwebs.aadharindia.housevisit.models.IllMembersModel;
import com.tailwebs.aadharindia.housevisit.models.KitchenTypesModel;
import com.tailwebs.aadharindia.housevisit.models.MarriedStatusModel;
import com.tailwebs.aadharindia.housevisit.models.NoOfCattlesModel;
import com.tailwebs.aadharindia.housevisit.models.NoOfRoomsModel;
import com.tailwebs.aadharindia.housevisit.models.PastEventsModel;
import com.tailwebs.aadharindia.housevisit.models.RoofTypesModel;
import com.tailwebs.aadharindia.housevisit.models.SocialParametersModel;
import com.tailwebs.aadharindia.housevisit.models.StayLengthsModel;
import com.tailwebs.aadharindia.housevisit.models.ToiletsModel;
import com.tailwebs.aadharindia.housevisit.models.UpcomingEventsModel;
import com.tailwebs.aadharindia.housevisit.models.WallTypesModel;

import java.util.ArrayList;

public class HouseInforationCreateResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("errors")
    @Expose
    private Object errors;

    @SerializedName("notice")
    @Expose
    private String notice;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("house_visit")
    @Expose
    private HouseInformationModel houseInformationModel;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HouseInformationModel getHouseInformationModel() {
        return houseInformationModel;
    }

    public void setHouseInformationModel(HouseInformationModel houseInformationModel) {
        this.houseInformationModel = houseInformationModel;
    }
}
