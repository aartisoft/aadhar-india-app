package com.tailwebs.aadharindia.member.models.cashincomestatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASCreditCheckReportModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASCustomerDetailModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASDocumentsFilledModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASEMICalculatorModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASLoanDetailModel;

public class CIStatusModel {

    @SerializedName("family_income")
    @Expose
    private CIFamilyIncomeModel familyIncomeModel;

    @SerializedName("alternate_income")
    @Expose
    private CIAlternateIncomeModel alternateIncomeModel;

    public CIFamilyIncomeModel getFamilyIncomeModel() {
        return familyIncomeModel;
    }

    public void setFamilyIncomeModel(CIFamilyIncomeModel familyIncomeModel) {
        this.familyIncomeModel = familyIncomeModel;
    }

    public CIAlternateIncomeModel getAlternateIncomeModel() {
        return alternateIncomeModel;
    }

    public void setAlternateIncomeModel(CIAlternateIncomeModel alternateIncomeModel) {
        this.alternateIncomeModel = alternateIncomeModel;
    }
}
