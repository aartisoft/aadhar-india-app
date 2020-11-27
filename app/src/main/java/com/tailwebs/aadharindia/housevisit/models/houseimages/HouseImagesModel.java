package com.tailwebs.aadharindia.housevisit.models.houseimages;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.UploadImages;

import java.util.ArrayList;

public class HouseImagesModel {

    @SerializedName("house_images")
    @Expose
    private ArrayList<HouseImageModel> houseImageModels;

    public ArrayList<HouseImageModel> getHouseImageModels() {
        return houseImageModels;
    }

    public void setHouseImageModels(ArrayList<HouseImageModel> houseImageModels) {
        this.houseImageModels = houseImageModels;
    }
}
