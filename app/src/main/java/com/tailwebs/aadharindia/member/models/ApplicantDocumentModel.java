package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.coapplicant.models.SecondaryDocumentImagesModel;
import com.tailwebs.aadharindia.models.ProfileImages;
import com.tailwebs.aadharindia.models.common.CACDSecondaryIDModel;

import java.util.ArrayList;

public class ApplicantDocumentModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("profile_image_not_available")
    @Expose
    private String profile_image_not_available;

    @SerializedName("address_proof_not_available")
    @Expose
    private String address_proof_not_available;

    @SerializedName("bank_detail_image_not_available")
    @Expose
    private String bank_detail_image_not_available;

    @SerializedName("aadhar_front_image_not_available")
    @Expose
    private String aadhar_front_image_not_available;

    @SerializedName("aadhar_back_image_not_available")
    @Expose
    private String aadhar_back_image_not_available;

    @SerializedName("bank_statement_images_not_available")
    @Expose
    private String bank_statement_images_not_available;

    @SerializedName("secondary_document_images_not_available")
    @Expose
    private String secondary_document_images_not_available;

    @SerializedName("other_form_data_images_not_available")
    @Expose
    private String other_form_data_images_not_available;

    @SerializedName("other_house_visit_images_not_available")
    @Expose
    private String other_house_visit_images_not_available;

    @SerializedName("secondary_document_id")
    @Expose
    private String secondary_document_id;

    @SerializedName("profile_image")
    @Expose
    private ProfileImages profileImages;

    @SerializedName("bank_detail_images")
    @Expose
    private UploadImages bank_detail_images;

    @SerializedName("aadhar_front_images")
    @Expose
    private UploadImages aadhar_front_images;

    @SerializedName("aadhar_back_images")
    @Expose
    private UploadImages aadhar_back_images;

    @SerializedName("address_proof_images")
    @Expose
    private ArrayList<AddressProofImagesModel> addressProofImagesModelArrayList;


    @SerializedName("secondary_document_images")
    @Expose
    private ArrayList<SecondaryDocumentImagesModel> secondaryDocumentImageModelArrayList;

    @SerializedName("other_loan_card_images")
    @Expose
    private ArrayList<OtherLoanCardImagesModel> otherLoanCardImagesModelArrayList;


    @SerializedName("other_form_data_images")
    @Expose
    private ArrayList<OtherFormDataImagesModel> otherFormDataImagesModelArrayList;

    @SerializedName("bank_statement_images")
    @Expose
    private ArrayList<BankStatementImagesModel> bankStatementImagesModelArrayList;

    @SerializedName("secondary_document")
    @Expose
    private CACDSecondaryIDModel secondaryIDModel;


    @SerializedName("secondary_document_images_is_correct")
    @Expose
    private String secondary_document_images_is_correct;

    @SerializedName("secondary_document_images_reject_reason")
    @Expose
    private String secondary_document_images_reject_reason;

    @SerializedName("bank_statement_images_is_correct")
    @Expose
    private String bank_statement_images_is_correct;

    @SerializedName("bank_statement_images_reject_reason")
    @Expose
    private String bank_statement_images_reject_reason;

    @SerializedName("address_proof_images_is_correct")
    @Expose
    private String address_proof_images_is_correct;

    @SerializedName("address_proof_images_reject_reason")
    @Expose
    private String address_proof_images_reject_reason;


    @SerializedName("other_loan_card_images_is_correct")
    @Expose
    private String other_loan_card_images_is_correct;

    @SerializedName("other_loan_card_images_reject_reason")
    @Expose
    private String other_loan_card_images_reject_reason;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String isProfile_image_not_available() {
        return profile_image_not_available;
    }

    public void setProfile_image_not_available(String profile_image_not_available) {
        this.profile_image_not_available = profile_image_not_available;
    }

    public String isBank_detail_image_not_available() {
        return bank_detail_image_not_available;
    }

    public void setBank_detail_image_not_available(String bank_detail_image_not_available) {
        this.bank_detail_image_not_available = bank_detail_image_not_available;
    }

    public String isAadhar_front_image_not_available() {
        return aadhar_front_image_not_available;
    }

    public void setAadhar_front_image_not_available(String aadhar_front_image_not_available) {
        this.aadhar_front_image_not_available = aadhar_front_image_not_available;
    }

    public String isAadhar_back_image_not_available() {
        return aadhar_back_image_not_available;
    }

    public void setAadhar_back_image_not_available(String aadhar_back_image_not_available) {
        this.aadhar_back_image_not_available = aadhar_back_image_not_available;
    }

    public ProfileImages getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(ProfileImages profileImages) {
        this.profileImages = profileImages;
    }

    public UploadImages getBank_detail_images() {
        return bank_detail_images;
    }

    public void setBank_detail_images(UploadImages bank_detail_images) {
        this.bank_detail_images = bank_detail_images;
    }

    public UploadImages getAadhar_front_images() {
        return aadhar_front_images;
    }

    public void setAadhar_front_images(UploadImages aadhar_front_images) {
        this.aadhar_front_images = aadhar_front_images;
    }

    public UploadImages getAadhar_back_images() {
        return aadhar_back_images;
    }

    public void setAadhar_back_images(UploadImages aadhar_back_images) {
        this.aadhar_back_images = aadhar_back_images;
    }

    public String isBank_statement_images_not_available() {
        return bank_statement_images_not_available;
    }

    public void setBank_statement_images_not_available(String bank_statement_images_not_available) {
        this.bank_statement_images_not_available = bank_statement_images_not_available;
    }

    public String isSecondary_document_images_not_available() {
        return secondary_document_images_not_available;
    }

    public void setSecondary_document_images_not_available(String secondary_document_images_not_available) {
        this.secondary_document_images_not_available = secondary_document_images_not_available;
    }

    public String isSecondary_document_id() {
        return secondary_document_id;
    }

    public void setSecondary_document_id(String secondary_document_id) {
        this.secondary_document_id = secondary_document_id;
    }

    public String getProfile_image_not_available() {
        return profile_image_not_available;
    }

    public String getBank_detail_image_not_available() {
        return bank_detail_image_not_available;
    }

    public String getAadhar_front_image_not_available() {
        return aadhar_front_image_not_available;
    }

    public String getAadhar_back_image_not_available() {
        return aadhar_back_image_not_available;
    }

    public String getBank_statement_images_not_available() {
        return bank_statement_images_not_available;
    }

    public String getSecondary_document_images_not_available() {
        return secondary_document_images_not_available;
    }

    public String getSecondary_document_id() {
        return secondary_document_id;
    }

    public ArrayList<SecondaryDocumentImagesModel> getSecondaryDocumentImageModelArrayList() {
        return secondaryDocumentImageModelArrayList;
    }

    public void setSecondaryDocumentImageModelArrayList(ArrayList<SecondaryDocumentImagesModel> secondaryDocumentImageModelArrayList) {
        this.secondaryDocumentImageModelArrayList = secondaryDocumentImageModelArrayList;
    }

    public ArrayList<OtherLoanCardImagesModel> getOtherLoanCardImagesModelArrayList() {
        return otherLoanCardImagesModelArrayList;
    }

    public void setOtherLoanCardImagesModelArrayList(ArrayList<OtherLoanCardImagesModel> otherLoanCardImagesModelArrayList) {
        this.otherLoanCardImagesModelArrayList = otherLoanCardImagesModelArrayList;
    }

    public ArrayList<BankStatementImagesModel> getBankStatementImagesModelArrayList() {
        return bankStatementImagesModelArrayList;
    }

    public void setBankStatementImagesModelArrayList(ArrayList<BankStatementImagesModel> bankStatementImagesModelArrayList) {
        this.bankStatementImagesModelArrayList = bankStatementImagesModelArrayList;
    }

    public CACDSecondaryIDModel getSecondaryIDModel() {
        return secondaryIDModel;
    }

    public void setSecondaryIDModel(CACDSecondaryIDModel secondaryIDModel) {
        this.secondaryIDModel = secondaryIDModel;
    }


    public String getSecondary_document_images_is_correct() {
        return secondary_document_images_is_correct;
    }

    public void setSecondary_document_images_is_correct(String secondary_document_images_is_correct) {
        this.secondary_document_images_is_correct = secondary_document_images_is_correct;
    }

    public String getOther_loan_card_images_is_correct() {
        return other_loan_card_images_is_correct;
    }

    public void setOther_loan_card_images_is_correct(String other_loan_card_images_is_correct) {
        this.other_loan_card_images_is_correct = other_loan_card_images_is_correct;
    }

    public String getOther_loan_card_images_reject_reason() {
        return other_loan_card_images_reject_reason;
    }

    public void setOther_loan_card_images_reject_reason(String other_loan_card_images_reject_reason) {
        this.other_loan_card_images_reject_reason = other_loan_card_images_reject_reason;
    }


    public String getSecondary_document_images_reject_reason() {
        return secondary_document_images_reject_reason;
    }

    public void setSecondary_document_images_reject_reason(String secondary_document_images_reject_reason) {
        this.secondary_document_images_reject_reason = secondary_document_images_reject_reason;
    }

    public String getBank_statement_images_is_correct() {
        return bank_statement_images_is_correct;
    }

    public void setBank_statement_images_is_correct(String bank_statement_images_is_correct) {
        this.bank_statement_images_is_correct = bank_statement_images_is_correct;
    }

    public String getBank_statement_images_reject_reason() {
        return bank_statement_images_reject_reason;
    }

    public void setBank_statement_images_reject_reason(String bank_statement_images_reject_reason) {
        this.bank_statement_images_reject_reason = bank_statement_images_reject_reason;
    }

    public String getAddress_proof_images_is_correct() {
        return address_proof_images_is_correct;
    }

    public void setAddress_proof_images_is_correct(String address_proof_images_is_correct) {
        this.address_proof_images_is_correct = address_proof_images_is_correct;
    }

    public String getAddress_proof_images_reject_reason() {
        return address_proof_images_reject_reason;
    }

    public void setAddress_proof_images_reject_reason(String address_proof_images_reject_reason) {
        this.address_proof_images_reject_reason = address_proof_images_reject_reason;
    }

    public String getAddress_proof_not_available() {
        return address_proof_not_available;
    }

    public void setAddress_proof_not_available(String address_proof_not_available) {
        this.address_proof_not_available = address_proof_not_available;
    }

    public ArrayList<AddressProofImagesModel> getAddressProofImagesModelArrayList() {
        return addressProofImagesModelArrayList;
    }

    public void setAddressProofImagesModelArrayList(ArrayList<AddressProofImagesModel> addressProofImagesModelArrayList) {
        this.addressProofImagesModelArrayList = addressProofImagesModelArrayList;
    }

    public String getOther_form_data_images_not_available() {
        return other_form_data_images_not_available;
    }

    public void setOther_form_data_images_not_available(String other_form_data_images_not_available) {
        this.other_form_data_images_not_available = other_form_data_images_not_available;
    }

    public String getOther_house_visit_images_not_available() {
        return other_house_visit_images_not_available;
    }

    public void setOther_house_visit_images_not_available(String other_house_visit_images_not_available) {
        this.other_house_visit_images_not_available = other_house_visit_images_not_available;
    }

    public ArrayList<OtherFormDataImagesModel> getOtherFormDataImagesModelArrayList() {
        return otherFormDataImagesModelArrayList;
    }

    public void setOtherFormDataImagesModelArrayList(ArrayList<OtherFormDataImagesModel> otherFormDataImagesModelArrayList) {
        this.otherFormDataImagesModelArrayList = otherFormDataImagesModelArrayList;
    }
}
