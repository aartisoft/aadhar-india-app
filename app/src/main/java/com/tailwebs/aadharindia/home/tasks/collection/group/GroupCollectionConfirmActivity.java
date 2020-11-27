package com.tailwebs.aadharindia.home.tasks.collection.group;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import com.tailwebs.aadharindia.home.dashboard.TaskDashboardActivity;
import com.tailwebs.aadharindia.home.models.GroupModel;
import com.tailwebs.aadharindia.home.models.GroupPaymentResponseModel;
import com.tailwebs.aadharindia.home.tasks.collection.CollectionTaskDetailsActivity;
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

public class GroupCollectionConfirmActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.group_leader_tv)
    TextView groupLeaderTv;

    @BindView(R.id.pending_amount_collected_tv)
    TextView pendingAmountCollectedTv;

    @BindView(R.id.mode_of_payment_tv)
    TextView modeOfPaymentTv;

    @BindView(R.id.transaction_id_tv)
    TextView transactionIdTv;

    @BindView(R.id.transaction_date_tv)
    TextView transactionDateTv;

    @BindView(R.id.confirm_button)
    Button confirmButton;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null,groupID=null,modeOfPaymentID,pendingAmount,transactionDateFormat;

    //password

    OtpView passwordOTPView;
    TextView passwordErrorTV;

    Button yesButton, noButton;

    private Dialog dialog;
    String passcode =null;

    DatePickerDialog picker;

    private GpsTracker gpsTracker;
    String currentLat=null,currentLong=null;

    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_collection_confirm);
        ButterKnife.bind(this);


        //show current Lat and Long

        try {
            gpsTracker = new GpsTracker(GroupCollectionConfirmActivity.this);
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



        loanTakerID = GlobalValue.loanTakerId;
        groupID =  GlobalValue.groupId;


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this,  "Collection - Group Confirmation", null);

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Group Overview");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);

        setValuesFromBundle(getIntent().getExtras());

        confirmButton.setOnClickListener(this);
    }

    private void setValuesFromBundle(Bundle extras) {

        if(extras.getString("city")!=null){
            cityTv.setText(extras.getString("city"));
        }


        if(extras.getString("group_leader")!=null){
            groupLeaderTv.setText("Group Leader : "+extras.getString("group_leader"));
        }


        if(extras.getString("members_count")!=null){
            membersCountTv.setText(extras.getString("members_count"));
        }


        if(extras.getString("pending_amount_format")!=null){
            pendingAmountCollectedTv.setText(extras.getString("pending_amount_format"));
            pendingAmount = extras.getString("pending_amount");
        }


        if(extras.getString("mode_of_payment")!=null){
            modeOfPaymentTv.setText(extras.getString("mode_of_payment"));
            modeOfPaymentID = extras.getString("mode_of_payment_id");
        }



        if(extras.getString("transaction_id")!=null){
            transactionIdTv.setText(extras.getString("transaction_id"));
        }



        if(extras.getString("transaction_date")!=null){
            transactionDateTv.setText(extras.getString("transaction_date"));
            transactionDateFormat  = extras.getString("transaction_date_format");
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;


            case R.id.confirm_button:
                showPasswordPopup();
                break;



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
        yesButton.setTextAppearance(GroupCollectionConfirmActivity.this,R.style.MyBluePopUpButton);
        yesButton.setText("CONFIRM");
        noButton.setText("CANCEL");
        messageTv.setText("Confirm cash of "+pendingAmountCollectedTv.getText().toString().trim()+" received");
        passwordOTPView.addTextChangedListener(new InputLayoutTextWatcher(passwordOTPView));
        AlertDialog.Builder builder = new AlertDialog.Builder(GroupCollectionConfirmActivity.this);
        builder.setCancelable(false)
                .setTitle("Enter Passcode")
                .setPositiveButton("", null)
                .setNeutralButton("", null)
                .setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
        dialog = alertDialog;
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(GroupCollectionConfirmActivity.this);
                yesButton.setEnabled(false);
                yesButton.setTextAppearance(GroupCollectionConfirmActivity.this,R.style.MyDisabledPopUpButton);
                passcode = passwordOTPView.getText().toString();
                if(UiUtils.checkValidationForOTP(GroupCollectionConfirmActivity.this, passwordOTPView,passwordErrorTV)){
                    if (NetworkUtils.haveNetworkConnection(GroupCollectionConfirmActivity.this)) {
                        sendGroupPaymentAPI();

                    } else {
                        hideProgressDialog();
                        UiUtils.showAlertDialogWithOKButton(GroupCollectionConfirmActivity.this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                yesButton.setEnabled(true);
                                yesButton.setTextAppearance(GroupCollectionConfirmActivity.this,R.style.MyBluePopUpButton);
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


    private void sendGroupPaymentAPI() {


        confirmButton.setEnabled(false);
        confirmButton.setBackground(getResources().getDrawable(R.drawable.disabled_button_bg));

        showProgressDialog(GroupCollectionConfirmActivity.this);

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("user_passcode", String.valueOf(passcode));
        builder.addFormDataPart("payment[amount]", pendingAmount);
//        builder.addFormDataPart("payment[amount]", pendingAmountCollectedTv.getText().toString().trim().replaceAll("[^\\d.]", ""));
        builder.addFormDataPart("payment[payment_type_id]", modeOfPaymentID);
        if(transactionDateTv.getText().toString().length()>0){
            builder.addFormDataPart("payment[transaction_date]", transactionDateFormat);
        }

        if(transactionIdTv.getText().toString().length()>0) {
            builder.addFormDataPart("payment[transaction_id]",transactionIdTv.getText().toString().trim());
        }

        RequestBody finalRequestBody = builder.build();
        ApiInterface apiService;
        if(currentLat !=null && currentLong !=null) {
            apiService = RetrofitClientWithHeadersForLocation.getClient().create(ApiInterface.class);
        }else{
            apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
        }
        Call<GroupPaymentResponseModel> call = apiService.sendGroupPayment(groupID,finalRequestBody
        );
        call.enqueue(new Callback<GroupPaymentResponseModel>() {
            @Override
            public void onResponse(Call<GroupPaymentResponseModel> call, Response<GroupPaymentResponseModel> response) {
                hideProgressDialog();
                Log.d("Drools onResponse", "" + response.code() + "--" + response.message());
                Log.d("Drools onResponse1", "" + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        //API Success is true
                        confirmButton.setEnabled(true);
                        confirmButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));


                        Bundle params = new Bundle();
                        params.putString("level","group_collection_done");
                        params.putString("mode_of_payment",modeOfPaymentTv.getText().toString());
                        mFirebaseAnalytics.logEvent("collection_group", params);



                        Toast.makeText(GroupCollectionConfirmActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                        goToDashboard(response.body().getGroupModel());
                    } else {
                        Log.d("Drools onResponse", "" + response.body().getErrors());
                        confirmButton.setEnabled(true);
                        confirmButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                        NetworkUtils.handleErrorsCasesForAPICalls(GroupCollectionConfirmActivity.this, response.code(), response.body().getErrors());
                    }
                } else {
                    Log.d("Drools onResponse", "" + response.body().getErrors());
                    confirmButton.setEnabled(true);
                    confirmButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                    NetworkUtils.handleErrorsForAPICalls(GroupCollectionConfirmActivity.this, null, null);
                }
            }

            @Override
            public void onFailure(Call<GroupPaymentResponseModel> call, Throwable t) {
                hideProgressDialog();
                confirmButton.setEnabled(true);
                confirmButton.setBackground(getResources().getDrawable(R.drawable.primary_button_bg));
                NetworkUtils.handleErrorsForAPICalls(GroupCollectionConfirmActivity.this, null, null);
            }
        });
    }


    private void goToDashboard(GroupModel groupModel) {

        CollectionTaskDetailsActivity.getInstance().init();

        Intent intent = new Intent(GroupCollectionConfirmActivity.this,GroupOverViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("city",cityTv.getText().toString().trim());
        bundle.putString("group_leader",groupLeaderTv.getText().toString().trim());
        bundle.putString("pending_amount",pendingAmount);
        bundle.putString("pending_amount_format",pendingAmountCollectedTv.getText().toString().trim());
        bundle.putString("members_count",membersCountTv.getText().toString().trim());
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                    boolean passwordStatus = UiUtils.checkValidationForOTP(GroupCollectionConfirmActivity.this, passwordOTPView,passwordErrorTV);

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
            GroupCollectionConfirmActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

}
