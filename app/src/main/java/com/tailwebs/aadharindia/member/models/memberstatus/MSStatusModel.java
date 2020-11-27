package com.tailwebs.aadharindia.member.models.memberstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASCreditCheckReportModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASCustomerDetailModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASDocumentsFilledModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASEMICalculatorModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASLoanDetailModel;

public class MSStatusModel {

    @SerializedName("applicant_filled")
    @Expose
    private MSApplicantModel applicantModel;

    @SerializedName("co_applicant_filled")
    @Expose
    private MSCoApplicantModel coApplicantModel;

    @SerializedName("income_filled")
    @Expose
    private MSIncomeModel incomeModel;

    @SerializedName("expense_filled")
    @Expose
    private MSExpenseModel expenseModel;

    @SerializedName("is_signed")
    @Expose
    private MSIsSignedModel isSignedModel;

    @SerializedName("rating")
    @Expose
    private MSRatingModel ratingModel;

    public MSApplicantModel getApplicantModel() {
        return applicantModel;
    }

    public void setApplicantModel(MSApplicantModel applicantModel) {
        this.applicantModel = applicantModel;
    }

    public MSCoApplicantModel getCoApplicantModel() {
        return coApplicantModel;
    }

    public void setCoApplicantModel(MSCoApplicantModel coApplicantModel) {
        this.coApplicantModel = coApplicantModel;
    }

    public MSIncomeModel getIncomeModel() {
        return incomeModel;
    }

    public void setIncomeModel(MSIncomeModel incomeModel) {
        this.incomeModel = incomeModel;
    }

    public MSExpenseModel getExpenseModel() {
        return expenseModel;
    }

    public void setExpenseModel(MSExpenseModel expenseModel) {
        this.expenseModel = expenseModel;
    }

    public MSIsSignedModel getIsSignedModel() {
        return isSignedModel;
    }

    public void setIsSignedModel(MSIsSignedModel isSignedModel) {
        this.isSignedModel = isSignedModel;
    }

    public MSRatingModel getRatingModel() {
        return ratingModel;
    }

    public void setRatingModel(MSRatingModel ratingModel) {
        this.ratingModel = ratingModel;
    }
}
