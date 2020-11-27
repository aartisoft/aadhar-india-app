package com.tailwebs.aadharindia.member.cashincome;

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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.applicant.AddCustomerDetailsActivity;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantDetailsActivity;
import com.tailwebs.aadharindia.member.expenditure.AddExpenditureActivity;
import com.tailwebs.aadharindia.member.expenditure.ExpenditureDetailsActivity;
import com.tailwebs.aadharindia.member.models.cashincomestatus.LoanTakerCashIncomeStatusResponseModel;
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

public class CashIncomeDetailsActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.family_income_tv)
    TextView familyIncomeTv;

    @BindView(R.id.alternate_income_tv)
    TextView alternateIncomeTv;


    @BindView(R.id.continue_button)
    Button continueButton;

    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null;

    static CashIncomeDetailsActivity instance;


    private boolean isFamilyIncomeCompleted = false,isAlternateIncomeCompleted=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_income_details);
        ButterKnife.bind(this);
        instance=this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Income");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        if(!getIntent().hasExtra("page_value")){
            Intent intent = new Intent(CashIncomeDetailsActivity.this,FamilyMembersListingActivity.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
        }

        init();

    }

    public void init() {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Cash Income Detail", null);

        loanTakerID = GlobalValue.loanTakerId;
        continueButton.setOnClickListener(this);
        familyIncomeTv.setOnClickListener(this);
        alternateIncomeTv.setOnClickListener(this);

        if(loanTakerID != null) {


            if (NetworkUtils.haveNetworkConnection(this)) {
                showProgressDialog(CashIncomeDetailsActivity.this);
                getCashIncomeStatus();
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

    private void getCashIncomeStatus() {


        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerCashIncomeStatusResponseModel> call = apiService.getCashIncomeStatus(loanTakerID,"income");
            call.enqueue(new Callback<LoanTakerCashIncomeStatusResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerCashIncomeStatusResponseModel> call, final Response<LoanTakerCashIncomeStatusResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponse(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(CashIncomeDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(CashIncomeDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerCashIncomeStatusResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(CashIncomeDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerCashIncomeStatusResponseModel body) {

        userNameTv.setText(body.getLoanTakerCashIncomeStatusModel().getName());
        userCoTv.setText("C.o. "+body.getLoanTakerCashIncomeStatusModel().getAadhar_co());

        GlobalValue.loanTakerIdForAnalytics = body.getLoanTakerCashIncomeStatusModel().getLoan_taker_id();

        Picasso.with(CashIncomeDetailsActivity.this)
                .load(body.getLoanTakerCashIncomeStatusModel().getProfileImages().getMedium())
                .placeholder(R.drawable.userimg_placeholder)
                .into(profileImageView);



        //family income
        if(body.getLoanTakerCashIncomeStatusModel().getCashIncomeModel().getStatusModel().getFamilyIncomeModel().getActivated()){

            UiUtils.setProcessActivated(CashIncomeDetailsActivity.this,familyIncomeTv);
            if(body.getLoanTakerCashIncomeStatusModel().getCashIncomeModel().getStatusModel().getFamilyIncomeModel().getCompleted()){
                UiUtils.setProcessCompleted(CashIncomeDetailsActivity.this,familyIncomeTv);
                isFamilyIncomeCompleted = true;
            }

        }else{
            UiUtils.setProcessNotCompleted(CashIncomeDetailsActivity.this,familyIncomeTv);
        }


        //alternate income
        if(body.getLoanTakerCashIncomeStatusModel().getCashIncomeModel().getStatusModel().getAlternateIncomeModel().getActivated()){

            UiUtils.setProcessActivated(CashIncomeDetailsActivity.this,alternateIncomeTv);
            if(body.getLoanTakerCashIncomeStatusModel().getCashIncomeModel().getStatusModel().getAlternateIncomeModel().getCompleted()){
                UiUtils.setProcessCompleted(CashIncomeDetailsActivity.this,alternateIncomeTv);
                isAlternateIncomeCompleted=true;
            }

        }else {
            UiUtils.setProcessNotCompleted(CashIncomeDetailsActivity.this,alternateIncomeTv);
        }


        setButtonValues(body.getLoanTakerCashIncomeStatusModel().getCashIncomeModel().getNext_action());
        buttonValue = body.getLoanTakerCashIncomeStatusModel().getCashIncomeModel().getNext_action();
    }

    private void setButtonValues(String next_action) {
        switch (next_action){

            case "family_income":
                continueButton.setText("Move to "+familyIncomeTv.getText().toString());
                break;

            case "alternate_income":
                continueButton.setText("Move to "+alternateIncomeTv.getText().toString());
                break;

            case "expenses":
                continueButton.setText("Move to Expenses");
                break;
        }
    }


    public static CashIncomeDetailsActivity getInstance() {
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

            case R.id.family_income_tv:
                if(isFamilyIncomeCompleted)
                    goToFamilyListing();
                break;

            case R.id.alternate_income_tv:
                if(isAlternateIncomeCompleted)
                    goToEditAlternateIncome();
                break;


            case R.id.continue_button:

                switch (buttonValue){

                    case "family_income":
                        if(isFamilyIncomeCompleted)
                            goToFamilyListing();
                        else
                            goToMemberInfo();
                        break;

                    case "alternate_income":
                        if(isAlternateIncomeCompleted)
                            goToEditAlternateIncome();
                        else
                            goToAlternateIncome();
                        break;

                    case "family_expenditure":
                        goToFamilyExpenditure();
                        break;


                }


                break;
        }
    }

    private void goToFamilyListing() {
        startActivity(new Intent(CashIncomeDetailsActivity.this,FamilyMembersListingActivity.class));
    }

    private void goToEditAlternateIncome() {
        startActivity(new Intent(CashIncomeDetailsActivity.this,EditAlternateIncomeActivity.class));
    }

    private void goToMemberInfo() {
        startActivity(new Intent(CashIncomeDetailsActivity.this,FamilyMembersListingActivity.class));
    }

    private void goToAlternateIncome() {
        startActivity(new Intent(CashIncomeDetailsActivity.this,AlternateIncomeActivity.class));
    }

    private void goToFamilyExpenditure() {
        Intent intent = new Intent(CashIncomeDetailsActivity.this,ExpenditureDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("cash_income","finished");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
