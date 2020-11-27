package com.tailwebs.aadharindia.member.applicant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.AddNewMemberScanActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.models.CalculateEMIResponseModel;
import com.tailwebs.aadharindia.models.common.CACDLoanAmountsModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTenuresModel;
import com.tailwebs.aadharindia.models.login.Login;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;
import com.tailwebs.aadharindia.utils.custom.singleselecttoggle.SingleSelectToggleGroup;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EMICalculatorActivity extends BaseActivity
        implements View.OnClickListener {

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;


    @BindView(R.id.emi_amount_tv)
    TextView emiAmountTv;

    @BindView(R.id.apply_loan_button)
    Button applyLoanButton;

    @BindView(R.id.repeat_customer_tv)
    TextView repeatCustomerTv;

    @BindView(R.id.loan_cycle_tv)
    TextView loanCycleTv;


    @BindView(R.id.fresh_message_box_layout)
    LinearLayout freshMessageBox;


    @BindView(R.id.fresh_customer_tv)
    TextView freshCustomerTv;


    @BindView(R.id.fresh_loan_cycle_tv)
    TextView freshLoanCycleTv;



    @BindView(R.id.success_message_box_layout)
    LinearLayout messageBox;

    @BindView(R.id.loan_amount_toggle_radiobutton)
    SingleSelectToggleGroup loanAmountToggle;

    @BindView(R.id.loan_tenure_toggle_radiobutton)
    SingleSelectToggleGroup loanTenureToggle;

    int selectedLoanAmount =0,selectedTenure =0;
    boolean isValidLoanAmount = false,isValidTenure=false;
    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emicalculator);
        ButterKnife.bind(this);

        loanTakerID =  GlobalValue.loanTakerId;


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "EMI Calculator", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("EMI Calculator");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        //Loan Amount
        addRadioButtonsForLoanAmounts(GlobalValue.applicantLoanAmountArrayList);
        loanAmountToggle.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidLoanAmount=true;
                selectedLoanAmount = checkedId;
                calculateAmount(selectedLoanAmount,selectedTenure);


            }
        });


        // Loan Tenure
        addRadioButtonsForTenure(GlobalValue.applicantLoanTenureArrayList);
        loanTenureToggle.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
                isValidTenure=true;
                selectedTenure = checkedId;
                calculateAmount(selectedLoanAmount,selectedTenure);
            }
        });

        applyLoanButton.setOnClickListener(this);



        if (NetworkUtils.haveNetworkConnection(this)) {
            showProgressDialog(EMICalculatorActivity.this);
            getCalculatedEMI();
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }

    private void getCalculatedEMI() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<CalculateEMIResponseModel> call = apiService.getCalculatedEMI(loanTakerID);
            call.enqueue(new Callback<CalculateEMIResponseModel>() {
                @Override
                public void onResponse(Call<CalculateEMIResponseModel> call, final Response<CalculateEMIResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            if(response.body().getCustomerModel().getIs_fresh_customer()){
                                freshMessageBox.setVisibility(View.VISIBLE);
                                freshLoanCycleTv.setText("Loan Cycle : "+response.body().getCustomerModel().getLoan_cycle());

                            }else{
                                messageBox.setVisibility(View.VISIBLE);
                                loanCycleTv.setText("Loan Cycle : "+response.body().getCustomerModel().getLoan_cycle());
                            }

                            if(response.body().getCustomerModel().getCalculatedEMIModel()!=null){

                                Log.i("Drools", "" + response.body().getCustomerModel().getCalculatedEMIModel().getAmount());

                                Log.i("Drools", "" + response.body().getCustomerModel().getCalculatedEMIModel().getTenure());

                                emiAmountTv.setText(response.body().getCustomerModel().getCalculatedEMIModel().getEmi_amount_with_format());
                            }else{
//                                Toast.makeText(EMICalculatorActivity.this, "null", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EMICalculatorActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(EMICalculatorActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<CalculateEMIResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(EMICalculatorActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }

    }

    private void calculateAmount(int selectedLoanAmount, int selectedTenure) {
        if((isValidLoanAmount) && (isValidTenure)){

            applyLoanButton.setEnabled(true);
            applyLoanButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
            callAPIForCalculatingEMIAmount(selectedLoanAmount,selectedTenure);

        }
    }

    private void updateTheStatusInApplicantDetailPage() {
        ApplicantDetailActivity.getInstance().init();
        MemberDetailActivity.getInstance().init();
    }

    private void callAPIForCalculatingEMIAmount(int selectedLoanAmount, int selectedTenure) {

        showProgressDialog(EMICalculatorActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<CalculateEMIResponseModel> call = apiService.doCalculateEMI(loanTakerID,
                 selectedLoanAmount,
                   selectedTenure);

            call.enqueue(new Callback<CalculateEMIResponseModel>() {
                @Override
                public void onResponse(Call<CalculateEMIResponseModel> call, Response<CalculateEMIResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {

                        String data = new Gson().toJson(response.body());
                        JSONObject jsonObject = null;

                        Log.i("AAdhar EMI", "--" + new Gson().toJson(response.body()));
                        if (response.body().getSuccess()) {
                            emiAmountTv.setText(response.body().getCustomerModel().getCalculatedEMIModel().getEmi_amount_with_format());
                            updateTheStatusInApplicantDetailPage();
                        } else {
                            NetworkUtils.handleErrorsForAPICalls(EMICalculatorActivity.this, response.body().getErrors(), response.body().getNotice());
                          
                        }
                    } else {
                        hideProgressDialog();
                        NetworkUtils.handleErrorsCasesForAPICalls(EMICalculatorActivity.this, response.code(), response.body().getErrors());
                       
                    }

                }

                @Override
                public void onFailure(Call<CalculateEMIResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(EMICalculatorActivity.this, null, null);
                   
                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.apply_loan_button:
                goToLoanDetailsPage();

                break;
        }

    }

    private void goToLoanDetailsPage() {

        Bundle params = new Bundle();
        params.putString("status","applicant_emi_calculator_created");
        params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
        mFirebaseAnalytics.logEvent("applicant_emi_calculator", params);

        Intent intent = new Intent(EMICalculatorActivity.this,LoanDetailsActivity.class);
        startActivity(intent);
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

    public void addRadioButtonsForLoanAmounts(ArrayList<CACDLoanAmountsModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                String numberWithoutDecimal = String.valueOf(value.get(i).getValue()).split("\\.")[0];
                rdbtn.setId(Integer.valueOf(numberWithoutDecimal));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                loanAmountToggle.addView(rdbtn);
            }
        }
    }

    public void addRadioButtonsForTenure(ArrayList<CACDLoanTenuresModel> value) {
        for (int row = 0; row < 1; row++) {
            for (int i = 0; i < value.size(); i++) {
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.valueOf(value.get(i).getValue()));
                rdbtn.setText(value.get(i).getName());
                rdbtn.setBackground(getResources().getDrawable(R.drawable.selector_bg_radio_button));
                rdbtn.setTextColor(ContextCompat.getColorStateList(this, R.color.selector_text_radio_button));
                rdbtn.setButtonDrawable(android.R.color.transparent);
                rdbtn.setPadding(64,32,64,32);
                loanTenureToggle.addView(rdbtn);
            }
        }
    }
}
