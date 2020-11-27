package com.tailwebs.aadharindia.retrofitapi;





import com.tailwebs.aadharindia.center.searchinmap.models.CityCenterResponseModel;
import com.tailwebs.aadharindia.center.searchinmap.models.CityCentersResponseModel;
import com.tailwebs.aadharindia.center.searchinmap.models.GroupResponseModel;
import com.tailwebs.aadharindia.home.dashboard.AllTaskResponseModel;
import com.tailwebs.aadharindia.home.models.CheckUpdateResponseModel;
import com.tailwebs.aadharindia.home.models.CollectionRatingResponseModel;
import com.tailwebs.aadharindia.home.models.GroupPaymentResponseModel;
import com.tailwebs.aadharindia.home.models.MemberCollectionResponseModel;
import com.tailwebs.aadharindia.home.models.PaymentTypeResponseModel;
import com.tailwebs.aadharindia.home.models.TaskResponseModel;
import com.tailwebs.aadharindia.postapproval.models.GroupDocumentResponseModel;
import com.tailwebs.aadharindia.postapproval.models.LoanTakerPostApprovalDocumentResponseModel;
import com.tailwebs.aadharindia.postapproval.models.digio.IndividualPostApprovalDigioResponseModel;
import com.tailwebs.aadharindia.postapproval.models.groupstatus.GroupStatusResponseModel;
import com.tailwebs.aadharindia.postapproval.models.postapprovalstatus.IndividualPostApprovalResponseModel;
import com.tailwebs.aadharindia.housevisit.models.HouseVisitCommonDataResponseModel;
import com.tailwebs.aadharindia.housevisit.models.houseimages.HouseImagesResponseModel;
import com.tailwebs.aadharindia.housevisit.models.houseinformationcreate.HouseInforationCreateResponseModel;
import com.tailwebs.aadharindia.housevisit.models.houseinformationcreate.HouseInforationEditResponseModel;
import com.tailwebs.aadharindia.housevisit.models.housepersonalinfo.PersonalInforationCreateResponseModel;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HouseVisitStatusResponseModel;
import com.tailwebs.aadharindia.member.GroupLeaderResponseModel;
import com.tailwebs.aadharindia.member.GroupMemberListingResponseModel;
import com.tailwebs.aadharindia.member.GroupMemberResponseModel;
import com.tailwebs.aadharindia.member.cashincome.models.AlternateIncomeResponseModel;
import com.tailwebs.aadharindia.member.cashincome.models.FamilyMemberListingResponseModel;
import com.tailwebs.aadharindia.member.cashincome.models.FamilyMemberResponseModel;
import com.tailwebs.aadharindia.member.cashincome.models.PrimaryJobResponseModel;
import com.tailwebs.aadharindia.member.coapplicant.models.CoApplicantStatusResponseModel;
import com.tailwebs.aadharindia.member.coapplicant.models.LoanTakerCoApplicantDetailsResponseModel;
import com.tailwebs.aadharindia.member.coapplicant.models.LoanTakerCoApplicantDocumentResponseModel;
import com.tailwebs.aadharindia.member.coapplicant.models.LoanTakerCoApplicantFamilyResponseModel;
import com.tailwebs.aadharindia.member.expenditure.OutstandingLoanResponseModel;
import com.tailwebs.aadharindia.member.models.CalculateEMIResponseModel;
import com.tailwebs.aadharindia.member.models.CustomerResponseModel;
import com.tailwebs.aadharindia.member.models.FamilyExpenditureResponseModel;
import com.tailwebs.aadharindia.member.models.LanguagesResponseModel;
import com.tailwebs.aadharindia.member.models.LoanTakerApplicantDocumentDetailResponseModel;
import com.tailwebs.aadharindia.member.models.LoanTakerCustomerDetailResponseModel;
import com.tailwebs.aadharindia.member.models.LoanTakerLoanDetailResponseModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.LoanTakerApplicantStatusResponseModel;
import com.tailwebs.aadharindia.member.models.cashincomestatus.LoanTakerCashIncomeStatusResponseModel;
import com.tailwebs.aadharindia.member.models.creditcheckreport.LoanTakerCreditCheckReportResponseModel;
import com.tailwebs.aadharindia.member.models.expenditurestatus.LoanTakerExpenditureStatusResponseModel;
import com.tailwebs.aadharindia.member.models.memberstatus.LoanTakerMemberStatusResponseModel;
import com.tailwebs.aadharindia.models.DeclarationAPIResponseModel;
import com.tailwebs.aadharindia.models.city.CityCenterCommonDataResponseModel;
import com.tailwebs.aadharindia.models.city.CityResponseModel;
import com.tailwebs.aadharindia.models.common.CashIncomeCommonDataResponseModel;
import com.tailwebs.aadharindia.models.common.CustomerApplicantCommonDataResponseModel;
import com.tailwebs.aadharindia.models.common.CustomerCoRelationResponseModel;
import com.tailwebs.aadharindia.models.forgotpassword.ForgotPassword;
import com.tailwebs.aadharindia.models.login.Login;
import com.tailwebs.aadharindia.models.Logout;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    /**
     * Login API
     *
     * @param phone
     * @param password
     * @return
     */

    @FormUrlEncoded
    @POST("sessions/create.json")
    Call<Login> doLogin(@Field("user[login]") String phone,
                        @Field("user[password]") String password);

    /**
     *
     * Forgot Password API
     *
     * @param mobile
     * @return
     */
    @FormUrlEncoded
    @POST("passwords.json")
    Call<ForgotPassword> doForgotPassword(@Field("user[phone_number]") String mobile);

    /**
     *
     * Logout API
     *
     * @return
     */
    @DELETE("user/logout.json")
    Call<Logout> doLogout();


    @FormUrlEncoded
    @PUT("user/update_password.json")
    Call<Login> doResetPassword(@Field("user[current_password]") String current_password,
                                @Field("user[password]") String password,
                                @Field("user[password_confirmation]") String confirm_password);

    @Multipart
    @POST("city_centers.json")
    Call<CityCenterResponseModel> addCityCenter(
            @Part("city_center[aadhar_number]")RequestBody aadharNo,
            @Part("city_center[contact_person_attributes][name]") RequestBody name,
            @Part("city_center[contact_person_attributes][dob]") RequestBody dob,
            @Part("city_center[contact_person_attributes][gender]") RequestBody gender,
            @Part("city_center[contact_person_attributes][phone_number]") RequestBody phone,
            @Part("city_center[contact_person_attributes][aadhar_co]") RequestBody co,
            @Part("city_center[center_address_attributes][line1]") RequestBody address_line1,
            @Part("city_center[center_address_attributes][line2]") RequestBody address_line2,
            @Part("city_center[center_address_attributes][city_google_place_id]") RequestBody city_google_id,
            @Part("city_center[center_address_attributes][pincode]") RequestBody pinCode,
            @Part("user_passcode") RequestBody pass_code,
            @Part MultipartBody.Part image
         );


    @POST("city_centers.json")
    Call<CityCenterResponseModel> createCenter(@Body RequestBody body);


    @GET("city_centers.json")
    Call<CityCentersResponseModel> getCityCentersInVillage(@Query("city_google_place_id") String placeId);

    @GET("city_centers/{city_id}.json")
    Call<Login> getCityCentersById(@Path("city_id") String cityId);



    @GET("city_centers/show_by_aadhar/{aadhar_no}.json")
    Call<CityCenterResponseModel> getCityCentersByAadharNumber(@Path("aadhar_no") String aadharNo);



    @FormUrlEncoded
    @POST("city_centers/{city_id}/groups.json")
    Call<GroupResponseModel> createGroup(@Path("city_id") String cityId,  @Field("task_id") String password);

    @GET("customers/show_by_aadhar/{aadhar_no}.json")
    Call<CustomerResponseModel> getCustomerByAadharNumber(@Path("aadhar_no") String aadharNo,@Query("group_id") String group_id);


    @GET("city_centers/{city_center_id}.json")
    Call<CityCenterResponseModel> getCustomerByCityCenterId(@Path("city_center_id") String cityCenterId);

    @GET("city_center_common_datas.json")
    Call<CityCenterCommonDataResponseModel> getCityCenterCommonDatas();


    //Get Common Datas


    @GET("add_customer_common_datas.json")
    Call<CustomerApplicantCommonDataResponseModel> getCustomerApplicantCommonDatas();



    @GET("add_family_member_common_datas.json")
    Call<CashIncomeCommonDataResponseModel> getCashIncomeCommonDatas();

    @GET("languages.json")
    Call<LanguagesResponseModel> getLanguages();


    //groups




    //add Customer Detail


    @GET("loan_takers/{loan_taker_id}.json")
    Call<LoanTakerCustomerDetailResponseModel> getCustomerDetails(@Path("loan_taker_id") String loanTakerId);

    @Multipart
    @POST("loan_takers.json")
    Call<LoanTakerCustomerDetailResponseModel> addLoanTakerCustomerDetails(
            @Part("loan_taker[aadhar_number]")RequestBody aadharNo,
            @Part("loan_taker[customer_attributes][name]") RequestBody name,
            @Part("loan_taker[dob]") RequestBody dob,
            @Part("loan_taker[customer_attributes][gender]") RequestBody gender,
            @Part("loan_taker[aadhar_co]") RequestBody co,
            @Part("loan_taker[aadhar_co_relation_id]") RequestBody co_id,
            @Part("loan_taker[aadhar_co_relation_name]") RequestBody relation_others,
            @Part("loan_taker[aadhar_address_attributes][line1]") RequestBody address_line1,
            @Part("loan_taker[aadhar_address_attributes][line2]]") RequestBody address_line2,
            @Part("loan_taker[aadhar_address_attributes][city_google_place_id]") RequestBody city_google_id,
            @Part("loan_taker[aadhar_address_attributes][pincode]") RequestBody pincode,
            @Part("loan_taker[primary_phone_number]") RequestBody primary_phone,
            @Part("loan_taker[pan_number]") RequestBody pan,
            @Part("loan_taker[voter_id]") RequestBody voterId,
            @Part("loan_taker[is_residing_address]") RequestBody is_residing,
            @Part("loan_taker[resident_address_attributes][line1]") RequestBody residing_address_line1,
            @Part("loan_taker[resident_address_attributes][line2]]") RequestBody residing_address_line2,
            @Part("loan_taker[resident_address_attributes][city_google_place_id]") RequestBody residing_city_google_id,
            @Part("loan_taker[resident_address_attributes][pincode]") RequestBody residing_pincode,
            @Part("loan_taker[group_id]") RequestBody group_id);


    @Multipart
    @POST("loan_takers.json")
    Call<LoanTakerCustomerDetailResponseModel> addLoanTakerCustomerDetailsIsScanned(
            @Part("loan_taker[aadhar_number]")RequestBody aadharNo,
            @Part("loan_taker[customer_attributes][name]") RequestBody name,
            @Part("loan_taker[dob]") RequestBody dob,
            @Part("loan_taker[customer_attributes][gender]") RequestBody gender,
            @Part("loan_taker[aadhar_co]") RequestBody co,
            @Part("loan_taker[aadhar_co_relation_id]") RequestBody co_id,
            @Part("loan_taker[aadhar_co_relation_name]") RequestBody relation_others,
            @Part("loan_taker[aadhar_address_attributes][line1]") RequestBody address_line1,
            @Part("loan_taker[aadhar_address_attributes][line2]]") RequestBody address_line2,
            @Part("loan_taker[aadhar_address_attributes][city_google_place_id]") RequestBody city_google_id,
            @Part("loan_taker[aadhar_address_attributes][pincode]") RequestBody pincode,
            @Part("loan_taker[primary_phone_number]") RequestBody primary_phone,
            @Part("loan_taker[pan_number]") RequestBody pan,
            @Part("loan_taker[voter_id]") RequestBody voterId,
            @Part("loan_taker[is_residing_address]") RequestBody is_residing,
            @Part("loan_taker[resident_address_attributes][line1]") RequestBody residing_address_line1,
            @Part("loan_taker[resident_address_attributes][line2]]") RequestBody residing_address_line2,
            @Part("loan_taker[resident_address_attributes][city_google_place_id]") RequestBody residing_city_google_id,
            @Part("loan_taker[resident_address_attributes][pincode]") RequestBody residing_pincode,
            @Part("loan_taker[group_id]") RequestBody group_id,
            @Part("aadhar_detail[uid]") RequestBody uid,
            @Part("aadhar_detail[name]") RequestBody aname,
            @Part("aadhar_detail[gender]") RequestBody agender,
            @Part("aadhar_detail[yob]") RequestBody yob,
            @Part("aadhar_detail[dob]") RequestBody adob,
            @Part("aadhar_detail[gname]") RequestBody gname,
            @Part("aadhar_detail[co]") RequestBody aco,
            @Part("aadhar_detail[house]") RequestBody house,
            @Part("aadhar_detail[street]") RequestBody street,
            @Part("aadhar_detail[loc]") RequestBody loc,
            @Part("aadhar_detail[vtc]") RequestBody vtc,
            @Part("aadhar_detail[po]") RequestBody po,
            @Part("aadhar_detail[district]") RequestBody district,
            @Part("aadhar_detail[sub_district]") RequestBody sub_district,
            @Part("aadhar_detail[state]") RequestBody state,
            @Part("aadhar_detail[pincode]") RequestBody apincode);

    @Multipart
    @PUT("loan_takers/{loan_taker_id}.json")
    Call<LoanTakerCustomerDetailResponseModel> updateLoanTakerCustomerDetails(
            @Path("loan_taker_id") String loanTakerId,
            @Part("loan_taker[aadhar_number]")RequestBody aadharNo,
            @Part("loan_taker[customer_attributes][name]") RequestBody name,
            @Part("loan_taker[dob]") RequestBody dob,
            @Part("loan_taker[customer_attributes][gender]") RequestBody gender,
            @Part("loan_taker[aadhar_co]") RequestBody co,
            @Part("loan_taker[aadhar_co_relation_id]") RequestBody co_id,
            @Part("loan_taker[aadhar_co_relation_name]") RequestBody relation_others,
            @Part("loan_taker[aadhar_address_attributes][id]") RequestBody address_id,
            @Part("loan_taker[aadhar_address_attributes][line1]") RequestBody address_line1,
            @Part("loan_taker[aadhar_address_attributes][line2]]") RequestBody address_line2,
            @Part("loan_taker[aadhar_address_attributes][city_google_place_id]") RequestBody city_google_id,
            @Part("loan_taker[aadhar_address_attributes][pincode]") RequestBody pincode,
            @Part("loan_taker[primary_phone_number]") RequestBody primary_phone,
            @Part("loan_taker[pan_number]") RequestBody pan,
            @Part("loan_taker[voter_id]") RequestBody voterId,
            @Part("loan_taker[is_residing_address]") RequestBody is_residing,
            @Part("loan_taker[resident_address_attributes][id") RequestBody residing_address_id,
            @Part("loan_taker[resident_address_attributes][line1]") RequestBody residing_address_line1,
            @Part("loan_taker[resident_address_attributes][line2]]") RequestBody residing_address_line2,
            @Part("loan_taker[resident_address_attributes][city_google_place_id]") RequestBody residing_city_google_id,
            @Part("loan_taker[resident_address_attributes][pincode]") RequestBody residing_pincode
            );

    //Loan Detail


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/loan_detail.json")
    Call<LoanTakerLoanDetailResponseModel> addLoanTakerLoanDetails(
            @Path("loan_taker_id") String loanTakerId,
            @Part("loan_taker[primary_phone_number]")RequestBody primaryPhoneNo,
            @Part("loan_taker[secondary_phone_number]") RequestBody secondaryPhoneNo,
            @Part("loan_taker[landline_phone_number]") RequestBody landline,
            @Part("loan_taker[martial_status_id]") RequestBody maritalStatusId,
            @Part("loan_taker[caste_id]") RequestBody casteId,
            @Part("loan_taker[religion_id]") RequestBody religionId,
            @Part("loan_taker[religion_name]") RequestBody religionName,
            @Part("loan_taker[ration_card_type_id]") RequestBody rationCardTypeId,
            @Part("loan_taker[ration_card_type_name]") RequestBody rationCardTypeName,
            @Part("loan_taker[preferred_language_id]") RequestBody languageId,
            @Part("loan_taker[loan_detail_attributes][loan_reason_id]") RequestBody reasonId,
            @Part("loan_taker[loan_detail_attributes][loan_reason_title]") RequestBody reasonTitle,
            @Part("loan_taker[loan_detail_attributes][descrption]") RequestBody loanDescription);




    @GET("loan_takers/{loan_taker_id}.json?action_type=loan_detail")
    Call<LoanTakerLoanDetailResponseModel> getLoanDetail(@Path("loan_taker_id") String loanTakerId);

    @Multipart
    @PUT("loan_takers/{loan_taker_id}/loan_detail.json")
    Call<LoanTakerLoanDetailResponseModel> updateLoanTakerLoanDetails(
            @Path("loan_taker_id") String loanTakerId,
            @Part("loan_taker[primary_phone_number]")RequestBody primaryPhoneNo,
            @Part("loan_taker[secondary_phone_number]") RequestBody secondaryPhoneNo,
            @Part("loan_taker[landline_phone_number]") RequestBody landline,
            @Part("loan_taker[martial_status_id]") RequestBody maritalStatusId,
            @Part("loan_taker[caste_id]") RequestBody casteId,
            @Part("loan_taker[religion_id]") RequestBody religionId,
            @Part("loan_taker[religion_name]") RequestBody religionName,
            @Part("loan_taker[ration_card_type_id]") RequestBody rationCardTypeId,
            @Part("loan_taker[ration_card_type_name]") RequestBody rationCardTypeName,
            @Part("loan_taker[preferred_language_id]") RequestBody languageId,
            @Part("loan_taker[loan_detail_attributes][loan_reason_id]") RequestBody reasonId,
            @Part("loan_taker[loan_detail_attributes][loan_reason_title]") RequestBody reasonTitle,
            @Part("loan_taker[loan_detail_attributes][descrption]") RequestBody loanDescription,
            @Part("loan_taker[loan_detail_attributes][id]") RequestBody loanTakerIdForUpdate);


    //Loan Documents

    @GET("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> getDocumentDetails(@Path("loan_taker_id") String loanTakerId);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> addProfileImage(@Path("loan_taker_id") String loanTakerId,
                                                                                  @Part MultipartBody.Part image);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> updateDocumentValidationFOrBankDetailFirstPage
            (@Path("loan_taker_id") String loanTakerId,
             @Part("document[bank_detail_image_not_available]") RequestBody value);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> updateDocumentValidationForOneYearBankStatement
            (@Path("loan_taker_id") String loanTakerId,
             @Part("document[bank_statement_images_not_available]") RequestBody value);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> updateDocumentValidationFOrAadharFront
            (@Path("loan_taker_id") String loanTakerId,
             @Part("document[aadhar_front_image_not_available]") RequestBody value);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> updateDocumentValidationFOrAadharBack
            (@Path("loan_taker_id") String loanTakerId,
             @Part("document[aadhar_back_image_not_available]") RequestBody value);

    @Multipart
    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> updateDocumentValidationFOrSecondaryID
            (@Path("loan_taker_id") String loanTakerId,
             @Part("document[secondary_document_images_not_available]") RequestBody value);



    //Credit check report

    @GET("loan_takers/{loan_taker_id}/credit_check_report.json")
    Call<LoanTakerCreditCheckReportResponseModel> getCreditCheckReport(@Path("loan_taker_id") String loanTakerId);

    @Multipart
    @PUT("loan_takers/{loan_taker_id}/reject.json")
    Call<LoanTakerCustomerDetailResponseModel> rejectLoanTaker(
            @Path("loan_taker_id") String loanTakerId,
            @Part("rejection[code]")RequestBody rejectionCOde);


    //Calculated EMI

    @FormUrlEncoded
    @PUT("loan_takers/{loan_taker_id}/calculate_emi.json")
    Call<CalculateEMIResponseModel> doCalculateEMI(@Path("loan_taker_id") String loanTakerId,
                                                   @Field("loan_taker[calculated_emis_attributes][0][amount]") int amount,
                                                   @Field("loan_taker[calculated_emis_attributes][0][tenure]") int tenure);


    @GET("loan_takers/{loan_taker_id}.json?action_type=calculated_emi")
    Call<CalculateEMIResponseModel> getCalculatedEMI(@Path("loan_taker_id") String loanTakerId);


    //Applicant Status
    @GET("loan_takers/{loan_taker_id}/get_status.json")
    Call<LoanTakerApplicantStatusResponseModel> getApplicantStatus(@Path("loan_taker_id") String loanTakerId,
                                                                   @Query("action_type") String placeId);




    //Hose Visit Status

    @GET("loan_takers/{loan_taker_id}/get_status.json?action_type=house_visit")
    Call<HouseVisitStatusResponseModel> getHouseVisitStatus(@Path("loan_taker_id") String loanTakerId);

    @GET("loan_takers/{loan_taker_id}/pre_approve.json")
    Call<LoanTakerCustomerDetailResponseModel> preApproveLoanTaker(@Path("loan_taker_id") String loanTakerId);





    @GET("city_centers/{city_id}/groups/{group_id}.json")
    Call<GroupMemberListingResponseModel> getMembersInGroup(@Path("city_id") String cityId, @Path("group_id") String groupId);

    //Member Status
    @GET("loan_takers/{loan_taker_id}/get_status.json")
    Call<LoanTakerMemberStatusResponseModel> getMemberStatus(@Path("loan_taker_id") String loanTakerId);



    //Co Applicant Status
    @GET("loan_takers/{loan_taker_id}/get_status.json?action_type=co_applicant")
    Call<CoApplicantStatusResponseModel> getCoApplicantStatus(@Path("loan_taker_id") String loanTakerId);

    @GET("relations.json")
    Call<CustomerCoRelationResponseModel> getCoApplicantRelation(@Query("loan_taker_id") String loan_taker_id);

    @GET("relations.json")
    Call<CustomerCoRelationResponseModel> getRelationFamilyMembers();

    @GET("loan_takers/{loan_taker_id}/co_applicants.json")
    Call<LoanTakerCoApplicantDetailsResponseModel> getCoApplicant(@Path("loan_taker_id") String loanTakerId);



    @Multipart
    @POST("loan_takers/{loan_taker_id}/co_applicants.json")
    Call<LoanTakerCoApplicantDetailsResponseModel> addCoApplicantDetails(@Path("loan_taker_id") String loanTakerId,
            @Part("co_applicant[relation_id]")RequestBody relationId,
            @Part("co_applicant[relation_name]")RequestBody relationName,
            @Part("co_applicant[aadhar_number]")RequestBody aadharNo,
            @Part("co_applicant[name]") RequestBody name,
            @Part("co_applicant[dob]") RequestBody dob,
            @Part("co_applicant[gender]") RequestBody gender,
            @Part("co_applicant[aadhar_co]") RequestBody co,
            @Part("co_applicant[aadhar_co_relation_id]") RequestBody co_id,
            @Part("co_applicant[aadhar_co_relation_name]") RequestBody relation_others,
             @Part("co_applicant[primary_phone_number]") RequestBody primaryNo,
             @Part("co_applicant[secondary_phone_number]") RequestBody secondaryNo,
            @Part("co_applicant[aadhar_address_attributes][line1]") RequestBody address_line1,
            @Part("co_applicant[aadhar_address_attributes][line2]]") RequestBody address_line2,
            @Part("co_applicant[aadhar_address_attributes][city_google_place_id]") RequestBody city_google_id,
            @Part("co_applicant[aadhar_address_attributes][pincode]") RequestBody pincode,
            @Part("co_applicant[is_residing_address]") RequestBody is_residing,
            @Part("co_applicant[resident_address_attributes][line1]") RequestBody residing_address_line1,
            @Part("co_applicant[resident_address_attributes][line2]]") RequestBody residing_address_line2,
            @Part("co_applicant[resident_address_attributes][city_google_place_id]") RequestBody residing_city_google_id,
            @Part("co_applicant[resident_address_attributes][pincode]") RequestBody residing_pincode

//             ,@Part("aadhar_detail[uid]")RequestBody aadhar_uid,
//             @Part("aadhar_detail[name]")RequestBody aadhar_name,
//             @Part("aadhar_detail[gender]")RequestBody aadhar_gender,
//             @Part("aadhar_detail[yob]") RequestBody aadhar_yob,
//             @Part("aadhar_detail[dob]") RequestBody aadhar_dob,
//             @Part("aadhar_detail[gname]")RequestBody aadhar_gname,
//             @Part("aadhar_detail[co]")RequestBody aadhar_co,
//             @Part("aadhar_detail[house]")RequestBody aadhar_house,
//             @Part("aadhar_detail[street]") RequestBody aadhar_street,
//             @Part("aadhar_detail[loc]") RequestBody aadhar_loc,
//             @Part("aadhar_detail[vtc]")RequestBody aadhar_vtc,
//             @Part("aadhar_detail[po]")RequestBody aadhar_po,
//             @Part("aadhar_detail[district]")RequestBody aadhar_district,
//             @Part("aadhar_detail[sub_district]") RequestBody aadhar_sub_dictrict,
//             @Part("aadhar_detail[state]") RequestBody aadhar_state,
//             @Part("aadhar_detail[pincode]") RequestBody aadhar_pincode

            );

    @Multipart
    @POST("loan_takers/{loan_taker_id}/co_applicants.json")
    Call<LoanTakerCoApplicantDetailsResponseModel> addCoApplicantDetailsifScanned(@Path("loan_taker_id") String loanTakerId,
                                                                         @Part("co_applicant[relation_id]")RequestBody relationId,
                                                                         @Part("co_applicant[relation_name]")RequestBody relationName,
                                                                         @Part("co_applicant[aadhar_number]")RequestBody aadharNo,
                                                                         @Part("co_applicant[name]") RequestBody name,
                                                                         @Part("co_applicant[dob]") RequestBody dob,
                                                                         @Part("co_applicant[gender]") RequestBody gender,
                                                                         @Part("co_applicant[aadhar_co]") RequestBody co,
                                                                         @Part("co_applicant[aadhar_co_relation_id]") RequestBody co_id,
                                                                         @Part("co_applicant[aadhar_co_relation_name]") RequestBody relation_others,
                                                                          @Part("co_applicant[primary_phone_number]") RequestBody primaryNo,
                                                                          @Part("co_applicant[secondary_phone_number]") RequestBody secondaryNo,
                                                                         @Part("co_applicant[aadhar_address_attributes][line1]") RequestBody address_line1,
                                                                         @Part("co_applicant[aadhar_address_attributes][line2]]") RequestBody address_line2,
                                                                         @Part("co_applicant[aadhar_address_attributes][city_google_place_id]") RequestBody city_google_id,
                                                                         @Part("co_applicant[aadhar_address_attributes][pincode]") RequestBody pincode,
                                                                         @Part("co_applicant[is_residing_address]") RequestBody is_residing,
                                                                         @Part("co_applicant[resident_address_attributes][line1]") RequestBody residing_address_line1,
                                                                         @Part("co_applicant[resident_address_attributes][line2]]") RequestBody residing_address_line2,
                                                                         @Part("co_applicant[resident_address_attributes][city_google_place_id]") RequestBody residing_city_google_id,
                                                                         @Part("co_applicant[resident_address_attributes][pincode]") RequestBody residing_pincode,
                                                                                    @Part("aadhar_detail[uid]") RequestBody uid,
                                                                                  @Part("aadhar_detail[name]") RequestBody aname,
                                                                                  @Part("aadhar_detail[gender]") RequestBody agender,
                                                                                  @Part("aadhar_detail[yob]") RequestBody yob,
                                                                                  @Part("aadhar_detail[dob]") RequestBody adob,
                                                                                  @Part("aadhar_detail[gname]") RequestBody gname,
                                                                                  @Part("aadhar_detail[co]") RequestBody aco,
                                                                                  @Part("aadhar_detail[house]") RequestBody house,
                                                                                  @Part("aadhar_detail[street]") RequestBody street,
                                                                                  @Part("aadhar_detail[loc]") RequestBody loc,
                                                                                  @Part("aadhar_detail[vtc]") RequestBody vtc,
                                                                                  @Part("aadhar_detail[po]") RequestBody po,
                                                                                  @Part("aadhar_detail[district]") RequestBody district,
                                                                                  @Part("aadhar_detail[sub_district]") RequestBody sub_district,
                                                                                  @Part("aadhar_detail[state]") RequestBody state,
                                                                                  @Part("aadhar_detail[pincode]") RequestBody apincode

//             ,@Part("aadhar_detail[uid]")RequestBody aadhar_uid,
//             @Part("aadhar_detail[name]")RequestBody aadhar_name,
//             @Part("aadhar_detail[gender]")RequestBody aadhar_gender,
//             @Part("aadhar_detail[yob]") RequestBody aadhar_yob,
//             @Part("aadhar_detail[dob]") RequestBody aadhar_dob,
//             @Part("aadhar_detail[gname]")RequestBody aadhar_gname,
//             @Part("aadhar_detail[co]")RequestBody aadhar_co,
//             @Part("aadhar_detail[house]")RequestBody aadhar_house,
//             @Part("aadhar_detail[street]") RequestBody aadhar_street,
//             @Part("aadhar_detail[loc]") RequestBody aadhar_loc,
//             @Part("aadhar_detail[vtc]")RequestBody aadhar_vtc,
//             @Part("aadhar_detail[po]")RequestBody aadhar_po,
//             @Part("aadhar_detail[district]")RequestBody aadhar_district,
//             @Part("aadhar_detail[sub_district]") RequestBody aadhar_sub_dictrict,
//             @Part("aadhar_detail[state]") RequestBody aadhar_state,
//             @Part("aadhar_detail[pincode]") RequestBody aadhar_pincode

    );


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/co_applicants.json")
    Call<LoanTakerCoApplicantDetailsResponseModel> updateCoApplicantDetails(@Path("loan_taker_id") String loanTakerId,
                                                                         @Part("co_applicant[relation_id]")RequestBody relationId,
                                                                         @Part("co_applicant[relation_name]")RequestBody relationName,
                                                                         @Part("co_applicant[aadhar_number]")RequestBody aadharNo,
                                                                         @Part("co_applicant[name]") RequestBody name,
                                                                         @Part("co_applicant[dob]") RequestBody dob,
                                                                         @Part("co_applicant[gender]") RequestBody gender,
                                                                         @Part("co_applicant[aadhar_co]") RequestBody co,
                                                                         @Part("co_applicant[aadhar_co_relation_id]") RequestBody co_id,
                                                                         @Part("co_applicant[aadhar_co_relation_name]") RequestBody relation_others,
                                                                            @Part("co_applicant[primary_phone_number]") RequestBody primaryNo,
                                                                            @Part("co_applicant[secondary_phone_number]") RequestBody secondaryNo,
                                                                            @Part("co_applicant[aadhar_address_attributes][id]") RequestBody address_id,
                                                                         @Part("co_applicant[aadhar_address_attributes][line1]") RequestBody address_line1,
                                                                         @Part("co_applicant[aadhar_address_attributes][line2]]") RequestBody address_line2,
                                                                         @Part("co_applicant[aadhar_address_attributes][city_google_place_id]") RequestBody city_google_id,
                                                                         @Part("co_applicant[aadhar_address_attributes][pincode]") RequestBody pincode,
                                                                         @Part("co_applicant[is_residing_address]") RequestBody is_residing,
                                                                            @Part("co_applicant[resident_address_attributes][id]") RequestBody residing_address_id,
                                                                         @Part("co_applicant[resident_address_attributes][line1]") RequestBody residing_address_line1,
                                                                         @Part("co_applicant[resident_address_attributes][line2]]") RequestBody residing_address_line2,
                                                                         @Part("co_applicant[resident_address_attributes][city_google_place_id]") RequestBody residing_city_google_id,
                                                                         @Part("co_applicant[resident_address_attributes][pincode]") RequestBody residing_pincode


    );



    @GET("loan_takers/{loan_taker_id}/co_applicants/show_by_aadhar/{aadhar_no}.json")
    Call<LoanTakerCoApplicantDetailsResponseModel> getCoApplicantByAadharNumber(@Path("loan_taker_id") String loanTakerId,
                                                               @Path("aadhar_no") String aadharNo);


    //co applicant documents

    @Multipart
    @PUT("loan_takers/{loan_taker_id}/co_applicant_documents.json")
    Call<LoanTakerCoApplicantDocumentResponseModel> addCoApplicantProfileImage(@Path("loan_taker_id") String loanTakerId,
                                                                        @Part MultipartBody.Part image);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> addApplicantMultipleImage(@Path("loan_taker_id") String loanTakerId,
                                                                        @Part MultipartBody.Part[] image);


    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> deleteApplicantMultipleOtherImage(@Path("loan_taker_id") String loanTakerId,
                                                                                          @Body RequestBody body);

    @Multipart
    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> addApplicantSecondaryWithoutMultipleImage(@Path("loan_taker_id") String loanTakerId,
                                                                                           @Part("document[secondary_document_id]") RequestBody sec_id);

    @Multipart
    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> addApplicantSecondaryMultipleImage(@Path("loan_taker_id") String loanTakerId,
                                                                                  @Part MultipartBody.Part[] image,
                                                                                           @Part("document[secondary_document_id]") RequestBody sec_id);

    @PUT("loan_takers/{loan_taker_id}/documents.json")
    Call<LoanTakerApplicantDocumentDetailResponseModel> deleteApplicantSeondaryMultipleImages(@Path("loan_taker_id") String loanTakerId,
                                                                                    @Body RequestBody body);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/co_applicant_documents.json")
    Call<LoanTakerCoApplicantDocumentResponseModel> addCoApplicantMultipleImages(@Path("loan_taker_id") String loanTakerId,
                                                                               @Part MultipartBody.Part[] image,
                                                                                 @Part("document[secondary_document_id]") RequestBody sec_id);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/co_applicant_documents.json")
    Call<LoanTakerCoApplicantDocumentResponseModel> addCoApplicantWithoutMultipleImages(@Path("loan_taker_id") String loanTakerId,
                                                                                 @Part("document[secondary_document_id]") RequestBody sec_id);


    @PUT("loan_takers/{loan_taker_id}/co_applicant_documents.json")
    Call<LoanTakerCoApplicantDocumentResponseModel> deleteCoApplicantMultipleImages(@Path("loan_taker_id") String loanTakerId,
                                                                                    @Body RequestBody body);
//    @POST("loan_takers/{loan_taker_id}/alternate_incomes.json")
//    Call<AlternateIncomeResponseModel> addAlternateIncome(@Path("loan_taker_id") String loanTakerId, @Body RequestBody body);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/co_applicant_documents.json")
    Call<LoanTakerCoApplicantDocumentResponseModel> updateCoApplicantDocumentValidationForProfilePic
            (@Path("loan_taker_id") String loanTakerId,
             @Part("document[profile_image_not_available]") RequestBody value);

    @Multipart
    @PUT("loan_takers/{loan_taker_id}/co_applicant_documents.json")
    Call<LoanTakerCoApplicantDocumentResponseModel> updateCoApplicantDocumentValidationForAadharFront
            (@Path("loan_taker_id") String loanTakerId,
             @Part("document[aadhar_front_image_not_available]") RequestBody value);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/co_applicant_documents.json")
    Call<LoanTakerCoApplicantDocumentResponseModel> updateCoApplicantDocumentValidationForAadharBack
            (@Path("loan_taker_id") String loanTakerId,
             @Part("document[aadhar_back_image_not_available]") RequestBody value);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/co_applicant_documents.json")
    Call<LoanTakerCoApplicantDocumentResponseModel> updateCoApplicantDocumentValidationForSecondaryID
            (@Path("loan_taker_id") String loanTakerId,
             @Part("document[secondary_document_images_not_available]") RequestBody value);


    @GET("loan_takers/{loan_taker_id}/family.json")
    Call<LoanTakerCoApplicantFamilyResponseModel> getFamily(@Path("loan_taker_id") String loanTakerId);


    @Multipart
    @POST("loan_takers/{loan_taker_id}/family.json")
    Call<LoanTakerCoApplicantFamilyResponseModel> addCoApplicantFamily(@Path("loan_taker_id") String loanTakerId,
                                                                         @Part("loan_taker[father_name]")RequestBody father_name,
                                                                         @Part("loan_taker[mother_name]")RequestBody mother_name

    );


    //co applicant documents

    @GET("loan_takers/{loan_taker_id}/co_applicant_documents.json")
    Call<LoanTakerCoApplicantDocumentResponseModel> getCoApplicantDocumentDetails(@Path("loan_taker_id") String loanTakerId);


    //Cash income Status
    @GET("loan_takers/{loan_taker_id}/get_status.json")
    Call<LoanTakerCashIncomeStatusResponseModel> getCashIncomeStatus(@Path("loan_taker_id") String loanTakerId,
                                                                     @Query("action_type") String placeId);


    @GET("loan_takers/{loan_taker_id}/alternate_incomes.json")
    Call<AlternateIncomeResponseModel> getAlternateIncome(@Path("loan_taker_id") String loanTakerId);


    @POST("loan_takers/{loan_taker_id}/alternate_incomes.json")
    Call<AlternateIncomeResponseModel> addAlternateIncome(@Path("loan_taker_id") String loanTakerId, @Body RequestBody body);


    //Expenditure Status
    @GET("loan_takers/{loan_taker_id}/get_status.json")
    Call<LoanTakerExpenditureStatusResponseModel> getExpenditureStatus(@Path("loan_taker_id") String loanTakerId,
                                                                       @Query("action_type") String placeId);

    @Multipart
    @POST("loan_takers/{loan_taker_id}/family_expenditures.json")
    Call<FamilyExpenditureResponseModel> addFamilyExpenditures(@Path("loan_taker_id") String loanTakerId,
                                                               @Part("family_expenditure[rent]")RequestBody rent,
                                                               @Part("family_expenditure[food]")RequestBody food,
                                                               @Part("family_expenditure[education]")RequestBody education,
                                                               @Part("family_expenditure[medical]") RequestBody medical,
                                                               @Part("family_expenditure[travel]") RequestBody travel,
                                                               @Part("family_expenditure[clothing]") RequestBody clothing,
                                                               @Part("family_expenditure[other]") RequestBody other);


    @GET("loan_takers/{loan_taker_id}/family_expenditures.json")
    Call<FamilyExpenditureResponseModel> getFamilyExpenditure(@Path("loan_taker_id") String loanTakerId);



    //signing
    @Multipart
    @PUT("loan_takers/{loan_taker_id}/signing.json")
    Call<CalculateEMIResponseModel> addSigning(@Path("loan_taker_id") String loanTakerId, @Part MultipartBody.Part image);

    //rating

    @FormUrlEncoded
    @PUT("loan_takers/{loan_taker_id}/rating.json")
    Call<CalculateEMIResponseModel> addRating(@Path("loan_taker_id") String loanTakerId,
                          @Field("loan_taker[rating]") String rating);


    //submit cpf

    @FormUrlEncoded
    @PUT("groups/{group_id}/submit_cpf.json")
    Call<GroupMemberResponseModel> submitCPF(@Path("group_id") String group_id,
                                             @Field("user_passcode") String user_passcode);


    // remove on form data
    @FormUrlEncoded
    @PUT("loan_takers/{loan_taker_id}/remove.json?type=form_data")
    Call<CalculateEMIResponseModel> removeOnFormData(@Path("loan_taker_id") String loan_taker_id,
                                             @Field("user_passcode") String user_passcode,
                                                     @Field("loan_taker[remove_reason]") String reason);

    // remove on house visit
    @FormUrlEncoded
    @PUT("loan_takers/{loan_taker_id}/remove.json?type=house_visit")
    Call<CalculateEMIResponseModel> removeOnHouseVisit(@Path("loan_taker_id") String loan_taker_id,
                                                    @Field("user_passcode") String user_passcode);


//    Family

    @GET("loan_takers/{loan_taker_id}/family_members.json")
    Call<FamilyMemberListingResponseModel> getAllFamilyGroups(@Path("loan_taker_id") String loanTakerId);


    //Outstanding Borrowings

    @GET("loan_takers/{loan_taker_id}/outside_borrowings.json")
    Call<OutstandingLoanResponseModel> getAllOutstandingBorrowings(@Path("loan_taker_id") String loanTakerId);


    @Multipart
    @POST("loan_takers/{loan_taker_id}/outside_borrowings.json")
    Call<OutstandingLoanResponseModel> addOutsideBorrowing(@Path("loan_taker_id") String loanTakerId,
                                                               @Part("outside_borrowing[name]")RequestBody name,
                                                               @Part("outside_borrowing[amount]")RequestBody amount);


    @POST("loan_takers/{loan_taker_id}/family_members.json")
    Call<FamilyMemberResponseModel> addFamilyMember(@Path("loan_taker_id") String loanTakerId, @Body RequestBody body);

    @PUT("loan_takers/{loan_taker_id}/family_members/{family_member_id}.json")
    Call<FamilyMemberResponseModel> updateFamilyMember(@Path("loan_taker_id") String loanTakerId, @Body RequestBody body,
                                                       @Path("family_member_id") String familyMemberId);


    @GET("loan_takers/{loan_taker_id}/family_members/{family_member_id}")
    Call<FamilyMemberResponseModel> getAFamilyMember(@Path("loan_taker_id") String loanTakerId,@Path("family_member_id") String familyMemberId);

    @GET("loan_takers/{loan_taker_id}/family_members/new.json?type=applicant")
    Call<FamilyMemberResponseModel> getApplicantData(@Path("loan_taker_id") String loanTakerId);

    @GET("loan_takers/{loan_taker_id}/family_members/new.json?type=co_applicant")
    Call<FamilyMemberResponseModel> getCoApplicantData(@Path("loan_taker_id") String loanTakerId);

    @Multipart
    @POST("loan_takers/{loan_taker_id}/family_members.json")
    Call<LoanTakerCustomerDetailResponseModel> addFamilyMember(
            @Part("family_member[name]")RequestBody aadharNo,
            @Part("family_member[age]") RequestBody name,
            @Part("family_member[relation_id]") RequestBody dob,
            @Part("family_member[health_id]") RequestBody gender,
            @Part("family_member[education_id]") RequestBody co,
            @Part("family_member[education_level_id]") RequestBody co_id,
            @Part("family_member[management_type_id]") RequestBody relation_others,
            @Part("family_member[studying_class]") RequestBody address_line1,
            @Part("loan_taker[aadhar_address_attributes][line2]]") RequestBody address_line2,
            @Part("loan_taker[aadhar_address_attributes][city_google_place_id]") RequestBody city_google_id,
            @Part("loan_taker[aadhar_address_attributes][pincode]") RequestBody pincode,
            @Part("loan_taker[primary_phone_number]") RequestBody primary_phone,
            @Part("loan_taker[pan_number]") RequestBody pan,
            @Part("loan_taker[voter_id]") RequestBody voterId,
            @Part("loan_taker[is_residing_address]") RequestBody is_residing,
            @Part("loan_taker[resident_address_attributes][line1]") RequestBody residing_address_line1,
            @Part("loan_taker[resident_address_attributes][line2]]") RequestBody residing_address_line2,
            @Part("loan_taker[resident_address_attributes][city_google_place_id]") RequestBody residing_city_google_id,
            @Part("loan_taker[resident_address_attributes][pincode]") RequestBody residing_pincode,
            @Part("loan_taker[group_id]") RequestBody group_id);



    @GET("primary_jobs.json")
    Call<PrimaryJobResponseModel> getPrimaryJobs( @Query("code") String code);

    @GET("primary_jobs/search.json")
    Call<PrimaryJobResponseModel> searchPrimaryJobs( @Query("search_string") String string);


    //House Visit


    @GET("house_visit_common_datas.json")
    Call<HouseVisitCommonDataResponseModel> getHouseVisitCommonDatas();


    @POST("loan_takers/{loan_taker_id}/house_visits")
    Call<HouseInforationCreateResponseModel> createHouseInformation(@Path("loan_taker_id") String loanTakerId, @Body RequestBody body);


    @PUT("loan_takers/{loan_taker_id}/house_visits")
    Call<PersonalInforationCreateResponseModel> createPersonalInformation(@Path("loan_taker_id") String loanTakerId, @Body RequestBody body);

    @GET("loan_takers/{loan_taker_id}/house_visits.json")
    Call<HouseInforationEditResponseModel> getHouseVisitInfo(@Path("loan_taker_id") String loanTakerId);

    @GET("loan_takers/{loan_taker_id}/house_visits.json?type=events")
    Call<PersonalInforationCreateResponseModel> getPersonalInfo(@Path("loan_taker_id") String loanTakerId);

    @GET("loan_takers/{loan_taker_id}/house_visits?type=images")
    Call<HouseImagesResponseModel> getHouseImages(@Path("loan_taker_id") String loanTakerId);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/house_visits?type=images")
    Call<HouseImagesResponseModel> addHousePhotos(@Path("loan_taker_id") String loanTakerId,
                                                  @Part MultipartBody.Part[] image);


    @PUT("loan_takers/{loan_taker_id}/house_visits?type=images")
    Call<HouseImagesResponseModel> deleteHousePhotos(@Path("loan_taker_id") String loanTakerId,
                                                     @Body RequestBody body);

    //Signature
    @Multipart
    @PUT("loan_takers/{loan_taker_id}/house_visits/signing.json")
    Call<HouseInforationCreateResponseModel> addSignature(@Path("loan_taker_id") String loanTakerId,
                                                                        @Part MultipartBody.Part image);


    //rating

    @FormUrlEncoded
    @PUT("loan_takers/{loan_taker_id}/house_visits.json?type=rating")
    Call<HouseInforationCreateResponseModel> addHouseVisitRating(@Path("loan_taker_id") String loanTakerId,
                                              @Field("house_visit[rating]") String rating);


    //city

    @FormUrlEncoded
    @POST("cities.json")
    Call<CityResponseModel> getCityDatas(@Field("city[google_place_id]") String cityGoogleId);



    @GET("tasks/new.json?task_type=custom")
    Call<TaskResponseModel> getNewCustomTask();


    @POST("tasks.json?task_type=custom")
    Call<TaskResponseModel> createNewCustomTask(@Body RequestBody body);


    @PUT("tasks/{task_id}.json?task_type=custom")
    Call<TaskResponseModel> updateCustomTaskNotes(@Path("task_id") String taskId,@Body RequestBody body);

    @FormUrlEncoded
    @PUT("tasks/{task_id}/cancel.json?task_type=custom")
    Call<TaskResponseModel> cancelCustomTask(@Path("task_id") String taskId,
                                            @Field("task[reason]") String task_reason);

    @FormUrlEncoded
    @PUT("tasks/{task_id}/negative_complete?task_type=custom")
    Call<TaskResponseModel> negativeCancelCustomTask(@Path("task_id") String taskId,
                                                    @Field("task[reason]") String task_reason);

    @FormUrlEncoded
    @PUT("tasks/{task_id}/complete.json?task_type=custom")
    Call<TaskResponseModel> completeCustomTask(@Path("task_id") String taskId,
                                             @Field("task[reason]") String task_reason);

    //task

    @GET("tasks/new.json")
    Call<TaskResponseModel> getNewTask();

    @POST("tasks.json")
    Call<TaskResponseModel> createNewTask(@Body RequestBody body);


    @GET("tasks/{task_id}.json")
    Call<TaskResponseModel> getTask(@Path("task_id") String taskId);


    @GET("tasks.json")
    Call<AllTaskResponseModel> getAllTasks();


    @FormUrlEncoded
    @PUT("tasks/{task_id}/cancel")
    Call<TaskResponseModel> cancelGroupTask(@Path("task_id") String taskId,
                                                                 @Field("task[reason]") String task_reason);


    @FormUrlEncoded
    @PUT("tasks/{task_id}/negative_complete")
    Call<TaskResponseModel> negativeCancelGroupTask(@Path("task_id") String taskId,
                                                             @Field("task[reason]") String task_reason);



    @GET("loan_takers/{loan_taker_id}/post_approval_documents.json")
    Call<LoanTakerPostApprovalDocumentResponseModel> getPostApprovalDocuments(@Path("loan_taker_id") String loanTakerId);


    @Multipart
    @PUT("loan_takers/{loan_taker_id}/post_approval_documents.json")
    Call<LoanTakerPostApprovalDocumentResponseModel> addPostApprovalDocuments(@Path("loan_taker_id") String loanTakerId,
                                                                        @Part MultipartBody.Part image);

    @Multipart
    @PUT("loan_takers/{loan_taker_id}/post_approval_documents.json")
    Call<LoanTakerPostApprovalDocumentResponseModel> addPostApprovalMultipleImage(@Path("loan_taker_id") String loanTakerId,
                                                                                  @Part MultipartBody.Part[] image);

    @PUT("loan_takers/{loan_taker_id}/post_approval_documents.json")
    Call<LoanTakerPostApprovalDocumentResponseModel> deletePostApprovalMultipleOtherImage(@Path("loan_taker_id") String loanTakerId,
                                                                                          @Body RequestBody body);

    @GET("groups/{group_id}/group_post_approval_documents.json")
    Call<GroupDocumentResponseModel> getGroupPostApprovalDocuments(@Path("group_id") String groupId
                                                                );



    @Multipart
    @PUT("groups/{group_id}/group_post_approval_documents.json")
    Call<GroupDocumentResponseModel> addGroupPostApprovalDocuments(@Path("group_id") String groupId,
                                                                   @Part MultipartBody.Part image);


    @Multipart
    @PUT("groups/{group_id}/group_post_approval_documents.json")
    Call<GroupDocumentResponseModel> addMultipleGroupPostApprovalDocuments(@Path("group_id") String group_id,
                                                                                  @Part MultipartBody.Part[] image);


    @PUT("groups/{group_id}/group_post_approval_documents.json")
    Call<GroupDocumentResponseModel> deleteMultipleGroupPostApprovalDocuments(@Path("group_id") String group_id,
                                                                                    @Body RequestBody body);




    //Group Status

    @GET("groups/{group_id}/get_status.json?action_type=post_approval")
    Call<GroupStatusResponseModel> getGroupStatus(@Path("group_id") String groupId);


    @GET("loan_takers/{loan_taker_id}/get_status.json?action_type=post_approved")
    Call<IndividualPostApprovalResponseModel> getIndividualPostApprovalStatus(@Path("loan_taker_id") String loanTakerId);


    // post approval Signature
    @Multipart
    @PUT("loan_takers/{loan_taker_id}/post_approval_documents.json")
    Call<LoanTakerPostApprovalDocumentResponseModel> postApprovalDocumentsSignature(@Path("loan_taker_id") String loanTakerId,
                                                          @Part MultipartBody.Part image);


    @FormUrlEncoded
    @PUT("groups/{group_id}.json")
    Call<GroupLeaderResponseModel> addGroupLeader(@Path("group_id") String groupId, @Field("group[group_leader_id]") String group_leader_id);



//    check update
    @GET("check_update.json")
    Call<CheckUpdateResponseModel> checkUpdate(@Query("version") int version, @Query("platform") String platform);


    @GET("payment_types.json")
    Call<PaymentTypeResponseModel> getPaymentTypes(@Query("group_id") String group_id);



    @POST("group_collections/{group_id}/recieve_payment.json")
    Call<GroupPaymentResponseModel> sendGroupPayment(@Path("group_id") String groupId,
                                                     @Body RequestBody body);


    @GET("group_collections/{group_id}.json")
    Call<GroupMemberListingResponseModel> getMembersInGroupCollection(@Path("group_id") String groupId);


    @POST("collections/{loan_taker_id}/add_discount.json")
    Call<MemberCollectionResponseModel> addDiscount(@Path("loan_taker_id") String loan_taker_id,
                                                    @Body RequestBody body);



    @POST("collections/{collection_id}/recieve_payment.json")
    Call<MemberCollectionResponseModel> sendIndividualMemberPayment(@Path("collection_id") String collection_id,
                                                     @Body RequestBody body);


    @GET("loan_takers/{loan_taker_id}.json")
    Call<MemberCollectionResponseModel> showMember(@Path("loan_taker_id") String loan_taker_id, @Query("action_type") String action_type);



    @FormUrlEncoded
    @POST("loan_takers/{loan_taker_id}/payment_ratings.json?type=rating")
    Call<CollectionRatingResponseModel> addCollectionRating(@Path("loan_taker_id") String loanTakerId,
                                                            @Field("payment_rating[rating]") String rating);


    @GET("loan_takers/{loan_taker_id}.json")
    Call<CalculateEMIResponseModel> getLoanTaker(@Path("loan_taker_id") String loanTakerId);

    @GET("tasks.json?type=collection")
    Call<AllTaskResponseModel> getAllCollections();


    @GET("loan_takers/{loan_taker_id}/post_approval_documents.json?action_type=esign_document")
    Call<IndividualPostApprovalDigioResponseModel> getIndividualDigioDocument(@Path("loan_taker_id") String loanTakerId);


    // post approval Signature

    @PUT("loan_takers/{loan_taker_id}/post_approval_documents.json")
    Call<LoanTakerPostApprovalDocumentResponseModel> postApprovalDocumentDigio(@Path("loan_taker_id") String loanTakerId,
                                                                               @Body RequestBody body);


    @GET("declarations.json?type=form_data")
    Call<DeclarationAPIResponseModel> getDeclarationFormData();

    @GET("declarations.json?type=house_visit")
    Call<DeclarationAPIResponseModel> getDeclarationHouseVisit();

    @GET("declarations.json?type=post_approval")
    Call<DeclarationAPIResponseModel> getDeclarationPostApproval();


    @FormUrlEncoded
    @POST("loan_takers/{loan_taker_id}/post_approval_documents/generate_loan_document.json")
    Call<IndividualPostApprovalDigioResponseModel> addContactsDigio(@Path("loan_taker_id") String loan_taker_id,
                                                         @Field("applicant_num") String applicant_num,
                                                         @Field("co_applicant_num") String co_applicant_num);

    @FormUrlEncoded
    @POST("loan_takers/{loan_taker_id}/post_approval_documents/generate_manual_loan_document.json")
    Call<IndividualPostApprovalDigioResponseModel> generateManuallySignedLoanAgreement(@Path("loan_taker_id") String loan_taker_id,
                                                                                       @Field("applicant_num") String applicant_num,
                                                                                       @Field("co_applicant_num") String co_applicant_num);
}
