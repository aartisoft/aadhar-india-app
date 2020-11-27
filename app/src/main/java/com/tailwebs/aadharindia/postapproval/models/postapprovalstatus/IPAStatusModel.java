package com.tailwebs.aadharindia.postapproval.models.postapprovalstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.postapproval.models.groupstatus.GGroupDocumentsModel;
import com.tailwebs.aadharindia.postapproval.models.groupstatus.GIndividualDocumentsModel;

public class IPAStatusModel {

    @SerializedName("esign_document")
    @Expose
    private IPAESignDocumentsModel eSignDocumentsModel;

    @SerializedName("insurance_form")
    @Expose
    private IPAInsuranceDocumentsModel insuranceDocumentsModel;

    @SerializedName("signature")
    @Expose
    private IPASignatureModel signatureModel;

    public IPAESignDocumentsModel geteSignDocumentsModel() {
        return eSignDocumentsModel;
    }

    public void seteSignDocumentsModel(IPAESignDocumentsModel eSignDocumentsModel) {
        this.eSignDocumentsModel = eSignDocumentsModel;
    }

    public IPAInsuranceDocumentsModel getInsuranceDocumentsModel() {
        return insuranceDocumentsModel;
    }

    public void setInsuranceDocumentsModel(IPAInsuranceDocumentsModel insuranceDocumentsModel) {
        this.insuranceDocumentsModel = insuranceDocumentsModel;
    }

    public IPASignatureModel getSignatureModel() {
        return signatureModel;
    }

    public void setSignatureModel(IPASignatureModel signatureModel) {
        this.signatureModel = signatureModel;
    }
}
