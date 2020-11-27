package com.tailwebs.aadharindia.member.coapplicant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.coapplicant.models.coapplicantstatus.COASDetailsModel;
import com.tailwebs.aadharindia.member.coapplicant.models.coapplicantstatus.COASDocumentsModel;
import com.tailwebs.aadharindia.member.coapplicant.models.coapplicantstatus.COASFamilyModel;
import com.tailwebs.aadharindia.member.models.UploadImages;

public class SecondaryDocumentImageModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("images")
    @Expose
    private UploadImages uploadImages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UploadImages getUploadImages() {
        return uploadImages;
    }

    public void setUploadImages(UploadImages uploadImages) {
        this.uploadImages = uploadImages;
    }
}
