package com.tailwebs.aadharindia.postapproval;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.postapproval.models.postapprovalstatus.IndividualPostApprovalResponseModel;
import com.tailwebs.aadharindia.member.models.LoanTakerCustomerDetailResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualMemberDetailActivity extends BaseActivity implements View.OnClickListener {



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

    @BindView(R.id.loan_details_tv)
    TextView loanDetailsTv;

    @BindView(R.id.show_other_charges_tv)
    TextView showOtherChargesTv;

    @BindView(R.id.e_sign_documents_tv)
    TextView eSignDocumentsTv;

    @BindView(R.id.insurance_docs_tv)
    TextView insuranceDocsTv;

    @BindView(R.id.signature_tv)
    TextView signatureTv;

    @BindView(R.id.e_sign_documents_iv)
    ImageView eSignDocumentsIv;

    @BindView(R.id.insurance_docs_iv)
    ImageView insuranceDocsIv;

    @BindView(R.id.signature_iv)
    ImageView signatureIv;


    @BindView(R.id.continue_button)
    Button continueButton;

    //choose value from intent;
    String loanTakerID;
    private ProgressDialog mProgressDialog;

    private FirebaseAnalytics mFirebaseAnalytics;

    String name=null,co_name=null, approvedLoan=null,processingFee=null,emiAmount=null,firstEmiAMount=null,emiCollectionDate=null,interestAmount=null,
            adminCharges=null,insuranceAmount=null,disbursalAmount=null,buttonValue=null;

    private boolean isESignCompleted = false,isInsuranceCompleted=false,isSignatureCompleted=false;
    static IndividualMemberDetailActivity instance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_member_detail);
        ButterKnife.bind(this);
        instance= this;


        loanTakerID =  GlobalValue.loanTakerId;


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "PA Member Detail", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Member Details");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        init();





        showOtherChargesTv.setOnClickListener(this);
        eSignDocumentsTv.setOnClickListener(this);
        insuranceDocsTv.setOnClickListener(this);
        signatureTv.setOnClickListener(this);
        eSignDocumentsIv.setOnClickListener(this);
        insuranceDocsIv.setOnClickListener(this);
        signatureIv.setOnClickListener(this);


    }

    public void init() {

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(IndividualMemberDetailActivity.this);
            getPostApprovalStatus();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }




    public static IndividualMemberDetailActivity getInstance() {
        return instance;
    }

    private void getPostApprovalStatus() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<IndividualPostApprovalResponseModel> call = apiService.getIndividualPostApprovalStatus(loanTakerID);
            call.enqueue(new Callback<IndividualPostApprovalResponseModel>() {
                @Override
                public void onResponse(Call<IndividualPostApprovalResponseModel> call, final Response<IndividualPostApprovalResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponseForStatus(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(IndividualMemberDetailActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(IndividualMemberDetailActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<IndividualPostApprovalResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(IndividualMemberDetailActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponseForStatus(IndividualPostApprovalResponseModel body) {

        GlobalValue.loanTakerIdForAnalytics = body.getIndividualPostApprovalModel().getLoan_taker_id();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(body));

            if(body.getIndividualPostApprovalModel().getIpaStatusResponseModel().getStatusModel().geteSignDocumentsModel().getActivated()){

                UiUtils.setProcessActivated(IndividualMemberDetailActivity.this,eSignDocumentsTv);
//

                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("esign_document").has("completed") ){


                    if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("esign_document").getString("completed") ==  null){

                    }else {
                        if (body.getIndividualPostApprovalModel().getIpaStatusResponseModel().getStatusModel().geteSignDocumentsModel().getCompleted()=="true") {
                            UiUtils.setProcessCompleted(IndividualMemberDetailActivity.this, eSignDocumentsTv);
                            isESignCompleted = true;
                        } else {
                            UiUtils.setProcessNotCompleteWithImaged(IndividualMemberDetailActivity.this, eSignDocumentsTv);
                        }
                    }
                }

            }else{
                UiUtils.setProcessNotCompleted(IndividualMemberDetailActivity.this,eSignDocumentsTv);
            }

            if(body.getIndividualPostApprovalModel().getIpaStatusResponseModel().getStatusModel().getInsuranceDocumentsModel().getActivated()){

                UiUtils.setProcessActivated(IndividualMemberDetailActivity.this,insuranceDocsTv);
                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("insurance_form").has("completed") ) {

                    if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("insurance_form").getString("completed") == null) {

                    } else {
                        if (body.getIndividualPostApprovalModel().getIpaStatusResponseModel().getStatusModel().getInsuranceDocumentsModel().getCompleted() == "true") {
                            UiUtils.setProcessCompleted(IndividualMemberDetailActivity.this, insuranceDocsTv);
                            isInsuranceCompleted = true;
                        } else {
                            UiUtils.setProcessNotCompleteWithImaged(IndividualMemberDetailActivity.this, insuranceDocsTv);
                        }
                    }
                }

            }else {
                UiUtils.setProcessNotCompleted(IndividualMemberDetailActivity.this,insuranceDocsTv);
            }


            if(body.getIndividualPostApprovalModel().getIpaStatusResponseModel().getStatusModel().getSignatureModel().getActivated()){

                UiUtils.setProcessActivated(IndividualMemberDetailActivity.this,signatureTv);
                if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("signature").has("completed") ) {

                    if (jsonObject.getJSONObject("loan_taker").getJSONObject("status").getJSONObject("status").getJSONObject("signature").getString("completed") == null) {

                    } else {
                        if (body.getIndividualPostApprovalModel().getIpaStatusResponseModel().getStatusModel().getSignatureModel().getCompleted() == "true") {
                            UiUtils.setProcessCompleted(IndividualMemberDetailActivity.this, signatureTv);
                            isSignatureCompleted = true;
                        } else {
                            UiUtils.setProcessNotCompleteWithImaged(IndividualMemberDetailActivity.this, signatureTv);
                        }
                    }
                }

            }else {
                UiUtils.setProcessNotCompleted(IndividualMemberDetailActivity.this,signatureTv);
            }



            setButtonValues(body.getIndividualPostApprovalModel().getIpaStatusResponseModel().getNext_action());
            buttonValue = body.getIndividualPostApprovalModel().getIpaStatusResponseModel().getNext_action();


            if (NetworkUtils.haveNetworkConnection(this)) {
                showProgressDialog(IndividualMemberDetailActivity.this);
                getCustomerDetails();
            } else {
                UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    private void setButtonValues(String next_action) {

        switch (next_action){

            case "esign_document":
                continueButton.setText("Move to "+eSignDocumentsTv.getText().toString());
                break;

            case "insurance_form":
                continueButton.setText("Move to "+insuranceDocsTv.getText().toString());
                break;


            case "signature":
                continueButton.setText("Move to "+signatureTv.getText().toString());
                break;

            case "":
                continueButton.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.e_sign_documents_tv:
                goToDeclaration();
                break;

            case R.id.e_sign_documents_iv:
                goToDeclaration();
                break;

            case R.id.insurance_docs_tv:
                goToInsurance();
                break;

            case R.id.insurance_docs_iv:
                goToInsurance();
                break;

            case R.id.signature_tv:
                goToSignature();
                break;


            case R.id.signature_iv:
                goToSignature();
                break;



            case R.id.show_other_charges_tv:

                Intent intent =  new Intent(IndividualMemberDetailActivity.this,ShowOtherChargesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                bundle.putString("co_name",co_name);
                bundle.putString("approved_loan",approvedLoan);
                bundle.putString("processing_fee",processingFee);
                bundle.putString("admin_charges",adminCharges);
                bundle.putString("insurance",insuranceAmount);
                bundle.putString("disbursal",disbursalAmount);
                bundle.putString("emi_amount",emiAmount);
                bundle.putString("first_emi_amount",firstEmiAMount);
                bundle.putString("emi_collection_date",emiCollectionDate);
                bundle.putString("interest_amount",interestAmount);
                intent.putExtras(bundle);
                startActivity(intent);
                break;



            case R.id.continue_button:

                switch (buttonValue){

                    case "esign_document":
                        goToDeclaration();
                        break;

                    case "insurance_form":
                        goToInsurance();
                        break;

                    case "signature":
                        goToSignature();
                        break;

                    case "":
//                      goToDeclaration();
                        break;
                }



        }
    }

    private void goToSignature() {
        if(isSignatureCompleted){
            startActivity(new Intent(IndividualMemberDetailActivity.this,PostApprovalSignatureShowActivity.class));
        }else{
            startActivity(new Intent(IndividualMemberDetailActivity.this,PostApprovalSignatureActivity.class));
        }

    }

    private void goToInsurance(){
        startActivity(new Intent(IndividualMemberDetailActivity.this,InsuranceFormActivity.class));
    }

    private void goToDeclaration(){
        startActivity(new Intent(IndividualMemberDetailActivity.this,PostDocumentsDeclarationActivity.class));
    }

    private void getCustomerDetails() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCustomerDetailResponseModel> call = apiService.getCustomerDetails(loanTakerID);
            call.enqueue(new Callback<LoanTakerCustomerDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCustomerDetailResponseModel> call, final Response<LoanTakerCustomerDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponse(response.body());




                        } else {
                            NetworkUtils.handleErrorsForAPICalls(IndividualMemberDetailActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(IndividualMemberDetailActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCustomerDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(IndividualMemberDetailActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerCustomerDetailResponseModel body) {

        userNameTv.setText(body.getLoanTakerModel().getName());
        userCoTv.setText(body.getLoanTakerModel().getAadhar_co());

        Picasso.with(IndividualMemberDetailActivity.this)
                .load(body.getLoanTakerModel().getProfileImages().getMedium())
                .placeholder(R.drawable.userimg_placeholder)
                .into(profileImageView);

        loanDetailsTv.setText(body.getLoanTakerModel().getApprovedLoanDetailModel().getApproved_amount_in_format()+" for "+
                body.getLoanTakerModel().getApprovedLoanDetailModel().getTenure()+" Months @ "+body.getLoanTakerModel().getApprovedLoanDetailModel().getRate_of_interest()+"% P.A.");


        name = body.getLoanTakerModel().getName();
        co_name = body.getLoanTakerModel().getAadhar_co();
        approvedLoan =body.getLoanTakerModel().getApprovedLoanDetailModel().getApproved_amount_in_format()+" for "+
                body.getLoanTakerModel().getApprovedLoanDetailModel().getTenure()+" Months @ "+body.getLoanTakerModel().getApprovedLoanDetailModel().getRate_of_interest()+"% P.A.";

        processingFee = body.getLoanTakerModel().getApprovedLoanDetailModel().getProcessing_fee_without_gst_in_format() +"  +  "+body.getLoanTakerModel().getApprovedLoanDetailModel().getProcessing_fee_gst_in_format()+
                "   =   "+body.getLoanTakerModel().getApprovedLoanDetailModel().getFinal_processing_fee_in_format();

        adminCharges =  body.getLoanTakerModel().getApprovedLoanDetailModel().getAdmin_charge_in_format() +"  +  "+body.getLoanTakerModel().getApprovedLoanDetailModel().getAdmin_charge_gst_in_format()+
                "   =   "+body.getLoanTakerModel().getApprovedLoanDetailModel().getFinal_admin_charge_in_format();

        insuranceAmount = body.getLoanTakerModel().getApprovedLoanDetailModel().getApplicant_insurance_amount_in_format() +"  +  "+body.getLoanTakerModel().getApprovedLoanDetailModel().getCo_aplicant_insurance_amount_in_format()+
                "   =   "+body.getLoanTakerModel().getApprovedLoanDetailModel().getInsurance_amount_in_format();

        disbursalAmount = body.getLoanTakerModel().getApprovedLoanDetailModel().getFinal_disbursal_amount_in_format();

        emiAmount = body.getLoanTakerModel().getApprovedLoanDetailModel().getEmi_amount_in_format();

        firstEmiAMount = body.getLoanTakerModel().getApprovedLoanDetailModel().getFirst_emi_amount();



        emiCollectionDate = body.getLoanTakerModel().getApprovedLoanDetailModel().getEmi_collection_date();

        interestAmount = body.getLoanTakerModel().getApprovedLoanDetailModel().getTotal_loan_interest();

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
