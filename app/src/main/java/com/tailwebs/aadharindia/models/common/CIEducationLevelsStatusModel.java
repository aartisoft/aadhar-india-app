package com.tailwebs.aadharindia.models.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CIEducationLevelsStatusModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("is_active")
    @Expose
    private String is_active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}
