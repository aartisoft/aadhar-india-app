package com.tailwebs.aadharindia.center.searchinmap.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityCenterImages {

    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("city_center_images")
    @Expose
    private CityCenterImage cityCenterImage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CityCenterImage getCityCenterImage() {
        return cityCenterImage;
    }

    public void setCityCenterImage(CityCenterImage cityCenterImage) {
        this.cityCenterImage = cityCenterImage;
    }
}
