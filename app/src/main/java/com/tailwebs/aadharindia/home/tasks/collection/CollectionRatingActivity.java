package com.tailwebs.aadharindia.home.tasks.collection;

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
import com.tailwebs.aadharindia.home.models.CollectionRatingResponseModel;
import com.tailwebs.aadharindia.home.tasks.collection.member.GroupMembersOverVIewActivity;
import com.tailwebs.aadharindia.home.tasks.collection.member.MemberOverviewActivity;
import com.tailwebs.aadharindia.housevisit.models.houseinformationcreate.HouseInforationCreateResponseModel;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantFamilyActivity;
import com.tailwebs.aadharindia.member.rating.SwitchMultiButton;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionRatingActivity extends BaseActivity implements View.OnClickListener {


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

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_rating);

        ButterKnife.bind(this);

        loanTakerID =  GlobalValue.loanTakerId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Customer Rating");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Collection Rating", null);

        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(CollectionRatingActivity.this);
            if (gpsTracker.canGetLocation()) {
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                currentLat = (String.valueOf(latitude));
                currentLong = (String.valueOf(longitude));

                if(currentLat !=null && currentLong !=null) {

                    GlobalValue.latitude = currentLat;
                    GlobalValue.longitude = currentLong;
                }

                Log.i("Lat-Long", "" + currentLat + "-" + currentLong);

            } else {
                gpsTracker.showSettingsAlert();
            }
        }catch (Exception e){
            System.out.print("Caught the NullPointerException");
        }

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

        showProgressDialog(CollectionRatingActivity.this);
        try {
            ApiInterface apiService;
            if(currentLat !=null && currentLong !=null) {
                apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
            }else{
                apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            }
            Call<CollectionRatingResponseModel> call = apiService.addCollectionRating(loanTakerID,
                    rateValue);

            call.enqueue(new Callback<CollectionRatingResponseModel>() {
                @Override
                public void onResponse(Call<CollectionRatingResponseModel> call, Response<CollectionRatingResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {

                        finishButton.setEnabled(true);
                        finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        GlobalValue.loanTakerId = response.body().getCollectionRatingModel().getLoan_taker_id();
                        Log.i("AAdhar EresponseMI", "--" + new Gson().toJson(response.body()));
                        goToMembersPage();
                        Toast.makeText(CollectionRatingActivity.this, ""+response.body().getNotice(), Toast.LENGTH_SHORT).show();
                    } else {
                        hideProgressDialog();
                        finishButton.setEnabled(true);
                        finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(CollectionRatingActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CollectionRatingResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    finishButton.setEnabled(true);
                    finishButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(CollectionRatingActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void goToMembersPage() {

        Intent intent = new Intent(CollectionRatingActivity.this, MemberOverviewActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
