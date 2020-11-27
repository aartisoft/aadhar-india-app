package com.tailwebs.aadharindia.member.rating;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.member.GroupMemberListingActivity;
import com.tailwebs.aadharindia.member.MemberDetailActivity;
import com.tailwebs.aadharindia.member.applicant.ApplicantDetailActivity;
import com.tailwebs.aadharindia.member.models.CalculateEMIResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends BaseActivity implements View.OnClickListener {


    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    @BindView(R.id.rate_switch_button)
    SwitchMultiButton rateSwitchButton;


    @BindView(R.id.submit_button)
    Button finishButton;

    String rateValue=null;
    private ProgressDialog mProgressDialog;

    //choose value from intent;
    String loanTakerID;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ButterKnife.bind(this);

        loanTakerID =  GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Customer Rating");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        finishButton.setOnClickListener(this);
        rateValue ="1";


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Applicant Rating", null);


        rateSwitchButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                rateValue = tabText;

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.submit_button:
                if(rateValue !=null)
                    submitCustomerRating();

                Bundle params = new Bundle();
                params.putString("applicant_rating", rateValue);
                mFirebaseAnalytics.logEvent("applicant", params);
                    break;

        }

    }

    private void updateStatusinMemberDetailPage() {
        ApplicantDetailActivity.getInstance().init();
        MemberDetailActivity.getInstance().init();
    }

    private void submitCustomerRating() {

        finishButton.setEnabled(false);
        finishButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(RatingActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<CalculateEMIResponseModel> call = apiService.addRating(loanTakerID,
                    rateValue);

            call.enqueue(new Callback<CalculateEMIResponseModel>() {
                @Override
                public void onResponse(Call<CalculateEMIResponseModel> call, Response<CalculateEMIResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {

                        finishButton.setEnabled(true);
                        finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));

                        Bundle params = new Bundle();
                        params.putString("status","applicant_rating_created");
                        params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                        mFirebaseAnalytics.logEvent("applicant_rating", params);

                        Log.i("AAdhar EresponseMI", "--" + new Gson().toJson(response.body()));
                        goToGroupListingPage();
                        Toast.makeText(RatingActivity.this, ""+response.body().getNotice(), Toast.LENGTH_SHORT).show();
                    } else {
                        finishButton.setEnabled(true);
                        finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(RatingActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CalculateEMIResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    finishButton.setEnabled(true);
                    finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(RatingActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToGroupListingPage() {

        Intent intent = new Intent(RatingActivity.this,GroupMemberListingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
