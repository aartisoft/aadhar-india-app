package com.tailwebs.aadharindia.housevisit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.memberstatus.LoanTakerMemberStatusModel;

import java.util.ArrayList;

public class HouseVisitCommonDataResponseModel {

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

    @SerializedName("house_types")
    @Expose
    private ArrayList<HouseTypesModel> houseTypesModels;

    @SerializedName("roof_types")
    @Expose
    private ArrayList<RoofTypesModel> roofTypesModels;

    @SerializedName("wall_types")
    @Expose
    private ArrayList<WallTypesModel> wallTypesModels;

    @SerializedName("kitchen_types")
    @Expose
    private ArrayList<KitchenTypesModel> kitchenTypesModels;

    @SerializedName("toilets")
    @Expose
    private ArrayList<ToiletsModel> toiletsModels;

    @SerializedName("num_of_rooms")
    @Expose
    private ArrayList<NoOfRoomsModel> noOfRoomsModels;

    @SerializedName("stay_lengths")
    @Expose
    private ArrayList<StayLengthsModel> stayLengthsModels;

    @SerializedName("married_sinces")
    @Expose
    private ArrayList<MarriedStatusModel> marriedStatusModels;

    @SerializedName("agricultural_lands")
    @Expose
    private ArrayList<AgriculturalLandsModel> agriculturalLandsModels;

    @SerializedName("num_of_cattles")
    @Expose
    private ArrayList<NoOfCattlesModel> noOfCattlesModels;

    @SerializedName("social_parameters")
    @Expose
    private ArrayList<SocialParametersModel> socialParametersModels;

    @SerializedName("family_types")
    @Expose
    private ArrayList<FamilyTypesModel> familyTypesModels;

    @SerializedName("ill_members")
    @Expose
    private ArrayList<IllMembersModel> illMembersModels;

    @SerializedName("past_events")
    @Expose
    private ArrayList<PastEventsModel> pastEventsModels;

    @SerializedName("upcoming_events")
    @Expose
    private ArrayList<UpcomingEventsModel> upcomingEventsModels;

    @SerializedName("house_image_count")
    @Expose
    private int house_image_count;


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

    public ArrayList<HouseTypesModel> getHouseTypesModels() {
        return houseTypesModels;
    }

    public void setHouseTypesModels(ArrayList<HouseTypesModel> houseTypesModels) {
        this.houseTypesModels = houseTypesModels;
    }

    public ArrayList<RoofTypesModel> getRoofTypesModels() {
        return roofTypesModels;
    }

    public void setRoofTypesModels(ArrayList<RoofTypesModel> roofTypesModels) {
        this.roofTypesModels = roofTypesModels;
    }

    public ArrayList<WallTypesModel> getWallTypesModels() {
        return wallTypesModels;
    }

    public void setWallTypesModels(ArrayList<WallTypesModel> wallTypesModels) {
        this.wallTypesModels = wallTypesModels;
    }

    public ArrayList<KitchenTypesModel> getKitchenTypesModels() {
        return kitchenTypesModels;
    }

    public void setKitchenTypesModels(ArrayList<KitchenTypesModel> kitchenTypesModels) {
        this.kitchenTypesModels = kitchenTypesModels;
    }

    public ArrayList<ToiletsModel> getToiletsModels() {
        return toiletsModels;
    }

    public void setToiletsModels(ArrayList<ToiletsModel> toiletsModels) {
        this.toiletsModels = toiletsModels;
    }

    public ArrayList<NoOfRoomsModel> getNoOfRoomsModels() {
        return noOfRoomsModels;
    }

    public void setNoOfRoomsModels(ArrayList<NoOfRoomsModel> noOfRoomsModels) {
        this.noOfRoomsModels = noOfRoomsModels;
    }

    public ArrayList<StayLengthsModel> getStayLengthsModels() {
        return stayLengthsModels;
    }

    public void setStayLengthsModels(ArrayList<StayLengthsModel> stayLengthsModels) {
        this.stayLengthsModels = stayLengthsModels;
    }

    public ArrayList<MarriedStatusModel> getMarriedStatusModels() {
        return marriedStatusModels;
    }

    public void setMarriedStatusModels(ArrayList<MarriedStatusModel> marriedStatusModels) {
        this.marriedStatusModels = marriedStatusModels;
    }

    public ArrayList<AgriculturalLandsModel> getAgriculturalLandsModels() {
        return agriculturalLandsModels;
    }

    public void setAgriculturalLandsModels(ArrayList<AgriculturalLandsModel> agriculturalLandsModels) {
        this.agriculturalLandsModels = agriculturalLandsModels;
    }

    public ArrayList<NoOfCattlesModel> getNoOfCattlesModels() {
        return noOfCattlesModels;
    }

    public void setNoOfCattlesModels(ArrayList<NoOfCattlesModel> noOfCattlesModels) {
        this.noOfCattlesModels = noOfCattlesModels;
    }

    public ArrayList<SocialParametersModel> getSocialParametersModels() {
        return socialParametersModels;
    }

    public void setSocialParametersModels(ArrayList<SocialParametersModel> socialParametersModels) {
        this.socialParametersModels = socialParametersModels;
    }

    public ArrayList<FamilyTypesModel> getFamilyTypesModels() {
        return familyTypesModels;
    }

    public void setFamilyTypesModels(ArrayList<FamilyTypesModel> familyTypesModels) {
        this.familyTypesModels = familyTypesModels;
    }

    public ArrayList<IllMembersModel> getIllMembersModels() {
        return illMembersModels;
    }

    public void setIllMembersModels(ArrayList<IllMembersModel> illMembersModels) {
        this.illMembersModels = illMembersModels;
    }

    public ArrayList<PastEventsModel> getPastEventsModels() {
        return pastEventsModels;
    }

    public void setPastEventsModels(ArrayList<PastEventsModel> pastEventsModels) {
        this.pastEventsModels = pastEventsModels;
    }

    public ArrayList<UpcomingEventsModel> getUpcomingEventsModels() {
        return upcomingEventsModels;
    }

    public void setUpcomingEventsModels(ArrayList<UpcomingEventsModel> upcomingEventsModels) {
        this.upcomingEventsModels = upcomingEventsModels;
    }

    public int getHouse_image_count() {
        return house_image_count;
    }

    public void setHouse_image_count(int house_image_count) {
        this.house_image_count = house_image_count;
    }
}
