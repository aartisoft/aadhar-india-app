package com.tailwebs.aadharindia.postapproval.models.groupstatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HVApplicantDocumentModel;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HVCoApplicantDocumentModel;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HVDeclarationModel;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HVHouseImageModel;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HVHouseInfoModel;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HVPersonalInfoModel;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HVRatingModel;

public class GStatusModel {

    @SerializedName("group_documents")
    @Expose
    private GGroupDocumentsModel groupDocumentsModel;

    @SerializedName("individual_documents")
    @Expose
    private GIndividualDocumentsModel individualDocumentsModel;

    public GGroupDocumentsModel getGroupDocumentsModel() {
        return groupDocumentsModel;
    }

    public void setGroupDocumentsModel(GGroupDocumentsModel groupDocumentsModel) {
        this.groupDocumentsModel = groupDocumentsModel;
    }

    public GIndividualDocumentsModel getIndividualDocumentsModel() {
        return individualDocumentsModel;
    }

    public void setIndividualDocumentsModel(GIndividualDocumentsModel individualDocumentsModel) {
        this.individualDocumentsModel = individualDocumentsModel;
    }
}
