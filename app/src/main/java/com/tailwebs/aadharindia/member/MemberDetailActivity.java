package com.tailwebs.aadharindia.member;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.mukesh.OtpView;
import com.squareup.picasso.Picasso;
import com.tailwebs.aadharindia.BaseActivity;
import com.tailwebs.aadharindia.R;
import com.tailwebs.aadharindia.housevisit.HouseDetailsActivity;
import com.tailwebs.aadharindia.member.applicant.AddCustomerDetailsActivity;
import com.tailwebs.aadharindia.member.applicant.ApplicantDetailActivity;
import com.tailwebs.aadharindia.member.cashincome.CashIncomeDetailsActivity;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantActivity;
import com.tailwebs.aadharindia.member.coapplicant.CoApplicantDetailsActivity;
import com.tailwebs.aadharindia.member.declaration.DeclarationActivity;
import com.tailwebs.aadharindia.member.expenditure.ExpenditureDetailsActivity;
import com.tailwebs.aadharindia.member.expenditure.OutsideBorrowingActivity;
import com.tailwebs.aadharindia.member.models.CalculateEMIResponseModel;
import com.tailwebs.aadharindia.member.models.applicantstatus.LoanTakerApplicantStatusResponseModel;
import com.tailwebs.aadharindia.member.models.memberstatus.LoanTakerMemberStatusResponseModel;
import com.tailwebs.aadharindia.member.rating.RatingActivity;
import com.tailwebs.aadharindia.models.common.CustomerApplicantCommonDataResponseModel;
import com.tailwebs.aadharindia.retrofitapi.ApiInterface;
import com.tailwebs.aadharindia.retrofitapi.RetrofitClientWithHeaders;
import com.tailwebs.aadharindia.utils.GlobalValue;
import com.tailwebs.aadharindia.utils.NetworkUtils;
import com.tailwebs.aadharindia.utils.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberDetailActivity extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.user_phone_no)
    TextView userPhoneTv;



    //
    @BindView(R.id.call_button)
    ImageButton callButton;


    //categories

    @BindView(R.id.applicant_tv)
    TextView applicantTv;

    @BindView(R.id.co_applicant_tv)
    TextView coApplicantTv;

    @BindView(R.id.income_tv)
    TextView incomeTv;

    @BindView(R.id.expenses_tv)
    TextView expensesTv;

    @BindView(R.id.signing_tv)
    TextView signingTv;


    @BindView(R.id.rating_tv)
    TextView ratingTv;

    private ProgressDialog mProgressDialog;

    TextView callMemberTv;
    TextView removeMemberTv;

    //choose value from intent;
    String loanTakerID=null,buttonValue=null,phoneNumber=null,passcode=null,reason=null;

    static MemberDetailActivity instance;

    OtpView passwordOTPView;
    TextView passwordErrorTV;

    private Dialog dialog;

    TextInputLayout reasonLayout;
    TextInputEditText reasonET;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;

    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);
        ButterKnife.bind(this);
        instance= this;

        //action bar
        backButton.setOnClickListener(this);
        headingTV.setText("Member Detail");
        headingTV.setTextAppearance(getApplicationContext(),R.style.MyActionBarHeading);
        rightActionBarButton.setVisibility(View.VISIBLE);
        rightActionBarButton.setOnClickListener(this);



        if(!getIntent().hasExtra("page_value")) {
            Intent intent = new Intent(MemberDetailActivity.this, ApplicantDetailActivity.class);
            intent.putExtras(getIntent().getExtras());
            startActivity(intent);
        }

        init();

    }

    public static MemberDetailActivity getInstance() {
        return instance;
    }

    public void init() {

        loanTakerID = GlobalValue.loanTakerId;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Member Detail", null);



        if (checkPermission(Manifest.permission.CALL_PHONE)) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MAKE_CALL_PERMISSION_REQUEST_CODE);
        }

        if(loanTakerID != null) {


            if (NetworkUtils.haveNetworkConnection(this)) {
                showProgressDialog(MemberDetailActivity.this);
                getMemberStatus();
            } else {
                UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }
        }

        applicantTv.setOnClickListener(this);
        coApplicantTv.setOnClickListener(this);
        callButton.setOnClickListener(this);
        incomeTv.setOnClickListener(this);
        expensesTv.setOnClickListener(this);
        signingTv.setOnClickListener(this);
        ratingTv.setOnClickListener(this);


//        if (NetworkUtils.haveNetworkConnection(this)) {
//            showProgressDialog(MemberDetailActivity.this);
//            getApplicantCommonDatas();
//        } else {
//            UiUtils.showAlertDialogWithOKButton(this, getString(R.string.error_no_internet), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.cancel();
//                }
//            });
//        }

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
                    boolean passwordStatus = UiUtils.checkValidationForOTP(MemberDetailActivity.this, passwordOTPView,passwordErrorTV);

                    if (passwordStatus == false) {

                        requestFocus(passwordOTPView);
                    } else {

                        passwordOTPView.setLineColor(getResources().getColor(R.color.primaryColor));

                    }
                    break;


                case R.id.input_reason:
                    itemPassed.clear();
                    itemPassed.add("");
                    boolean reasonStatus = UiUtils.checkValidation(MemberDetailActivity.this, reasonET,reasonLayout,itemPassed);

                    if (reasonStatus == false) {
                        requestFocus(reasonET);
                    } else {
                        reasonLayout.setErrorEnabled(false);
                    }
                    break;

            }
        }

    }


    public void requestFocus(View view) {
        if (view.requestFocus())
            MemberDetailActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void getMemberStatus() {

        try {

            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
            Call<LoanTakerMemberStatusResponseModel> call = apiService.getMemberStatus(loanTakerID);
            call.enqueue(new Callback<LoanTakerMemberStatusResponseModel>() {
                @Override
                public void onResponse(Call<LoanTakerMemberStatusResponseModel> call, final Response<LoanTakerMemberStatusResponseModel> response) {
                    hideProgressDialog();
                    Log.i("Drools", "" + response.message());
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            Log.i("Drools", "" + new Gson().toJson(response.body()));

                            setValuesFromResponse(response.body());

                        } else {
                            NetworkUtils.handleErrorsForAPICalls(MemberDetailActivity.this, response.body().getErrors(), response.body().getNotice());
                        }
                    } else {
                        NetworkUtils.handleErrorsCasesForAPICalls(MemberDetailActivity.this, response.code(), response.body().getErrors());
                    }
                }

                @Override
                public void onFailure(Call<LoanTakerMemberStatusResponseModel> call, Throwable t) {
                    hideProgressDialog();
                    Log.i("Drools", "" + t.getMessage());
                    NetworkUtils.handleErrorsForAPICalls(MemberDetailActivity.this, null, null);
                }
            });

        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }
    }

    public void call(String phoneNumber){
        if (checkPermission(Manifest.permission.CALL_PHONE)) {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            Toast.makeText(MemberDetailActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private void setValuesFromResponse(LoanTakerMemberStatusResponseModel body) {


        GlobalValue.isApplicantDeclarationCompleted = false;
        GlobalValue.loanTakerIdForAnalytics = body.getLoanTakerMemberStatusModel().getLoan_taker_id();
        userNameTv.setText(body.getLoanTakerMemberStatusModel().getName());
        userCoTv.setText("C.o. "+body.getLoanTakerMemberStatusModel().getAadhar_co());
        userPhoneTv.setText(body.getLoanTakerMemberStatusModel().getPrimary_phone_number());
        callButton.setClickable(true);
        callButton.setVisibility(View.VISIBLE);

        Picasso.with(MemberDetailActivity.this)
                .load(body.getLoanTakerMemberStatusModel().getProfileImages().getMedium())
                .placeholder(R.drawable.userimg_placeholder)
                .into(profileImageView);

        phoneNumber = body.getLoanTakerMemberStatusModel().getPrimary_phone_number();



        //Applicant

        if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getApplicantModel().getActivated()){

            UiUtils.setProcessActivated(MemberDetailActivity.this,applicantTv);
            if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getApplicantModel().getCompleted()){
                UiUtils.setProcessCompleted(MemberDetailActivity.this,applicantTv);
            }

        }else{
            UiUtils.setProcessNotCompleted(MemberDetailActivity.this,applicantTv);
        }

        //Co applicant
        if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getCoApplicantModel().getActivated()){

            UiUtils.setProcessActivated(MemberDetailActivity.this,coApplicantTv);
            if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getCoApplicantModel().getCompleted()){
                UiUtils.setProcessCompleted(MemberDetailActivity.this,coApplicantTv);
            }

        }else {
            UiUtils.setProcessNotCompleted(MemberDetailActivity.this,coApplicantTv);
        }


        //income
        if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getIncomeModel().getActivated()){

            UiUtils.setProcessActivated(MemberDetailActivity.this,incomeTv);
            if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getIncomeModel().getCompleted()){
                UiUtils.setProcessCompleted(MemberDetailActivity.this,incomeTv);
            }

        }else {
            UiUtils.setProcessNotCompleted(MemberDetailActivity.this,incomeTv);
        }

        //expenses
        if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getExpenseModel().getActivated()){

            UiUtils.setProcessActivated(MemberDetailActivity.this,expensesTv);
            if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getExpenseModel().getCompleted()){
                UiUtils.setProcessCompleted(MemberDetailActivity.this,expensesTv);
            }

        }else {
            UiUtils.setProcessNotCompleted(MemberDetailActivity.this,expensesTv);
        }


        //signing
        if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getIsSignedModel().getActivated()){

            UiUtils.setProcessActivated(MemberDetailActivity.this,signingTv);
            if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getIsSignedModel().getCompleted()){
                UiUtils.setProcessCompleted(MemberDetailActivity.this,signingTv);
                GlobalValue.isApplicantDeclarationCompleted = true;
            }

        }else {
            UiUtils.setProcessNotCompleted(MemberDetailActivity.this,signingTv);
        }


        //rating
        if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getRatingModel().getActivated()){

            UiUtils.setProcessActivated(MemberDetailActivity.this,ratingTv);
            if(body.getLoanTakerMemberStatusModel().getMemberStatusModel().getStatusModel().getRatingModel().getCompleted()){
                UiUtils.setProcessCompleted(MemberDetailActivity.this,ratingTv);
            }

        }else {
            UiUtils.setProcessNotCompleted(MemberDetailActivity.this,ratingTv);
        }

    }

//    private void getApplicantCommonDatas() {
//        try {
//
//            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);
//            Call<CustomerApplicantCommonDataResponseModel> call = apiService.getCustomerApplicantCommonDatas();
//            call.enqueue(new Callback<CustomerApplicantCommonDataResponseModel>() {
//                @Override
//                public void onResponse(Call<CustomerApplicantCommonDataResponseModel> call, Response<CustomerApplicantCommonDataResponseModel> response) {
//                    hideProgressDialog();
//                    Log.i("Drools", "" + response.message());
//                    if (response.isSuccessful()) {
//                        if (response.body().getSuccess()) {
//                            //API Success is true
//
//                            GlobalValue.applicantLoanAmountArrayList = response.body().getCacdLoanAmountsModelArrayList();
//                            GlobalValue.applicantLoanTenureArrayList = response.body().getCacdLoanTenuresModelArrayList();
//                            GlobalValue.applicantLoanTakerRelationsArrayList =  response.body().getCacdLoanTakerRelationsModelArrayList();
//                            GlobalValue.applicantLoanReasonsArrayList = response.body().getCacdLoanReasonsModelArrayList();
//                            GlobalValue.applicantMaritalStatusArrayList = response.body().getCacdMaritalStatusModelArrayList();
//                            GlobalValue.applicantReligionsArrayList = response.body().getCacdReligionsModelArrayList();
//                            GlobalValue.applicantRationCardTypesArrayList =  response.body().getCacdRationCardTypesModels();
//
//                        } else {
//                            NetworkUtils.handleErrorsForAPICalls(MemberDetailActivity.this, response.body().getErrors(), response.body().getNotice());
//                        }
//                    } else {
//                        NetworkUtils.handleErrorsCasesForAPICalls(MemberDetailActivity.this, response.code(), response.body().getErrors());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CustomerApplicantCommonDataResponseModel> call, Throwable t) {
//                    hideProgressDialog();
//                    Log.i("Drools", "" + t.getMessage());
//                    NetworkUtils.handleErrorsForAPICalls(MemberDetailActivity.this, null, null);
//                }
//            });
//
//        } catch (NullPointerException e) {
//            System.out.print("Caught the NullPointerException");
//        }
//    }

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


    private void showPopup() {
        View view = getLayoutInflater().inflate(R.layout.custom_house_call_remove_dialog, null);
        callMemberTv=(TextView) view.findViewById(R.id.call_member_tv);
        removeMemberTv=(TextView) view.findViewById(R.id.remove_member_tv);

        callMemberTv.setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MemberDetailActivity.this, "yyy", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CancelCustomListener(alertDialog));

        removeMemberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showAreYouSurePopUp();
            }
        });
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


    private void showAreYouSurePopUp(){
        View view = getLayoutInflater().inflate(R.layout.custom_message_yes_no_dialog, null);
        TextView messageTV =(TextView)view.findViewById(R.id.message_tv);
        Button yesButton =(Button)view.findViewById(R.id.yes_button);
        Button noButton =(Button)view.findViewById(R.id.no_button);
        messageTV.setText(getResources().getString(R.string.hint_remove_member_from_message));
        yesButton.setText(getResources().getString(R.string.hint_yes));
        noButton.setText(getResources().getString(R.string.hint_no));
        noButton.setTextAppearance(MemberDetailActivity.this,R.style.MyBluePopUpButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(MemberDetailActivity.this);
        builder.setCancelable(false)
                .setTitle(getResources().getString(R.string.hint_remove_member_from_title))
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
                showReasonForRejectionPopup();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    private void showReasonForRejectionPopup(){
        View view = getLayoutInflater().inflate(R.layout.custom_rejection_dialog, null);
         reasonLayout =(TextInputLayout)view.findViewById(R.id.input_layout_reason);
         reasonET =(TextInputEditText)view.findViewById(R.id.input_reason);
        reasonET.addTextChangedListener(new InputLayoutTextWatcher(reasonET));
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberDetailActivity.this);
        builder.setCancelable(false)
                .setTitle("Reason for Rejection ")
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MemberDetailActivity.this, "yyy", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new RejectListener(alertDialog));


    }

    private class RejectListener implements View.OnClickListener {

        private Dialog dialog;

        public RejectListener(AlertDialog alertDialog) {
            this.dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here

            if(UiUtils.checkValidation(MemberDetailActivity.this, reasonET,reasonLayout,new ArrayList<String>())) {
                reason = reasonET.getText().toString().trim();
                this.dialog.dismiss();
                showPasswordDialog();
            }
        }
    }

    private void showPasswordDialog(){
        // title, custom view, actions dialog

        passcode= null;
        View view = getLayoutInflater().inflate(R.layout.custom_password_dialog, null);
        passwordOTPView=(OtpView) view.findViewById(R.id.password_otp_view);
        passwordErrorTV =(TextView)view.findViewById(R.id.password_error_tv);
        passwordOTPView.addTextChangedListener(new InputLayoutTextWatcher(passwordOTPView));
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberDetailActivity.this);
        builder.setCancelable(false)
                .setTitle("Enter Passcode")
                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("", null)
                .setView(view);


        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Button theButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new CustomListener(alertDialog));

    }


    private void removeMember(){

        Log.i("aadh passcode", "--" + passcode+loanTakerID);

        showProgressDialog(MemberDetailActivity.this);
        try {
            ApiInterface apiService = RetrofitClientWithHeaders.getClient().create(ApiInterface.class);

            Call<CalculateEMIResponseModel> call = apiService.removeOnFormData(loanTakerID,
                    passcode,reason);

            call.enqueue(new Callback<CalculateEMIResponseModel>() {
                @Override
                public void onResponse(Call<CalculateEMIResponseModel> call, Response<CalculateEMIResponseModel> response) {
                    hideProgressDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess()) {
                            //API Success is true
                            dialog.dismiss();
                            Toast.makeText(MemberDetailActivity.this, "" + response.body().getNotice(), Toast.LENGTH_SHORT).show();
                            goToGroupListingPage();
                        } else {
                            Log.d("aadh onResponse", "" + response.body().getErrors());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                Log.d("aadh onResponse type", "" + jsonObject.getString("type"));
                                Log.d("aadh onResponse json", "" + jsonObject);

                                if(jsonObject.getString("type").equalsIgnoreCase("invalid_passcode")){
                                    passwordOTPView.setLineColor(getResources().getColor(R.color.errorColor));
                                    passwordErrorTV.setVisibility(View.VISIBLE);
                                    passwordErrorTV.setText("Invalid Passcode");
                                    Toast.makeText(MemberDetailActivity.this, "Invalid passcode", Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            NetworkUtils.handleErrorsCasesForAPICalls(MemberDetailActivity.this, response.code(), response.body().getErrors());
                        }
                    } else {
                        hideProgressDialog();
                        Log.i("aadh Error", "--" + new Gson().toJson(response.body()));
                        NetworkUtils.handleErrorsCasesForAPICalls(MemberDetailActivity.this, response.code(), response.body().getErrors());

                    }

                }

                @Override
                public void onFailure(Call<CalculateEMIResponseModel> call, Throwable t) {

                    hideProgressDialog();
                    NetworkUtils.handleErrorsForAPICalls(MemberDetailActivity.this, null, null);

                }
            });
        } catch (NullPointerException e) {
            System.out.print("Caught the NullPointerException");
        }

    }

    private void goToGroupListingPage() {
        Intent intent = new Intent(MemberDetailActivity.this,GroupMemberListingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    private class CustomListener implements View.OnClickListener {

        public CustomListener(AlertDialog alertDialog) {
            dialog = alertDialog;
        }

        @Override
        public void onClick(View v) {
            // put your code here
            passcode = passwordOTPView.getText().toString();
            if(UiUtils.checkValidationForOTP(MemberDetailActivity.this, passwordOTPView,passwordErrorTV)){
                removeMember();
            }
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;

            case R.id.right_action_bar_button:
                if(loanTakerID!=null){
                    showPopup();
                }

                break;


            case R.id.applicant_tv:

                Intent intent = new Intent(MemberDetailActivity.this,ApplicantDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("page_value","from_member");
                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case R.id.co_applicant_tv:
                Intent co_intent = new Intent(MemberDetailActivity.this,CoApplicantDetailsActivity.class);
                Bundle co_bundle = new Bundle();
                co_bundle.putString("page_value","from_member");
                co_intent.putExtras(co_bundle);
                startActivity(co_intent);
                break;


            case R.id.income_tv:
                Intent income_intent = new Intent(MemberDetailActivity.this,CashIncomeDetailsActivity.class);
                Bundle income_bundle = new Bundle();
                income_bundle.putString("page_value","from_member");
                income_intent.putExtras(income_bundle);
                startActivity(income_intent);
                break;


            case R.id.expenses_tv:
                Intent expenses_intent = new Intent(MemberDetailActivity.this,ExpenditureDetailsActivity.class);
                Bundle expenses_bundle = new Bundle();
                expenses_bundle.putString("page_value","from_member");
                expenses_intent.putExtras(expenses_bundle);
                startActivity(expenses_intent);
                break;

            case R.id.signing_tv:
                Intent signing_intent = new Intent(MemberDetailActivity.this,DeclarationActivity.class);
                Bundle signing_bundle = new Bundle();
                signing_bundle.putString("page_value","from_member");
                signing_intent.putExtras(signing_bundle);
                startActivity(signing_intent);
                break;


            case R.id.rating_tv:
                Intent rating_intent = new Intent(MemberDetailActivity.this,RatingActivity.class);
                Bundle rating_bundle = new Bundle();
                rating_bundle.putString("page_value","from_member");
                rating_intent.putExtras(rating_bundle);
                startActivity(rating_intent);
                break;

            case R.id.call_button:
                call(phoneNumber);
                break;
        }
    }
}
