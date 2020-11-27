package com.tailwebs.aadharindia.member.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.center.searchinmap.models.ResidentAddressModel;
import com.tailwebs.aadharindia.models.HomeAddress;
import com.tailwebs.aadharindia.models.ProfileImages;
import com.tailwebs.aadharindia.models.common.CACDCastesModel;
import com.tailwebs.aadharindia.models.common.CACDMaritalStatusModel;
import com.tailwebs.aadharindia.models.common.CACDRationCardTypesModel;
import com.tailwebs.aadharindia.models.common.CACDReligionsModel;

public class LoanTakerLoanDetailsModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("loan_taker_id")
    @Expose
    private String loan_taker_id;

    @SerializedName("aadhar_number")
    @Expose
    private String aadhar_number;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("group_id")
    @Expose
    private String group_id;

    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("aadhar_co")
    @Expose
    private String aadhar_co;

    @SerializedName("primary_phone_number")
    @Expose
    private String primary_phone_number;

    @SerializedName("secondary_phone_number")
    @Expose
    private String secondary_phone_number;

    @SerializedName("landline_phone_number")
    @Expose
    private String landline_phone_number;

    @SerializedName("pan_number")
    @Expose
    private String pan_number;

    @SerializedName("voter_id")
    @Expose
    private String voter_id;

    @SerializedName("martial_status_id")
    @Expose
    private String martial_status_id;

    @SerializedName("caste_id")
    @Expose
    private String caste_id;


    @SerializedName("religion_id")
    @Expose
    private String religion_id;


    @SerializedName("ration_card_type_id")
    @Expose
    private String ration_card_type_id;


    @SerializedName("preferred_language_id")
    @Expose
    private String preferred_language_id;


    @SerializedName("preferred_language")
    @Expose
    private LanguageModel preferred_language;

    @SerializedName("religion_name")
    @Expose
    private String religion_name;

    @SerializedName("ration_card_type_name")
    @Expose
    private String ration_card_type_name;

    @SerializedName("rating")
    @Expose
    private String rating;


    @SerializedName("is_married")
    @Expose
    private Boolean is_married;

    @SerializedName("is_fresh_customer")
    @Expose
    private String is_fresh_customer;

    @SerializedName("loan_cycle")
    @Expose
    private String loan_cycle;


    @SerializedName("loan_detail")
    @Expose
    private LoanDetailModel loanDetailModel;

    @SerializedName("martial_status")
    @Expose
    private CACDMaritalStatusModel maritalStatusModel;


    @SerializedName("caste")
    @Expose
    private CACDCastesModel castesModel;

    @SerializedName("religion")
    @Expose
    private CACDReligionsModel religionsModel;

    @SerializedName("ration_card_type")
    @Expose
    private CACDRationCardTypesModel rationCardTypesModel;

    @SerializedName("profile_images")
    @Expose
    private ProfileImages profileImages;

    @SerializedName("customer")
    @Expose
    private CustomerModel customerModel;


    @SerializedName("calculated_emi")
    @Expose
    private CalculatedEMIModel calculatedEMIModel;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoan_taker_id() {
        return loan_taker_id;
    }

    public void setLoan_taker_id(String loan_taker_id) {
        this.loan_taker_id = loan_taker_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAadhar_co() {
        return aadhar_co;
    }

    public void setAadhar_co(String aadhar_co) {
        this.aadhar_co = aadhar_co;
    }

    public String getPrimary_phone_number() {
        return primary_phone_number;
    }

    public void setPrimary_phone_number(String primary_phone_number) {
        this.primary_phone_number = primary_phone_number;
    }

    public String getSecondary_phone_number() {
        return secondary_phone_number;
    }

    public void setSecondary_phone_number(String secondary_phone_number) {
        this.secondary_phone_number = secondary_phone_number;
    }

    public String getLandline_phone_number() {
        return landline_phone_number;
    }

    public void setLandline_phone_number(String landline_phone_number) {
        this.landline_phone_number = landline_phone_number;
    }

    public String getPan_number() {
        return pan_number;
    }

    public void setPan_number(String pan_number) {
        this.pan_number = pan_number;
    }

    public String getVoter_id() {
        return voter_id;
    }

    public void setVoter_id(String voter_id) {
        this.voter_id = voter_id;
    }

    public String getMartial_status_id() {
        return martial_status_id;
    }

    public void setMartial_status_id(String martial_status_id) {
        this.martial_status_id = martial_status_id;
    }

    public String getCaste_id() {
        return caste_id;
    }

    public void setCaste_id(String caste_id) {
        this.caste_id = caste_id;
    }

    public String getReligion_id() {
        return religion_id;
    }

    public void setReligion_id(String religion_id) {
        this.religion_id = religion_id;
    }

    public String getRation_card_type_id() {
        return ration_card_type_id;
    }

    public void setRation_card_type_id(String ration_card_type_id) {
        this.ration_card_type_id = ration_card_type_id;
    }

    public String getPreferred_language_id() {
        return preferred_language_id;
    }

    public void setPreferred_language_id(String preferred_language_id) {
        this.preferred_language_id = preferred_language_id;
    }

    public String getReligion_name() {
        return religion_name;
    }

    public void setReligion_name(String religion_name) {
        this.religion_name = religion_name;
    }

    public String getRation_card_type_name() {
        return ration_card_type_name;
    }

    public void setRation_card_type_name(String ration_card_type_name) {
        this.ration_card_type_name = ration_card_type_name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public LoanDetailModel getLoanDetailModel() {
        return loanDetailModel;
    }

    public void setLoanDetailModel(LoanDetailModel loanDetailModel) {
        this.loanDetailModel = loanDetailModel;
    }

    public CACDMaritalStatusModel getMaritalStatusModel() {
        return maritalStatusModel;
    }

    public void setMaritalStatusModel(CACDMaritalStatusModel maritalStatusModel) {
        this.maritalStatusModel = maritalStatusModel;
    }

    public CACDCastesModel getCastesModel() {
        return castesModel;
    }

    public void setCastesModel(CACDCastesModel castesModel) {
        this.castesModel = castesModel;
    }

    public CACDReligionsModel getReligionsModel() {
        return religionsModel;
    }

    public void setReligionsModel(CACDReligionsModel religionsModel) {
        this.religionsModel = religionsModel;
    }

    public CACDRationCardTypesModel getRationCardTypesModel() {
        return rationCardTypesModel;
    }

    public void setRationCardTypesModel(CACDRationCardTypesModel rationCardTypesModel) {
        this.rationCardTypesModel = rationCardTypesModel;
    }

    public ProfileImages getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(ProfileImages profileImages) {
        this.profileImages = profileImages;
    }

    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    public CalculatedEMIModel getCalculatedEMIModel() {
        return calculatedEMIModel;
    }

    public void setCalculatedEMIModel(CalculatedEMIModel calculatedEMIModel) {
        this.calculatedEMIModel = calculatedEMIModel;
    }

    public Boolean getIs_married() {
        return is_married;
    }

    public void setIs_married(Boolean is_married) {
        this.is_married = is_married;
    }

    public String getIs_fresh_customer() {
        return is_fresh_customer;
    }

    public void setIs_fresh_customer(String is_fresh_customer) {
        this.is_fresh_customer = is_fresh_customer;
    }

    public String getLoan_cycle() {
        return loan_cycle;
    }

    public void setLoan_cycle(String loan_cycle) {
        this.loan_cycle = loan_cycle;
    }

    public LanguageModel getPreferred_language() {
        return preferred_language;
    }

    public void setPreferred_language(LanguageModel preferred_language) {
        this.preferred_language = preferred_language;
    }
}
