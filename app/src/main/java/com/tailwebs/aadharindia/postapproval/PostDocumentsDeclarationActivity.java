package com.tailwebs.aadharindia.postapproval;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digio.in.esign2sdk.Digio;
import com.digio.in.esign2sdk.DigioConfig;
import com.digio.in.esign2sdk.DigioEnvironment;
import com.digio.in.esign2sdk.DigioServiceMode;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.postapproval.models.LoanTakerPostApprovalDocumentResponseModel;
import com.tailwebs.aadharindia.postapproval.models.digio.IndividualPostApprovalDigioResponseModel;
import com.tailwebs.aadharindia.housevisit.HouseVisitDeclarationActivity;
import com.tailwebs.aadharindia.inappbrowser.InAppBrowserActivity;
import com.tailwebs.aadharindia.models.DeclarationAPIResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDocumentsDeclarationActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;


    @BindView(R.id.loan_agreement_tv)
    TextView loanAgreementTv;


    @BindView(R.id.sanction_letter_tv)
    TextView sanctionLetterTv;


    @BindView(R.id.guarantors_tv)
    TextView guarantorsTv;

    @BindView(R.id.esign_cpf_button)
    Button esignCpfButton;

    @BindView(R.id.applicant_signature_tv)
            TextView applicantSignatureTv;


    @BindView(R.id.co_applicant_signature_tv)
            TextView coAPplicantSignatureTv;

    @BindView(R.id.digio_layout)
    LinearLayout digioSignatureLayout;

    @BindView(R.id.manual_layout)
    LinearLayout manualSignatureLayout;

    @BindView(R.id.applicant_signature_tv_m)
    TextView applicantSignatureTv2;

    @BindView(R.id.co_applicant_signature_tv_m)
    TextView coAPplicantSignatureTv2;

    @BindView(R.id.loan_agreement_tv_m)
    TextView loanAgreementTvm;




    String loanTakerID = null,loanAgreement=null,sanctionLetter=null,guarantors=null,documentId=null,
    applicantMobileNo=null,coApplicantMobileNo=null;

    boolean isApplicantSignatureDone=false,isCoApplicantSignatureDone=false,isApplicantSignatureClicked=false,isCoApplicantSignatureClicked,
            isValidLoanAgreementGenerated = false,isApplicantSignature2Done=false,isCoApplicantSignature2Done=false,
            isApplicantSignature2Clicked=false,isCoApplicantSignature2Clicked=false;



    @BindView(R.id.declaration_tv)
    TextView declarationTv;

    static PostDocumentsDeclarationActivity instance;
    IndividualPostApprovalDigioResponseModel individualPostApprovalDigioResponseModel;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_documents_declaration);
        ButterKnife.bind(this);
        instance = this;

        loanTakerID = GlobalValue.loanTakerId;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "PA Declaration and E-sign", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("E-Sign Documents");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        loanAgreementTv.setOnClickListener(this);
        sanctionLetterTv.setOnClickListener(this);
        guarantorsTv.setOnClickListener(this);
//        esignCpfButton.setOnClickListener(this);
        applicantSignatureTv.setOnClickListener(this);
        coAPplicantSignatureTv.setOnClickListener(this);
        applicantSignatureTv2.setOnClickListener(this);
        coAPplicantSignatureTv2.setOnClickListener(this);
        loanAgreementTvm.setOnClickListener(this);



        init();

       
        haveStoragePermission();


    }

    private void getDeclaration() {
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<DeclarationAPIResponseModel> call = apiService.getDeclarationPostApproval();
            call.enqueue(new Callback<DeclarationAPIResponseModel>() {
                @Override
                public void onResponse(Call<DeclarationAPIResponseModel> call, final Response<DeclarationAPIResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            setValuesFromResponse(response.body());


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(PostDocumentsDeclarationActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(PostDocumentsDeclarationActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<DeclarationAPIResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(PostDocumentsDeclarationActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    private void setValuesFromResponse(DeclarationAPIResponseModel body) {

        declarationTv.setText(body.getContent());

    }



    private void init(){
        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(PostDocumentsDeclarationActivity.this);
            getDigioDocumentDetails();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }


    private void getDigioDocumentDetails() {

        showProgressDialog(PostDocumentsDeclarationActivity.this);

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<IndividualPostApprovalDigioResponseModel> call = apiService.getIndividualDigioDocument(loanTakerID);
            call.enqueue(new Callback<IndividualPostApprovalDigioResponseModel>() {
                @Override
                public void onResponse(Call<IndividualPostApprovalDigioResponseModel> call, final Response<IndividualPostApprovalDigioResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            Log.i("Drools", "" + new Gson().toJson(response.body()));
                            individualPostApprovalDigioResponseModel = response.body();
                            setValuesFromResponse(response.body());


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(PostDocumentsDeclarationActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(PostDocumentsDeclarationActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<IndividualPostApprovalDigioResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(PostDocumentsDeclarationActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(IndividualPostApprovalDigioResponseModel body) {

        if (body.getPostApprovalDocumentModel().isLoan_agreement_generated() == true &&
                body.getPostApprovalDocumentModel().isApplicant_signed() != true &&
                body.getPostApprovalDocumentModel().isCo_applicant_signed() != true) {

            isValidLoanAgreementGenerated = true;

            loanAgreementTv.setText("Loan Agreement");
            digioSignatureLayout.setVisibility(View.VISIBLE);


            loanAgreement = body.getPostApprovalDocumentModel().getLoan_agreement();

            try {
                JSONObject jsonObject = new JSONObject(new Gson().toJson(body));

                if(jsonObject.getJSONObject("post_approval_document").has("digio_loan_agreement_id")){

                    if(body.getPostApprovalDocumentModel().getDigio_loan_agreement_id()!=null){
                        documentId = body.getPostApprovalDocumentModel().getDigio_loan_agreement_id();
                    }

                }


                if(jsonObject.getJSONObject("post_approval_document").has("applicant_num")){
                    applicantMobileNo = body.getPostApprovalDocumentModel().getApplicant_num();
                }


                if(jsonObject.getJSONObject("post_approval_document").has("co_applicant_num")){
                    coApplicantMobileNo = body.getPostApprovalDocumentModel().getCo_applicant_num();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{
            isValidLoanAgreementGenerated = false;
            digioSignatureLayout.setVisibility(View.INVISIBLE);
            loanAgreementTv.setText("Generate Loan Agreement");
        }


        if (body.getPostApprovalDocumentModel().getApplicant_num() != null && body.getPostApprovalDocumentModel().getCo_applicant_num() != null &&
                (body.getPostApprovalDocumentModel().isApplicant_esigned() != true || body.getPostApprovalDocumentModel().isCo_applicant_esigned() != true)) {

            manualSignatureLayout.setVisibility(View.VISIBLE);

            if (body.getPostApprovalDocumentModel().isApplicant_signed() == true && body.getPostApprovalDocumentModel().isCo_applicant_signed() == true) {
                if (body.getPostApprovalDocumentModel().isLoan_agreement_generated() != true) {
                    loanAgreementTvm.setText("Generate Signed Agreement");
                }
                else {
                    loanAgreementTvm.setText("Signed Loan Agreement");
                }
            }
            else {
                loanAgreementTvm.setText("Loan Agreement");
            }
        }






        if(body.getPostApprovalDocumentModel().isApplicant_esigned()){
                UiUtils.setProcessCompleted(PostDocumentsDeclarationActivity.this,applicantSignatureTv);
            isApplicantSignatureDone = true;
        }else{
            UiUtils.setProcessActivated(PostDocumentsDeclarationActivity.this,applicantSignatureTv);
            isApplicantSignatureDone = false;
        }


        if(body.getPostApprovalDocumentModel().isCo_applicant_esigned()){
            UiUtils.setProcessCompleted(PostDocumentsDeclarationActivity.this,coAPplicantSignatureTv);
            isCoApplicantSignatureDone = true;
        }else{
            UiUtils.setProcessActivated(PostDocumentsDeclarationActivity.this,coAPplicantSignatureTv);
            isCoApplicantSignatureDone = false;

        }

        if(body.getPostApprovalDocumentModel().isApplicant_signed()){
            UiUtils.setProcessCompleted(PostDocumentsDeclarationActivity.this,applicantSignatureTv2);
            isApplicantSignature2Done = true;
        }else{
            UiUtils.setProcessActivated(PostDocumentsDeclarationActivity.this,applicantSignatureTv2);
            isApplicantSignature2Done = false;
        }


        if(body.getPostApprovalDocumentModel().isCo_applicant_signed()){
            UiUtils.setProcessCompleted(PostDocumentsDeclarationActivity.this,coAPplicantSignatureTv2);
            isCoApplicantSignature2Done = true;
        }else{
            UiUtils.setProcessActivated(PostDocumentsDeclarationActivity.this,coAPplicantSignatureTv2);
            isCoApplicantSignature2Done = false;
        }

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(PostDocumentsDeclarationActivity.this);
            getDeclaration();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;



            case R.id.loan_agreement_tv:

                if(isValidLoanAgreementGenerated){
                    Intent l_intent = new Intent(PostDocumentsDeclarationActivity.this,ViewLoanAgreementPDFActivity.class);
                    Bundle l_bundle = new Bundle();
                    l_bundle.putString("title","Loan Agreement");
                    l_bundle.putString("file",loanAgreement);
                    l_intent.putExtras(l_bundle);
                    startActivity(l_intent);
                }else{
                    Intent intent = new Intent(PostDocumentsDeclarationActivity.this,AddContactsDigioActivity.class);
                    startActivity(intent);
                }


                break;

            case R.id.esign_cpf_button:
                
                if((isApplicantSignatureDone) && (isCoApplicantSignatureDone)) {

                    Bundle params = new Bundle();
                    params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                    params.putString("status","post_approval_declaration_digio_created");
                    mFirebaseAnalytics.logEvent("post_approval_declaration_digio", params);

                    IndividualDocumentsActivity.getInstance().init();
                    IndividualMemberDetailActivity.getInstance().init();
                    PostApprovalTaskDetailsActivity.getInstance().init();

                    startActivity(new Intent(PostDocumentsDeclarationActivity.this, InsuranceFormActivity.class));
                }
                else if((isApplicantSignature2Done) && (isCoApplicantSignature2Done) && isValidLoanAgreementGenerated) {

                    Bundle params = new Bundle();
                    params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                    params.putString("status","post_approval_declaration_signature_created");
                    mFirebaseAnalytics.logEvent("post_approval_declaration_signature", params);

                    IndividualDocumentsActivity.getInstance().init();
                    IndividualMemberDetailActivity.getInstance().init();
                    PostApprovalTaskDetailsActivity.getInstance().init();

                    startActivity(new Intent(PostDocumentsDeclarationActivity.this, InsuranceFormActivity.class));
                }
                else{
                    Toast.makeText(this, "Please complete e-sign", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.applicant_signature_tv:
                
                if(!isApplicantSignatureDone) {

                    if (documentId != null && applicantMobileNo != null){

                        // Invoke Esign
                        Digio digio = new Digio();
                    DigioConfig digioConfig = new DigioConfig();
                    digioConfig.setLogo("http://tailwebs.com/images/logo/nav-logo"); //Your company logo
                    digioConfig.setEnvironment(DigioEnvironment.PRODUCTION);   //Stage is sandbox
                    digioConfig.setServiceMode(DigioServiceMode.FP);//FP is fingerprint

                    isApplicantSignatureClicked = true;
                    isCoApplicantSignatureClicked = false;

                    try {
                        digio.init(PostDocumentsDeclarationActivity.this, digioConfig);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        digio.esign(documentId, applicantMobileNo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                        Toast.makeText(this, "Document not available for E-sign", Toast.LENGTH_SHORT).show();
                    }
                }


                break;

            case R.id.co_applicant_signature_tv:

                if(!isCoApplicantSignatureDone){
                    // Invoke Esign

                    if (documentId != null && coApplicantMobileNo != null) {

                        Digio digioCo = new Digio();
                        DigioConfig digioConfigCo = new DigioConfig();
                        digioConfigCo.setLogo("http://tailwebs.com/images/logo/nav-logo"); //Your company logo
                        digioConfigCo.setEnvironment(DigioEnvironment.PRODUCTION);   //Stage is sandbox
                        digioConfigCo.setServiceMode(DigioServiceMode.FP);//FP is fingerprint


                        isApplicantSignatureClicked = false;
                        isCoApplicantSignatureClicked = true;

                        try {
                            digioCo.init(PostDocumentsDeclarationActivity.this, digioConfigCo);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            digioCo.esign(documentId, coApplicantMobileNo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(this, "Document not available for E-sign", Toast.LENGTH_SHORT).show();
                    }
                }
              
                break;

            case R.id.applicant_signature_tv_m:
                if (!isApplicantSignature2Done) {
                    isApplicantSignature2Clicked = true;
                    isCoApplicantSignature2Clicked = false;
                    startActivity(new Intent(PostDocumentsDeclarationActivity.this,
                            ApplicantSignatureActivity.class));
                }
                break;

            case R.id.co_applicant_signature_tv_m:
                if (!isCoApplicantSignature2Done) {
                    isApplicantSignature2Clicked = false;
                    isCoApplicantSignature2Clicked = true;
                    startActivity(new Intent(PostDocumentsDeclarationActivity.this,
                            CoApplicantSignatureActivity.class));
                }
                break;

            case R.id.loan_agreement_tv_m:
                generateAndShowManuallySignedLoanAgreement();
                break;
        }

    }

    private void postDigio() {

        try {
            Log.d("Profile image", "start");
            //For Image
            File file = null;
            MultipartBody.Builder builder = new MultipartBody.Builder();

                if(isApplicantSignatureClicked){
                    builder.addFormDataPart("document[applicant_esigned]", "true");

                }else if(isCoApplicantSignatureClicked){
                    builder.addFormDataPart("document[co_applicant_esigned]", "true");
                }

            RequestBody finalRequestBody = builder.build();

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerPostApprovalDocumentResponseModel> call;
            call = apiService.postApprovalDocumentDigio(
                    loanTakerID,
                    finalRequestBody);

            call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, Response<LoanTakerPostApprovalDocumentResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            init();
                            Toast.makeText(PostDocumentsDeclarationActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(PostDocumentsDeclarationActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(PostDocumentsDeclarationActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(PostDocumentsDeclarationActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    // Callback listener functions
    public void onSigningSuccess(String documentId){
        Toast.makeText(this, documentId+" signed successfully", Toast.LENGTH_SHORT).show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                goToSigningDOct();
            }
        });
//      goToSigningDOcument();
    }


    private void startDialog()
    {

        mProgressDialog = ProgressDialog.show(this, "title", "loading");

        //start a new thread to process job
        new Thread(new Runnable() {
            @Override
            public void run() {
                //heavy job here
                //send message to main thread
                goToSigningDOcument();
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
        }
    };

    private void goToSigningDOct() {

        Log.i("Qwe","sksks");


        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                //code to do the HTTP request
                goToSigningDOcument();
            }
        });
        thread.start();
    }

    private void goToSigningDOcument() {
        if (NetworkUtils.haveNetworkConnection(this)) {
            postDigio();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }

    public void onSigningFailure(String documentId, int code, String response){
        Toast.makeText(this, "Signing process Failed"+response, Toast.LENGTH_SHORT).show();
    }

    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            return true;
        }
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



    public static PostDocumentsDeclarationActivity getInstance() {
        return instance;
    }


    public void postSignatures() {

        try {
            File file = null;
            MultipartBody.Builder builder = new MultipartBody.Builder();

            if(isApplicantSignature2Clicked){
                builder.addFormDataPart("document[applicant_signed]", "true");

            }else if(isCoApplicantSignature2Clicked){
                builder.addFormDataPart("document[co_applicant_signed]", "true");
            }

            RequestBody finalRequestBody = builder.build();

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerPostApprovalDocumentResponseModel> call;
            call = apiService.postApprovalDocumentDigio(
                    loanTakerID,
                    finalRequestBody);

            call.enqueue(new Callback<LoanTakerPostApprovalDocumentResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerPostApprovalDocumentResponseModel> call, Response<LoanTakerPostApprovalDocumentResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            init();
                            Toast.makeText(PostDocumentsDeclarationActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Drools onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(PostDocumentsDeclarationActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Drools onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(PostDocumentsDeclarationActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerPostApprovalDocumentResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(PostDocumentsDeclarationActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    public void generateAndShowManuallySignedLoanAgreement() {
        showProgressDialog(PostDocumentsDeclarationActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<IndividualPostApprovalDigioResponseModel> call = apiService.generateManuallySignedLoanAgreement(loanTakerID,
                    individualPostApprovalDigioResponseModel.getPostApprovalDocumentModel().getApplicant_num(),
                    individualPostApprovalDigioResponseModel.getPostApprovalDocumentModel().getCo_applicant_num());

            call.enqueue(new Callback<IndividualPostApprovalDigioResponseModel>() {
                @Override
                public void onResponse(Call<IndividualPostApprovalDigioResponseModel> call, Response<IndividualPostApprovalDigioResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        Bundle params = new Bundle();
                        params.putString("status","manually_signed_loan_agreement_generated");
                        params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                        mFirebaseAnalytics.logEvent("manually_signed_loan_agreement_generated", params);
                        Log.i("AAdhar EresponseMI", "--" + new Gson().toJson(response.body()));
                        goToPDFPage(response.body().getPostApprovalDocumentModel().getLoan_agreement());
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(PostDocumentsDeclarationActivity.this, response.code(), response.body().getErrors());

                    }
                }

                @Override
                public void onFailure(Call<IndividualPostApprovalDigioResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(PostDocumentsDeclarationActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }


    private void goToPDFPage(String loan_agreement) {
        Intent l_intent = new Intent(PostDocumentsDeclarationActivity.this,ViewLoanAgreementPDFActivity.class);
        Bundle l_bundle = new Bundle();
        l_bundle.putString("title","Loan Agreement");
        l_bundle.putString("file",loan_agreement);
        l_intent.putExtras(l_bundle);
        startActivity(l_intent);
    }

}
