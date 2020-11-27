package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollectionRatingModel {

    @SerializedName("rating")
    @Expose
    private int rating;

    @SerializedName("loan_taker_id")
    @Expose
    private String loan_taker_id;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getLoan_taker_id() {
        return loan_taker_id;
    }

    public void setLoan_taker_id(String loan_taker_id) {
        this.loan_taker_id = loan_taker_id;
    }
}
