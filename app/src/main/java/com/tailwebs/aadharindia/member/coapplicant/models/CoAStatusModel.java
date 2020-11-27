package com.tailwebs.aadharindia.member.coapplicant.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.coapplicant.models.coapplicantstatus.COASDetailsModel;
import com.tailwebs.aadharindia.member.coapplicant.models.coapplicantstatus.COASDocumentsModel;
import com.tailwebs.aadharindia.member.coapplicant.models.coapplicantstatus.COASFamilyModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASCreditCheckReportModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASCustomerDetailModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASDocumentsFilledModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASEMICalculatorModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.ASLoanDetailModel;

public class CoAStatusModel {

    @SerializedName("family")
    @Expose
    private COASFamilyModel familyModel;

    @SerializedName("co_applicant")
    @Expose
    private COASDetailsModel coApplicantDetailModel;

    @SerializedName("co_applicant_documents_filled")
    @Expose
    private COASDocumentsModel documentsModel;

    public COASFamilyModel getFamilyModel() {
        return familyModel;
    }

    public void setFamilyModel(COASFamilyModel familyModel) {
        this.familyModel = familyModel;
    }

    public COASDetailsModel getCoApplicantDetailModel() {
        return coApplicantDetailModel;
    }

    public void setCoApplicantDetailModel(COASDetailsModel coApplicantDetailModel) {
        this.coApplicantDetailModel = coApplicantDetailModel;
    }

    public COASDocumentsModel getDocumentsModel() {
        return documentsModel;
    }

    public void setDocumentsModel(COASDocumentsModel documentsModel) {
        this.documentsModel = documentsModel;
    }
}
