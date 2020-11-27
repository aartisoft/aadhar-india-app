package com.tailwebs.aadharindia.housevisit.models.housevisitstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.coapplicant.models.coapplicantstatus.COASDetailsModel;
import com.tailwebs.aadharindia.member.coapplicant.models.coapplicantstatus.COASDocumentsModel;
import com.tailwebs.aadharindia.member.coapplicant.models.coapplicantstatus.COASFamilyModel;

public class HVStatusModel {

    @SerializedName("house_visit_house_info")
    @Expose
    private HVHouseInfoModel houseInfoModel;

    @SerializedName("house_visit_personal_info")
    @Expose
    private HVPersonalInfoModel personalInfoModel;

    @SerializedName("house_visit_applicant_doc")
    @Expose
    private HVApplicantDocumentModel applicantDocumentModel;

    @SerializedName("house_visit_co_applicant_doc")
    @Expose
    private HVCoApplicantDocumentModel coApplicantDocumentModel;

    @SerializedName("house_visit_house_image")
    @Expose
    private HVHouseImageModel houseImageModel;

    @SerializedName("house_visit_signed")
    @Expose
    private HVDeclarationModel declarationModel;

    @SerializedName("house_visit_rating")
    @Expose
    private HVRatingModel ratingModel;

    public HVHouseInfoModel getHouseInfoModel() {
        return houseInfoModel;
    }

    public void setHouseInfoModel(HVHouseInfoModel houseInfoModel) {
        this.houseInfoModel = houseInfoModel;
    }

    public HVPersonalInfoModel getPersonalInfoModel() {
        return personalInfoModel;
    }

    public void setPersonalInfoModel(HVPersonalInfoModel personalInfoModel) {
        this.personalInfoModel = personalInfoModel;
    }

    public HVApplicantDocumentModel getApplicantDocumentModel() {
        return applicantDocumentModel;
    }

    public void setApplicantDocumentModel(HVApplicantDocumentModel applicantDocumentModel) {
        this.applicantDocumentModel = applicantDocumentModel;
    }

    public HVCoApplicantDocumentModel getCoApplicantDocumentModel() {
        return coApplicantDocumentModel;
    }

    public void setCoApplicantDocumentModel(HVCoApplicantDocumentModel coApplicantDocumentModel) {
        this.coApplicantDocumentModel = coApplicantDocumentModel;
    }

    public HVHouseImageModel getHouseImageModel() {
        return houseImageModel;
    }

    public void setHouseImageModel(HVHouseImageModel houseImageModel) {
        this.houseImageModel = houseImageModel;
    }

    public HVDeclarationModel getDeclarationModel() {
        return declarationModel;
    }

    public void setDeclarationModel(HVDeclarationModel declarationModel) {
        this.declarationModel = declarationModel;
    }

    public HVRatingModel getRatingModel() {
        return ratingModel;
    }

    public void setRatingModel(HVRatingModel ratingModel) {
        this.ratingModel = ratingModel;
    }
}
