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
import com.tailwebs.aadharindia.housevisit.models.RoofTypesModel;
import com.tailwebs.aadharindia.housevisit.models.SocialParametersModel;
import com.tailwebs.aadharindia.housevisit.models.StayLengthsModel;
import com.tailwebs.aadharindia.housevisit.models.ToiletsModel;
import com.tailwebs.aadharindia.housevisit.models.WallTypesModel;
import com.tailwebs.aadharindia.models.ProfileImages;

import java.util.ArrayList;

public class HouseInformationModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("loan_taker_id")
    @Expose
    private int loan_taker_id;

    @SerializedName("house_type_id")
    @Expose
    private int house_type_id;

    @SerializedName("roof_type_id")
    @Expose
    private int roof_type_id;

    @SerializedName("wall_type_id")
    @Expose
    private int wall_type_id;

    @SerializedName("toilet_id")
    @Expose
    private int toilet_id;

    @SerializedName("num_of_room_id")
    @Expose
    private int num_of_room_id;

    @SerializedName("stay_length_id")
    @Expose
    private int stay_length_id;

    @SerializedName("married_since_id")
    @Expose
    private int married_since_id;

    @SerializedName("agricultural_land_id")
    @Expose
    private int agricultural_land_id;

    @SerializedName("num_of_members_in_the_family")
    @Expose
    private int num_of_members_in_the_family;

    @SerializedName("ill_member_id")
    @Expose
    private int ill_member_id;

    @SerializedName("kitchen_type_id")
    @Expose
    private int kitchen_type_id;

    @SerializedName("num_of_cattle_id")
    @Expose
    private int num_of_cattle_id;

    @SerializedName("family_type_id")
    @Expose
    private int family_type_id;

    @SerializedName("num_of_dependents_in_the_family")
    @Expose
    private int num_of_dependents_in_the_family;

    @SerializedName("num_of_smartphones_in_the_family")
    @Expose
    private int num_of_smartphones_in_the_family;

    @SerializedName("house_type")
    @Expose
    private HouseTypesModel houseTypesModel;

    @SerializedName("roof_type")
    @Expose
    private RoofTypesModel roofTypesModel;

    @SerializedName("wall_type")
    @Expose
    private WallTypesModel wallTypesModel;

    @SerializedName("kitchen_type")
    @Expose
    private KitchenTypesModel kitchenTypesModel;

    @SerializedName("toilet")
    @Expose
    private ToiletsModel toiletsModel;

    @SerializedName("num_of_room")
    @Expose
    private NoOfRoomsModel noOfRoomsModel;

    @SerializedName("stay_length")
    @Expose
    private StayLengthsModel stayLengthsModel;

    @SerializedName("married_since")
    @Expose
    private MarriedStatusModel marriedStatusModel;

    @SerializedName("agricultural_land")
    @Expose
    private AgriculturalLandsModel agriculturalLandsModel;

    @SerializedName("num_of_cattle")
    @Expose
    private NoOfCattlesModel noOfCattlesModel;

    @SerializedName("house_visit_social_parameters")
    @Expose
    private ArrayList<SocialParametersModel> socialParametersModelArrayList;

    @SerializedName("family_type")
    @Expose
    private FamilyTypesModel familyTypesModel;

    @SerializedName("ill_member")
    @Expose
    private IllMembersModel illMembersModel;

    @SerializedName("signature_images")
    @Expose
    private ProfileImages signatureImagesModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLoan_taker_id() {
        return loan_taker_id;
    }

    public void setLoan_taker_id(int loan_taker_id) {
        this.loan_taker_id = loan_taker_id;
    }

    public int getHouse_type_id() {
        return house_type_id;
    }

    public void setHouse_type_id(int house_type_id) {
        this.house_type_id = house_type_id;
    }

    public int getRoof_type_id() {
        return roof_type_id;
    }

    public void setRoof_type_id(int roof_type_id) {
        this.roof_type_id = roof_type_id;
    }

    public int getWall_type_id() {
        return wall_type_id;
    }

    public void setWall_type_id(int wall_type_id) {
        this.wall_type_id = wall_type_id;
    }

    public int getToilet_id() {
        return toilet_id;
    }

    public void setToilet_id(int toilet_id) {
        this.toilet_id = toilet_id;
    }

    public int getNum_of_room_id() {
        return num_of_room_id;
    }

    public void setNum_of_room_id(int num_of_room_id) {
        this.num_of_room_id = num_of_room_id;
    }

    public int getStay_length_id() {
        return stay_length_id;
    }

    public void setStay_length_id(int stay_length_id) {
        this.stay_length_id = stay_length_id;
    }

    public int getMarried_since_id() {
        return married_since_id;
    }

    public void setMarried_since_id(int married_since_id) {
        this.married_since_id = married_since_id;
    }

    public int getAgricultural_land_id() {
        return agricultural_land_id;
    }

    public void setAgricultural_land_id(int agricultural_land_id) {
        this.agricultural_land_id = agricultural_land_id;
    }

    public int getNum_of_members_in_the_family() {
        return num_of_members_in_the_family;
    }

    public void setNum_of_members_in_the_family(int num_of_members_in_the_family) {
        this.num_of_members_in_the_family = num_of_members_in_the_family;
    }

    public int getIll_member_id() {
        return ill_member_id;
    }

    public void setIll_member_id(int ill_member_id) {
        this.ill_member_id = ill_member_id;
    }

    public int getKitchen_type_id() {
        return kitchen_type_id;
    }

    public void setKitchen_type_id(int kitchen_type_id) {
        this.kitchen_type_id = kitchen_type_id;
    }

    public int getNum_of_cattle_id() {
        return num_of_cattle_id;
    }

    public void setNum_of_cattle_id(int num_of_cattle_id) {
        this.num_of_cattle_id = num_of_cattle_id;
    }

    public int getFamily_type_id() {
        return family_type_id;
    }

    public void setFamily_type_id(int family_type_id) {
        this.family_type_id = family_type_id;
    }

    public int getNum_of_dependents_in_the_family() {
        return num_of_dependents_in_the_family;
    }

    public void setNum_of_dependents_in_the_family(int num_of_dependents_in_the_family) {
        this.num_of_dependents_in_the_family = num_of_dependents_in_the_family;
    }

    public HouseTypesModel getHouseTypesModel() {
        return houseTypesModel;
    }

    public void setHouseTypesModel(HouseTypesModel houseTypesModel) {
        this.houseTypesModel = houseTypesModel;
    }

    public RoofTypesModel getRoofTypesModel() {
        return roofTypesModel;
    }

    public void setRoofTypesModel(RoofTypesModel roofTypesModel) {
        this.roofTypesModel = roofTypesModel;
    }

    public WallTypesModel getWallTypesModel() {
        return wallTypesModel;
    }

    public void setWallTypesModel(WallTypesModel wallTypesModel) {
        this.wallTypesModel = wallTypesModel;
    }

    public KitchenTypesModel getKitchenTypesModel() {
        return kitchenTypesModel;
    }

    public void setKitchenTypesModel(KitchenTypesModel kitchenTypesModel) {
        this.kitchenTypesModel = kitchenTypesModel;
    }

    public ToiletsModel getToiletsModel() {
        return toiletsModel;
    }

    public void setToiletsModel(ToiletsModel toiletsModel) {
        this.toiletsModel = toiletsModel;
    }

    public NoOfRoomsModel getNoOfRoomsModel() {
        return noOfRoomsModel;
    }

    public void setNoOfRoomsModel(NoOfRoomsModel noOfRoomsModel) {
        this.noOfRoomsModel = noOfRoomsModel;
    }

    public StayLengthsModel getStayLengthsModel() {
        return stayLengthsModel;
    }

    public void setStayLengthsModel(StayLengthsModel stayLengthsModel) {
        this.stayLengthsModel = stayLengthsModel;
    }

    public MarriedStatusModel getMarriedStatusModel() {
        return marriedStatusModel;
    }

    public void setMarriedStatusModel(MarriedStatusModel marriedStatusModel) {
        this.marriedStatusModel = marriedStatusModel;
    }

    public AgriculturalLandsModel getAgriculturalLandsModel() {
        return agriculturalLandsModel;
    }

    public void setAgriculturalLandsModel(AgriculturalLandsModel agriculturalLandsModel) {
        this.agriculturalLandsModel = agriculturalLandsModel;
    }

    public NoOfCattlesModel getNoOfCattlesModel() {
        return noOfCattlesModel;
    }

    public void setNoOfCattlesModel(NoOfCattlesModel noOfCattlesModel) {
        this.noOfCattlesModel = noOfCattlesModel;
    }

    public ArrayList<SocialParametersModel> getSocialParametersModelArrayList() {
        return socialParametersModelArrayList;
    }

    public void setSocialParametersModelArrayList(ArrayList<SocialParametersModel> socialParametersModelArrayList) {
        this.socialParametersModelArrayList = socialParametersModelArrayList;
    }

    public FamilyTypesModel getFamilyTypesModel() {
        return familyTypesModel;
    }

    public void setFamilyTypesModel(FamilyTypesModel familyTypesModel) {
        this.familyTypesModel = familyTypesModel;
    }

    public IllMembersModel getIllMembersModel() {
        return illMembersModel;
    }

    public void setIllMembersModel(IllMembersModel illMembersModel) {
        this.illMembersModel = illMembersModel;
    }

    public int getNum_of_smartphones_in_the_family() {
        return num_of_smartphones_in_the_family;
    }

    public void setNum_of_smartphones_in_the_family(int num_of_smartphones_in_the_family) {
        this.num_of_smartphones_in_the_family = num_of_smartphones_in_the_family;
    }


    public ProfileImages getSignatureImagesModel() {
        return signatureImagesModel;
    }

    public void setSignatureImagesModel(ProfileImages signatureImagesModel) {
        this.signatureImagesModel = signatureImagesModel;
    }
}
