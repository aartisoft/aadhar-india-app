package com.tailwebs.aadharindia.center.searchinmap.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CenterModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("center_id")
    @Expose
    private String center_id;

    @SerializedName("aadhar_number")
    @Expose
    private String aadhar_number;

    @SerializedName("phone_number")
    @Expose
    private String phone_number;

    @SerializedName("city_google_place_id")
    @Expose
    private String city_google_place_id;

    @SerializedName("city_id")
    @Expose
    private String city_id;

    @SerializedName("contact_person_id")
    @Expose
    private String contact_person_id;

    @SerializedName("center_address_id")
    @Expose
    private String center_address_id;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("center_address")
    @Expose
    private CenterAddressModel centerAddressModel;

    @SerializedName("contact_person")
    @Expose
    private ContactPersonModel contactPersonModel;

    @SerializedName("city_center_images")
    @Expose
    private ArrayList<CityCenterImages> cityCenterImages;


    @SerializedName("groups")
    @Expose
    private ArrayList<GroupModel> groupModelArrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCenter_id() {
        return center_id;
    }

    public void setCenter_id(String center_id) {
        this.center_id = center_id;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCity_google_place_id() {
        return city_google_place_id;
    }

    public void setCity_google_place_id(String city_google_place_id) {
        this.city_google_place_id = city_google_place_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getContact_person_id() {
        return contact_person_id;
    }

    public void setContact_person_id(String contact_person_id) {
        this.contact_person_id = contact_person_id;
    }

    public String getCenter_address_id() {
        return center_address_id;
    }

    public void setCenter_address_id(String center_address_id) {
        this.center_address_id = center_address_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public CenterAddressModel getCenterAddressModel() {
        return centerAddressModel;
    }

    public void setCenterAddressModel(CenterAddressModel centerAddressModel) {
        this.centerAddressModel = centerAddressModel;
    }

    public ContactPersonModel getContactPersonModel() {
        return contactPersonModel;
    }

    public void setContactPersonModel(ContactPersonModel contactPersonModel) {
        this.contactPersonModel = contactPersonModel;
    }

    public ArrayList<CityCenterImages> getCityCenterImages() {
        return cityCenterImages;
    }

    public void setCityCenterImages(ArrayList<CityCenterImages> cityCenterImages) {
        this.cityCenterImages = cityCenterImages;
    }

    public ArrayList<GroupModel> getGroupModelArrayList() {
        return groupModelArrayList;
    }

    public void setGroupModelArrayList(ArrayList<GroupModel> groupModelArrayList) {
        this.groupModelArrayList = groupModelArrayList;
    }
}
