package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingPenaltyModel {

    @SerializedName("heading")
    @Expose
    private String heading;

    @SerializedName("amount_in_format")
    @Expose
    private String amount_in_format;

    @SerializedName("amount")
    @Expose
    private String amount;


    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getAmount_in_format() {
        return amount_in_format;
    }

    public void setAmount_in_format(String amount_in_format) {
        this.amount_in_format = amount_in_format;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
