package com.tailwebs.aadharindia.housevisit.models.housepersonalinfo;

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
import com.tailwebs.aadharindia.housevisit.models.RoofTypesModel;
import com.tailwebs.aadharindia.housevisit.models.SocialParametersModel;
import com.tailwebs.aadharindia.housevisit.models.StayLengthsModel;
import com.tailwebs.aadharindia.housevisit.models.ToiletsModel;
import com.tailwebs.aadharindia.housevisit.models.WallTypesModel;

import java.util.ArrayList;

public class PersonalInformationModel {



    @SerializedName("house_visit_upcoming_events")
    @Expose
    private ArrayList<UpcomingResponseModel> upcomingResponseModelArrayList;

    @SerializedName("house_visit_past_events")
    @Expose
    private ArrayList<PastResponseModel> pastResponseModelArrayList;


    public ArrayList<UpcomingResponseModel> getUpcomingResponseModelArrayList() {
        return upcomingResponseModelArrayList;
    }

    public void setUpcomingResponseModelArrayList(ArrayList<UpcomingResponseModel> upcomingResponseModelArrayList) {
        this.upcomingResponseModelArrayList = upcomingResponseModelArrayList;
    }

    public ArrayList<PastResponseModel> getPastResponseModelArrayList() {
        return pastResponseModelArrayList;
    }

    public void setPastResponseModelArrayList(ArrayList<PastResponseModel> pastResponseModelArrayList) {
        this.pastResponseModelArrayList = pastResponseModelArrayList;
    }
}
