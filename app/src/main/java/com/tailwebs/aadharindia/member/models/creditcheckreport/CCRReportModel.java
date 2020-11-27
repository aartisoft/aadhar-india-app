package com.tailwebs.aadharindia.member.models.creditcheckreport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CCRReportModel {

    @SerializedName("total_balance")
    @Expose
    private CCRTotalBalanceModel totalBalanceModel;

    @SerializedName("past_due_accounts")
    @Expose
    private CCRPastDueAccountsModel pastDueAccountsModel;

    @SerializedName("past_due_amount")
    @Expose
    private CCRPastDueAmountsModel pastDueAmountsModel;

    @SerializedName("written_off_amount")
    @Expose
    private CCRWrittenOffAmountsModel writtenOffAmountsModel;

    @SerializedName("credit_score")
    @Expose
    private CCRCreditScoreModel creditScoreModel;

    @SerializedName("dpd_count")
    @Expose
    private CCRDPDCountModel dpdCountModel;

    @SerializedName("latest_dpd_count")
    @Expose
    private CCRDPDCountModel latestDpdCountModel;

    public CCRTotalBalanceModel getTotalBalanceModel() {
        return totalBalanceModel;
    }

    public void setTotalBalanceModel(CCRTotalBalanceModel totalBalanceModel) {
        this.totalBalanceModel = totalBalanceModel;
    }

    public CCRPastDueAccountsModel getPastDueAccountsModel() {
        return pastDueAccountsModel;
    }

    public void setPastDueAccountsModel(CCRPastDueAccountsModel pastDueAccountsModel) {
        this.pastDueAccountsModel = pastDueAccountsModel;
    }

    public CCRPastDueAmountsModel getPastDueAmountsModel() {
        return pastDueAmountsModel;
    }

    public void setPastDueAmountsModel(CCRPastDueAmountsModel pastDueAmountsModel) {
        this.pastDueAmountsModel = pastDueAmountsModel;
    }

    public CCRWrittenOffAmountsModel getWrittenOffAmountsModel() {
        return writtenOffAmountsModel;
    }

    public void setWrittenOffAmountsModel(CCRWrittenOffAmountsModel writtenOffAmountsModel) {
        this.writtenOffAmountsModel = writtenOffAmountsModel;
    }

    public CCRCreditScoreModel getCreditScoreModel() {
        return creditScoreModel;
    }

    public void setCreditScoreModel(CCRCreditScoreModel creditScoreModel) {
        this.creditScoreModel = creditScoreModel;
    }

    public CCRDPDCountModel getDpdCountModel() {
        return dpdCountModel;
    }

    public void setDpdCountModel(CCRDPDCountModel dpdCountModel) {
        this.dpdCountModel = dpdCountModel;
    }

    public CCRDPDCountModel getLatestDpdCountModel() {
        return latestDpdCountModel;
    }

    public void setLatestDpdCountModel(CCRDPDCountModel latestDpdCountModel) {
        this.latestDpdCountModel = latestDpdCountModel;
    }
}
