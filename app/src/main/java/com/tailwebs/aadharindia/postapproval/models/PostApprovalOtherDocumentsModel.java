package com.tailwebs.aadharindia.postapproval.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.coapplicant.models.SecondaryDocumentImageModel;
import com.tailwebs.aadharindia.member.models.UploadImages;
import com.tailwebs.aadharindia.models.PDFFiles;

public class PostApprovalOtherDocumentsModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("images")
    @Expose
    private PDFFiles uploadImages;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public PDFFiles getUploadImages() {
        return uploadImages;
    }

    public void setUploadImages(PDFFiles uploadImages) {
        this.uploadImages = uploadImages;
    }
}
