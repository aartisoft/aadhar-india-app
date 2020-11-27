package com.tailwebs.aadharindia.member.models.expenditurestatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.cashincomestatus.CIAlternateIncomeModel;
import com.tailwebs.aadharindia.member.models.cashincomestatus.CIFamilyIncomeModel;

public class EStatusModel {

    @SerializedName("family_expenditure")
    @Expose
    private EFamilyExpenditureModel familyExpenditureModel;

    @SerializedName("outside_borrowing")
    @Expose
    private EOutsideBorrowingModel outsideBorrowingModel;

    public EFamilyExpenditureModel getFamilyExpenditureModel() {
        return familyExpenditureModel;
    }

    public void setFamilyExpenditureModel(EFamilyExpenditureModel familyExpenditureModel) {
        this.familyExpenditureModel = familyExpenditureModel;
    }

    public EOutsideBorrowingModel getOutsideBorrowingModel() {
        return outsideBorrowingModel;
    }

    public void setOutsideBorrowingModel(EOutsideBorrowingModel outsideBorrowingModel) {
        this.outsideBorrowingModel = outsideBorrowingModel;
    }
}
