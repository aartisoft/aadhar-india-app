package com.tailwebs.aadharindia.member.coapplicant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.UploadImages;
import com.tailwebs.aadharindia.models.ProfileImages;
import com.tailwebs.aadharindia.models.common.CACDSecondaryIDModel;

import java.util.ArrayList;

public class CoApplicantDocumentModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("profile_image_not_available")
    @Expose
    private String profile_image_not_available;

    @SerializedName("aadhar_front_image_not_available")
    @Expose
    private String aadhar_front_image_not_available;

    @SerializedName("aadhar_back_image_not_available")
    @Expose
    private String aadhar_back_image_not_available;

    @SerializedName("secondary_document_images_not_available")
    @Expose
    private String secondary_document_images_not_available;

    @SerializedName("secondary_document_images_is_correct")
    @Expose
    private String secondary_document_images_is_correct;

    @SerializedName("secondary_document_images_reject_reason")
    @Expose
    private String secondary_document_images_reject_reason;

    @SerializedName("secondary_document_id")
    @Expose
    private String secondary_document_id;

    @SerializedName("profile_image")
    @Expose
    private ProfileImages profileImages;

    @SerializedName("aadhar_front_images")
    @Expose
    private UploadImages aadhar_front_images;

    @SerializedName("aadhar_back_images")
    @Expose
    private UploadImages aadhar_back_images;

    @SerializedName("secondary_document_images")
    @Expose
    private ArrayList<SecondaryDocumentImagesModel> secondaryDocumentImageModelArrayList;

    @SerializedName("secondary_document")
    @Expose
    private CACDSecondaryIDModel secondaryIDModel;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String isProfile_image_not_available() {
        return profile_image_not_available;
    }

    public void setProfile_image_not_available(String profile_image_not_available) {
        this.profile_image_not_available = profile_image_not_available;
    }

    public String isAadhar_front_image_not_available() {
        return aadhar_front_image_not_available;
    }

    public void setAadhar_front_image_not_available(String aadhar_front_image_not_available) {
        this.aadhar_front_image_not_available = aadhar_front_image_not_available;
    }

    public String isAadhar_back_image_not_available() {
        return aadhar_back_image_not_available;
    }

    public void setAadhar_back_image_not_available(String aadhar_back_image_not_available) {
        this.aadhar_back_image_not_available = aadhar_back_image_not_available;
    }

    public ProfileImages getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(ProfileImages profileImages) {
        this.profileImages = profileImages;
    }


    public UploadImages getAadhar_front_images() {
        return aadhar_front_images;
    }

    public void setAadhar_front_images(UploadImages aadhar_front_images) {
        this.aadhar_front_images = aadhar_front_images;
    }

    public UploadImages getAadhar_back_images() {
        return aadhar_back_images;
    }

    public void setAadhar_back_images(UploadImages aadhar_back_images) {
        this.aadhar_back_images = aadhar_back_images;
    }

    public String isSecondary_document_images_not_available() {
        return secondary_document_images_not_available;
    }

    public void setSecondary_document_images_not_available(String secondary_document_images_not_available) {
        this.secondary_document_images_not_available = secondary_document_images_not_available;
    }

    public String isSecondary_document_id() {
        return secondary_document_id;
    }

    public void setSecondary_document_id(String secondary_document_id) {
        this.secondary_document_id = secondary_document_id;
    }

    public String getProfile_image_not_available() {
        return profile_image_not_available;
    }

    public String getAadhar_front_image_not_available() {
        return aadhar_front_image_not_available;
    }

    public String getAadhar_back_image_not_available() {
        return aadhar_back_image_not_available;
    }

    public String getSecondary_document_images_not_available() {
        return secondary_document_images_not_available;
    }

    public String getSecondary_document_id() {
        return secondary_document_id;
    }

    public ArrayList<SecondaryDocumentImagesModel> getSecondaryDocumentImageModelArrayList() {
        return secondaryDocumentImageModelArrayList;
    }

    public void setSecondaryDocumentImageModelArrayList(ArrayList<SecondaryDocumentImagesModel> secondaryDocumentImageModelArrayList) {
        this.secondaryDocumentImageModelArrayList = secondaryDocumentImageModelArrayList;
    }


    public CACDSecondaryIDModel getSecondaryIDModel() {
        return secondaryIDModel;
    }

    public void setSecondaryIDModel(CACDSecondaryIDModel secondaryIDModel) {
        this.secondaryIDModel = secondaryIDModel;
    }


    public String getSecondary_document_images_is_correct() {
        return secondary_document_images_is_correct;
    }

    public void setSecondary_document_images_is_correct(String secondary_document_images_is_correct) {
        this.secondary_document_images_is_correct = secondary_document_images_is_correct;
    }

    public String getSecondary_document_images_reject_reason() {
        return secondary_document_images_reject_reason;
    }

    public void setSecondary_document_images_reject_reason(String secondary_document_images_reject_reason) {
        this.secondary_document_images_reject_reason = secondary_document_images_reject_reason;
    }
}
