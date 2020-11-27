package com.tailwebs.aadharindia.models.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.CustomerModel;

import java.util.ArrayList;

public class CustomerApplicantCommonDataResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("errors")
    @Expose
    private Object errors;

    @SerializedName("notice")
    @Expose
    private String notice;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("genders")
    @Expose
    private  ArrayList<String> genders;

    @SerializedName("loan_amounts")
    @Expose
    private ArrayList<CACDLoanAmountsModel> cacdLoanAmountsModelArrayList;

    @SerializedName("loan_tenures")
    @Expose
    private ArrayList<CACDLoanTenuresModel> cacdLoanTenuresModelArrayList;

    @SerializedName("loan_taker_relations")
    @Expose
    private ArrayList<CACDLoanTakerRelationsModel> cacdLoanTakerRelationsModelArrayList;

    @SerializedName("loan_reasons")
    @Expose
    private ArrayList<CACDLoanReasonsModel> cacdLoanReasonsModelArrayList;

    @SerializedName("martial_statuses")
    @Expose
    private ArrayList<CACDMaritalStatusModel> cacdMaritalStatusModelArrayList;

    @SerializedName("castes")
    @Expose
    private ArrayList<CACDCastesModel> cacdCastesModelArrayList;

    @SerializedName("religions")
    @Expose
    private ArrayList<CACDReligionsModel> cacdReligionsModelArrayList;

    @SerializedName("ration_card_types")
    @Expose
    private ArrayList<CACDRationCardTypesModel> cacdRationCardTypesModels;

    @SerializedName("secondary_documents")
    @Expose
    private ArrayList<CACDSecondaryIDModel> cacdSecondaryIDModels;

    @SerializedName("bank_statement_image_count")
    @Expose
    private int bank_statement_image_count;

    @SerializedName("secondary_document_image_count")
    @Expose
    private int secondary_document_image_count;

    @SerializedName("other_loan_card_image_count")
    @Expose
    private int other_loan_card_image_count;



    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public ArrayList<CACDLoanAmountsModel> getCacdLoanAmountsModelArrayList() {
        return cacdLoanAmountsModelArrayList;
    }

    public void setCacdLoanAmountsModelArrayList(ArrayList<CACDLoanAmountsModel> cacdLoanAmountsModelArrayList) {
        this.cacdLoanAmountsModelArrayList = cacdLoanAmountsModelArrayList;
    }

    public ArrayList<CACDLoanTenuresModel> getCacdLoanTenuresModelArrayList() {
        return cacdLoanTenuresModelArrayList;
    }

    public void setCacdLoanTenuresModelArrayList(ArrayList<CACDLoanTenuresModel> cacdLoanTenuresModelArrayList) {
        this.cacdLoanTenuresModelArrayList = cacdLoanTenuresModelArrayList;
    }

    public ArrayList<CACDLoanTakerRelationsModel> getCacdLoanTakerRelationsModelArrayList() {
        return cacdLoanTakerRelationsModelArrayList;
    }

    public void setCacdLoanTakerRelationsModelArrayList(ArrayList<CACDLoanTakerRelationsModel> cacdLoanTakerRelationsModelArrayList) {
        this.cacdLoanTakerRelationsModelArrayList = cacdLoanTakerRelationsModelArrayList;
    }

    public ArrayList<CACDLoanReasonsModel> getCacdLoanReasonsModelArrayList() {
        return cacdLoanReasonsModelArrayList;
    }

    public void setCacdLoanReasonsModelArrayList(ArrayList<CACDLoanReasonsModel> cacdLoanReasonsModelArrayList) {
        this.cacdLoanReasonsModelArrayList = cacdLoanReasonsModelArrayList;
    }

    public ArrayList<CACDMaritalStatusModel> getCacdMaritalStatusModelArrayList() {
        return cacdMaritalStatusModelArrayList;
    }

    public void setCacdMaritalStatusModelArrayList(ArrayList<CACDMaritalStatusModel> cacdMaritalStatusModelArrayList) {
        this.cacdMaritalStatusModelArrayList = cacdMaritalStatusModelArrayList;
    }

    public ArrayList<CACDCastesModel> getCacdCastesModelArrayList() {
        return cacdCastesModelArrayList;
    }

    public void setCacdCastesModelArrayList(ArrayList<CACDCastesModel> cacdCastesModelArrayList) {
        this.cacdCastesModelArrayList = cacdCastesModelArrayList;
    }

    public ArrayList<CACDReligionsModel> getCacdReligionsModelArrayList() {
        return cacdReligionsModelArrayList;
    }

    public void setCacdReligionsModelArrayList(ArrayList<CACDReligionsModel> cacdReligionsModelArrayList) {
        this.cacdReligionsModelArrayList = cacdReligionsModelArrayList;
    }

    public ArrayList<CACDRationCardTypesModel> getCacdRationCardTypesModels() {
        return cacdRationCardTypesModels;
    }

    public void setCacdRationCardTypesModels(ArrayList<CACDRationCardTypesModel> cacdRationCardTypesModels) {
        this.cacdRationCardTypesModels = cacdRationCardTypesModels;
    }


    public ArrayList<String> getGenders() {
        return genders;
    }

    public void setGenders(ArrayList<String> genders) {
        this.genders = genders;
    }


    public ArrayList<CACDSecondaryIDModel> getCacdSecondaryIDModels() {
        return cacdSecondaryIDModels;
    }

    public void setCacdSecondaryIDModels(ArrayList<CACDSecondaryIDModel> cacdSecondaryIDModels) {
        this.cacdSecondaryIDModels = cacdSecondaryIDModels;
    }

    public int getBank_statement_image_count() {
        return bank_statement_image_count;
    }

    public void setBank_statement_image_count(int bank_statement_image_count) {
        this.bank_statement_image_count = bank_statement_image_count;
    }

    public int getSecondary_document_image_count() {
        return secondary_document_image_count;
    }

    public void setSecondary_document_image_count(int secondary_document_image_count) {
        this.secondary_document_image_count = secondary_document_image_count;
    }

    public int getOther_loan_card_image_count() {
        return other_loan_card_image_count;
    }

    public void setOther_loan_card_image_count(int other_loan_card_image_count) {
        this.other_loan_card_image_count = other_loan_card_image_count;
    }
}
