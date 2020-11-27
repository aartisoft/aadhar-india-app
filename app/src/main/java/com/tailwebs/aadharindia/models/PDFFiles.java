
package com.tailwebs.aadharindia.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PDFFiles {

    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("medium")
    @Expose
    private String medium;
    @SerializedName("original")
    @Expose
    private String original;
    

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

}
