package com.tailwebs.aadharindia.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeAddress {

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
    private String pinCode;

    @SerializedName("city_id")
    @Expose
    private String cityId;

    @SerializedName("google_place_id")
    @Expose
    private String googlePlaceId;

    @SerializedName("city_name")
    @Expose
    private String city_name;

    @SerializedName("district_name")
    @Expose
    private String districtName;

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

    @SerializedName("full_address")
    @Expose
    private String full_address;


    @SerializedName("is_geocoded")
    @Expose
    private String isGeoCoded;

    @SerializedName("city")
    @Expose
    private City city;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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

    public String getFull_address() {
        return full_address;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public String getIsGeoCoded() {
        return isGeoCoded;
    }

    public void setIsGeoCoded(String isGeoCoded) {
        this.isGeoCoded = isGeoCoded;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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
}
