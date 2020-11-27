package com.tailwebs.aadharindia.member.models.applicantstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.creditcheckreport.CCRCreditScoreModel;
import com.tailwebs.aadharindia.member.models.creditcheckreport.CCRPastDueAccountsModel;
import com.tailwebs.aadharindia.member.models.creditcheckreport.CCRPastDueAmountsModel;
import com.tailwebs.aadharindia.member.models.creditcheckreport.CCRTotalBalanceModel;
import com.tailwebs.aadharindia.member.models.creditcheckreport.CCRWrittenOffAmountsModel;

public class ASCustomerDetailModel {

    @SerializedName("completed")
    @Expose
    private Boolean completed;

    @SerializedName("activated")
    @Expose
    private Boolean activated;


    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
}
