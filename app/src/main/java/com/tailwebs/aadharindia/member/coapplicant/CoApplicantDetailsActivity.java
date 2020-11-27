package com.tailwebs.aadharindia.member.coapplicant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.applicant.AddCustomerDetailsActivity;
import com.tailwebs.aadharindia.member.applicant.ApplicantDetailActivity;
import com.tailwebs.aadharindia.member.cashincome.CashIncomeDetailsActivity;
import com.tailwebs.aadharindia.member.coapplicant.models.CoApplicantStatusResponseModel;
import com.tailwebs.aadharindia.models.common.CustomerApplicantCommonDataResponseModel;
import com.tailwebs.aadharindia.models.common.CustomerCoRelationResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoApplicantDetailsActivity extends BaseActivity implements View.OnClickListener {

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    //categories

    @BindView(R.id.co_applicant_tv)
    TextView coApplicantTv;
    
    @BindView(R.id.co_applicant_documents_tv)
    TextView coApplicantDocumentsTv;

    @BindView(R.id.family_tv)
    TextView familyTv;
    

    //user

    @BindView(R.id.user_name)
    TextView userNameTv;

    @BindView(R.id.profile_image)
    ImageView profileImageView;

    @BindView(R.id.user_co)
    TextView userCoTv;


    @BindView(R.id.continue_button)
    Button continueButton;

    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null;

    static CoApplicantDetailsActivity instance;

    private boolean isCoApplicantCompleted = false,isCoApplicantDocumentsCompleted=false,isFamilyCompleted=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_applicant_details);
        ButterKnife.bind(this);
        instance=this;

        loanTakerID = GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Co-Applicant Detail");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        if(!getIntent().hasExtra("page_value")){
            Intent intent = new Intent(CoApplicantDetailsActivity.this,AddNewCoApplicantScanActivity.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
        }

        init();
    }

    public static CoApplicantDetailsActivity getInstance() {
        return instance;
    }

    public void init() {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Co-Applicant Detail", null);

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(CoApplicantDetailsActivity.this);
            getRelationDropDown();
            getApplicantCommonDatas();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

        continueButton.setOnClickListener(this);
        coApplicantTv.setOnClickListener(this);
        coApplicantDocumentsTv.setOnClickListener(this);
        familyTv.setOnClickListener(this);



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
                            GlobalValue.applicantGenderList =  response.body().getGenders();


                            if (NetworkUtils.haveNetworkConnection(CoApplicantDetailsActivity.this)) {
                                showProgressDialog(CoApplicantDetailsActivity.this);
                                getCoApplicantStatus();
                            } else {
                                UiUtils.showAlertDialogWithOKButton(CoApplicantDetailsActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                            }



                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CoApplicantDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CustomerApplicantCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    private void getRelationDropDown() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CustomerCoRelationResponseModel> call = apiService.getCoApplicantRelation(loanTakerID);
            call.enqueue(new Callback<CustomerCoRelationResponseModel>() {
                @Override
                public void onResponse(Call<CustomerCoRelationResponseModel> call, final Response<CustomerCoRelationResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.i("Drools", "" + response.body().getRelationModels().size());
                            if(response.body().getRelationModels().size() >0){

                                GlobalValue.relationModels = response.body().getRelationModels();

                                Log.i("Drools", "" + "done");
                            }



                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CoApplicantDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call <CustomerCoRelationResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    private void getCoApplicantStatus() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CoApplicantStatusResponseModel> call = apiService.getCoApplicantStatus(loanTakerID);
            call.enqueue(new Callback<CoApplicantStatusResponseModel>() {
                @Override
                public void onResponse(Call<CoApplicantStatusResponseModel> call, final Response<CoApplicantStatusResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponse(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CoApplicantDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CoApplicantDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CoApplicantStatusResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CoApplicantDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(CoApplicantStatusResponseModel body) {

        userNameTv.setText(body.getCoApplicantStatusModel().getName());
        userCoTv.setText("C.o. "+body.getCoApplicantStatusModel().getAadhar_co());
        GlobalValue.loanTakerIdForAnalytics = body.getCoApplicantStatusModel().getLoan_taker_id();

        Picasso.with(CoApplicantDetailsActivity.this)
                .load(body.getCoApplicantStatusModel().getProfileImages().getMedium())
                .placeholder(R.drawable.userimg_placeholder)
                .into(profileImageView);

        Log.i("Drools", "" + body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getCoApplicantDetailModel().getActivated());
        Log.i("Drools", "" + body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getCoApplicantDetailModel().getCompleted());

        if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getCoApplicantDetailModel().getActivated()){

            coApplicantTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            coApplicantTv.setTextAppearance(this,R.style.MyFormHeadingTextView);
            coApplicantTv.setEnabled(true);
            Log.i("Drools", "getActivated");
//            UiUtils.setProcessActivated(CoApplicantDetailsActivity.this,coApplicantTv);
            if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getCoApplicantDetailModel().getCompleted()){
//                UiUtils.setProcessCompleted(CoApplicantDetailsActivity.this,coApplicantTv);
                coApplicantTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                coApplicantTv.setTextAppearance(this,R.style.MyFormHeadingTextView);
                coApplicantTv.setEnabled(true);

                isCoApplicantCompleted=true;
                Log.i("Drools", "getCompleted");
            }

        }else{
//            UiUtils.setProcessNotCompleted(CoApplicantDetailsActivity.this,coApplicantTv);
            coApplicantTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            coApplicantTv.setTextAppearance(this,R.style.MyDisabledFormHeadingTextView);
            coApplicantTv.setEnabled(false);
            Log.i("Drools", "false");
        }


        if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getDocumentsModel().getActivated()){

            coApplicantDocumentsTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            coApplicantDocumentsTv.setTextAppearance(this,R.style.MyFormHeadingTextView);
            coApplicantDocumentsTv.setEnabled(true);
//            UiUtils.setProcessActivated(CoApplicantDetailsActivity.this,coApplicantTv);
            if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getDocumentsModel().getCompleted()){
//                UiUtils.setProcessCompleted(CoApplicantDetailsActivity.this,coApplicantTv);
                coApplicantDocumentsTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                coApplicantDocumentsTv.setTextAppearance(this,R.style.MyFormHeadingTextView);
                coApplicantDocumentsTv.setEnabled(true);

                isCoApplicantDocumentsCompleted = true;
            }

        }else{
//            UiUtils.setProcessNotCompleted(CoApplicantDetailsActivity.this,coApplicantTv);
            coApplicantDocumentsTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            coApplicantDocumentsTv.setTextAppearance(this,R.style.MyDisabledFormHeadingTextView);
            coApplicantDocumentsTv.setEnabled(false);
        }


        if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getFamilyModel().getActivated()){

            familyTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            familyTv.setTextAppearance(this,R.style.MyFormHeadingTextView);
            familyTv.setEnabled(true);
//            UiUtils.setProcessActivated(CoApplicantDetailsActivity.this,coApplicantTv);
            if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getFamilyModel().getCompleted()){
//                UiUtils.setProcessCompleted(CoApplicantDetailsActivity.this,coApplicantTv);
                familyTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
                familyTv.setTextAppearance(this,R.style.MyFormHeadingTextView);
                familyTv.setEnabled(true);

                isFamilyCompleted = true;
            }

        }else{
//            UiUtils.setProcessNotCompleted(CoApplicantDetailsActivity.this,coApplicantTv);
            familyTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            familyTv.setTextAppearance(this,R.style.MyDisabledFormHeadingTextView);
            familyTv.setEnabled(false);
        }




//        if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getDocumentsModel().getActivated()){
//
//            UiUtils.setProcessActivated(CoApplicantDetailsActivity.this,coApplicantDocumentsTv);
//            if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getDocumentsModel().getCompleted()){
//                UiUtils.setProcessCompleted(CoApplicantDetailsActivity.this,coApplicantDocumentsTv);
//            }
//
//        }else {
//            UiUtils.setProcessNotCompleted(CoApplicantDetailsActivity.this,coApplicantDocumentsTv);
//        }

//        if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getFamilyModel().getActivated()){
//
//            UiUtils.setProcessActivated(CoApplicantDetailsActivity.this,familyTv);
//            if(body.getCoApplicantStatusModel().getApplicantStatusModel().getStatusModel().getFamilyModel().getCompleted()){
//                UiUtils.setProcessCompleted(CoApplicantDetailsActivity.this,familyTv);
//            }
//
//        }else {
//            UiUtils.setProcessNotCompleted(CoApplicantDetailsActivity.this,familyTv);
//        }


        setButtonValues(body.getCoApplicantStatusModel().getApplicantStatusModel().getNext_action());
        buttonValue = body.getCoApplicantStatusModel().getApplicantStatusModel().getNext_action();

    }

    private void setButtonValues(String next_action) {
        switch (next_action){

            case "co_applicant":
                continueButton.setText("Move to "+coApplicantTv.getText().toString());
                break;

            case "co_applicant_documents_filled":
                continueButton.setText("Move to "+coApplicantDocumentsTv.getText().toString());
                break;

            case "family":
                continueButton.setText("Move to "+familyTv.getText().toString());
                break;

            case "income":
                continueButton.setText("Move to Income");
                break;


        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.continue_button:

                switch (buttonValue){

                    case "co_applicant":
                        if(isCoApplicantCompleted)
                            goToCoApplicant();
                        else
                            gotToCoApplicantDetails();
                        break;

                    case "co_applicant_documents_filled":
                        gotToCoApplicantDocuments();
                        break;

                    case "family":
                        gotToCoApplicantFamily();
                        break;
                    case "income":
                        goToCashIncome();

                        break;
                }


                break;

            case R.id.co_applicant_tv:
                if(isCoApplicantCompleted)
                    goToCoApplicant();
                break;

            case R.id.co_applicant_documents_tv:
                if(isCoApplicantDocumentsCompleted)
                    goToCoApplicantDocuments();
                break;


            case R.id.family_tv:
                if(isFamilyCompleted)
                    goToCoApplicantFamily();
                break;
        }

    }

    private void goToCashIncome() {
        Intent intent = new Intent(CoApplicantDetailsActivity.this,CashIncomeDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("applicant","finished");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void goToCoApplicantFamily() {
        startActivity(new Intent(CoApplicantDetailsActivity.this,CoApplicantFamilyActivity.class));
    }

    private void goToCoApplicantDocuments() {
        startActivity(new Intent(CoApplicantDetailsActivity.this,CoApplicantDocumentsActivity.class));
    }

    private void goToCoApplicant() {
        startActivity(new Intent(CoApplicantDetailsActivity.this,EditCoApplicantActivity.class));
    }

    private void gotToCoApplicantDocuments() {
        startActivity(new Intent(CoApplicantDetailsActivity.this,CoApplicantDocumentsActivity.class));
    }

    private void gotToCoApplicantFamily() {
        startActivity(new Intent(CoApplicantDetailsActivity.this,CoApplicantFamilyActivity.class));
    }

    private void gotToCoApplicantDetails() {
        startActivity(new Intent(CoApplicantDetailsActivity.this,AddNewCoApplicantScanActivity.class));
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
}
