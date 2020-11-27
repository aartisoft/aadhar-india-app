package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.coapplicant.models.SecondaryDocumentImageModel;

public class BankStatementImagesModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("bank_statement_image")
    @Expose
    private BankStatementImageModel bankStatementImageModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BankStatementImageModel getBankStatementImageModel() {
        return bankStatementImageModel;
    }

    public void setBankStatementImageModel(BankStatementImageModel bankStatementImageModel) {
        this.bankStatementImageModel = bankStatementImageModel;
    }
}
