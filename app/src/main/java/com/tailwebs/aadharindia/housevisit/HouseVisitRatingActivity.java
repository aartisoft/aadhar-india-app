package com.tailwebs.aadharindia.housevisit;

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
import com.tailwebs.aadharindia.home.tasks.housevisit.HouseVisitGroupReviewActivity;
import com.tailwebs.aadharindia.home.tasks.housevisit.HouseVisitTaskDetailsActivity;
import com.tailwebs.aadharindia.housevisit.models.houseinformationcreate.HouseInforationCreateResponseModel;
import com.tailwebs.aadharindia.member.models.CalculateEMIResponseModel;
import com.tailwebs.aadharindia.member.rating.RatingActivity;
import com.tailwebs.aadharindia.member.rating.SwitchMultiButton;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseVisitRatingActivity extends BaseActivity implements View.OnClickListener {


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
        setContentView(R.layout.activity_house_visit_rating);

        ButterKnife.bind(this);

        loanTakerID =  GlobalValue.loanTakerId;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "HV Rating", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Customer Rating");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        finishButton.setOnClickListener(this);
        rateValue ="1";


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
                break;

        }
    }



    private void submitCustomerRating() {

        finishButton.setEnabled(false);
        finishButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(HouseVisitRatingActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<HouseInforationCreateResponseModel> call = apiService.addHouseVisitRating(loanTakerID,
                    rateValue);

            call.enqueue(new Callback<HouseInforationCreateResponseModel>() {
                @Override
                public void onResponse(Call<HouseInforationCreateResponseModel> call, Response<HouseInforationCreateResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        finishButton.setEnabled(true);
                        finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));

                        Bundle params = new Bundle();
                        params.putString("applicant_id", GlobalValue.loanTakerIdForAnalytics);
                        params.putString("status","house_visit_rating_created");
                        mFirebaseAnalytics.logEvent("house_visit_rating", params);

                        Log.i("AAdhar EresponseMI", "--" + new Gson().toJson(response.body()));
                        goToHouseVisitMemberListingPage();
                        Toast.makeText(HouseVisitRatingActivity.this, ""+response.body().getNotice(), Toast.LENGTH_SHORT).show();
                    } else {
                        finishButton.setEnabled(true);
                        finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(HouseVisitRatingActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<HouseInforationCreateResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    finishButton.setEnabled(true);
                    finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(HouseVisitRatingActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToHouseVisitMemberListingPage() {
        HouseVisitTaskDetailsActivity.getInstance().init();
        Intent intent = new Intent(HouseVisitRatingActivity.this,HouseVisitGroupReviewActivity.class);
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
