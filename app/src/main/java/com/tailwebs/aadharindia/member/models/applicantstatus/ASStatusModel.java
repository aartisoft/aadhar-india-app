package com.tailwebs.aadharindia.member.models.applicantstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ASStatusModel {

    @SerializedName("customer_detail")
    @Expose
    private ASCustomerDetailModel customerDetailModel;

    @SerializedName("credit_check_report")
    @Expose
    private ASCreditCheckReportModel creditCheckReportModel;

    @SerializedName("emi_calculator")
    @Expose
    private ASEMICalculatorModel emiCalculatorModel;

    @SerializedName("loan_detail")
    @Expose
    private ASLoanDetailModel loanDetailModel;

    @SerializedName("documents_filled")
    @Expose
    private ASDocumentsFilledModel documentsFilledModel;

    public ASCustomerDetailModel getCustomerDetailModel() {
        return customerDetailModel;
    }

    public void setCustomerDetailModel(ASCustomerDetailModel customerDetailModel) {
        this.customerDetailModel = customerDetailModel;
    }

    public ASCreditCheckReportModel getCreditCheckReportModel() {
        return creditCheckReportModel;
    }

    public void setCreditCheckReportModel(ASCreditCheckReportModel creditCheckReportModel) {
        this.creditCheckReportModel = creditCheckReportModel;
    }

    public ASEMICalculatorModel getEmiCalculatorModel() {
        return emiCalculatorModel;
    }

    public void setEmiCalculatorModel(ASEMICalculatorModel emiCalculatorModel) {
        this.emiCalculatorModel = emiCalculatorModel;
    }

    public ASLoanDetailModel getLoanDetailModel() {
        return loanDetailModel;
    }

    public void setLoanDetailModel(ASLoanDetailModel loanDetailModel) {
        this.loanDetailModel = loanDetailModel;
    }

    public ASDocumentsFilledModel getDocumentsFilledModel() {
        return documentsFilledModel;
    }

    public void setDocumentsFilledModel(ASDocumentsFilledModel documentsFilledModel) {
        this.documentsFilledModel = documentsFilledModel;
    }
}
