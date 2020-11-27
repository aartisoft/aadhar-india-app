package com.tailwebs.aadharindia.housevisit;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.center.CenterConfirmationActivity;
import com.tailwebs.aadharindia.housevisit.models.HouseVisitCommonDataResponseModel;
import com.tailwebs.aadharindia.housevisit.models.housevisitstatus.HouseVisitStatusResponseModel;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.CalculateEMIResponseModel;
import com.tailwebs.aadharindia.models.common.CustomerApplicantCommonDataResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseDetailsActivity extends BaseActivity implements View.OnClickListener {



    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;



    //user

    @BindView(R.id.user_name)
    TextView userNameTv;

    @BindView(R.id.profile_image)
    ImageView profileImageView;

    @BindView(R.id.user_co)
    TextView userCoTv;


    //categories

    @BindView(R.id.house_information_tv)
    TextView houseInformationTv;

    @BindView(R.id.personal_information_tv)
    TextView personalInformationTv;

    @BindView(R.id.applicant_documents_tv)
    TextView applicantDocumentsTv;

    @BindView(R.id.co_applicant_documents_tv)
    TextView coApplicantDocumentsTv;

    @BindView(R.id.house_photos_tv)
    TextView housePhotosTv;

    @BindView(R.id.declaration_tv)
    TextView declarationTv;

    @BindView(R.id.rating_tv)
    TextView ratingTv;

    @BindView(R.id.continue_button)
    Button continueButton;

    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null,phoneNumber=null,passcode=null,reason=null;


    TextView callMemberTv;
    TextView removeMemberTv;
    static HouseDetailsActivity instance;

    //password

    OtpView passwordOTPView;
    TextView passwordErrorTV;
    TextInputLayout reasonLayout;
    TextInputEditText reasonET;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    private Dialog dialog;


    private boolean isHouseInfoCompleted = false,isPersonalInfoCompleted=false,isApplicantDocumentsCompleted=false,
            isCoApplicantCompleted=false,isHousePhotosCompleted=false,isDeclarationCompleted=false,isRatingCompleted=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        ButterKnife.bind(this);
        instance=this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("House Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        rightActionBarButton.setVisibility(View.VISIBLE);
        rightActionBarButton.setOnClickListener(this);

        GlobalValue.isHouseVisitDeclarationCompleted=false;

//        if(!getIntent().hasExtra("page_value")){
//            Intent intent = new Intent(HouseDetailsActivity.this,HouseInfoCreateActivity.class);
//            startActivity(intent);
//        }

        init();
    }

    public void init() {

        loanTakerID = GlobalValue.loanTakerId;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "HV Details", null);


        if (checkPermission(Manifest.permission.CALL_PHONE)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

        continueButton.setOnClickListener(this);
        houseInformationTv.setOnClickListener(this);
        personalInformationTv.setOnClickListener(this);
        applicantDocumentsTv.setOnClickListener(this);
        coApplicantDocumentsTv.setOnClickListener(this);
        housePhotosTv.setOnClickListener(this);
        declarationTv.setOnClickListener(this);
        ratingTv.setOnClickListener(this);

        if(loanTakerID != null) {


            if (NetworkUtils.haveNetworkConnection(this)) {
                showProgressDialog(HouseDetailsActivity.this);
                getHouseVisitStatus();
                getHouseVisitCommonDatas();
                getApplicantCommonDatas();
            } else {
                UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }
        }


    }


    private void getApplicantCommonDatas() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CustomerApplicantCommonDataResponseModel> call = apiService.getCustomerApplicantCommonDatas();
            call.enqueue(new Callback<CustomerApplicantCommonDataResponseModel>() {
                @Override
                public void onResponse(Call<CustomerApplicantCommonDataResponseModel> call, final Response<CustomerApplicantCommonDataResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            GlobalValue.applicantLoanAmountArrayList = response.body().getCacdLoanAmountsModelArrayList();
                            GlobalValue.applicantLoanTenureArrayList = response.body().getCacdLoanTenuresModelArrayList();
                            GlobalValue.applicantLoanTakerRelationsArrayList =  response.body().getCacdLoanTakerRelationsModelArrayList();
                            GlobalValue.applicantLoanReasonsArrayList = response.body().getCacdLoanReasonsModelArrayList();
                            GlobalValue.applicantMaritalStatusArrayList = response.body().getCacdMaritalStatusModelArrayList();
                            GlobalValue.applicantCastesArrayList = response.body().getCacdCastesModelArrayList();
                            GlobalValue.applicantReligionsArrayList = response.body().getCacdReligionsModelArrayList();
                            GlobalValue.applicantRationCardTypesArrayList =  response.body().getCacdRationCardTypesModels();
                            GlobalValue.applicantSecondaryIDArrayList =  response.body().getCacdSecondaryIDModels();

                            GlobalValue.secondaryDocumentImageCount = response.body().getSecondary_document_image_count();
                            GlobalValue.bankStatementImageCount = response.body().getBank_statement_image_count();
                            GlobalValue.otherLoanCardImageCount = response.body().getOther_loan_card_image_count();




                        } else {
                            NetworkUtils.handleErrorsForAPICalls(HouseDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CustomerApplicantCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void getHouseVisitCommonDatas() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<HouseVisitCommonDataResponseModel> call = apiService.getHouseVisitCommonDatas();
            call.enqueue(new Callback<HouseVisitCommonDataResponseModel>() {
                @Override
                public void onResponse(Call<HouseVisitCommonDataResponseModel> call, final Response<HouseVisitCommonDataResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true


                            GlobalValue.houseTypesModelArrayList = response.body().getHouseTypesModels();
                            GlobalValue.roofTypesModelArrayList = response.body().getRoofTypesModels();
                            GlobalValue.wallTypesModelArrayList = response.body().getWallTypesModels();
                            GlobalValue.kitchenTypesModelArrayList = response.body().getKitchenTypesModels();
                            GlobalValue.toiletsModelArrayList = response.body().getToiletsModels();
                            GlobalValue.noOfRoomsModelArrayList = response.body().getNoOfRoomsModels();
                            GlobalValue.stayLengthsModelArrayList =  response.body().getStayLengthsModels();
                            GlobalValue.marriedStatusModelArrayList = response.body().getMarriedStatusModels();
                            GlobalValue.agriculturalLandsModelArrayList = response.body().getAgriculturalLandsModels();
                            GlobalValue.cattlesModelArrayList = response.body().getNoOfCattlesModels();
                            GlobalValue.familyTypesModelArrayList = response.body().getFamilyTypesModels();
                            GlobalValue.illMembersModelArrayList = response.body().getIllMembersModels();
                            GlobalValue.houseImageCount = response.body().getHouse_image_count();
                            GlobalValue.socialParametersModelArrayList = response.body().getSocialParametersModels();


                            GlobalValue.pastEventsModelArrayList =  response.body().getPastEventsModels();
                            GlobalValue.upcomingEventsModelArrayList = response.body().getUpcomingEventsModels();


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(HouseDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }



                @Override
                public void onFailure(Call<HouseVisitCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void getHouseVisitStatus() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<HouseVisitStatusResponseModel> call = apiService.getHouseVisitStatus(loanTakerID);
            call.enqueue(new Callback<HouseVisitStatusResponseModel>() {
                @Override
                public void onResponse(Call<HouseVisitStatusResponseModel> call, final Response<HouseVisitStatusResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponse(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(HouseDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<HouseVisitStatusResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(HouseDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(HouseVisitStatusResponseModel body) {

        userNameTv.setText(body.getHouseVisitStatusModel().getName());
        userCoTv.setText("C.o. "+body.getHouseVisitStatusModel().getAadhar_co());
        phoneNumber = body.getHouseVisitStatusModel().getPrimary_phone_number();

        GlobalValue.loanTakerIdForAnalytics = body.getHouseVisitStatusModel().getLoan_taker_id();

        Picasso.with(HouseDetailsActivity.this)
                .load(body.getHouseVisitStatusModel().getProfileImages().getMedium())
                .placeholder(R.drawable.userimg_placeholder)
                .into(profileImageView);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(body));


        if(body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getHouseInfoModel().getActivated()){

            UiUtils.setProcessActivated(HouseDetailsActivity.this,houseInformationTv);
            if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_house_info").has("completed")) {

                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_house_info").getString("completed") == null) {

                } else {
                    if (body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getHouseInfoModel().getCompleted()=="true") {
                        UiUtils.setProcessCompleted(HouseDetailsActivity.this, houseInformationTv);
                        isHouseInfoCompleted = true;
                    } else {
                        UiUtils.setProcessNotCompleteWithImaged(HouseDetailsActivity.this, houseInformationTv);
                    }
                }
            }

        }else{
            UiUtils.setProcessNotCompleted(HouseDetailsActivity.this,houseInformationTv);
        }

        if(body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getPersonalInfoModel().getActivated()){

            UiUtils.setProcessActivated(HouseDetailsActivity.this,personalInformationTv);
            if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_personal_info").has("completed")) {
                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_personal_info").getString("completed") == null) {

                } else {

                    if (body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getPersonalInfoModel().getCompleted()=="true") {
                        UiUtils.setProcessCompleted(HouseDetailsActivity.this, personalInformationTv);
                        isPersonalInfoCompleted = true;
                    } else {
                        UiUtils.setProcessNotCompleteWithImaged(HouseDetailsActivity.this, personalInformationTv);
                    }
                }
            }

        }else {
            UiUtils.setProcessNotCompleted(HouseDetailsActivity.this,personalInformationTv);
        }

        if(body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getApplicantDocumentModel().getActivated()){

            UiUtils.setProcessActivated(HouseDetailsActivity.this,applicantDocumentsTv);
            if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_applicant_doc").has("completed")) {
                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_applicant_doc").getString("completed") == null) {

                } else {
                    if (body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getApplicantDocumentModel().getCompleted()=="true") {
                        UiUtils.setProcessCompleted(HouseDetailsActivity.this, applicantDocumentsTv);
                        isApplicantDocumentsCompleted = true;
                    } else {
                        UiUtils.setProcessNotCompleteWithImaged(HouseDetailsActivity.this, applicantDocumentsTv);
                    }
                }
            }

        }else {
            UiUtils.setProcessNotCompleted(HouseDetailsActivity.this,applicantDocumentsTv);
        }

        if(body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getCoApplicantDocumentModel().getActivated()){

            UiUtils.setProcessActivated(HouseDetailsActivity.this,coApplicantDocumentsTv);
            if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_co_applicant_doc").has("completed")) {
                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_co_applicant_doc").getString("completed") == null) {

                } else {
                    if (body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getCoApplicantDocumentModel().getCompleted()=="true") {
                        UiUtils.setProcessCompleted(HouseDetailsActivity.this, coApplicantDocumentsTv);
                        isCoApplicantCompleted = true;
                    } else {
                        UiUtils.setProcessNotCompleteWithImaged(HouseDetailsActivity.this, coApplicantDocumentsTv);
                    }
                }
            }

        }else {
            UiUtils.setProcessNotCompleted(HouseDetailsActivity.this,coApplicantDocumentsTv);
        }

        if(body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getHouseImageModel().getActivated()){

            UiUtils.setProcessActivated(HouseDetailsActivity.this,housePhotosTv);
            if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_house_image").has("completed")) {
                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_house_image").getString("completed") == null) {

                } else {
                    if (body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getHouseImageModel().getCompleted()=="true") {
                        UiUtils.setProcessCompleted(HouseDetailsActivity.this, housePhotosTv);
                        isHousePhotosCompleted = true;
                    } else {
                        UiUtils.setProcessNotCompleteWithImaged(HouseDetailsActivity.this, housePhotosTv);
                    }
                }
            }

        }else {
            UiUtils.setProcessNotCompleted(HouseDetailsActivity.this,housePhotosTv);
        }

        if(body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getDeclarationModel().getActivated()){

            UiUtils.setProcessActivated(HouseDetailsActivity.this,declarationTv);
            if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_signed").has("completed")) {
                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_signed").getString("completed") == null) {

                } else {
                    if (body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getDeclarationModel().getCompleted()=="true") {
                        UiUtils.setProcessCompleted(HouseDetailsActivity.this, declarationTv);
                        isDeclarationCompleted = true;
                        GlobalValue.isHouseVisitDeclarationCompleted = true;
                    } else {
                        UiUtils.setProcessNotCompleteWithImaged(HouseDetailsActivity.this, declarationTv);
                    }
                }
            }

        }else {
            UiUtils.setProcessNotCompleted(HouseDetailsActivity.this,declarationTv);
        }


        if(body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getRatingModel().getActivated()){

            UiUtils.setProcessActivated(HouseDetailsActivity.this,ratingTv);
            if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_rating").has("completed")) {
                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("house_visit_rating").getString("completed") == null) {

                } else {
                    if (body.getHouseVisitStatusModel().getHvStatusResponseModel().getStatusModel().getRatingModel().getCompleted()=="true") {
                        UiUtils.setProcessCompleted(HouseDetailsActivity.this, ratingTv);
                        isRatingCompleted = true;
                    } else {
                        UiUtils.setProcessNotCompleteWithImaged(HouseDetailsActivity.this, ratingTv);
                    }
                }
            }

        }else {
            UiUtils.setProcessNotCompleted(HouseDetailsActivity.this,ratingTv);
        }

        setButtonValues(body.getHouseVisitStatusModel().getHvStatusResponseModel().getNext_action());
        buttonValue = body.getHouseVisitStatusModel().getHvStatusResponseModel().getNext_action();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setButtonValues(String next_action) {

        switch (next_action){

            case "house_visit_house_info":
                continueButton.setText("Move to "+houseInformationTv.getText().toString());
                break;

            case "house_visit_personal_info":
                continueButton.setText("Move to "+personalInformationTv.getText().toString());
                break;

            case "house_visit_applicant_doc":
                continueButton.setText("Move to "+applicantDocumentsTv.getText().toString());
                break;


            case "house_visit_co_applicant_doc":
                continueButton.setText("Move to "+coApplicantDocumentsTv.getText().toString());
                break;

            case "house_visit_house_image":
                continueButton.setText("Move to "+housePhotosTv.getText().toString());
                break;

            case "house_visit_signin":
                continueButton.setText("Move to "+declarationTv.getText().toString());
                break;

            case "house_visit_rating":
                continueButton.setText("Move to "+ratingTv.getText().toString());
                break;

            case "":
                continueButton.setVisibility(View.GONE);
                continueButton.setText("Continue");
                break;
        }
    }

    public static HouseDetailsActivity getInstance() {
        return instance;
    }

    public void showProgressDialog(Context context) {
        if (!((Activity) context).isFinishing()) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context, R.style.DialogCustom);
                mProgressDialog.setMessage(context.getString(R.string.loading));
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }

    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.right_action_bar_button:
                showPopup();
                break;

            case R.id.house_information_tv:
                if(isHouseInfoCompleted)
                    goToHouseEditInfo();
                break;

            case R.id.personal_information_tv:
                if(isPersonalInfoCompleted)
                    goToEditPersonalInfo();
                break;


            case R.id.applicant_documents_tv:
                if(isApplicantDocumentsCompleted)
                    goToApplicantDoc();
                break;

            case R.id.co_applicant_documents_tv:
                if(isCoApplicantCompleted)
                    goToCoApplicantDoc();
                break;

            case R.id.house_photos_tv:
                if(isHousePhotosCompleted)
                    goToHousePhotos();
                break;

            case R.id.declaration_tv:
                if(isDeclarationCompleted)
                    goToDeclaraedForm();
                else{
                    goToDeclaration();
                }
                break;

            case R.id.rating_tv:
                if(isRatingCompleted)
                    goToHouseRating();
                break;

            case R.id.continue_button:

                switch (buttonValue){

                    case "house_visit_house_info":
                        if(isHouseInfoCompleted)
                            goToHouseEditInfo();
                        else
                            goToCreateHouseInfo();
                        break;

                    case "house_visit_personal_info":
                        if(isPersonalInfoCompleted)
                            goToEditPersonalInfo();
                        else
                        goToCreatePersonalInfo();
                        break;

                    case "house_visit_applicant_doc":
                        goToApplicantDoc();
                        break;


                    case "house_visit_co_applicant_doc":
                        goToCoApplicantDoc();
                        break;

                    case "house_visit_house_image":
                        goToHousePhotos();
                        break;


                    case "house_visit_signin":
                        goToDeclaration();
                        break;

                    case "house_visit_rating":
                        goToHouseRating();
                        break;

                    case "":
                        Toast.makeText(instance, "Will be updated soon", Toast.LENGTH_SHORT).show();
                        break;
                }


                break;
        }
    }




    public void requestFocus(View view) {
        if (view.requestFocus())
            HouseDetailsActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private class InputLayoutTextWatcher implements TextWatcher {

        private View view;
        ArrayList<String> itemPassed = new ArrayList<String>();

        private InputLayoutTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {


            switch (view.getId()) {

                case R.id.password_otp_view:
                    itemPassed.clear();
                    itemPassed.add("otp");
                    boolean passwordStatus = UiUtils.checkValidationForOTP(HouseDetailsActivity.this, passwordOTPView,passwordErrorTV);

                    if (passwordStatus == false) {

                        requestFocus(passwordOTPView);
                    } else {

                        passwordOTPView.setLineColor(getResources().getColor(R.color.primaryColor));

                    }
                    break;


                case R.id.input_reason:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean reasonStatus = UiUtils.checkValidation(HouseDetailsActivity.this, reasonET,reasonLayout,itemPassed);

                    if (reasonStatus == false) {
                        requestFocus(reasonET);
                    } else {
                        reasonLayout.setErrorEnabled(false);
                    }
                    break;

            }
        }

    }

    private void showReasonForRejectionPopup(){
        View view = getLayoutInflater().inflate(R.layout.custom_rejection_dialog, null);
         reasonLayout =(TextInputLayout)view.findViewById(R.id.input_layout_reason);
         reasonET =(TextInputEditText)view.findViewById(R.id.input_reason);
        reasonET.addTextChangedListener(new InputLayoutTextWatcher(reasonET));
        AlertDialog.Builder builder = new AlertDialog.Builder(HouseDetailsActivity.this);
        builder.setCancelable(false)
                .setTitle("Reason for Rejection ")
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(HouseDetailsActivity.this, "yyy", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new RejectListener(alertDialog));


    }

    private class RejectListener implements View.OnClickListener {

        private Dialog dialog;

        public RejectListener(AlertDialog alertDialog) {
            this.dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here

            if(UiUtils.checkValidation(HouseDetailsActivity.this, reasonET,reasonLayout,new ArrayList<String>())){
                reason = reasonET.getText().toString();
                this.dialog.dismiss();
                showPasswordDialog();
            }

        }
    }

    private void showAreYouSurePopUp(){
        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText(getResources().getString(R.string.hint_remove_member_from_message));
        yesButton.setText(getResources().getString(R.string.hint_yes));
        noButton.setText(getResources().getString(R.string.hint_no));
        noButton.setTextAppearance(HouseDetailsActivity.this,R.style.MyBluePopUpButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(HouseDetailsActivity.this);
        builder.setCancelable(false)
                .setTitle(getResources().getString(R.string.hint_remove_member_from_title))
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showReasonForRejectionPopup();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void showPasswordDialog(){
        // title, custom view, actions dialog

        passcode= null;
        View view = getLayoutInflater().inflate(R.layout.custom_password_dialog, null);
        passwordOTPView=(OtpView) view.findViewById(R.id.password_otp_view);
        passwordErrorTV =(TextView)view.findViewById(R.id.password_error_tv);
        passwordOTPView.addTextChangedListener(new InputLayoutTextWatcher(passwordOTPView));
        AlertDialog.Builder builder = new AlertDialog.Builder(HouseDetailsActivity.this);
        builder.setCancelable(false)
                .setTitle("Enter Passcode")
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(HouseDetailsActivity.this, "yyy", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog));

    }


    private class CustomListener implements View.OnClickListener {

        public CustomListener(AlertDialog alertDialog) {
            dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
            passcode = passwordOTPView.getText().toString();
//            Toast.makeText(CenterConfirmationActivity.this, ""+mValue, Toast.LENGTH_SHORT).show();
            if(UiUtils.checkValidationForOTP(HouseDetailsActivity.this, passwordOTPView,passwordErrorTV)){
                removeMember();
            }
        }
    }


    private void removeMember(){

        Log.i("aadh passcode", "--" + passcode+loanTakerID);

        showProgressDialog(HouseDetailsActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<CalculateEMIResponseModel> call = apiService.removeOnFormData(loanTakerID,
                    passcode,reason);

            call.enqueue(new Callback<CalculateEMIResponseModel>() {
                @Override
                public void onResponse(Call<CalculateEMIResponseModel> call, Response<CalculateEMIResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            dialog.dismiss();
                            Toast.makeText(HouseDetailsActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToHouseMemberListingPage();
                        } else {
                            Log.d("aadh onResponse", "" + response.body().getErrors());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                Log.d("aadh onResponse type", "" + jsonObject.getString("type"));
                                Log.d("aadh onResponse json", "" + jsonObject);

                                if(jsonObject.getString("type").equalsIgnoreCase("invalid_passcode")){
                                    passwordOTPView.setLineColor(getResources().getColor(R.color.errorColor));
                                    passwordErrorTV.setVisibility(View.VISIBLE);
                                    passwordErrorTV.setText("Invalid Passcode");
                                    Toast.makeText(HouseDetailsActivity.this, "Invalid passcode", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            NetworkUtils.handleErrorsCasesForAPICalls(HouseDetailsActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        hideProgressDialog();
                        Log.i("aadh Error", "--" + new Gson().toJson(response.body()));
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseDetailsActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CalculateEMIResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(HouseDetailsActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }

    }

    private void goToHouseMemberListingPage() {
        finish();
        HouseVisitMemberListingActivity.getInstance().init();
    }


    private void showPopup() {
        View view = getLayoutInflater().inflate(R.layout.custom_house_call_remove_dialog, null);
        callMemberTv=(TextView) view.findViewById(R.id.call_member_tv);
        removeMemberTv=(TextView) view.findViewById(R.id.remove_member_tv);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(HouseDetailsActivity.this, "yyy", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CancelCustomListener(alertDialog));

        callMemberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                call(phoneNumber);
                alertDialog.dismiss();

            }
        });


        removeMemberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showAreYouSurePopUp();
            }
        });
    }


    private class CancelCustomListener implements View.OnClickListener {

        public CancelCustomListener(AlertDialog alertDialog) {
            dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
            dialog.dismiss();
        }
    }


    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }


    public void call(String phoneNumber){
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            Toast.makeText(HouseDetailsActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToEditPersonalInfo() {
        startActivity(new Intent(HouseDetailsActivity.this,PersonalInformationEditActivity.class));

    }

    private void goToHouseEditInfo() {
        startActivity(new Intent(HouseDetailsActivity.this,HouseInfoEditActivity.class));
    }

    private void goToHousePhotos() {
        startActivity(new Intent(HouseDetailsActivity.this,HousePhotosActivity.class));
    }

    private void goToCoApplicantDoc() {
        startActivity(new Intent(HouseDetailsActivity.this,HouseVisitCoApplicantDocActivity.class));
    }

    private void goToApplicantDoc() {
        startActivity(new Intent(HouseDetailsActivity.this,HouseVisitApplicantDocActivity.class));
    }

    private void goToCreatePersonalInfo() {
        startActivity(new Intent(HouseDetailsActivity.this,PersonalInformationActivity.class));
    }

    private void goToCreateHouseInfo() {
        startActivity(new Intent(HouseDetailsActivity.this,HouseInfoCreateActivity.class));
    }


    private void goToDeclaration() {

        startActivity(new Intent(HouseDetailsActivity.this,HouseVisitDeclarationActivity.class));
    }

    private void goToDeclaraedForm() {

        startActivity(new Intent(HouseDetailsActivity.this,SignatureShowActivity.class));
    }

    private void goToHouseRating() {

        startActivity(new Intent(HouseDetailsActivity.this,HouseVisitRatingActivity.class));
    }
}
