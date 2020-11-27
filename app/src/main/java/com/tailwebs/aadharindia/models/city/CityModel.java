package com.tailwebs.aadharindia.models.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.models.District;
import com.tailwebs.aadharindia.models.LocationState;

public class CityModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("long_name")
    @Expose
    private String long_name;

    @SerializedName("short_name")
    @Expose
    private String short_name;

    @SerializedName("pincode")
    @Expose
    private String pincode;

    @SerializedName("district_id")
    @Expose
    private String district_id;

    @SerializedName("district_name")
    @Expose
    private String district_name;

    @SerializedName("location_state_id")
    @Expose
    private String location_state_id;

    @SerializedName("location_state_name")
    @Expose
    private String location_state_name;

    @SerializedName("country_name")
    @Expose
    private String country_name;

    @SerializedName("country_code")
    @Expose
    private String country_code;

    @SerializedName("lat")
    @Expose
    private String lat;

    @SerializedName("lng")
    @Expose
    private String lng;

    @SerializedName("google_place_id")
    @Expose
    private String google_place_id;

    @SerializedName("district")
    @Expose
    private District district;

    @SerializedName("location_state")
    @Expose
    private LocationState location_state;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public String getLocation_state_id() {
        return location_state_id;
    }

    public void setLocation_state_id(String location_state_id) {
        this.location_state_id = location_state_id;
    }

    public String getLocation_state_name() {
        return location_state_name;
    }

    public void setLocation_state_name(String location_state_name) {
        this.location_state_name = location_state_name;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
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

    public String getGoogle_place_id() {
        return google_place_id;
    }

    public void setGoogle_place_id(String google_place_id) {
        this.google_place_id = google_place_id;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public LocationState getLocation_state() {
        return location_state;
    }

    public void setLocation_state(LocationState location_state) {
        this.location_state = location_state;
    }
}
