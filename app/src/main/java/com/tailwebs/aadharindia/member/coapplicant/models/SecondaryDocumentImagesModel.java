package com.tailwebs.aadharindia.member.coapplicant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.UploadImages;

public class SecondaryDocumentImagesModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("secondary_document_image")
    @Expose
    private SecondaryDocumentImageModel secondaryDocumentImageModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SecondaryDocumentImageModel getSecondaryDocumentImageModel() {
        return secondaryDocumentImageModel;
    }

    public void setSecondaryDocumentImageModel(SecondaryDocumentImageModel secondaryDocumentImageModel) {
        this.secondaryDocumentImageModel = secondaryDocumentImageModel;
    }
}
