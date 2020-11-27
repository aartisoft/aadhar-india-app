package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankStatementImageModel {

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
