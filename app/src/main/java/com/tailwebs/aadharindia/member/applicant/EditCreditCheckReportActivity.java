package com.tailwebs.aadharindia.member.applicant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.LoanTakerCustomerDetailResponseModel;
import com.tailwebs.aadharindia.member.models.creditcheckreport.LoanTakerCreditCheckReportResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCreditCheckReportActivity extends BaseActivity implements View.OnClickListener {

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    //fields

    @BindView(R.id.input_layout_outstanding_balance)
    TextInputLayout outstandingBalanceLayout;

    @BindView(R.id.input_outstanding_balance)
    TextInputEditText outstandingBalanceET;

    @BindView(R.id.input_layout_past_due_accounts)
    TextInputLayout pastDueAccountsLayout;

    @BindView(R.id.input_past_due_accounts)
    TextInputEditText pastDueAccountsET;


    @BindView(R.id.input_layout_past_due_amount)
    TextInputLayout pastDueAmountLayout;

    @BindView(R.id.input_past_due_amount)
    TextInputEditText pastDueAmountET;

    @BindView(R.id.input_layout_written_off_amount)
    TextInputLayout writtenOffAmountLayout;

    @BindView(R.id.input_written_off_amount)
    TextInputEditText writtenOffAmountET;


    @BindView(R.id.input_layout_credit_score)
    TextInputLayout creditScoreLayout;

    @BindView(R.id.input_credit_score)
    TextInputEditText creditScoreET;


    @BindView(R.id.input_layout_dpd_count)
    TextInputLayout dpdCountLayout;

    @BindView(R.id.input_dpd_count)
    TextInputEditText dpdCountET;

    @BindView(R.id.input_latest_dpd_count)
    TextInputEditText latestDpdCountET;

    //Success Box

    @BindView(R.id.success_message_box_layout)
    FrameLayout successBox;

    @BindView(R.id.error_message_box_layout)
    FrameLayout errorBox;

    @BindView(R.id.success_box_tv)
    TextView successBoxTv;

    @BindView(R.id.error_box_tv)
    TextView errorBoxTv;


    @BindView(R.id.reject_button)
    Button rejectButton;

    @BindView(R.id.continue_button)
    Button continueButton;

    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID=null,isRepeatCustomer=null,loanCycle=null;

    @BindView(R.id.credit_check_report_layout)
    LinearLayout creditCheckReportLayout;

    @BindView(R.id.button_layout)
    LinearLayout buttonLayout;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_check_report);
        ButterKnife.bind(this);

        loanTakerID =  GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Credit Check Report");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Edit Credit Check Report", null);

        rejectButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);

        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(EditCreditCheckReportActivity.this);
            getCreditCheckReport();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }

    }

    private void getCreditCheckReport() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCreditCheckReportResponseModel> call = apiService.getCreditCheckReport(loanTakerID);
            call.enqueue(new Callback<LoanTakerCreditCheckReportResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCreditCheckReportResponseModel> call, final Response<LoanTakerCreditCheckReportResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));


                            setValuesFromResponse(response.body());
                            updateTheStatusInApplicantDetailPage();




                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EditCreditCheckReportActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EditCreditCheckReportActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCreditCheckReportResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EditCreditCheckReportActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerCreditCheckReportResponseModel body) {


        if(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().isIs_report_generated()) {

            creditCheckReportLayout.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.VISIBLE);


            loanCycle = body.getLoanTakerCreditCheckReportModel().getLoan_cycle();
            isRepeatCustomer = body.getLoanTakerCreditCheckReportModel().getIs_fresh_customer();


            //outstanding balance

            outstandingBalanceET.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getTotalBalanceModel().getAmount());
            setStatusForTheFields(outstandingBalanceET, body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getTotalBalanceModel().getIs_satisfied());


            //past due acount
            pastDueAccountsET.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getPastDueAccountsModel().getAccount());
            setStatusForTheFields(pastDueAccountsET, body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getPastDueAccountsModel().getIs_satisfied());


            //past due amount
            pastDueAmountET.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getPastDueAmountsModel().getAmount());
            setStatusForTheFields(pastDueAmountET, body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getPastDueAmountsModel().getIs_satisfied());


            //written off amount
            writtenOffAmountET.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getWrittenOffAmountsModel().getAmount());
            setStatusForTheFields(writtenOffAmountET, body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getWrittenOffAmountsModel().getIs_satisfied());


            //dpd count
            dpdCountET.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getDpdCountModel().getCount());
            setStatusForTheFields(dpdCountET,body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getDpdCountModel().getIs_satisfied());

            //latest dpd count
            latestDpdCountET.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getLatestDpdCountModel().getCount());
            setStatusForTheFields(latestDpdCountET,body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getLatestDpdCountModel().getIs_satisfied());


            //credit score
//        creditScoreET.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getCreditScoreModel().getScore());
//        setStatusForTheFields(creditScoreET,body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrReportModel().getCreditScoreModel().getIs_satisfied());


            //success or error
            if (body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrStatusModel().getIs_satisfied()) {
                successBox.setVisibility(View.VISIBLE);
                errorBox.setVisibility(View.GONE);
                successBoxTv.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrStatusModel().getMessage());

            } else {
                errorBox.setVisibility(View.VISIBLE);
                successBox.setVisibility(View.GONE);
                errorBoxTv.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getCcrStatusModel().getMessage());

            }
        }else{
            creditCheckReportLayout.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);

            View view = getLayoutInflater().inflate(R.layout.custom_credit_check_message_yes_no_dialog, null);
            TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
            TextView errorTv=(TextView)view.findViewById(R.id.error_tv);
            Button yesButton =(Button)view.findViewById(R.id.yes_button);
            Button noButton =(Button)view.findViewById(R.id.no_button);
            messageTV.setText(getResources().getString(R.string.hint_equifax_message));
            yesButton.setText(getResources().getString(R.string.hint_skip_continue));
            noButton.setText(getResources().getString(R.string.hint_go_back));

            errorTv.setText(body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getError_code()+":"+
                    body.getLoanTakerCreditCheckReportModel().getCreditCheckReportModel().getError_message());

            AlertDialog.Builder builder = new AlertDialog.Builder(EditCreditCheckReportActivity.this);
            builder.setCancelable(false)
                    .setTitle(getResources().getString(R.string.hint_leave_page_title))
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
                    goToEMICalculatorPage();
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    finish();
                }
            });
        }
    }


    private void setStatusForTheFields(TextInputEditText textInputEditText,Boolean value){

        if(value){
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.green_tick, 0);
        }else{
            textInputEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.red_cross, 0);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.reject_button:
                callRejectAPI();
                break;

            case R.id.continue_button:

                goToEMICalculatorPage();


                break;
        }
    }

    private void callRejectAPI() {

        showProgressDialog(EditCreditCheckReportActivity.this);
        try {
            Log.d("Aadhar onResponse", "--start" );
//            Add Loan taker Id TODO
//            String loanTakerId =GlobalValue.loanTakerId;


            RequestBody rejectionCode = RequestBody.create(MediaType.parse("text/plain"),"equifax");

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCustomerDetailResponseModel> call;
            call = apiService.rejectLoanTaker(
                    loanTakerID,
                    rejectionCode);
            call.enqueue(new Callback<LoanTakerCustomerDetailResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCustomerDetailResponseModel> call, Response<LoanTakerCustomerDetailResponseModel> response) {
                    hideProgressDialog();
                    Log.d("Aadhar onResponse", "" + response.code() + "--" + response.message());
                    Log.d("Aadhar onResponse1", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            goToMemberListingPage();

                        } else {
                            Log.d("Aadhar onResponse2", "" + response.body().getErrors());
                            NetworkUtils.handleErrorsCasesForAPICalls(EditCreditCheckReportActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        Log.d("Aadhar onResponse3", "" + response.body().getErrors());
                        NetworkUtils.handleErrorsForAPICalls(EditCreditCheckReportActivity.this, null, null);
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCustomerDetailResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(EditCreditCheckReportActivity.this, null, null);
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToMemberListingPage() {
        Intent intent = new Intent(EditCreditCheckReportActivity.this,GroupMemberListingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void goToEMICalculatorPage() {
        finish();
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

    private void updateTheStatusInApplicantDetailPage() {
        ApplicantDetailActivity.getInstance().init();
        MemberDetailActivity.getInstance().init();
    }

}
