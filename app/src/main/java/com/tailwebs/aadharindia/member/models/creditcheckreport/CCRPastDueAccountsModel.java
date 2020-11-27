package com.tailwebs.aadharindia.member.models.creditcheckreport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CCRPastDueAccountsModel {

    @SerializedName("account")
    @Expose
    private String account;

    @SerializedName("is_satisfied")
    @Expose
    private  Boolean is_satisfied;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Boolean getIs_satisfied() {
        return is_satisfied;
    }

    public void setIs_satisfied(Boolean is_satisfied) {
        this.is_satisfied = is_satisfied;
    }
}
