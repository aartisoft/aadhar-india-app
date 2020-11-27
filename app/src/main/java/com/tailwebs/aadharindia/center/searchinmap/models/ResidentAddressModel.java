package com.tailwebs.aadharindia.center.searchinmap.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.models.City;

public class ResidentAddressModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("line1")
    @Expose
    private String line1;

    @SerializedName("line2")
    @Expose
    private String line2;

    @SerializedName("pincode")
    @Expose
    private String pincode;

    @SerializedName("city_id")
    @Expose
    private String city_id;

    @SerializedName("google_place_id")
    @Expose
    private String google_place_id;

    @SerializedName("city_name")
    @Expose
    private String city_name;

    @SerializedName("district_name")
    @Expose
    private String district_name;

    @SerializedName("state_name")
    @Expose
    private String state_name;

    @SerializedName("country_name")
    @Expose
    private String country_name;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("lng")
    @Expose
    private String lng;

    @SerializedName("is_geocoded")
    @Expose
    private boolean is_geocoded;

    @SerializedName("city")
    @Expose
    private City city;

    @SerializedName("full_address")
    @Expose
    private String full_address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getGoogle_place_id() {
        return google_place_id;
    }

    public void setGoogle_place_id(String google_place_id) {
        this.google_place_id = google_place_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public boolean isIs_geocoded() {
        return is_geocoded;
    }

    public void setIs_geocoded(boolean is_geocoded) {
        this.is_geocoded = is_geocoded;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }
}
