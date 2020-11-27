package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtherFormDataImagesModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("other_form_data_image")
    @Expose
    private OtherFormDataImageModel otherFormDataImageModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OtherFormDataImageModel getOtherFormDataImageModel() {
        return otherFormDataImageModel;
    }

    public void setOtherFormDataImageModel(OtherFormDataImageModel otherFormDataImageModel) {
        this.otherFormDataImageModel = otherFormDataImageModel;
    }
}
