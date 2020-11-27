package com.tailwebs.aadharindia.member.applicant;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.RelationsSpinnerAdapter;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantDetailsActivity;
import com.tailwebs.aadharindia.member.models.applicantstatus.LoanTakerApplicantStatusResponseModel;
import com.tailwebs.aadharindia.models.common.CustomerApplicantCommonDataResponseModel;
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

public class ApplicantDetailActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.customer_details_tv)
    TextView customerDetailsTv;

    @BindView(R.id.credit_check_report_tv)
    TextView creditCheckReportTv;

    @BindView(R.id.emi_calculator_tv)
    TextView emiCalculatorTv;

    @BindView(R.id.loan_details_tv)
    TextView loanDetailsTv;

    @BindView(R.id.documents_tv)
    TextView documentsTv;


    @BindView(R.id.continue_button)
    Button continueButton;

    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null;

    static ApplicantDetailActivity instance;


    private boolean isCustomerDetailsCompleted = false,isCreditCheckReportCompleted=false,isEMICalculatorCompleted=false,
            isLoanDetailsCompleted=false,isDocumentsCompleted=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_detail);
        ButterKnife.bind(this);
        instance=this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Applicant Detail");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);



        if(!getIntent().hasExtra("page_value")){
            Intent intent = new Intent(ApplicantDetailActivity.this,AddCustomerDetailsActivity.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
        }

        init();

    }

    public void init() {

        loanTakerID = GlobalValue.loanTakerId;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Applicant Detail", null);

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(ApplicantDetailActivity.this);
//            GlobalValue.applicantLoanAmountArrayList = null;
//            GlobalValue.applicantLoanTenureArrayList = null;
//            GlobalValue.applicantLoanTakerRelationsArrayList = null;
//            GlobalValue.applicantLoanReasonsArrayList = null;
//            GlobalValue.applicantMaritalStatusArrayList = null;
//            GlobalValue.applicantReligionsArrayList = null;
//            GlobalValue.applicantCastesArrayList = null;
//            GlobalValue.applicantRationCardTypesArrayList = null;
//            GlobalValue.applicantSecondaryIDArrayList = null;
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
        customerDetailsTv.setOnClickListener(this);
        creditCheckReportTv.setOnClickListener(this);
        emiCalculatorTv.setOnClickListener(this);
        loanDetailsTv.setOnClickListener(this);
        documentsTv.setOnClickListener(this);




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
                            Log.i("Drools", "" + new Gson().toJson(response.body()));
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



                            if(loanTakerID != null) {


                                if (NetworkUtils.haveNetworkConnection(ApplicantDetailActivity.this)) {
                                    showProgressDialog(ApplicantDetailActivity.this);
                                    getApplicantStatus();
                                } else {
                                    UiUtils.showAlertDialogWithOKButton(ApplicantDetailActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                                }
                            }


                        } else {
                            NetworkUtils.handleErrorsForAPICalls(ApplicantDetailActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDetailActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CustomerApplicantCommonDataResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDetailActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    public void finish() {
        super.finish();
    }

    public static ApplicantDetailActivity getInstance() {
        return instance;
    }

    private void getApplicantStatus() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerApplicantStatusResponseModel> call = apiService.getApplicantStatus(loanTakerID,"applicant");
            call.enqueue(new Callback<LoanTakerApplicantStatusResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerApplicantStatusResponseModel> call, final Response<LoanTakerApplicantStatusResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponse(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(ApplicantDetailActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(ApplicantDetailActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerApplicantStatusResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(ApplicantDetailActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerApplicantStatusResponseModel body) {

        GlobalValue.loanTakerIdForAnalytics = body.getLoanTakerApplicantStatusModel().getLoan_taker_id();
        userNameTv.setText(body.getLoanTakerApplicantStatusModel().getName());
        userCoTv.setText("C.o. "+body.getLoanTakerApplicantStatusModel().getAadhar_co());

        Picasso.with(ApplicantDetailActivity.this)
                .load(body.getLoanTakerApplicantStatusModel().getProfileImages().getMedium())
                .placeholder(R.drawable.userimg_placeholder)
                .into(profileImageView);



        if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getCustomerDetailModel().getActivated()){

            UiUtils.setProcessActivated(ApplicantDetailActivity.this,customerDetailsTv);
            if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getCustomerDetailModel().getCompleted()){
                UiUtils.setProcessCompleted(ApplicantDetailActivity.this,customerDetailsTv);
                isCustomerDetailsCompleted = true;
            }

        }else{
            UiUtils.setProcessNotCompleted(ApplicantDetailActivity.this,customerDetailsTv);
        }

        if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getCreditCheckReportModel().getActivated()){

            UiUtils.setProcessActivated(ApplicantDetailActivity.this,creditCheckReportTv);
            if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getCreditCheckReportModel().getCompleted()){
                UiUtils.setProcessCompleted(ApplicantDetailActivity.this,creditCheckReportTv);
                isCreditCheckReportCompleted=true;
            }

        }else {
            UiUtils.setProcessNotCompleted(ApplicantDetailActivity.this,creditCheckReportTv);
        }

        if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getEmiCalculatorModel().getActivated()){

            UiUtils.setProcessActivated(ApplicantDetailActivity.this,emiCalculatorTv);
            if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getEmiCalculatorModel().getCompleted()){
                UiUtils.setProcessCompleted(ApplicantDetailActivity.this,emiCalculatorTv);
                isEMICalculatorCompleted= true;
            }

        }else {
            UiUtils.setProcessNotCompleted(ApplicantDetailActivity.this,emiCalculatorTv);
        }

        if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getLoanDetailModel().getActivated()){

            UiUtils.setProcessActivated(ApplicantDetailActivity.this,loanDetailsTv);
            if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getLoanDetailModel().getCompleted()){
                UiUtils.setProcessCompleted(ApplicantDetailActivity.this,loanDetailsTv);
                isLoanDetailsCompleted= true;
            }

        }else {
            UiUtils.setProcessNotCompleted(ApplicantDetailActivity.this,loanDetailsTv);
        }

        if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getDocumentsFilledModel().getActivated()){

            UiUtils.setProcessActivated(ApplicantDetailActivity.this,documentsTv);
            if(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getStatusModel().getDocumentsFilledModel().getCompleted()){
                UiUtils.setProcessCompleted(ApplicantDetailActivity.this,documentsTv);
                isDocumentsCompleted = true;
                GlobalValue.isApplicantDocumentCompleted = true;
            }

        }else {
            UiUtils.setProcessNotCompleted(ApplicantDetailActivity.this,documentsTv);
        }

        setButtonValues(body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getNext_action());
        buttonValue = body.getLoanTakerApplicantStatusModel().getApplicantStatusModel().getNext_action();




    }

    private void setButtonValues(String next_action) {

        switch (next_action){

            case "customer_detail":
                continueButton.setText("Move to "+customerDetailsTv.getText().toString());
                break;

            case "credit_check_report":
                continueButton.setText("Move to "+creditCheckReportTv.getText().toString());
                break;

            case "emi_calculator":
                continueButton.setText("Move to "+emiCalculatorTv.getText().toString());
                break;


            case "loan_detail":
                continueButton.setText("Move to "+loanDetailsTv.getText().toString());
                break;

            case "documents_filled":
                continueButton.setText("Move to "+documentsTv.getText().toString());
                break;

            case "co_applicant":
                continueButton.setText("Move to Co-Applicants");
                break;
                
                
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.customer_details_tv:
                if(isCustomerDetailsCompleted)
                goToCustomerEditPage();
                break;

            case R.id.credit_check_report_tv:
                if(isCreditCheckReportCompleted)
                goToEditCreditCheckReport();
                break;


            case R.id.emi_calculator_tv:
                if(isEMICalculatorCompleted)
                    goToEditEMICalculator();
                break;

            case R.id.loan_details_tv:
                if(isLoanDetailsCompleted)
                    goToEditLoanDetail();
                break;

            case R.id.documents_tv:
                if(isDocumentsCompleted)
                    goToDocumentFilled();
                break;

            case R.id.continue_button:
                
                if(GlobalValue.loanTakerId!=null){
                    switch (buttonValue){

                        case "customer_detail":
                            if(isCustomerDetailsCompleted)
                                goToCustomerEditPage();
                            else
                                goToCustomerDetail();
                            break;

                        case "credit_check_report":
                            goToCreditCheckReport();
                            break;

                        case "emi_calculator":
                            if(isEMICalculatorCompleted)
                                goToEditEMICalculator();
                            else
                                goToEMICalculator();
                            break;


                        case "loan_detail":
                            if(isLoanDetailsCompleted)
                                goToEditLoanDetail();
                            else
                                goToLoanDetail();
                            break;

                        case "documents_filled":
                            goToDocumentFilled();
                            break;

                        case "co_applicant":
                            goToCoApplicant();
                            break;
                    }

                }else{

                    Intent intent = new Intent(ApplicantDetailActivity.this,GroupMemberListingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

              

                break;
        }
    }

    private void goToEditLoanDetail() {
        startActivity(new Intent(ApplicantDetailActivity.this,EditLoanDetailsActivity.class));
    }

    private void goToEditEMICalculator() {
        startActivity(new Intent(ApplicantDetailActivity.this,EditEMICalculatorActivity.class));
    }

    private void goToCustomerEditPage() {
            startActivity(new Intent(ApplicantDetailActivity.this,EditCustomerDetailsActivity.class));

    }

    private void goToDocumentFilled() {
        startActivity(new Intent(ApplicantDetailActivity.this,ApplicantDocumentActivity.class));
    }

    private void goToLoanDetail() {
        startActivity(new Intent(ApplicantDetailActivity.this,LoanDetailsActivity.class));
    }

    private void goToEMICalculator() {
        startActivity(new Intent(ApplicantDetailActivity.this,EMICalculatorActivity.class));
    }

    private void goToCreditCheckReport() {

        startActivity(new Intent(ApplicantDetailActivity.this,CreditCheckReportActivity.class));
    }

    private void goToEditCreditCheckReport() {

        startActivity(new Intent(ApplicantDetailActivity.this,EditCreditCheckReportActivity.class));
    }

    private void goToCustomerDetail() {

        startActivity(new Intent(ApplicantDetailActivity.this,AddCustomerDetailsActivity.class));
        
    }

    private void goToCoApplicant() {

        if( GlobalValue.isApplicantDocumentCompleted){

            finish();
        }else{
            Intent intent = new Intent(ApplicantDetailActivity.this,CoApplicantDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("applicant","finished");
            intent.putExtras(bundle);
            startActivity(intent);
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
}
