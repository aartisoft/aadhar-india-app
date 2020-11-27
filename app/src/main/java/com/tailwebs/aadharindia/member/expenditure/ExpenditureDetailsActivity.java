package com.tailwebs.aadharindia.member.expenditure;

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
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantFamilyActivity;
import com.tailwebs.aadharindia.member.declaration.DeclarationActivity;
import com.tailwebs.aadharindia.member.declaration.FormFillingSignaturePadActivity;
import com.tailwebs.aadharindia.member.models.expenditurestatus.LoanTakerExpenditureStatusResponseModel;
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

public class ExpenditureDetailsActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.family_expenditure_tv)
    TextView familyExpenditureTv;

    @BindView(R.id.outside_borrowing_tv)
    TextView outsideBorrowingTv;


    @BindView(R.id.continue_button)
    Button continueButton;

    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null;

    static ExpenditureDetailsActivity instance;

    private boolean isFamilyExpenditureCompleted = false,isOutsideBorrowingCompleted=false;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_details);
        ButterKnife.bind(this);
        instance=this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Expenses");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        if(!getIntent().hasExtra("page_value")){
            Intent intent = new Intent(ExpenditureDetailsActivity.this,AddExpenditureActivity.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
        }

        init();
    }

    public void init() {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Expenses Detail", null);

        loanTakerID = GlobalValue.loanTakerId;
        continueButton.setOnClickListener(this);
        familyExpenditureTv.setOnClickListener(this);
        outsideBorrowingTv.setOnClickListener(this);

        if(loanTakerID != null) {


            if (NetworkUtils.haveNetworkConnection(this)) {
                showProgressDialog(ExpenditureDetailsActivity.this);
                getExpenditureStatus();
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

    private void getExpenditureStatus() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerExpenditureStatusResponseModel> call = apiService.getExpenditureStatus(loanTakerID,"expence");
            call.enqueue(new Callback<LoanTakerExpenditureStatusResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerExpenditureStatusResponseModel> call, final Response<LoanTakerExpenditureStatusResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponse(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(ExpenditureDetailsActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(ExpenditureDetailsActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerExpenditureStatusResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(ExpenditureDetailsActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerExpenditureStatusResponseModel body) {

        userNameTv.setText(body.getLoanTakerExpenditureStatusModel().getName());
        userCoTv.setText("C.o. "+body.getLoanTakerExpenditureStatusModel().getAadhar_co());

        GlobalValue.loanTakerIdForAnalytics = body.getLoanTakerExpenditureStatusModel().getLoan_taker_id();

        Picasso.with(ExpenditureDetailsActivity.this)
                .load(body.getLoanTakerExpenditureStatusModel().getProfileImages().getMedium())
                .placeholder(R.drawable.userimg_placeholder)
                .into(profileImageView);



        //family income
        if(body.getLoanTakerExpenditureStatusModel().getExpenditureModel().getStatusModel().getFamilyExpenditureModel().getActivated()){

            UiUtils.setProcessActivated(ExpenditureDetailsActivity.this,familyExpenditureTv);
            if(body.getLoanTakerExpenditureStatusModel().getExpenditureModel().getStatusModel().getFamilyExpenditureModel().getCompleted()){
                UiUtils.setProcessCompleted(ExpenditureDetailsActivity.this,familyExpenditureTv);
                isFamilyExpenditureCompleted = true;
            }

        }else{
            UiUtils.setProcessNotCompleted(ExpenditureDetailsActivity.this,familyExpenditureTv);
        }


        //alternate income
        if(body.getLoanTakerExpenditureStatusModel().getExpenditureModel().getStatusModel().getOutsideBorrowingModel().getActivated()){

            UiUtils.setProcessActivated(ExpenditureDetailsActivity.this,outsideBorrowingTv);
            if(body.getLoanTakerExpenditureStatusModel().getExpenditureModel().getStatusModel().getOutsideBorrowingModel().getCompleted()){
                UiUtils.setProcessCompleted(ExpenditureDetailsActivity.this,outsideBorrowingTv);
                isOutsideBorrowingCompleted=true;
            }

        }else {
            UiUtils.setProcessNotCompleted(ExpenditureDetailsActivity.this,outsideBorrowingTv);
        }


        setButtonValues(body.getLoanTakerExpenditureStatusModel().getExpenditureModel().getNext_action());
        buttonValue = body.getLoanTakerExpenditureStatusModel().getExpenditureModel().getNext_action();
    }

    private void setButtonValues(String next_action) {
        switch (next_action){

            case "family_expenditure":
                continueButton.setText("Move to "+familyExpenditureTv.getText().toString());
                break;

            case "outside_borrowing":
                continueButton.setText("Move to "+outsideBorrowingTv.getText().toString());
                break;

            case "signing":
                continueButton.setText("Move to Signing");
                break;
        }
    }


    public static ExpenditureDetailsActivity getInstance() {
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

            case R.id.family_expenditure_tv:
                if(isFamilyExpenditureCompleted)
                    goToAddExpenditure();
                    break;

            case R.id.outside_borrowing_tv:
                if(isOutsideBorrowingCompleted)
                    goToOustsideBorrowing();
                    break;


            case R.id.continue_button:

                switch (buttonValue){

                    case "family_expenditure":
                        goToAddExpenditure();
                        break;

                    case "outside_borrowing":
                        goToOustsideBorrowing();
                        break;

                    case "signing":
                        goToDeclaration();
                        break;


                }


                break;
        }


    }

    private void goToDeclaration() {
        startActivity(new Intent(ExpenditureDetailsActivity.this,DeclarationActivity.class));
    }

    private void goToOustsideBorrowing() {
        startActivity(new Intent(ExpenditureDetailsActivity.this,OutsideBorrowingActivity.class));
    }

    private void goToAddExpenditure() {
        startActivity(new Intent(ExpenditureDetailsActivity.this,AddExpenditureActivity.class));
    }
}
