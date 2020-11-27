package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressProofImagesModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("address_proof_image")
    @Expose
    private AddressProofImageModel addressProofImageModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AddressProofImageModel getAddressProofImageModel() {
        return addressProofImageModel;
    }

    public void setAddressProofImageModel(AddressProofImageModel addressProofImageModel) {
        this.addressProofImageModel = addressProofImageModel;
    }
}
