package com.tailwebs.aadharindia.home.tasks.collection.member;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.home.models.MemberCollectionResponseModel;
import com.tailwebs.aadharindia.home.tasks.collection.group.GroupCollectionConfirmActivity;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantFamilyActivity;
import com.tailwebs.aadharindia.member.models.LoanTakerCalculatedEMIModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeadersForLocation;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.GpsTracker;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenaltyDiscountActivity extends BaseActivity implements View.OnClickListener {

    //Action Bar

    @BindView(R.id.back_button)
    ImageView backButton;

    @BindView(R.id.heading_tv)
    TextView headingTV;

    @BindView(R.id.right_action_bar_button)
    ImageView rightActionBarButton;

    private ProgressDialog mProgressDialog;


    @BindView(R.id.distance_tv)
    TextView distanceTv;

    @BindView(R.id.city_tv)
    TextView cityTv;

    @BindView(R.id.members_count_tv)
    TextView membersCountTv;

    @BindView(R.id.co_tv)
    TextView coTv;

    @BindView(R.id.penalty_amount_tv)
    TextView penaltyAmountTv;

    @BindView(R.id.input_layout_discount)
    TextInputLayout discountLayout;

    @BindView(R.id.input_discount)
    TextInputEditText discountET;

    @BindView(R.id.confirm_button)
    Button confirmButton;

    //choose value from intent;

    String penaltyAmount =null;
    String loanTakerID=null,buttonValue=null,groupID=null,colleactionId=null;


    //password

    OtpView passwordOTPView;
    TextView passwordErrorTV;

    Button yesButton, noButton;

    private Dialog dialog;
    String passcode =null;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalty_discount);
        ButterKnife.bind(this);


        loanTakerID = GlobalValue.loanTakerId;
        groupID =  GlobalValue.groupId;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Member Overview");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Penalty Discount", null);

        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(PenaltyDiscountActivity.this);
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



        if (NetworkUtils.haveNetworkConnection(this)) {

            showMemberDetails(loanTakerID);
        } else {
            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
        
        confirmButton.setOnClickListener(this);
    }

    private void showMemberDetails(String loanTakerId) {

        showProgressDialog(PenaltyDiscountActivity.this);
        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<MemberCollectionResponseModel> call = apiService.showMember(loanTakerId,"collection");
            call.enqueue(new Callback<MemberCollectionResponseModel>() {
                @Override
                public void onResponse(Call<MemberCollectionResponseModel> call, final Response<MemberCollectionResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true

                            setValuesFromResponse(response.body().getLoanTakerModels());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(PenaltyDiscountActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(PenaltyDiscountActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<MemberCollectionResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(PenaltyDiscountActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    private void setValuesFromResponse(LoanTakerCalculatedEMIModel loanTakerModels) {

        cityTv.setText(loanTakerModels.getCity().getLong_name());
        membersCountTv.setText(loanTakerModels.getName());
        coTv.setText("C.o. "+loanTakerModels.getAadhar_co());

        penaltyAmount = loanTakerModels.getCollectionModel().getCurrent_pending_penalties();
        penaltyAmountTv .setText(loanTakerModels.getCollectionModel().getCurrent_pending_penalties_in_format());
        colleactionId = loanTakerModels.getCollectionModel().getId();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.confirm_button:
                checkValidation();
                break;


        }

    }

    private void checkValidation() {

        Double discountAmount = Double.parseDouble(discountET.getText().toString().trim());
        Double penalty_amount = Double.parseDouble(penaltyAmount);

        if(discountAmount > penalty_amount){

            Toast.makeText(this, "Please enter a number less than the penalty amount", Toast.LENGTH_SHORT).show();
        }else{
            showPasswordPopup();
        }
    }



    private void showPasswordPopup() {
        View view = getLayoutInflater().inflate(R.layout.custom_collection_password_dialog, null);
        TextView messageTv = (TextView)view.findViewById(R.id.message_tv);
        passwordOTPView=(OtpView) view.findViewById(R.id.password_otp_view);
        passwordErrorTV =(TextView)view.findViewById(R.id.password_error_tv);
        RelativeLayout buttonLayout = (RelativeLayout)view.findViewById(R.id.button_layout);
        buttonLayout.setVisibility(View.VISIBLE);
        yesButton =(Button)view.findViewById(R.id.yes_button);
        noButton =(Button)view.findViewById(R.id.no_button);
        yesButton.setEnabled(true);
        yesButton.setTextAppearance(PenaltyDiscountActivity.this,R.style.MyBluePopUpButton);
        yesButton.setText("CONFIRM");
        noButton.setText("CANCEL");
        messageTv.setText("Are you sure you want to offer Rs."+discountET.getText().toString().trim()+" as penalty discount");
        passwordOTPView.addTextChangedListener(new InputLayoutTextWatcher(passwordOTPView));
        AlertDialog.Builder builder = new AlertDialog.Builder(PenaltyDiscountActivity.this);
        builder.setCancelable(false)
                .setTitle("Enter Passcode")
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        dialog = alertDialog;
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(PenaltyDiscountActivity.this);
                yesButton.setEnabled(false);
                yesButton.setTextAppearance(PenaltyDiscountActivity.this,R.style.MyDisabledPopUpButton);
                passcode = passwordOTPView.getText().toString();
                if(UiUtils.checkValidationForOTP(PenaltyDiscountActivity.this, passwordOTPView,passwordErrorTV)){
                    if (NetworkUtils.haveNetworkConnection(PenaltyDiscountActivity.this)) {
                        addDiscountAPI();

                    } else {
                        hideProgressDialog();
                        UiUtils.showAlertDialogWithOKButton(PenaltyDiscountActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                yesButton.setEnabled(true);
                                yesButton.setTextAppearance(PenaltyDiscountActivity.this,R.style.MyBluePopUpButton);
                            }
                        });
                    }

                }else{
                    hideProgressDialog();
                }
            }
        });


        noButton.setOnClickListener(new CancelCustomListener(alertDialog));
    }

    private void addDiscountAPI() {

        confirmButton.setEnabled(false);
        confirmButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(PenaltyDiscountActivity.this);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("user_passcode", String.valueOf(passcode));
        builder.addFormDataPart("amount", discountET.getText().toString().trim());


        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService;
        if(currentLat !=null && currentLong !=null) {
            apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
        }else{
            apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        }
        Call<MemberCollectionResponseModel> call = apiService.addDiscount(colleactionId,finalRequestBody
        );
        call.enqueue(new Callback<MemberCollectionResponseModel>() {
            @Override
            public void onResponse(Call<MemberCollectionResponseModel> call, Response<MemberCollectionResponseModel> response) {
                hideProgressDialog();
                Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true

                        confirmButton.setEnabled(true);
                        confirmButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));

                        Bundle params = new Bundle();
                        params.putString("level","penalty_discount_added");
                        params.putString("penalty_amount",discountET.getText().toString());
                        mFirebaseAnalytics.logEvent("collection_penalty_discount", params);

                        Toast.makeText(PenaltyDiscountActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                      goToPreviousPage();
                    } else {
                        Log.d("Drools onResponse", "" + response.body().getErrors());
                        confirmButton.setEnabled(true);
                        confirmButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(PenaltyDiscountActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Drools onResponse", "" + response.body().getErrors());
                    confirmButton.setEnabled(true);
                    confirmButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(PenaltyDiscountActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<MemberCollectionResponseModel> call, Throwable t) {
                hideProgressDialog();
                confirmButton.setEnabled(true);
                confirmButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(PenaltyDiscountActivity.this, null, null);
            }
        });
    }

    private void goToPreviousPage() {
        finish();
        GroupMembersOverVIewActivity.getInstance().init();
        MemberOverviewActivity.getInstance().init();
        Intent intent = new Intent(PenaltyDiscountActivity.this,MemberOverviewActivity.class);
        startActivity(intent);
    }


    private class CancelCustomListener implements View.OnClickListener {

        public CancelCustomListener(AlertDialog alertDialog) {
            dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
            dialog.dismiss();
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

    private class InputLayoutTextWatcher implements TextWatcher {

        private View view;
        ArrayList<String> itemPassed = new ArrayList<String>();

        private InputLayoutTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {


            switch (view.getId()) {

                case R.id.password_otp_view:
                    itemPassed.clear();
                    itemPassed.add("otp");
                    boolean passwordStatus = UiUtils.checkValidationForOTP(PenaltyDiscountActivity.this, passwordOTPView,passwordErrorTV);

                    if (passwordStatus == false) {

                        requestFocus(passwordOTPView);
                    } else {

                        passwordOTPView.setLineColor(getResources().getColor(R.color.primaryColor));

                    }
                    break;



            }
        }

    }


    public void requestFocus(View view) {
        if (view.requestFocus())
            PenaltyDiscountActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
